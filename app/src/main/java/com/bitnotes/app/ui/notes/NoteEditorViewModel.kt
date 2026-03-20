package com.bitnotes.app.ui.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitnotes.app.data.model.DecryptedNote
import com.bitnotes.app.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class EditorState {
    object Loading : EditorState()
    data class Loaded(val note: DecryptedNote) : EditorState()
    object NewNote : EditorState()
    object Saved : EditorState()
    data class Error(val message: String) : EditorState()
}

@HiltViewModel
class NoteEditorViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val _editorState = MutableLiveData<EditorState>(EditorState.Loading)
    val editorState: LiveData<EditorState> = _editorState

    private var currentNoteId: Long = -1
    private var currentColorIndex: Int = 0
    private var currentIsPinned: Boolean = false

    fun loadNote(noteId: Long) {
        if (noteId == -1L) {
            currentNoteId = -1
            _editorState.value = EditorState.NewNote
            return
        }

        currentNoteId = noteId
        viewModelScope.launch {
            val note = noteRepository.getNoteById(noteId)
            if (note != null) {
                currentColorIndex = note.colorIndex
                currentIsPinned = note.isPinned
                _editorState.value = EditorState.Loaded(note)
            } else {
                _editorState.value = EditorState.Error("Note not found")
            }
        }
    }

    fun saveNote(title: String, content: String, tags: String = "") {
        if (title.isBlank() && content.isBlank()) {
            // Don't save empty notes
            _editorState.value = EditorState.Saved
            return
        }

        viewModelScope.launch {
            try {
                if (currentNoteId == -1L) {
                    noteRepository.createNote(
                        title = title,
                        content = content,
                        tags = tags,
                        colorIndex = currentColorIndex
                    )
                } else {
                    noteRepository.updateNote(
                        id = currentNoteId,
                        title = title,
                        content = content,
                        tags = tags,
                        colorIndex = currentColorIndex,
                        isPinned = currentIsPinned
                    )
                }
                _editorState.value = EditorState.Saved
            } catch (e: Exception) {
                _editorState.value = EditorState.Error("Failed to save: ${e.message}")
            }
        }
    }

    fun setColorIndex(index: Int) {
        currentColorIndex = index
    }
}
