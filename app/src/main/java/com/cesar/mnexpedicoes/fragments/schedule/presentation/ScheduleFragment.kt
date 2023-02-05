package com.cesar.mnexpedicoes.fragments.schedule.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cesar.mnexpedicoes.R
import com.cesar.mnexpedicoes.databinding.CellEventBinding
import com.cesar.mnexpedicoes.databinding.CellFilterTypeBinding
import com.cesar.mnexpedicoes.databinding.FragmentScheduleBinding
import com.cesar.mnexpedicoes.fragments.events.presentation.EventDetailsFragment
import com.cesar.mnexpedicoes.fragments.home.cell.EventCell
import com.cesar.mnexpedicoes.fragments.home.model.EventResponse
import com.cesar.mnexpedicoes.fragments.home.model.Ticket
import com.cesar.mnexpedicoes.fragments.schedule.cell.FilterTypeCell
import com.cesar.mnexpedicoes.fragments.schedule.model.FilterResponse
import io.github.enicolas.genericadapter.AdapterHolderType
import io.github.enicolas.genericadapter.adapter.GenericRecyclerAdapter
import io.github.enicolas.genericadapter.adapter.GenericRecylerAdapterDelegate

class ScheduleFragment : Fragment() {

    private lateinit var binding: FragmentScheduleBinding
    private val adapterFilters = GenericRecyclerAdapter()
    private val adapterEvents = GenericRecyclerAdapter()
    private val events = mutableListOf<EventResponse>()
    private var all = FilterResponse("all", true)
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
        createData()
        setupRecyclerViewEvents()
        setupRecyclerViewFilters()
        binding.txtFiltering.text = getString(R.string.generic_filtering_for, "all")
    }

    private fun setupRecyclerViewFilters() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rcvFilters.layoutManager = layoutManager
        binding.rcvFilters.adapter = adapterFilters
        adapterFilters.delegate = recyclerViewDelegateFilters
        adapterFilters.snapshot?.snapshotList = filters
    }

    private fun setupRecyclerViewEvents() {
        binding.rcvEvents.layoutManager = LinearLayoutManager(requireContext())
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
                val fragment = EventDetailsFragment()
                val transaction = parentFragmentManager.beginTransaction()
                transaction.addToBackStack(null)
                val bundle = Bundle()
                bundle.putSerializable("event", events[index])
                fragment.arguments = bundle
                transaction.replace(R.id.fcv_fragment_container, fragment).commit()
            }
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
                val checkIfIsNotSelected = !filters[index].isSelected
                if (checkIfIsNotSelected) {
                    filters.forEach {
                        it.isSelected = false
                    }
                    filters[index].isSelected = !filters[index].isSelected
                    setupRecyclerViewFilters()
                    binding.rcvFilters.scrollToPosition(index)
                    binding.txtFiltering.text = getString(
                        R.string.generic_filtering_for,
                        setStringByType(filters[index].type)
                    )
                    fetchData()
                }

            }
        }

    private fun fetchData() {

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
        }
        return ""
    }

    private fun createData() {
        events.clear()
        events.addAll(
            listOf(
                EventResponse(id = 1, type = "show", title = "Roberto Carlos", date = "10/03/2023", img = "https://veja.abril.com.br/wp-content/uploads/2022/06/ROBERTO-CARLOS-1248.jpg?quality=70&strip=info&w=733&resize=1200,800", status = "available", location = arrayListOf("Vivo Rio"), hour = "16:00", description = "Em suas últimas edições, o projeto foi considerado o mais buscado da folia nacional em São Paulo e fora da internet o público fez jus aos números: todas as apresentações tiveram seus ingressos esgotados. Com repertório sempre cheio de novidades que passa por sucessos de Olodum, Banda Eva, Ara Ketu, Daniela Mercury, Gilberto Gil, Caetano Veloso, Novos Baianos e Ivete Sangalo, o Bloco do Silva chega mais uma vez a Salvador com convidados especiais pra todo mundo dançar e cantar junto do começo ao fim.", included = arrayListOf("Transporte porta a porta", "Fotografia", "Guia", "Guia Auxiliar"), notIncluded = arrayListOf("Extras de qualquer natureza", "Bebidas alcoólicas"), tickets = arrayListOf(
                    Ticket("full_price", price = "120,00"), Ticket("full_price", ticketTitle = "VIP", price = "300,00"), Ticket("half_price", price = "60,00")
                )),
                EventResponse(id = 2, type = "show", title = "Alcione", date = "05/04/2023", img = "https://diariodorio.com/wp-content/uploads/2022/10/Alcione-2019-PROMO-AO-VIVO-fotos-@MarcosHermes-4-1-1.jpg", status = "warning", location = arrayListOf("Espaço Ribalta"), hour = "16:00", description = "Em suas últimas edições, o projeto foi considerado o mais buscado da folia nacional em São Paulo e fora da internet o público fez jus aos números: todas as apresentações tiveram seus ingressos esgotados. Com repertório sempre cheio de novidades que passa por sucessos de Olodum, Banda Eva, Ara Ketu, Daniela Mercury, Gilberto Gil, Caetano Veloso, Novos Baianos e Ivete Sangalo, o Bloco do Silva chega mais uma vez a Salvador com convidados especiais pra todo mundo dançar e cantar junto do começo ao fim.", included = arrayListOf("Transporte porta a porta", "Fotografia", "Guia", "Guia Auxiliar"), notIncluded = arrayListOf("Extras de qualquer natureza", "Bebidas alcoólicas"), tickets = arrayListOf(
                    Ticket(ticketType = "full_price", price = "200")
                )),
                EventResponse(id = 3, type = "show", title = "Elvis Experience", date = "20/06/2023", img = "https://journalmetro.com/wp-content/uploads/2022/08/CreditNathalieLemelin-CourtoisieLCQProductions.jpg?resize=1051%2C591", status = "available", location = arrayListOf("Teatro das Artes"), hour = "16:00", description = "Em suas últimas edições, o projeto foi considerado o mais buscado da folia nacional em São Paulo e fora da internet o público fez jus aos números: todas as apresentações tiveram seus ingressos esgotados. Com repertório sempre cheio de novidades que passa por sucessos de Olodum, Banda Eva, Ara Ketu, Daniela Mercury, Gilberto Gil, Caetano Veloso, Novos Baianos e Ivete Sangalo, o Bloco do Silva chega mais uma vez a Salvador com convidados especiais pra todo mundo dançar e cantar junto do começo ao fim.", included = arrayListOf("Transporte porta a porta", "Fotografia", "Guia", "Guia Auxiliar"), notIncluded = arrayListOf("Extras de qualquer natureza", "Bebidas alcoólicas"), tickets = arrayListOf(
                    Ticket(ticketType = "full_price", price = "160,00")
                )),
                EventResponse(id = 4, type = "show", title = "Roupa Nova", date = "30/08/2023", img = "https://i0.statig.com.br/bancodeimagens/4u/in/vm/4uinvm448ru9lhs5521avfy4f.jpg", status = "soldout", location = arrayListOf("Jeunesse Arena"), hour = "16:00", description = "Em suas últimas edições, o projeto foi considerado o mais buscado da folia nacional em São Paulo e fora da internet o público fez jus aos números: todas as apresentações tiveram seus ingressos esgotados. Com repertório sempre cheio de novidades que passa por sucessos de Olodum, Banda Eva, Ara Ketu, Daniela Mercury, Gilberto Gil, Caetano Veloso, Novos Baianos e Ivete Sangalo, o Bloco do Silva chega mais uma vez a Salvador com convidados especiais pra todo mundo dançar e cantar junto do começo ao fim.", included = arrayListOf("Transporte porta a porta", "Fotografia", "Guia", "Guia Auxiliar"), notIncluded = arrayListOf("Extras de qualquer natureza", "Bebidas alcoólicas"), tickets = arrayListOf(
                    Ticket(ticketType = "full_price", price = "300,00")
                ))
            )
        )
    }
}