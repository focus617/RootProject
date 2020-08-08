package com.example.pomodoro2.core.platform

import androidx.lifecycle.Observer

/**
 * Define a custom Event class to make LiveData as a event wrapper which can be consumed only once
 *
 * @sample
 * In ViewModel: showSnackBar() is exposed to trigger this one-time event
 *     private val _showSnackBarEvent = MutableLiveData<SingleLiveEvent<Unit>>()
 *     val showSnackBarEvent: LiveData<SingleLiveEvent<Unit>> = _showSnackBarEvent
 *
 *     private fun showSnackBar() {
 *        _showSnackBarEvent.value = SingleLiveEvent(Unit)
 *     }
 *
 * @sample
 * In Fragment: Add an Observer on the state variable for showing a SnackBar message
 *     viewModel.showSnackBarEvent.observe(viewLifecycleOwner, EventObserver {
 *         Snackbar.make(
 *            requireActivity().findViewById(android.R.id.content),
 *            getString(R.string.goodbye_message),
 *            Snackbar.LENGTH_SHORT // How long to display the message.
 *         ).show()
 *     })
 *
 */
open class SingleLiveEvent<out T>(private val content: T) {

    @Suppress("MemberVisibilityCanBePrivate")
    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}

/**
 * An [Observer] for [SingleLiveEvent]s, simplifying the pattern of checking if the [SingleLiveEvent]'s content has
 * already been handled.
 *
 * [onEventUnhandledContent] is *only* called if the [SingleLiveEvent]'s contents has not been handled.
 */
class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<SingleLiveEvent<T>> {
    override fun onChanged(singleLiveEvent: SingleLiveEvent<T>?) {
        singleLiveEvent?.getContentIfNotHandled()?.let {
            onEventUnhandledContent(it)
        }
    }
}
