package com.bitnotes.app.ui.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bitnotes.app.data.model.NotePreview
import com.bitnotes.app.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: LiveData<String> = _searchQuery.asLiveData()

    private val _isSearching = MutableLiveData(false)
    val isSearching: LiveData<Boolean> = _isSearching

    val notes: LiveData<List<NotePreview>> = noteRepository.getAllNotePreviews()
        .combine(_searchQuery) { notes, query ->
            if (query.isEmpty()) notes
            else notes.filter { note ->
                note.title.contains(query, ignoreCase = true) ||
                        note.contentPreview.contains(query, ignoreCase = true) ||
                        note.tags.contains(query, ignoreCase = true)
            }
        }
        .asLiveData()

    val trashNotes: LiveData<List<NotePreview>> = noteRepository.getTrashPreviews().asLiveData()

    private val _snackbarMessage = MutableLiveData<String?>()
    val snackbarMessage: LiveData<String?> = _snackbarMessage

    fun updateSearch(query: String) {
        _searchQuery.value = query
    }

    fun toggleSearch() {
        _isSearching.value = !(_isSearching.value ?: false)
        if (_isSearching.value == false) {
            _searchQuery.value = ""
        }
    }

    fun moveToTrash(noteId: Long) {
        viewModelScope.launch {
            noteRepository.moveToTrash(noteId)
            _snackbarMessage.value = "Note moved to trash"
        }
    }

    fun restoreFromTrash(noteId: Long) {
        viewModelScope.launch {
            noteRepository.restoreFromTrash(noteId)
            _snackbarMessage.value = "Note restored"
        }
    }

    fun permanentlyDelete(noteId: Long) {
        viewModelScope.launch {
            noteRepository.permanentlyDelete(noteId)
            _snackbarMessage.value = "Note permanently deleted"
        }
    }

    fun togglePin(noteId: Long, currentlyPinned: Boolean) {
        viewModelScope.launch {
            noteRepository.setPinned(noteId, !currentlyPinned)
        }
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }
}
