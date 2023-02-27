package com.cesar.mnexpedicoes.features.home.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cesar.mnexpedicoes.activities.main.presentation.MainActivity
import com.cesar.mnexpedicoes.databinding.CellEventBinding
import com.cesar.mnexpedicoes.databinding.FragmentHomeBinding
import com.cesar.mnexpedicoes.features.events.presentation.EventDetailsFragment
import com.cesar.mnexpedicoes.features.home.adapter.ViewPagerAdapter
import com.cesar.mnexpedicoes.features.home.cell.EventCell
import com.cesar.mnexpedicoes.features.home.model.EventResponse
import com.cesar.mnexpedicoes.utils.*
import io.github.enicolas.genericadapter.AdapterHolderType
import io.github.enicolas.genericadapter.adapter.GenericRecyclerAdapter
import io.github.enicolas.genericadapter.adapter.GenericRecylerAdapterDelegate

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = requireNotNull(_binding)
    private val viewModel: HomeFragmentViewModel by viewModels()
    private val adapter = GenericRecyclerAdapter()
    private var skeleton: HomeSkeleton? = null

    private var genericEvents = listOf<EventResponse>()
    private var trips = listOf<EventResponse>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFragment()
    }

    private fun setupFragment() {
        setupSkeleton()
        skeleton?.showSkeleton()
        setTripsRecyclerViewAnimation()
        fetchData()
        navigateToPhotoAlbum()
    }

    private fun setupSkeleton() {
        skeleton = HomeSkeleton(binding.ctlMainContainer)
    }

    private fun fetchData() {
        viewModel.getSchedule {
            skeleton?.hideSkeleton()
            trips = viewModel.getTrips()
            genericEvents = viewModel.getGenericEvents()
            setupRecyclerViewGenericEvents()
            setupViewPagerAdapter()
        }
    }

    private fun navigateToPhotoAlbum() {
        binding.cdvNavigatePhotoAlbum.setOnClickListener {
            this@HomeFragment.openBrowserWith(Constants.ALBUM_URL)
        }
    }

    private fun setupViewPagerAdapter() {
        binding.vpgTrips.adapter =
            ViewPagerAdapter(viewModel.getTrips(), parentFragmentManager, this@HomeFragment)
        binding.wdiDots.attachTo(binding.vpgTrips)
    }

    private fun setupRecyclerViewGenericEvents() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rcvNextEvents.layoutManager = layoutManager
        binding.rcvNextEvents.adapter = adapter
        adapter.delegate = recyclerViewDelegate
        adapter.snapshot?.snapshotList = viewModel.getGenericEvents()
    }

    private var recyclerViewDelegate =
        object : GenericRecylerAdapterDelegate {

            override fun numberOfRows(adapter: GenericRecyclerAdapter): Int = genericEvents.size

            override fun registerCellAtPosition(
                adapter: GenericRecyclerAdapter,
                position: Int
            ): AdapterHolderType {
                return AdapterHolderType(
                    viewBinding = CellEventBinding::class.java,
                    clazz = EventCell::class.java,
                    reuseIdentifier = 0
                )
            }

            override fun cellForPosition(
                adapter: GenericRecyclerAdapter,
                cell: RecyclerView.ViewHolder,
                position: Int
            ) {
                (cell as EventCell).let { c ->
                    val event = genericEvents[position]
                    c.setupCell(
                        event = event,
                        fragment = this@HomeFragment,
                        isVertical = false,
                        isLastCell = position == (genericEvents.size - 1)
                    )
                }
            }

            override fun didSelectItemAtIndex(adapter: GenericRecyclerAdapter, index: Int) {
                super.didSelectItemAtIndex(adapter, index)
                val args = Bundle()
                args.putSerializable("event", genericEvents[index])
                push(EventDetailsFragment(), args)
            }
        }

    private fun setTripsRecyclerViewAnimation() {
        binding.rcvNextEvents.addItemDecoration(HorizontalItemDecoration())
        PageItemSnapHelper().attachToRecyclerView(binding.rcvNextEvents)
    }

    override fun onStart() {
        super.onStart()
        val activity = requireActivity()
        if (activity is MainActivity) {
            activity.apply {
                bottomBarHidden = false
                backBtnVisible = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}