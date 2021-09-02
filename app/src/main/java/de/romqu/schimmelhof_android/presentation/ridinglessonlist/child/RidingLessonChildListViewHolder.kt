package de.romqu.schimmelhof_android.presentation.ridinglessonlist.child

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import de.romqu.schimmelhof_android.R
import de.romqu.schimmelhof_android.data.RidingLessonDto.RidingLessonState.*
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
            ridingTextView.text = item.text
        }
    }
}