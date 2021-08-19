package de.romqu.schimmelhof_android.presentation.ridinglessonlist.parent

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.romqu.schimmelhof_android.databinding.ItemParentRidinglessonBinding

class RidingLessonParentListAdapter(
    private val items: MutableList<RidingLessonParentItem>,
    private val recycledViewPool: RecyclerView.RecycledViewPool,
) : RecyclerView.Adapter<RidingLessonParentListViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RidingLessonParentListViewHolder = RidingLessonParentListViewHolder(
        ItemParentRidinglessonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
    )

    override fun onBindViewHolder(
        holder: RidingLessonParentListViewHolder,
        position: Int,
    ) = holder.bind(items[position], recycledViewPool)

    override fun getItemCount(): Int = items.size

    fun updateData(list: List<RidingLessonParentItem>){
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}