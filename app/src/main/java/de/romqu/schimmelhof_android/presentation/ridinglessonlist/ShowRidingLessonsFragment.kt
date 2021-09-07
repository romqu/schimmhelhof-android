package de.romqu.schimmelhof_android.presentation.ridinglessonlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import de.romqu.schimmelhof_android.R
import de.romqu.schimmelhof_android.databinding.FragmentShowRidingLessonsBinding
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.book.BookLessonLayoutFactory
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.day.RidingLessonDayListAdapter
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.lesson.RidingLessonItem
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.util.attachSnapHelperWithListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class ShowRidingLessonsFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentShowRidingLessonsBinding? = null
    private val binding get() = _binding!!

    @Inject
    @Named(ON_ITEM_CLICK)
    lateinit var onItemClickChannel: MutableSharedFlow<RidingLessonItem>

    @Inject
    lateinit var bookLayoutFactory: BookLessonLayoutFactory

    private val lessonsAdapter by lazy {
        RidingLessonDayListAdapter(
            mutableListOf(),
            RecyclerView.RecycledViewPool(),
            onItemClickChannel
        )
    }

    val viewModel by viewModels<ShowRidingLessonsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentShowRidingLessonsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ConcatAdapter()

        binding.ridingLessonDayParentRcv.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = lessonsAdapter
        }

        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(binding.ridingLessonDayParentRcv)
        binding.ridingLessonDayParentRcv.attachSnapHelperWithListener(pagerSnapHelper) { position ->
            viewModel.onNextPage(position)
        }

        lifecycleScope.launchWhenCreated {
            viewModel.setInitialItems.collect {
                lessonsAdapter.updateData(it)
                lessonsAdapter.notifyChange()
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.scrollToPosition.collect {
                binding.ridingLessonDayParentRcv.scrollToPosition(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.dispatchListUpdates.collect {
                lessonsAdapter.updateData(it.list)
                it.diffResult?.dispatchUpdatesTo(lessonsAdapter)
                viewModel.onListDispatched()
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.updateDayName.collect {
                binding.ridingLessonDayTextView.text = it
            }
        }

        bookLayoutFactory.create(lifecycleScope, requireContext(), viewModel)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}