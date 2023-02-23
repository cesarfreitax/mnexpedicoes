package com.cesar.mnexpedicoes.fragments.schedule.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cesar.mnexpedicoes.R
import com.cesar.mnexpedicoes.activities.main.presentation.MainActivity
import com.cesar.mnexpedicoes.databinding.CellEventBinding
import com.cesar.mnexpedicoes.databinding.CellFilterTypeBinding
import com.cesar.mnexpedicoes.databinding.FragmentScheduleBinding
import com.cesar.mnexpedicoes.fragments.events.presentation.EventDetailsFragment
import com.cesar.mnexpedicoes.fragments.home.cell.EventCell
import com.cesar.mnexpedicoes.fragments.home.model.EventResponse
import com.cesar.mnexpedicoes.fragments.schedule.cell.FilterTypeCell
import com.cesar.mnexpedicoes.fragments.schedule.model.FilterResponse
import com.cesar.mnexpedicoes.utils.hideKeyboard
import com.cesar.mnexpedicoes.utils.parseHtml
import com.cesar.mnexpedicoes.utils.toggleVisibility
import com.cesar.mnexpedicoes.utils.unaccent
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import io.github.enicolas.genericadapter.AdapterHolderType
import io.github.enicolas.genericadapter.adapter.GenericRecyclerAdapter
import io.github.enicolas.genericadapter.adapter.GenericRecylerAdapterDelegate

class ScheduleFragment : Fragment() {

    private val viewModel: ScheduleFragmentViewModel by viewModels()
    private lateinit var binding: FragmentScheduleBinding
    private var skeleton: ScheduleSkeleton? = null
    private val adapterFilters = GenericRecyclerAdapter()
    private val adapterEvents = GenericRecyclerAdapter()
    private var events = mutableListOf<EventResponse>()
    private var all = FilterResponse("all", false)
    private var trip = FilterResponse("trip", false)
    private var show = FilterResponse("show", false)
    private var theater = FilterResponse("theater", false)
    private var nature = FilterResponse("nature", false)
    private var cinema = FilterResponse("cinema", false)
    private var filters = mutableListOf(all, trip, show, theater, nature, cinema)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentScheduleBinding.bind(view)
        setupFragment()
    }

    private fun setupFragment() {
        setupSkeleton()
        skeleton?.showSkeleton()
        viewModel.getSchedule {
            skeleton?.hideSkeleton()
            setupRecyclerViewEvents()
            setupRecyclerViewFilters()
            setupFilterAllByDefault()
            setupSearchView()
        }
    }

    private fun setupSkeleton() {
        skeleton = ScheduleSkeleton(binding.ctlMainContainer)
    }

    private fun setupSearchView() {
        binding.srcEvents.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                events = viewModel.events.filter {
                    it.title.toString().unaccent().contains(query.toString().unaccent(), true)
                }.toMutableList()
                setFilter(query.toString())
                emptyEventsHandler()
                adapterEvents.notifyDataSetChanged()
                this@ScheduleFragment.hideKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    setupFilterAllByDefault()
                    adapterEvents.notifyDataSetChanged()
                }
                return true
            }

        })
    }

    private fun setupRecyclerViewFilters() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rcvFilters.layoutManager = layoutManager
        binding.rcvFilters.adapter = adapterFilters
        adapterFilters.delegate = recyclerViewDelegateFilters
        adapterFilters.snapshot?.snapshotList = filters
    }

    private fun setupRecyclerViewEvents() {
        val layoutManager = FlexboxLayoutManager(requireContext(), FlexDirection.COLUMN)
        layoutManager.justifyContent = JustifyContent.FLEX_END
        binding.rcvEvents.layoutManager = layoutManager
        binding.rcvEvents.adapter = adapterEvents
        binding.rcvEvents.isNestedScrollingEnabled = false
        adapterEvents.delegate = recyclerViewDelegateEvents
        adapterEvents.snapshot?.snapshotList = events
    }

    private var recyclerViewDelegateEvents =
        object : GenericRecylerAdapterDelegate {

            override fun numberOfRows(adapter: GenericRecyclerAdapter): Int = events.size

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
                    val event = events[position]
                    c.setupCell(event, this@ScheduleFragment, true)
                }
            }

            override fun didSelectItemAtIndex(adapter: GenericRecyclerAdapter, index: Int) {
                super.didSelectItemAtIndex(adapter, index)
                navigateToEventDetails(index)
            }
        }

    private fun navigateToEventDetails(index: Int) {
        val fragment = EventDetailsFragment()
        val transaction = parentFragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        val bundle = Bundle()
        bundle.putSerializable("event", events[index])
        fragment.arguments = bundle
        transaction.replace(R.id.fcv_fragment_container, fragment).commit()
    }

    private var recyclerViewDelegateFilters =
        object : GenericRecylerAdapterDelegate {

            override fun numberOfRows(adapter: GenericRecyclerAdapter): Int = filters.size

            override fun registerCellAtPosition(
                adapter: GenericRecyclerAdapter,
                position: Int
            ): AdapterHolderType {
                return AdapterHolderType(
                    viewBinding = CellFilterTypeBinding::class.java,
                    clazz = FilterTypeCell::class.java,
                    reuseIdentifier = 0
                )
            }

            override fun cellForPosition(
                adapter: GenericRecyclerAdapter,
                cell: RecyclerView.ViewHolder,
                position: Int
            ) {
                (cell as FilterTypeCell).let { c ->
                    val filter = filters[position]
                    c.setupCell(filter, this@ScheduleFragment)
                }
            }

            override fun didSelectItemAtIndex(adapter: GenericRecyclerAdapter, index: Int) {
                super.didSelectItemAtIndex(adapter, index)

                val isNotSelected = !filters[index].isSelected
                if (isNotSelected) {
                    setFilter(filters[index].type)
                    events = if (filters[index].type == "all") {
                        viewModel.events
                    } else {
                        val selectedType = filters[index].type
                        viewModel.getEventsFiltered(selectedType)
                    }
                    emptyEventsHandler()
                    adapterEvents.notifyDataSetChanged()
                }

            }
        }

    private fun setupFilterAllByDefault() {
        setFilter("all")
        events = viewModel.events
        emptyEventsHandler()
        adapterEvents.notifyDataSetChanged()
    }

    private fun setFilter(type: String) {
        filters.forEach {
            it.isSelected = it.type == type
        }
        setupRecyclerViewFilters()
        binding.txtFiltering.text =
            getString(R.string.generic_filtering_for, setStringByType(type)).parseHtml()
    }

    private fun setStringByType(type: String): String {
        when (type) {
            "all" -> {
                return "Todos"
            }
            "show" -> {
                return "Show"
            }
            "theater" -> {
                return "Teatro"
            }
            "nature" -> {
                return "Natureza"
            }
            "trip" -> {
                return "Viagem"
            }
            "cinema" -> {
                return "Cinema"
            }
            else -> return type
        }
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as MainActivity).apply {
            bottomBarHidden = false
            backBtnVisible = false
        }
    }

    private fun emptyEventsHandler() {
        binding.txtPlaceholderEmptyEvents.toggleVisibility(events.isEmpty())
        binding.rcvEvents.toggleVisibility(events.isNotEmpty())
    }

}