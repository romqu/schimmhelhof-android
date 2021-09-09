package de.romqu.schimmelhof_android.presentation.ridinglessonlist.lesson

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.romqu.schimmelhof_android.databinding.ItemChildRidinglessonBinding
import de.romqu.schimmelhof_android.databinding.ItemEmptyListBinding
import de.romqu.schimmelhof_android.presentation.shared.recyclerview.EmptyItem
import de.romqu.schimmelhof_android.presentation.shared.recyclerview.EmptyListViewHolder
import kotlinx.coroutines.flow.MutableSharedFlow

class RidingLessonListAdapter(
    private val items: MutableList<RidingLessonItem>,
    private val onItemClickChannel: MutableSharedFlow<RidingLessonItem>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val EMPTY_VIEW_TYPE = 0
        const val LESSONS_VIEW_TYPE = 1
    }

    override fun getItemViewType(position: Int): Int =
        if (items.size == 0) EMPTY_VIEW_TYPE else LESSONS_VIEW_TYPE

    override fun getItemCount(): Int = if (items.size == 0) 1 else items.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {

        return when (viewType) {
            EMPTY_VIEW_TYPE -> EmptyListViewHolder(
                ItemEmptyListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> RidingLessonListViewHolder(
                ItemChildRidinglessonBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                )
            ) { position ->
                onItemClickChannel.tryEmit(items[position])
            }
        }
    }

    fun updateItems(list: List<RidingLessonItem>) {
        items.clear()
        items.addAll(list)
    }

    fun notifyChanged() {
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        when (holder) {
            is RidingLessonListViewHolder -> holder.bind(items[position])
            is EmptyListViewHolder -> holder.bind(EmptyItem("Keine Stunden verfügbar :("))
        }
    }

}