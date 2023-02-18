package com.cesar.mnexpedicoes.fragments.home.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.cesar.mnexpedicoes.R
import com.cesar.mnexpedicoes.activities.main.presentation.MainActivity
import com.cesar.mnexpedicoes.databinding.CellEventBinding
import com.cesar.mnexpedicoes.databinding.FragmentHomeBinding
import com.cesar.mnexpedicoes.fragments.events.presentation.EventDetailsFragment
import com.cesar.mnexpedicoes.fragments.home.adapter.ViewPagerAdapter
import com.cesar.mnexpedicoes.fragments.home.cell.EventCell
import com.cesar.mnexpedicoes.fragments.home.model.EventResponse
import com.cesar.mnexpedicoes.utils.PageItemSnapHelper
import io.github.enicolas.genericadapter.AdapterHolderType
import io.github.enicolas.genericadapter.adapter.GenericRecyclerAdapter
import io.github.enicolas.genericadapter.adapter.GenericRecylerAdapterDelegate

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeFragmentViewModel by viewModels()
    private val adapter = GenericRecyclerAdapter()
    private var skeleton: HomeSkeleton? = null

    private var genericEvents = listOf<EventResponse>()
    private var trips = listOf<EventResponse>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentHomeBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
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
            setupRecyclerView()
            setupViewPagerAdapter()
        }
    }

    private fun navigateToPhotoAlbum() {
        binding.cdvNavigatePhotoAlbum.setOnClickListener {
            val url = "https://www.flickr.com/photos/148352424@N08/albums"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }

    private fun setupViewPagerAdapter() {
        binding.vpgTrips.adapter = ViewPagerAdapter(viewModel.getTrips(), parentFragmentManager, this@HomeFragment)
        binding.wdiDots.attachTo(binding.vpgTrips)
    }

    private fun setupRecyclerView() {
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
                    c.setupCell(event, this@HomeFragment, isFirstCell = position == 0)
                }
            }

            override fun didSelectItemAtIndex(adapter: GenericRecyclerAdapter, index: Int) {
                super.didSelectItemAtIndex(adapter, index)
                val fragment = EventDetailsFragment()
                val transaction = parentFragmentManager.beginTransaction()
                transaction.addToBackStack(null)
                val bundle = Bundle()
                bundle.putSerializable("event", genericEvents[index])
                fragment.arguments = bundle
                transaction.replace(R.id.fcv_fragment_container, fragment).commit()
            }
        }

    private fun setTripsRecyclerViewAnimation() {
        binding.rcvNextEvents.addItemDecoration(com.cesar.mnexpedicoes.utils.HorizontalItemDecoration())
        PageItemSnapHelper().attachToRecyclerView(binding.rcvNextEvents)
    }

    private fun autoSlider(viewPager: ViewPager2) {
        var i = 0
        val rr = Runnable {
            val position = viewPager.currentItem
            if (position >= i && position != trips.size - 1) {
                i = position
                i++
            } else if (position < i - 1) {
                i = position
                i++
            }
            viewPager.setCurrentItem(i, true)
            i++
            if (i >= viewModel.getTrips().size - 1) i = 0
            autoSlider(viewPager)
        }
        Handler().postDelayed(rr, 6000)
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as MainActivity).apply {
            bottomBarHidden = false
            backBtnVisible = false
        }
    }
}