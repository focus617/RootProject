package com.focus617.bookreader.ui.home

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.focus617.bookreader.R
import com.focus617.bookreader.databinding.FragmentHomeBinding
import com.focus617.bookreader.framework.MyViewModelFactory
import com.focus617.bookreader.ui.MainActivity
import com.focus617.bookreader.ui.util.IntentUtil
import com.focus617.core.domain.Book
import com.focus617.platform.view_util.setupSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: MyViewModelFactory
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Get a reference to the binding object and inflate the fragment views.
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        // Get the instance of ViewModel
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[HomeViewModel::class.java]
        binding.homeViewModel = viewModel


        val adapter = setupRecyclerView()

//        viewModel.books.observe(viewLifecycleOwner) {
//            it?.let {
//                adapter.addHeaderAndSubmitList(it)
//            }
//        }

        lifecycle.coroutineScope.launch {
            viewModel.loadBooksByFlow()
                // Intermediate catch operator. If an exception is thrown,
                // catch and update the UI
                .catch { exception -> viewModel.notifyError(exception) }
                .collect() {
                    adapter.addHeaderAndSubmitList(it)
                }
        }

        viewModel.loadBooks()

        viewModel.navigateToSelectedBookEvent.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                this.findNavController()
                    .navigate(HomeFragmentDirections.actionNavHomeToNavDetail(it))
            }
        }

        setupSwipeRefresh()
        setupFab()
        setHasOptionsMenu(true)

        return binding.root
    }

    private fun setupFab() {
        binding.fabAddNewBook.setOnClickListener {
            startActivityForResult(IntentUtil.createOpenIntent(), MainActivity.READ_REQUEST_CODE)
            viewModel.showSnackbarMessage(R.string.AddNewBookMessage)
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadBooks()
            viewModel.showSnackbarMessage(R.string.RefreshMessage)
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun setupRecyclerView(): BookListAdapter {
        // Initialize the recyclerView with GridLayout
        val manager = GridLayoutManager(activity, 3)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) = when (position) {
                0 -> 3
                else -> 1
            }
        }

        val adapter = BookListAdapter(BookListener { book: Book ->
            Toast.makeText(context, "《${book.title}》will be opened.", Toast.LENGTH_SHORT).show()
            viewModel.displayBookDetail(book)
        })
        binding.bookList.adapter = adapter
        binding.bookList.layoutManager = manager
        return adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupSnackbar()
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, viewModel.snackbarText)
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.option_menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_add -> {
                // Create a new book and insert it into the database.
                viewModel.onMenuAdd()
                true
            }
            R.id.action_quit -> {
                val activity = activity as MainActivity
                activity.sendOfflineBroadcast()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}