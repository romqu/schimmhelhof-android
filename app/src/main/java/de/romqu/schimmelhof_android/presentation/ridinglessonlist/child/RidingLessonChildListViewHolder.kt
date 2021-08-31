package de.romqu.schimmelhof_android.presentation.ridinglessonlist.child

import androidx.recyclerview.widget.RecyclerView
import de.romqu.schimmelhof_android.databinding.ItemChildRidinglessonBinding
import kotlinx.coroutines.flow.MutableSharedFlow

class RidingLessonChildListViewHolder(
    private val binding: ItemChildRidinglessonBinding,
    onItemClickChannel: MutableSharedFlow<Int>,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            onItemClickChannel.tryEmit(bindingAdapterPosition)
        }
    }

    fun bind(item: RidingLessonChildItem) {
        binding.apply {
            ridingTextView.text = item.text
        }
    }
}