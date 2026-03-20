package com.bitnotes.app.ui.notes

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bitnotes.app.R
import com.bitnotes.app.data.model.DecryptedNote
import com.bitnotes.app.databinding.ActivityNoteEditorBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteEditorActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NOTE_ID = "note_id"
    }

    private lateinit var binding: ActivityNoteEditorBinding
    private val viewModel: NoteEditorViewModel by viewModels()
    private var noteId: Long = -1L
    private var currentNote: DecryptedNote? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        noteId = intent.getLongExtra(EXTRA_NOTE_ID, -1L)
        viewModel.loadNote(noteId)
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.editorState.observe(this) { state ->
            when (state) {
                is EditorState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is EditorState.NewNote -> {
                    binding.progressBar.visibility = View.GONE
                    binding.etTitle.hint = "Title"
                    binding.etContent.hint = "Start writing..."
                    binding.etTitle.requestFocus()
                }
                is EditorState.Loaded -> {
                    binding.progressBar.visibility = View.GONE
                    currentNote = state.note
                    binding.etTitle.setText(state.note.title)
                    binding.etContent.setText(state.note.content)
                    binding.etTags.setText(state.note.tags)
                }
                is EditorState.Saved -> {
                    finish()
                }
                is EditorState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun saveNote() {
        val title = binding.etTitle.text?.toString() ?: ""
        val content = binding.etContent.text?.toString() ?: ""
        val tags = binding.etTags.text?.toString() ?: ""
        viewModel.saveNote(title, content, tags)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.editor_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                saveNote()
                true
            }
            R.id.action_save -> {
                saveNote()
                true
            }
            R.id.action_color -> {
                showColorPicker()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showColorPicker() {
        val colorNames = arrayOf(
            "Default", "Red", "Orange", "Yellow", "Green", "Blue"
        )
        MaterialAlertDialogBuilder(this)
            .setTitle("Note Color")
            .setItems(colorNames) { _, which ->
                viewModel.setColorIndex(which)
            }
            .show()
    }

    override fun onBackPressed() {
        saveNote()
    }
}
