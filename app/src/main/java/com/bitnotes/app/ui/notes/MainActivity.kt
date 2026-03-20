package com.bitnotes.app.ui.notes

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bitnotes.app.R
import com.bitnotes.app.crypto.KeyManager
import com.bitnotes.app.data.model.NotePreview
import com.bitnotes.app.databinding.ActivityMainBinding
import com.bitnotes.app.ui.auth.AuthActivity
import com.bitnotes.app.ui.backup.BackupActivity
import com.bitnotes.app.ui.settings.SettingsActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: NotesViewModel by viewModels()
    private lateinit var notesAdapter: NotesAdapter

    @Inject
    lateinit var keyManager: KeyManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        setupRecyclerView()
        setupFab()
        setupObservers()
        setupBackPress()
    }

    private fun setupRecyclerView() {
        notesAdapter = NotesAdapter(
            onNoteClick = { note -> openNoteEditor(note.id) },
            onNoteLongClick = { note -> showNoteOptions(note) }
        )

        binding.recyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = notesAdapter
            setHasFixedSize(false)
        }
    }

    private fun setupFab() {
        binding.fabNewNote.setOnClickListener {
            openNoteEditor(-1L)
        }
    }

    private fun setupObservers() {
        viewModel.notes.observe(this) { notes ->
            notesAdapter.submitList(notes)
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

    private fun setupBackPress() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewModel.isSearching.value == true) {
                    viewModel.toggleSearch()
                    invalidateOptionsMenu()
                } else {
                    // Lock the app when back is pressed from main screen
                    keyManager.lock()
                    finish()
                }
            }
        })
    }

    private fun openNoteEditor(noteId: Long) {
        val intent = Intent(this, NoteEditorActivity::class.java).apply {
            putExtra(NoteEditorActivity.EXTRA_NOTE_ID, noteId)
        }
        startActivity(intent)
    }

    private fun showNoteOptions(note: NotePreview) {
        val options = arrayOf(
            if (note.isPinned) "Unpin" else "Pin",
            "Delete"
        )
        MaterialAlertDialogBuilder(this)
            .setTitle(note.title.ifEmpty { "Untitled" })
            .setItems(options) { _, which ->
                when (which) {
                    0 -> viewModel.togglePin(note.id, note.isPinned)
                    1 -> confirmDelete(note)
                }
            }
            .show()
    }

    private fun confirmDelete(note: NotePreview) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Move to Trash?")
            .setMessage("\"${note.title.ifEmpty { "Untitled" }}\" will be moved to trash.")
            .setPositiveButton("Move to Trash") { _, _ ->
                viewModel.moveToTrash(note.id)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private var searchView: SearchView? = null

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView
        searchView?.queryHint = "Search notes..."

        // Restore current search query if active
        val currentQuery = viewModel.searchQuery.value
        if (!currentQuery.isNullOrEmpty()) {
            searchItem.expandActionView()
            searchView?.setQuery(currentQuery, false)
            searchView?.clearFocus()
        }

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.updateSearch(newText ?: "")
                return true
            }
        })

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean = true
            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                viewModel.updateSearch("")
                return true
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_backup -> {
                startActivity(Intent(this, BackupActivity::class.java))
                true
            }
            R.id.action_trash -> {
                startActivity(Intent(this, TrashActivity::class.java))
                true
            }
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            R.id.action_lock -> {
                keyManager.lock()
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        // Check if still unlocked (could have been locked in background)
        if (!keyManager.isUnlocked()) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
    }
}
