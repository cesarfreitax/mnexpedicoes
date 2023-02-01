package com.cesar.mnexpedicoes.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cesar.mnexpedicoes.databinding.CellTripBinding
import com.cesar.mnexpedicoes.databinding.FragmentHomeBinding
import com.cesar.mnexpedicoes.fragments.home.cell.TripCell
import com.cesar.mnexpedicoes.fragments.home.model.TripResponse
import io.github.enicolas.genericadapter.AdapterHolderType
import io.github.enicolas.genericadapter.adapter.GenericRecyclerAdapter
import io.github.enicolas.genericadapter.adapter.GenericRecylerAdapterDelegate

class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        createData()
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rcvTrips.layoutManager = layoutManager
        binding.rcvTrips.adapter = adapter
        adapter.delegate = recyclerViewDelegate
        adapter.snapshot?.snapshotList = trips
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
                    c.setupCell(trip, context!!.applicationContext)
                }
            }
        }

    private fun createData() {
        trips.addAll(
            listOf(
                TripResponse(id = 1, title = "Jalapão", startDate = "25/05/2023", endDate = "29/05/2023", img = "https://www.google.com/search?q=jalapao&client=safari&rls=en&sxsrf=AJOqlzUK210aWx3LGR1P7yEsZgzCPog0LA:1675290893290&source=lnms&tbm=isch&sa=X&ved=2ahUKEwin8dv0sPX8AhV2gpUCHekABrAQ_AUoAXoECAEQAw&biw=1390&bih=795&dpr=2#imgrc=FBDF5nFr8sJTXM&imgdii=-7FAIKXv6Yn29M"),
                TripResponse(id = 2, title = "Fernando de Noronha", startDate = "10/02/2023", endDate = "14/02/2023", img = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.dicasdeviagem.com%2Ffernando-de-noronha%2F&psig=AOvVaw3ptstHaU37lemVZHKLaeJ2&ust=1675377711561000&source=images&cd=vfe&ved=0CBAQjRxqFwoTCMj527yy9fwCFQAAAAAdAAAAABAE"),
                TripResponse(id = 3, title = "Capitólio, Serra da Canastra e Tiradentes", startDate = "04/03/2023", endDate = "12/03/2023", img = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fguia.melhoresdestinos.com.br%2Fcapitolio-243-c.html&psig=AOvVaw2BFKByu0I_Cg4IkD87HKiz&ust=1675377753774000&source=images&cd=vfe&ved=0CBAQjRxqFwoTCLDxztCy9fwCFQAAAAAdAAAAABAE"),
                TripResponse(id = 4, title = "Caraíva, Abrolhos e Porto Seguro", startDate = "15/07/2023", endDate = "24/07/2023", img = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fjanelasabertas.com%2F2022%2F04%2F22%2Fonde-ficar-em-porto-seguro%2F&psig=AOvVaw1V6xywpxP9D6v36CbSMP1z&ust=1675377802479000&source=images&cd=vfe&ved=0CBAQjRxqFwoTCIjM--ey9fwCFQAAAAAdAAAAABAZ"),
                TripResponse(id = 5, title = "Paris", startDate = "20/04/2024", endDate = "27/04/2024", img = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.passagenspromo.com.br%2Fblog%2Fviagem-para-paris%2F&psig=AOvVaw0Ia3UtKeYuZgQ94OTgGZj8&ust=1675377838960000&source=images&cd=vfe&ved=0CBAQjRxqFwoTCPjvn_my9fwCFQAAAAAdAAAAABAE")
            )

        )
    }


}