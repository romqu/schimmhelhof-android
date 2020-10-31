package de.romqu.schimmelhof_android.presentation.ridinglessonlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import dagger.hilt.android.AndroidEntryPoint
import de.romqu.schimmelhof_android.R
import de.romqu.schimmelhof_android.data.ridinglesson.RidingLessonRepository
import de.romqu.schimmelhof_android.databinding.FragmentShowRidingLessonsBinding
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.child.RidingLessonChildItem
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.parent.RidingLessonParentItem
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.parent.RidingLessonParentListAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class ShowRidingLessonsFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentShowRidingLessonsBinding? = null
    private val binding get() = _binding!!

    private val lessonsAdapter  by lazy {
        RidingLessonParentListAdapter(
            mutableListOf(),
            RecyclerView.RecycledViewPool()
        )
    }

    val viewModel by viewModels<ShowRidingLessonsViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentShowRidingLessonsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ridingLessonDayParentRcv.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = lessonsAdapter
        }

        PagerSnapHelper().attachToRecyclerView(binding.ridingLessonDayParentRcv)

        lifecycleScope.launchWhenCreated {
            viewModel.ridingLessonItems.collect {
                lessonsAdapter.updateData(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}