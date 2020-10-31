package de.romqu.schimmelhof_android.presentation.ridinglessonlist.child

import androidx.recyclerview.widget.RecyclerView
import de.romqu.schimmelhof_android.databinding.ItemChildRidinglessonBinding

class RidingLessonChildListViewHolder(private val binding: ItemChildRidinglessonBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: RidingLessonChildItem) {
        binding.apply {
            ridingTextView.text = item.text
        }
    }
}