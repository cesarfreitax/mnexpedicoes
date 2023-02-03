package com.cesar.mnexpedicoes.fragments.events

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cesar.mnexpedicoes.R
import com.cesar.mnexpedicoes.databinding.CellItemIncludedBinding
import com.cesar.mnexpedicoes.databinding.FragmentEventDetailsBinding
import com.cesar.mnexpedicoes.fragments.events.cell.ItemIncludedCell
import com.cesar.mnexpedicoes.fragments.home.model.EventResponse
import com.cesar.mnexpedicoes.utils.*
import io.github.enicolas.genericadapter.AdapterHolderType
import io.github.enicolas.genericadapter.adapter.GenericRecyclerAdapter
import io.github.enicolas.genericadapter.adapter.GenericRecylerAdapterDelegate

class EventDetailsFragment : Fragment() {

    private lateinit var binding: FragmentEventDetailsBinding
    private val adapterIncluded = GenericRecyclerAdapter()
    private val adapterNotIncluded = GenericRecyclerAdapter()
    private var included = mutableListOf<String>()
    private var notIncluded = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_details, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val event = arguments?.getSerializable("event") as? EventResponse ?: EventResponse()
        binding = FragmentEventDetailsBinding.bind(view)
        binding.imgEvent.load(event.img, requireContext())
        binding.imgEventBlur.setRenderEffect(RenderEffect.createBlurEffect(50f, 50f, Shader.TileMode.MIRROR))
        binding.imgEventBlur.load(event.img, requireContext())
        binding.txtEventTitle.text = event.title
        binding.txtEventDate.text = event.date?.formatDateEvent()
        binding.txtEventLocation.text = event.location
        binding.txtEventDescription.text = event.description
        included = event.included!!
        setupRecyclerViewIncluded()
        notIncluded = event.notIncluded!!
        setupRecyclerViewNotIncluded()
        setupButtons()
    }

    private fun setupButtons() {
        setupDescriptionButton()
        setupIncludedButton()
        setupNotIncludedButton()
    }

    private fun setupNotIncludedButton() {
        binding.cdvEventNotIncluded.setOnClickListener {
            binding.lnrEventNotIncludedDescription.toggleVisibility(!binding.lnrEventNotIncludedDescription.isVisible)
            if (binding.lnrEventNotIncludedDescription.isVisible) {
                binding.txtEventNotIncludedTitle.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_up,
                    0
                )
                binding.lnrEventNotIncludedDescription.startAnimation(
                    animateDirection(
                        down = true,
                        context = requireContext()
                    )
                )
                animateAlpha(binding.lnrEventNotIncludedDescription)
            } else {
                binding.txtEventNotIncludedTitle.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_down,
                    0
                )
            }
        }
    }

    private fun setupIncludedButton() {
        binding.cdvEventIncluded.setOnClickListener {
            binding.lnrEventIncludedDescription.toggleVisibility(!binding.lnrEventIncludedDescription.isVisible)
            if (binding.lnrEventIncludedDescription.isVisible) {
                binding.txtEventIncludedTitle.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_up,
                    0
                )
                binding.lnrEventIncludedDescription.startAnimation(
                    animateDirection(
                        down = true,
                        context = requireContext()
                    )
                )
                animateAlpha(binding.lnrEventIncludedDescription)
            } else {
                binding.txtEventIncludedTitle.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_down,
                    0
                )
            }
        }
    }

    private fun setupDescriptionButton() {
        binding.cdvEventHeader.setOnClickListener {
            binding.lnrEventDescription.toggleVisibility(!binding.lnrEventDescription.isVisible)
            if (binding.lnrEventDescription.isVisible) {
                binding.txtEventHeader.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_up,
                    0
                )
                binding.lnrEventDescription.startAnimation(
                    animateDirection(
                        down = true,
                        context = requireContext()
                    )
                )
                animateAlpha(binding.lnrEventDescription)
            } else {
                binding.txtEventHeader.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_down,
                    0
                )
            }

        }
    }

    private fun setupRecyclerViewIncluded() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rcvIncluded.layoutManager = layoutManager
        binding.rcvIncluded.adapter = adapterIncluded
        adapterIncluded.delegate = recyclerViewDelegateIncluded
        adapterIncluded.snapshot?.snapshotList = included
    }

    private fun setupRecyclerViewNotIncluded() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rcvNotIncluded.layoutManager = layoutManager
        binding.rcvNotIncluded.adapter = adapterNotIncluded
        adapterNotIncluded.delegate = recyclerViewDelegateNotIncluded
        adapterNotIncluded.snapshot?.snapshotList = notIncluded
    }

    private var recyclerViewDelegateIncluded =
        object : GenericRecylerAdapterDelegate {

            override fun numberOfRows(adapter: GenericRecyclerAdapter): Int = included.size

            override fun registerCellAtPosition(
                adapter: GenericRecyclerAdapter,
                position: Int
            ): AdapterHolderType {
                return AdapterHolderType(
                    viewBinding = CellItemIncludedBinding::class.java,
                    clazz = ItemIncludedCell::class.java,
                    reuseIdentifier = 0
                )
            }

            override fun cellForPosition(
                adapter: GenericRecyclerAdapter,
                cell: RecyclerView.ViewHolder,
                position: Int
            ) {
                (cell as ItemIncludedCell).let { c ->
                    val item = included[position]
                    c.setupCell(item, fragment = this@EventDetailsFragment)
                }
            }

            override fun didSelectItemAtIndex(adapter: GenericRecyclerAdapter, index: Int) {
                super.didSelectItemAtIndex(adapter, index)
            }
        }

    private var recyclerViewDelegateNotIncluded =
        object : GenericRecylerAdapterDelegate {

            override fun numberOfRows(adapter: GenericRecyclerAdapter): Int = notIncluded.size

            override fun registerCellAtPosition(
                adapter: GenericRecyclerAdapter,
                position: Int
            ): AdapterHolderType {
                return AdapterHolderType(
                    viewBinding = CellItemIncludedBinding::class.java,
                    clazz = ItemIncludedCell::class.java,
                    reuseIdentifier = 0
                )
            }

            override fun cellForPosition(
                adapter: GenericRecyclerAdapter,
                cell: RecyclerView.ViewHolder,
                position: Int
            ) {
                (cell as ItemIncludedCell).let { c ->
                    val item = notIncluded[position]
                    c.setupCell(item, false, this@EventDetailsFragment)
                }
            }

            override fun didSelectItemAtIndex(adapter: GenericRecyclerAdapter, index: Int) {
                super.didSelectItemAtIndex(adapter, index)
            }
        }
}