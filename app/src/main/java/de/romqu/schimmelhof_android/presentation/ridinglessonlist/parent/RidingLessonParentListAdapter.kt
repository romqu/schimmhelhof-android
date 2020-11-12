package de.romqu.schimmelhof_android.presentation.ridinglessonlist.parent

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.romqu.schimmelhof_android.databinding.ItemParentRidinglessonBinding

class RidingLessonParentListAdapter(
    private val items: MutableList<RidingLessonParentItem>,
    private val recycledViewPool: RecyclerView.RecycledViewPool,
) : RecyclerView.Adapter<RidingLessonParentListViewHolder>() {

    // TODO: Might should use delegate pattern at some point
    companion object{
        const val EMPTY_VIEW = 0
        const val LESSONS_VIEW = 1
    }

    override fun getItemViewType(position: Int): Int =
        if(items.size == 0) EMPTY_VIEW else LESSONS_VIEW

    override fun getItemCount(): Int = if(items.size == 0) 1 else items.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RidingLessonParentListViewHolder {
        return RidingLessonParentListViewHolder(
            ItemParentRidinglessonBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
        )
    }

    override fun onBindViewHolder(
        holder: RidingLessonParentListViewHolder,
        position: Int,
    ) = holder.bind(items[position], recycledViewPool)

    fun updateData(list: List<RidingLessonParentItem>){
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}