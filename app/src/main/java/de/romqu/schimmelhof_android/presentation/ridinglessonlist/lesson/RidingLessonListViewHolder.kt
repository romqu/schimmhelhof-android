package de.romqu.schimmelhof_android.presentation.ridinglessonlist.lesson

import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import de.romqu.schimmelhof_android.R
import de.romqu.schimmelhof_android.data.ridinglesson.RidingLessonStateEntity.*
import de.romqu.schimmelhof_android.databinding.ItemChildRidinglessonBinding

class RidingLessonListViewHolder(
    private val binding: ItemChildRidinglessonBinding,
    onItemClick: (position: Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            Log.d("AAAAA", "CLICK")
            onItemClick(bindingAdapterPosition)
        }
    }

    fun bind(item: RidingLessonItem) {
        val colorId = when (item.state) {
            EXPIRED -> R.color.white
            BOOKED_OUT -> R.color.white
            WAIT_LIST -> R.color.white
            BOOKED -> R.color.purple_200
            AVAILABLE -> R.color.white
        }

        binding.childRidingCardView.setCardBackgroundColor(ContextCompat.getColor(
            binding.root.context,
            colorId)
        )

        binding.apply {
            ridingTextView.text = item.title
        }
    }
}