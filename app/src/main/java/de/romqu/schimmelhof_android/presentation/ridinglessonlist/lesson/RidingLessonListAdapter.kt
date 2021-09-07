package de.romqu.schimmelhof_android.presentation.ridinglessonlist.lesson

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.romqu.schimmelhof_android.databinding.ItemChildRidinglessonBinding
import kotlinx.coroutines.flow.MutableSharedFlow

class RidingLessonListAdapter(
    private val list: MutableList<RidingLessonItem>,
    private val onItemClickChannel: MutableSharedFlow<RidingLessonItem>,
) : RecyclerView.Adapter<RidingLessonListViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RidingLessonListViewHolder = RidingLessonListViewHolder(
        ItemChildRidinglessonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
    ) { position ->
        Log.d("AAAAA", "EMIT")
        onItemClickChannel.tryEmit(list[position])
    }

    override fun onBindViewHolder(
        holder: RidingLessonListViewHolder,
        position: Int,
    ) = holder.bind(list[position])

    override fun getItemCount(): Int = list.size
}