package com.cesar.mnexpedicoes.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.cesar.mnexpedicoes.databinding.CellTripBinding
import com.cesar.mnexpedicoes.databinding.FragmentHomeBinding
import com.cesar.mnexpedicoes.fragments.home.adapter.ViewPagerAdapter
import com.cesar.mnexpedicoes.fragments.home.cell.TripCell
import com.cesar.mnexpedicoes.fragments.home.model.TripResponse
import io.github.enicolas.genericadapter.AdapterHolderType
import io.github.enicolas.genericadapter.adapter.GenericRecyclerAdapter
import io.github.enicolas.genericadapter.adapter.GenericRecylerAdapterDelegate


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val adapter = GenericRecyclerAdapter()
    private val trips = mutableListOf<TripResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
    }

    private fun setupRecyclerView() {
//        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//        binding.rcvTrips.layoutManager = layoutManager
//        binding.rcvTrips.adapter = adapter
//        adapter.delegate = recyclerViewDelegate
//        adapter.snapshot?.snapshotList = trips
    }

    private var recyclerViewDelegate =
        object : GenericRecylerAdapterDelegate {

            override fun numberOfRows(adapter: GenericRecyclerAdapter): Int = trips.size

            override fun registerCellAtPosition(
                adapter: GenericRecyclerAdapter,
                position: Int
            ): AdapterHolderType {
                return AdapterHolderType(
                    viewBinding = CellTripBinding::class.java,
                    clazz = TripCell::class.java,
                    reuseIdentifier = 0
                )
            }

            override fun cellForPosition(
                adapter: GenericRecyclerAdapter,
                cell: RecyclerView.ViewHolder,
                position: Int
            ) {
                (cell as TripCell).let { c ->
                    val trip = trips[position]
                    c.setupCell(trip, this@HomeFragment)

                }
            }

            override fun didSelectItemAtIndex(adapter: GenericRecyclerAdapter, index: Int) {
                super.didSelectItemAtIndex(adapter, index)
            }
        }

    private fun createData() {
        trips.addAll(
            listOf(
                TripResponse(id = 1, title = "Jalapão", startDate = "25/05/2023", endDate = "29/05/2023", img = "https://viagemeturismo.abril.com.br/wp-content/uploads/2016/12/fervedouro-jalapao-tocantins.jpeg?quality=70&strip=info&w=1024&resize=1200,800", status = "available"),
                TripResponse(id = 2, title = "Fernando de Noronha", startDate = "10/02/2023", endDate = "14/02/2023", img = "https://www.viajali.com.br/wp-content/uploads/2017/03/fernando-de-noronha-0.jpg", status = "available"),
                TripResponse(id = 3, title = "Capitólio, Serra da Canastra e Tiradentes", startDate = "04/03/2023", endDate = "12/03/2023", img = "https://imgmd.net/images/v1/guia/1613817/canion-de-furnas.jpg", status = "warning"),
                TripResponse(id = 4, title = "Caraíva, Abrolhos e Porto Seguro", startDate = "15/07/2023", endDate = "24/07/2023", img = "https://a.cdn-hotels.com/gdcs/production196/d546/d00e74a4-31ed-405a-87c7-96af472077c9.jpg?impolicy=fcrop&w=800&h=533&q=medium", status = "soldout"),
                TripResponse(id = 5, title = "Paris", startDate = "20/04/2024", endDate = "27/04/2024", img = "https://www.passagenspromo.com.br/blog/wp-content/uploads/2019/04/viagem-para-paris.jpg", status = "warning")
            )

        )
    }

    private fun setTripsRecyclerViewAnimation() {
//        binding.rcvTrips.addItemDecoration(com.cesar.mnexpedicoes.utils.HorizontalItemDecoration())
//        PageItemSnapHelper().attachToRecyclerView(binding.rcvTrips)
    }
}