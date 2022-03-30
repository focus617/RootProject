package com.focus617.bookreader.ui.gallery

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import com.focus617.bookreader.R
import com.focus617.bookreader.databinding.FragmentGalleryBinding
import com.focus617.bookreader.framework.MyViewModelFactory
import com.focus617.core.domain.Note
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: MyViewModelFactory
    private lateinit var viewModel: GalleryViewModel
    private lateinit var adapter: NoteListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)

        // Get the instance of ViewModel
        viewModel =
            ViewModelProvider(this, viewModelFactory)[GalleryViewModel::class.java]
        binding.viewModel = viewModel

        adapter = setupRecyclerView()

        viewModel.notes.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
                updateNoDataIcon(it)
            }
        }
//        viewModel.loadNotes()
        val size = viewModel.notes.value?.size
        Toast.makeText(context, "Total $size Notes.", Toast.LENGTH_SHORT).show()

        setupSwipeRefresh()
        setupFab()
        setHasOptionsMenu(true)

        return binding.root
    }

    private fun updateNoDataIcon(list: List<Note>) {
        /**
         * If there are any notes in the database, show the NO_DATA icon.
         */
        if (list.isEmpty()) {
            binding.noteNoDataImageView.visibility = View.VISIBLE
            binding.noteNoDataTextView.visibility = View.VISIBLE
        } else {
            binding.noteNoDataImageView.visibility = View.INVISIBLE
            binding.noteNoDataTextView.visibility = View.INVISIBLE
        }
    }

    private fun setupFab() {
        binding.fabAddNewNote.setOnClickListener {
            viewModel.showSnackbarMessage(R.string.AddNewNoteMessage)
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadNotes()
            viewModel.showSnackbarMessage(R.string.RefreshMessage)
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun setupRecyclerView(): NoteListAdapter {
        val adapter = NoteListAdapter(NoteListener { note: Note ->
            Toast.makeText(context, "《${note.id}》will be opened.", Toast.LENGTH_SHORT).show()
            viewModel.displayNoteDetail(note)
        })
        binding.noteList.adapter = adapter

        ItemTouchHelper(NoteItemTouchCallback(adapter)).attachToRecyclerView(binding.noteList)

        return adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.option_menu_note, menu)

        val mSearchView = menu.findItem(R.id.action_search).actionView as SearchView
        with(mSearchView) {
            queryHint = getString(R.string.SearchHint)

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.apply {
                        val newList = viewModel.notes.value?.filter { it.content.contains(newText) }
                        adapter.submitList(newList)
                    }

                    return false
                }
            })
        }
    }
}