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
import dagger.hilt.android.AndroidEntryPoint
import de.romqu.schimmelhof_android.R
import de.romqu.schimmelhof_android.databinding.FragmentShowRidingLessonsBinding
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.book.BookLessonLayoutFactory
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.book.BookLessonRunner
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.parent.RidingLessonParentListAdapter
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
    lateinit var onItemClickChannel: MutableSharedFlow<Int>

    @Inject
    lateinit var bookLessonRunner: BookLessonRunner

    @Inject
    lateinit var bookLayoutFactory: BookLessonLayoutFactory

    private val lessonsAdapter by lazy {
        RidingLessonParentListAdapter(
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
            viewModel.ridingLessonParentItems.collect {
                lessonsAdapter.updateData(it)
                lessonsAdapter.notifyChange()
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.dispatchListUpdates.collect {
                lessonsAdapter.updateData(it.list)
                it.diffResult?.dispatchUpdatesTo(lessonsAdapter)
            }
        }


        lifecycleScope.launchWhenCreated {
            viewModel.updateDayName.collect {
                binding.ridingLessonDayTextView.text = it
            }
        }

        bookLayoutFactory.create(lifecycleScope, requireContext())

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}