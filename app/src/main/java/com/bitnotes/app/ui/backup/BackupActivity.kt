package com.bitnotes.app.ui.backup

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bitnotes.app.R
import com.bitnotes.app.databinding.ActivityBackupBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class BackupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBackupBinding
    private val viewModel: BackupViewModel by viewModels()

    private val pickFileLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                showRestorePasswordDialog(uri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBackupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Backup & Restore"
        }

        setupButtons()
        setupObservers()
    }

    private fun setupButtons() {
        binding.btnCreateBackup.setOnClickListener {
            showBackupPasswordDialog()
        }

        binding.btnRestoreBackup.setOnClickListener {
            pickBackupFile()
        }
    }

    private fun setupObservers() {
        viewModel.state.observe(this) { state ->
            when (state) {
                is BackupState.Idle -> {
                    binding.progressBar.visibility = View.GONE
                }
                is BackupState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is BackupState.BackupSuccess -> {
                    binding.progressBar.visibility = View.GONE
                    val result = state.result
                    showShareDialog(result.filePath!!, result.noteCount)
                    viewModel.resetState()
                }
                is BackupState.RestoreSuccess -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(
                        binding.root,
                        "Restored ${state.result.noteCount} notes successfully",
                        Snackbar.LENGTH_LONG
                    ).show()
                    viewModel.resetState()
                }
                is BackupState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                    viewModel.resetState()
                }
            }
        }

        viewModel.existingBackups.observe(this) { backups ->
            updateBackupsList(backups)
        }
    }

    private fun showBackupPasswordDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_password_input, null)
        val passwordField = dialogView.findViewById<TextInputEditText>(R.id.etPassword)
        val confirmField = dialogView.findViewById<TextInputEditText>(R.id.etConfirmPassword)

        MaterialAlertDialogBuilder(this)
            .setTitle("Backup Password")
            .setMessage("Set a password to encrypt your backup.\nYou'll need this to restore.\n\nMin 8 characters.")
            .setView(dialogView)
            .setPositiveButton("Create Backup") { _, _ ->
                val password = passwordField.text?.toString() ?: ""
                val confirm = confirmField.text?.toString() ?: ""
                if (password != confirm) {
                    Snackbar.make(binding.root, "Passwords don't match", Snackbar.LENGTH_SHORT).show()
                } else {
                    viewModel.createBackup(password)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showRestorePasswordDialog(uri: Uri) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_restore_password, null)
        val passwordField = dialogView.findViewById<TextInputEditText>(R.id.etPassword)

        MaterialAlertDialogBuilder(this)
            .setTitle("Restore Backup")
            .setMessage("Enter the password used to encrypt this backup.")
            .setView(dialogView)
            .setPositiveButton("Restore") { _, _ ->
                val password = passwordField.text?.toString() ?: ""
                viewModel.restoreBackup(uri, password)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showShareDialog(filePath: String, noteCount: Int) {
        val file = File(filePath)
        val uri = viewModel.getBackupUri(file)

        MaterialAlertDialogBuilder(this)
            .setTitle("Backup Created")
            .setMessage("$noteCount notes backed up.\n\nSave this file to a safe location.")
            .setPositiveButton("Share / Save") { _, _ ->
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "application/octet-stream"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                startActivity(Intent.createChooser(intent, "Save Backup"))
            }
            .setNegativeButton("Done", null)
            .show()
    }

    private fun pickBackupFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        pickFileLauncher.launch(intent)
    }

    private fun updateBackupsList(backups: List<File>) {
        binding.tvBackupsHeader.visibility = if (backups.isEmpty()) View.GONE else View.VISIBLE
        binding.backupsContainer.removeAllViews()

        val dateFormat = SimpleDateFormat("MMM d, yyyy HH:mm", Locale.getDefault())

        for (backup in backups) {
            val view = layoutInflater.inflate(R.layout.item_backup_file, binding.backupsContainer, false)
            view.findViewById<android.widget.TextView>(R.id.tvFileName).text = backup.name
            view.findViewById<android.widget.TextView>(R.id.tvFileDate).text =
                dateFormat.format(Date(backup.lastModified()))
            view.findViewById<android.widget.TextView>(R.id.tvFileSize).text =
                formatFileSize(backup.length())

            view.findViewById<View>(R.id.btnShare).setOnClickListener {
                val uri = viewModel.getBackupUri(backup)
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "application/octet-stream"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                startActivity(Intent.createChooser(intent, "Share Backup"))
            }

            view.findViewById<View>(R.id.btnDelete).setOnClickListener {
                MaterialAlertDialogBuilder(this)
                    .setTitle("Delete Backup?")
                    .setMessage("This backup file will be permanently deleted.")
                    .setPositiveButton("Delete") { _, _ ->
                        viewModel.deleteBackup(backup)
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }

            binding.backupsContainer.addView(view)
        }
    }

    private fun formatFileSize(bytes: Long): String = when {
        bytes < 1024 -> "$bytes B"
        bytes < 1024 * 1024 -> "${bytes / 1024} KB"
        else -> "${bytes / (1024 * 1024)} MB"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
