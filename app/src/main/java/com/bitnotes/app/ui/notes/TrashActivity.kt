package com.bitnotes.app.ui.notes

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bitnotes.app.R
import com.bitnotes.app.data.model.NotePreview
import com.bitnotes.app.databinding.ActivityTrashBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrashActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrashBinding
    private val viewModel: NotesViewModel by viewModels()
    private lateinit var trashAdapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Trash"
        }

        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        trashAdapter = NotesAdapter(
            onNoteClick = { note -> showTrashOptions(note) },
            onNoteLongClick = { note -> showTrashOptions(note) }
        )

        binding.recyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = trashAdapter
        }
    }

    private fun setupObservers() {
        viewModel.trashNotes.observe(this) { notes ->
            trashAdapter.submitList(notes)
            binding.emptyState.visibility = if (notes.isEmpty()) View.VISIBLE else View.GONE
            binding.recyclerView.visibility = if (notes.isEmpty()) View.GONE else View.VISIBLE
        }

        viewModel.snackbarMessage.observe(this) { message ->
            if (message != null) {
                Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
                viewModel.clearSnackbar()
            }
        }
    }

    private fun showTrashOptions(note: NotePreview) {
        MaterialAlertDialogBuilder(this)
            .setTitle(note.title.ifEmpty { "Untitled" })
            .setItems(arrayOf("Restore", "Delete Permanently")) { _, which ->
                when (which) {
                    0 -> viewModel.restoreFromTrash(note.id)
                    1 -> confirmPermanentDelete(note)
                }
            }
            .show()
    }

    private fun confirmPermanentDelete(note: NotePreview) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Permanently Delete?")
            .setMessage("\"${note.title.ifEmpty { "Untitled" }}\" will be permanently deleted and cannot be recovered.")
            .setPositiveButton("Delete Forever") { _, _ ->
                viewModel.permanentlyDelete(note.id)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
