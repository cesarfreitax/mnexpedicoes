package com.cesar.mnexpedicoes.fragments.home

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.cesar.mnexpedicoes.R
import com.cesar.mnexpedicoes.activities.event.EventDetailsFragment
import com.cesar.mnexpedicoes.databinding.CellEventBinding
import com.cesar.mnexpedicoes.databinding.FragmentHomeBinding
import com.cesar.mnexpedicoes.fragments.home.adapter.ViewPagerAdapter
import com.cesar.mnexpedicoes.fragments.home.cell.EventCell
import com.cesar.mnexpedicoes.fragments.home.model.EventResponse
import com.cesar.mnexpedicoes.fragments.home.model.TripResponse
import com.cesar.mnexpedicoes.utils.PageItemSnapHelper
import io.github.enicolas.genericadapter.AdapterHolderType
import io.github.enicolas.genericadapter.adapter.GenericRecyclerAdapter
import io.github.enicolas.genericadapter.adapter.GenericRecylerAdapterDelegate

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val adapter = GenericRecyclerAdapter()
    private val trips = mutableListOf<TripResponse>()
    private val events = mutableListOf<EventResponse>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentHomeBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        createData()
        setupRecyclerView()
        setTripsRecyclerViewAnimation()
        setupViewPagerAdapter()
    }

    private fun setupViewPagerAdapter() {
        binding.vpgTrips.adapter = ViewPagerAdapter(trips, this@HomeFragment)
        binding.wdiDots.attachTo(binding.vpgTrips)
        autoSlider(binding.vpgTrips)
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rcvNextEvents.layoutManager = layoutManager
        binding.rcvNextEvents.adapter = adapter
        adapter.delegate = recyclerViewDelegate
        adapter.snapshot?.snapshotList = events
    }

    private var recyclerViewDelegate =
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
                    c.setupCell(event, this@HomeFragment, position + 1 == events.size)
                }
            }

            override fun didSelectItemAtIndex(adapter: GenericRecyclerAdapter, index: Int) {
                super.didSelectItemAtIndex(adapter, index)
                val fragment = EventDetailsFragment()
                val transaction = parentFragmentManager.beginTransaction()
                transaction.addToBackStack(null)
                val bundle = Bundle()
                bundle.putSerializable("event", events[index])
                fragment.arguments = bundle
                transaction.replace(R.id.fcv_fragment_container, fragment).commit()
            }
        }

    private fun createData() {
        trips.addAll(
            listOf(
                TripResponse(id = 1, title = "Jalapão", startDate = "25/05/2023", endDate = "29/05/2023", img = "https://viagemeturismo.abril.com.br/wp-content/uploads/2016/12/fervedouro-jalapao-tocantins.jpeg?quality=70&strip=info&w=1024&resize=1200,800", status = "available"),
                TripResponse(id = 2, title = "Fernando de Noronha", startDate = "10/02/2023", endDate = "14/02/2023", img = "https://www.viajali.com.br/wp-content/uploads/2017/03/fernando-de-noronha-0.jpg", status = "available"),
                TripResponse(id = 3, title = "Capitólio, Serra da Canastra e Tiradentes", startDate = "04/03/2023", endDate = "12/03/2023", img = "https://imgmd.net/images/v1/guia/1613817/canion-de-furnas.jpg", status = "warning"),
                TripResponse(id = 4, title = "Caraíva, Abrolhos e Porto Seguro", startDate = "15/07/2023", endDate = "24/07/2023", img = "https://a.cdn-hotels.com/gdcs/production196/d546/d00e74a4-31ed-405a-87c7-96af472077c9.jpg?impolicy=fcrop&w=800&h=533&q=medium", status = "soldout"),
                TripResponse(id = 5, title = "Paris", startDate = "20/04/2024", endDate = "27/04/2024", img = "https://www.passagenspromo.com.br/blog/wp-content/uploads/2019/04/viagem-para-paris.jpg", status = "warning"),
            )

        )

        events.addAll(
            listOf(
                EventResponse(id = 1, title = "Roberto Carlos", date = "10/03/2023", img = "https://veja.abril.com.br/wp-content/uploads/2022/06/ROBERTO-CARLOS-1248.jpg?quality=70&strip=info&w=733&resize=1200,800", status = "available", location = "Vivo Rio"),
                EventResponse(id = 2, title = "Alcione", date = "05/04/2023", img = "https://diariodorio.com/wp-content/uploads/2022/10/Alcione-2019-PROMO-AO-VIVO-fotos-@MarcosHermes-4-1-1.jpg", status = "warning", location = "Espaço Ribalta"),
                EventResponse(id = 3, title = "Elvis Experience", date = "20/06/2023", img = "https://journalmetro.com/wp-content/uploads/2022/08/CreditNathalieLemelin-CourtoisieLCQProductions.jpg?resize=1051%2C591", status = "available", location = "Teatro das Artes"),
                EventResponse(id = 4, title = "Roupa Nova", date = "30/08/2023", img = "https://i0.statig.com.br/bancodeimagens/4u/in/vm/4uinvm448ru9lhs5521avfy4f.jpg", status = "soldout", location = "Jeunesse Arena"),
                EventResponse()
            )
        )
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
            if (i >= trips.size - 1) i = 0
            autoSlider(viewPager)
        }
        Handler().postDelayed(rr, 3000)
    }
}