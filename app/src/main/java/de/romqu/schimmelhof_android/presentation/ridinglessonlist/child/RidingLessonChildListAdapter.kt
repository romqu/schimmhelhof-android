package de.romqu.schimmelhof_android.presentation.ridinglessonlist.child

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.romqu.schimmelhof_android.databinding.ItemChildRidinglessonBinding

class RidingLessonChildListAdapter(
    private val list: MutableList<RidingLessonChildItem>,
) : RecyclerView.Adapter<RidingLessonChildListViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RidingLessonChildListViewHolder = RidingLessonChildListViewHolder(
        ItemChildRidinglessonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(
        holderChild: RidingLessonChildListViewHolder,
        position: Int,
    ) = holderChild.bind(list[position])

    override fun getItemCount(): Int = list.size
}