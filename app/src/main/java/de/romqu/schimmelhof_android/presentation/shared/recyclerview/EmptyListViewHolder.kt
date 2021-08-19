package de.romqu.schimmelhof_android.presentation.shared.recyclerview

import androidx.recyclerview.widget.RecyclerView
import de.romqu.schimmelhof_android.databinding.ItemChildRidinglessonBinding
import de.romqu.schimmelhof_android.databinding.ItemEmptyListBinding

class EmptyListViewHolder(private val binding: ItemEmptyListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: EmptyItem) {
        binding.apply {
            emptyTextView.text = item.text
        }
    }
}