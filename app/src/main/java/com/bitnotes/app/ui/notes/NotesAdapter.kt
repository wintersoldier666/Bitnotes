package com.bitnotes.app.ui.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bitnotes.app.R
import com.bitnotes.app.data.model.NotePreview
import com.bitnotes.app.databinding.ItemNoteBinding
import java.text.SimpleDateFormat
import java.util.*

class NotesAdapter(
    private val onNoteClick: (NotePreview) -> Unit,
    private val onNoteLongClick: (NotePreview) -> Unit
) : ListAdapter<NotePreview, NotesAdapter.NoteViewHolder>(NotesDiffCallback()) {

    companion object {
        // Note card colors
        val NOTE_COLORS = intArrayOf(
            R.color.note_color_0, // Default
            R.color.note_color_1,
            R.color.note_color_2,
            R.color.note_color_3,
            R.color.note_color_4,
            R.color.note_color_5
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NoteViewHolder(
        private val binding: ItemNoteBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(note: NotePreview) {
            binding.tvTitle.text = note.title.ifEmpty { "Untitled" }
            binding.tvContent.text = note.contentPreview
            binding.tvContent.visibility = if (note.contentPreview.isEmpty()) View.GONE else View.VISIBLE
            binding.tvDate.text = formatDate(note.updatedAt)

            // Tags
            if (note.tags.isNotEmpty()) {
                binding.tvTags.text = note.tags
                binding.tvTags.visibility = View.VISIBLE
            } else {
                binding.tvTags.visibility = View.GONE
            }

            // Pin indicator
            binding.ivPin.visibility = if (note.isPinned) View.VISIBLE else View.GONE

            // Card color
            val colorRes = if (note.colorIndex in NOTE_COLORS.indices) {
                NOTE_COLORS[note.colorIndex]
            } else {
                NOTE_COLORS[0]
            }
            binding.cardView.setCardBackgroundColor(
                binding.root.context.getColor(colorRes)
            )

            binding.root.setOnClickListener { onNoteClick(note) }
            binding.root.setOnLongClickListener {
                onNoteLongClick(note)
                true
            }
        }

        private fun formatDate(timestamp: Long): String {
            val now = System.currentTimeMillis()
            val diff = now - timestamp
            return when {
                diff < 60_000L -> "Just now"
                diff < 3_600_000L -> "${diff / 60_000}m ago"
                diff < 86_400_000L -> "${diff / 3_600_000}h ago"
                diff < 604_800_000L -> "${diff / 86_400_000}d ago"
                else -> SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(timestamp))
            }
        }
    }

    class NotesDiffCallback : DiffUtil.ItemCallback<NotePreview>() {
        override fun areItemsTheSame(oldItem: NotePreview, newItem: NotePreview) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: NotePreview, newItem: NotePreview) =
            oldItem == newItem
    }
}
