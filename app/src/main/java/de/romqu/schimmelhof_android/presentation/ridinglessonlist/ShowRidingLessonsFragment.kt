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
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import de.romqu.schimmelhof_android.R
import de.romqu.schimmelhof_android.databinding.FragmentShowRidingLessonsBinding
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.parent.RidingLessonParentListAdapter
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.refresh.RefreshLayout
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.util.attachSnapHelperWithListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class ShowRidingLessonsFragment : Fragment(R.layout.fragment_login) {

    @Inject
    lateinit var refreshLayout: RefreshLayout

    private var _binding: FragmentShowRidingLessonsBinding? = null
    private val binding get() = _binding!!

    private val lessonsAdapter by lazy {
        RidingLessonParentListAdapter(
            mutableListOf(),
            RecyclerView.RecycledViewPool()
        )
    }

    private val networkDisconnectedSnackbar by lazy {
        Snackbar.make(binding.root, "", Snackbar.LENGTH_INDEFINITE)
    }

    private val viewModel by viewModels<ShowRidingLessonsViewModel>()

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

        refreshLayout.setup(binding, lessonsAdapter)

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

        lifecycleScope.launch {
            viewModel.ridingLessonDayName.collect {
                binding.ridingLessonDayTextView.text = it
            }
        }

        lifecycleScope.launch {
            viewModel.ridingLessonItems.collect {
                lessonsAdapter.updateData(it)
            }
        }

        lifecycleScope.launch {
            viewModel.hideInitialLoading.collect {
                binding.shimmerViewContainer.hideShimmer()
                binding.shimmerViewContainer.visibility = View.GONE
            }
        }

        lifecycleScope.launch {
            viewModel.showList.collect {
                binding.ridingLessonDayParentRcv.visibility = View.VISIBLE
            }
        }

        lifecycleScope.launch {
            viewModel.showDayName.collect {
                binding.ridingLessonDayTextView.visibility = View.VISIBLE
            }
        }

        lifecycleScope.launch {
            viewModel.showNetworkDisconnectedMessage.collect {
                networkDisconnectedSnackbar.setText(it).show()
            }
        }

        lifecycleScope.launch {
            viewModel.dismissNetworkDisconnectedMessage.collect {
                networkDisconnectedSnackbar.dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}