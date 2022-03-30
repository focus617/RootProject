package com.focus617.bookreader.ui.gallery

import android.app.Application
import android.text.TextUtils.isEmpty
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.focus617.bookreader.framework.interactors.Interactors
import com.focus617.core.domain.Book
import com.focus617.core.domain.Note
import com.focus617.platform.event.Event
import com.focus617.platform.uicontroller.BaseViewModel
import kotlinx.coroutines.*

class GalleryViewModel(application: Application, val interactors: Interactors) :
BaseViewModel(application) {

    // Declare Job() and cancel jobs in onCleared().
    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    // Define uiScope for coroutines.
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _notes = MutableLiveData<List<Note>>().apply { value = emptyList() }
    val notes: LiveData<List<Note>> = _notes

    /**
     * init{} is called immediately when this ViewModel is created.
     */
    init {
        loadNotes()
    }

    fun loadNotes(){
        _notes.value = listOf(
            Note.ExampleNote1, Note.ExampleNote2, Note.ExampleNote3,
            Note.ExampleNote4, Note.ExampleNote5)
    }

    /**
     * Executes when the CLEAR button is clicked.
     */
    fun onClear() {
       _notes.value = emptyList()
    }

    // To handle navigation to the selected note
    private val _navigateToSelectedNoteEvent = MutableLiveData<Event<Note>>()
    val navigateToSelectedNoteEvent: LiveData<Event<Note>> = _navigateToSelectedNoteEvent

    /**
     * Executes when the Note item in recyclerView is clicked.
     * It will trigger the navigation to NoteDetail Fragment
     */
    fun displayNoteDetail(note: Note) {
        _navigateToSelectedNoteEvent.value = Event(note)
    }

    /**
     * If there are any notes in the database, show the CLEAR button.
     */
    val clearButtonVisible = Transformations.map(notes) {
        it?.isNotEmpty()
    }
}