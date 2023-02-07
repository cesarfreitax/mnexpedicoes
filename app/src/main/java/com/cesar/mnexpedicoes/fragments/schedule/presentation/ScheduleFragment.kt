package com.cesar.mnexpedicoes.fragments.schedule.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.fragment.app.Fragment
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
import com.cesar.mnexpedicoes.fragments.home.model.Ticket
import com.cesar.mnexpedicoes.fragments.schedule.cell.FilterTypeCell
import com.cesar.mnexpedicoes.fragments.schedule.model.FilterResponse
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import io.github.enicolas.genericadapter.AdapterHolderType
import io.github.enicolas.genericadapter.adapter.GenericRecyclerAdapter
import io.github.enicolas.genericadapter.adapter.GenericRecylerAdapterDelegate

class ScheduleFragment : Fragment() {

    private lateinit var binding: FragmentScheduleBinding
    private val adapterFilters = GenericRecyclerAdapter()
    private val adapterEvents = GenericRecyclerAdapter()
    private val events = ArrayList<EventResponse>()
    private var eventsFiltered = mutableListOf<EventResponse>()
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
        createData()
        setupRecyclerViewEvents()
        setupRecyclerViewFilters()
        setupFilterAllByDefault()
        setupSearchView()
    }

    private fun setupSearchView() {
        binding.srcEvents.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val eventsCopy = events.toList()
                eventsFiltered = eventsCopy.filter { it.title!!.contains(query.toString(), true) }.toMutableList()
                filters.forEach { it.isSelected = false }
                setupRecyclerViewFilters()
                setupTxtFilterHint(query)
                closeKeyboard()
                adapterEvents.notifyDataSetChanged()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    val eventsCopy = events.toList()
                    eventsFiltered = eventsCopy.toMutableList()
                    filters.first().isSelected = true
                    setupRecyclerViewFilters()
                    setupTxtFilterHint("Todos")
                    adapterEvents.notifyDataSetChanged()
                }
                return true
            }

        })
    }

    private fun closeKeyboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.srcEvents.windowToken, 0)
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
        adapterEvents.snapshot?.snapshotList = eventsFiltered
    }

    private var recyclerViewDelegateEvents =
        object : GenericRecylerAdapterDelegate {

            override fun numberOfRows(adapter: GenericRecyclerAdapter): Int = eventsFiltered.size

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
                    val event = eventsFiltered[position]
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
        bundle.putSerializable("event", eventsFiltered[index])
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
                    filters.forEach {
                        it.isSelected = false
                    }
                    filters[index].isSelected = true
                    setupRecyclerViewFilters()
                    setupTxtFilterHint(setStringByType(filters[index].type))

                    eventsFiltered.clear()
                    val eventsCopy = events.toList()
                    eventsFiltered = if (filters[index].type == "all") {
                        eventsCopy.toMutableList()
                    } else {
                        val selectedType = filters[index].type
                        eventsCopy.filter { it.type == selectedType}.toMutableList()
                    }
                    adapterEvents.notifyDataSetChanged()
                }

            }
        }

    private fun setupFilterAllByDefault() {
        filters.forEach {
            it.isSelected = it.type == "all"
        }
        setupRecyclerViewFilters()
        val eventsCopy = events.toList()
        setupTxtFilterHint("Todos")
        eventsCopy.forEach { e ->
            eventsFiltered.add(e)
        }
        adapterEvents.notifyDataSetChanged()

    }

    private fun setupTxtFilterHint(filter: String?) {
        binding.txtFiltering.text = getString(R.string.generic_filtering_for, filter)
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
                EventResponse(
                    id = 1,
                    type = "show",
                    title = "Roberto Carlos",
                    date = "10/03/2023",
                    img = "https://veja.abril.com.br/wp-content/uploads/2022/06/ROBERTO-CARLOS-1248.jpg?quality=70&strip=info&w=733&resize=1200,800",
                    status = "available",
                    locations = arrayListOf("Vivo Rio"),
                    hour = "16:00",
                    description = "Em suas últimas edições, o projeto foi considerado o mais buscado da folia nacional em São Paulo e fora da internet o público fez jus aos números: todas as apresentações tiveram seus ingressos esgotados. Com repertório sempre cheio de novidades que passa por sucessos de Olodum, Banda Eva, Ara Ketu, Daniela Mercury, Gilberto Gil, Caetano Veloso, Novos Baianos e Ivete Sangalo, o Bloco do Silva chega mais uma vez a Salvador com convidados especiais pra todo mundo dançar e cantar junto do começo ao fim.",
                    included = arrayListOf(
                        "Transporte porta a porta",
                        "Fotografia",
                        "Guia",
                        "Guia Auxiliar"
                    ),
                    notIncluded = arrayListOf("Extras de qualquer natureza", "Bebidas alcoólicas"),
                    tickets = arrayListOf(
                        Ticket("full_price", price = "120,00"),
                        Ticket("full_price", ticketTitle = "VIP", price = "300,00"),
                        Ticket("half_price", price = "60,00")
                    )
                ),
                EventResponse(
                    id = 2,
                    type = "show",
                    title = "Alcione",
                    date = "05/04/2023",
                    img = "https://diariodorio.com/wp-content/uploads/2022/10/Alcione-2019-PROMO-AO-VIVO-fotos-@MarcosHermes-4-1-1.jpg",
                    status = "warning",
                    locations = arrayListOf("Espaço Ribalta"),
                    hour = "16:00",
                    description = "Em suas últimas edições, o projeto foi considerado o mais buscado da folia nacional em São Paulo e fora da internet o público fez jus aos números: todas as apresentações tiveram seus ingressos esgotados. Com repertório sempre cheio de novidades que passa por sucessos de Olodum, Banda Eva, Ara Ketu, Daniela Mercury, Gilberto Gil, Caetano Veloso, Novos Baianos e Ivete Sangalo, o Bloco do Silva chega mais uma vez a Salvador com convidados especiais pra todo mundo dançar e cantar junto do começo ao fim.",
                    included = arrayListOf(
                        "Transporte porta a porta",
                        "Fotografia",
                        "Guia",
                        "Guia Auxiliar"
                    ),
                    notIncluded = arrayListOf("Extras de qualquer natureza", "Bebidas alcoólicas"),
                    tickets = arrayListOf(
                        Ticket(ticketType = "full_price", price = "200")
                    )
                ),
                EventResponse(
                    id = 3,
                    type = "show",
                    title = "Elvis Experience",
                    date = "20/06/2023",
                    img = "https://journalmetro.com/wp-content/uploads/2022/08/CreditNathalieLemelin-CourtoisieLCQProductions.jpg?resize=1051%2C591",
                    status = "available",
                    locations = arrayListOf("Teatro das Artes"),
                    hour = "16:00",
                    description = "Em suas últimas edições, o projeto foi considerado o mais buscado da folia nacional em São Paulo e fora da internet o público fez jus aos números: todas as apresentações tiveram seus ingressos esgotados. Com repertório sempre cheio de novidades que passa por sucessos de Olodum, Banda Eva, Ara Ketu, Daniela Mercury, Gilberto Gil, Caetano Veloso, Novos Baianos e Ivete Sangalo, o Bloco do Silva chega mais uma vez a Salvador com convidados especiais pra todo mundo dançar e cantar junto do começo ao fim.",
                    included = arrayListOf(
                        "Transporte porta a porta",
                        "Fotografia",
                        "Guia",
                        "Guia Auxiliar"
                    ),
                    notIncluded = arrayListOf("Extras de qualquer natureza", "Bebidas alcoólicas"),
                    tickets = arrayListOf(
                        Ticket(ticketType = "full_price", price = "160,00")
                    )
                ),
                EventResponse(
                    id = 4,
                    type = "show",
                    title = "Roupa Nova",
                    date = "30/08/2023",
                    img = "https://i0.statig.com.br/bancodeimagens/4u/in/vm/4uinvm448ru9lhs5521avfy4f.jpg",
                    status = "soldout",
                    locations = arrayListOf("Jeunesse Arena"),
                    hour = "16:00",
                    description = "Em suas últimas edições, o projeto foi considerado o mais buscado da folia nacional em São Paulo e fora da internet o público fez jus aos números: todas as apresentações tiveram seus ingressos esgotados. Com repertório sempre cheio de novidades que passa por sucessos de Olodum, Banda Eva, Ara Ketu, Daniela Mercury, Gilberto Gil, Caetano Veloso, Novos Baianos e Ivete Sangalo, o Bloco do Silva chega mais uma vez a Salvador com convidados especiais pra todo mundo dançar e cantar junto do começo ao fim.",
                    included = arrayListOf(
                        "Transporte porta a porta",
                        "Fotografia",
                        "Guia",
                        "Guia Auxiliar"
                    ),
                    notIncluded = arrayListOf("Extras de qualquer natureza", "Bebidas alcoólicas"),
                    tickets = arrayListOf(
                        Ticket(ticketType = "full_price", price = "300,00")
                    )
                ),

                EventResponse(
                    id = 5,
                    type = "trip",
                    title = "Jalapão",
                    description = "Em suas últimas edições, o projeto foi considerado o mais buscado da folia nacional em São Paulo e fora da internet o público fez jus aos números: todas as apresentações tiveram seus ingressos esgotados. Com repertório sempre cheio de novidades que passa por sucessos de Olodum, Banda Eva, Ara Ketu, Daniela Mercury, Gilberto Gil, Caetano Veloso, Novos Baianos e Ivete Sangalo, o Bloco do Silva chega mais uma vez a Salvador com convidados especiais pra todo mundo dançar e cantar junto do começo ao fim.",
                    startDate = "25/05/2023",
                    endDate = "29/05/2023",
                    img = "https://viagemeturismo.abril.com.br/wp-content/uploads/2016/12/fervedouro-jalapao-tocantins.jpeg?quality=70&strip=info&w=1024&resize=1200,800",
                    status = "available",
                    tickets = arrayListOf(Ticket(ticketType = "full_price", price = "10.000,00")),
                    locations = arrayListOf("Rio de Janeiro", "Duque de Caxias", "Belford Roxo")
                ),
                EventResponse(
                    id = 6,
                    type = "trip",
                    title = "Fernando de Noronha",
                    description = "Em suas últimas edições, o projeto foi considerado o mais buscado da folia nacional em São Paulo e fora da internet o público fez jus aos números: todas as apresentações tiveram seus ingressos esgotados. Com repertório sempre cheio de novidades que passa por sucessos de Olodum, Banda Eva, Ara Ketu, Daniela Mercury, Gilberto Gil, Caetano Veloso, Novos Baianos e Ivete Sangalo, o Bloco do Silva chega mais uma vez a Salvador com convidados especiais pra todo mundo dançar e cantar junto do começo ao fim.",
                    startDate = "25/05/2023",
                    endDate = "29/05/2023",
                    img = "https://www.viajali.com.br/wp-content/uploads/2017/03/fernando-de-noronha-0.jpg",
                    status = "available",
                    tickets = arrayListOf(Ticket(ticketType = "full_price", price = "10.000,00")),
                    locations = arrayListOf("Rio de Janeiro", "Duque de Caxias", "Belford Roxo")
                ),
                EventResponse(
                    id = 7,
                    type = "trip",
                    title = "Capitólio, Serra da Canastra e Tiradentes",
                    description = "Em suas últimas edições, o projeto foi considerado o mais buscado da folia nacional em São Paulo e fora da internet o público fez jus aos números: todas as apresentações tiveram seus ingressos esgotados. Com repertório sempre cheio de novidades que passa por sucessos de Olodum, Banda Eva, Ara Ketu, Daniela Mercury, Gilberto Gil, Caetano Veloso, Novos Baianos e Ivete Sangalo, o Bloco do Silva chega mais uma vez a Salvador com convidados especiais pra todo mundo dançar e cantar junto do começo ao fim.",
                    startDate = "25/05/2023",
                    endDate = "29/05/2023",
                    img = "https://imgmd.net/images/v1/guia/1613817/canion-de-furnas.jpg",
                    status = "warning",
                    tickets = arrayListOf(Ticket(ticketType = "full_price", price = "10.000,00")),
                    locations = arrayListOf("Rio de Janeiro", "Duque de Caxias", "Belford Roxo")
                ),
                EventResponse(
                    id = 8,
                    type = "trip",
                    title = "Caraíva, Abrolhos e Porto Seguro",
                    description = "Em suas últimas edições, o projeto foi considerado o mais buscado da folia nacional em São Paulo e fora da internet o público fez jus aos números: todas as apresentações tiveram seus ingressos esgotados. Com repertório sempre cheio de novidades que passa por sucessos de Olodum, Banda Eva, Ara Ketu, Daniela Mercury, Gilberto Gil, Caetano Veloso, Novos Baianos e Ivete Sangalo, o Bloco do Silva chega mais uma vez a Salvador com convidados especiais pra todo mundo dançar e cantar junto do começo ao fim.",
                    startDate = "25/05/2023",
                    endDate = "29/05/2023",
                    img = "https://a.cdn-hotels.com/gdcs/production196/d546/d00e74a4-31ed-405a-87c7-96af472077c9.jpg?impolicy=fcrop&w=800&h=533&q=medium",
                    status = "soldout",
                    tickets = arrayListOf(Ticket(ticketType = "full_price", price = "10.000,00")),
                    locations = arrayListOf("Rio de Janeiro", "Duque de Caxias", "Belford Roxo")
                ),
                EventResponse(
                    id = 9,
                    type = "trip",
                    title = "Paris",
                    description = "Em suas últimas edições, o projeto foi considerado o mais buscado da folia nacional em São Paulo e fora da internet o público fez jus aos números: todas as apresentações tiveram seus ingressos esgotados. Com repertório sempre cheio de novidades que passa por sucessos de Olodum, Banda Eva, Ara Ketu, Daniela Mercury, Gilberto Gil, Caetano Veloso, Novos Baianos e Ivete Sangalo, o Bloco do Silva chega mais uma vez a Salvador com convidados especiais pra todo mundo dançar e cantar junto do começo ao fim.",
                    startDate = "25/05/2023",
                    endDate = "29/05/2023",
                    img = "https://www.passagenspromo.com.br/blog/wp-content/uploads/2019/04/viagem-para-paris.jpg",
                    status = "warning",
                    locations = arrayListOf("Rio de Janeiro", "Sao Paulo")
                ),

                EventResponse(
                    id = 10,
                    type = "cinema",
                    title = "Titanic",
                    description = "Em suas últimas edições, o projeto foi considerado o mais buscado da folia nacional em São Paulo e fora da internet o público fez jus aos números: todas as apresentações tiveram seus ingressos esgotados. Com repertório sempre cheio de novidades que passa por sucessos de Olodum, Banda Eva, Ara Ketu, Daniela Mercury, Gilberto Gil, Caetano Veloso, Novos Baianos e Ivete Sangalo, o Bloco do Silva chega mais uma vez a Salvador com convidados especiais pra todo mundo dançar e cantar junto do começo ao fim.",
                    date = "16/05/2023",
                    img = "https://johto.legiaodosherois.com.br/wp-content/uploads/2020/07/legiao_ORHfcj7xT0B8.jpg",
                    status = "available",
                    tickets = arrayListOf(Ticket(ticketType = "full_price", price = "10.000,00")),
                    locations = arrayListOf("Rio de Janeiro", "Duque de Caxias", "Belford Roxo"),
                    hour = "14:00"
                ),
                EventResponse(
                    id = 11,
                    type = "nature",
                    title = "Mirante Dona Marta + Piquenique na Lagoa",
                    description = "Em suas últimas edições, o projeto foi considerado o mais buscado da folia nacional em São Paulo e fora da internet o público fez jus aos números: todas as apresentações tiveram seus ingressos esgotados. Com repertório sempre cheio de novidades que passa por sucessos de Olodum, Banda Eva, Ara Ketu, Daniela Mercury, Gilberto Gil, Caetano Veloso, Novos Baianos e Ivete Sangalo, o Bloco do Silva chega mais uma vez a Salvador com convidados especiais pra todo mundo dançar e cantar junto do começo ao fim.",
                    date = "16/05/2023",
                    img = "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/15/ea/87/99/mirante-dona-marta.jpg?w=1200&h=1200&s=1",
                    status = "available",
                    tickets = arrayListOf(Ticket(ticketType = "full_price", price = "10.000,00")),
                    locations = arrayListOf("Rio de Janeiro", "Duque de Caxias", "Belford Roxo"),
                    hour = "14:00"
                ),
                EventResponse(
                    id = 12,
                    type = "theater",
                    title = "A Mentira",
                    description = "Em suas últimas edições, o projeto foi considerado o mais buscado da folia nacional em São Paulo e fora da internet o público fez jus aos números: todas as apresentações tiveram seus ingressos esgotados. Com repertório sempre cheio de novidades que passa por sucessos de Olodum, Banda Eva, Ara Ketu, Daniela Mercury, Gilberto Gil, Caetano Veloso, Novos Baianos e Ivete Sangalo, o Bloco do Silva chega mais uma vez a Salvador com convidados especiais pra todo mundo dançar e cantar junto do começo ao fim.",
                    date = "16/05/2023",
                    img = "https://rotacult.com.br/wp-content/uploads/2022/06/A-Mentira.jpg",
                    status = "warning",
                    tickets = arrayListOf(Ticket(ticketType = "full_price", price = "10.000,00")),
                    locations = arrayListOf("Rio de Janeiro", "Duque de Caxias", "Belford Roxo"),
                    hour = "14:00"
                ),
                EventResponse(
                    id = 13,
                    type = "cinema",
                    title = "O Resgate do Soldado Ryan",
                    description = "Em suas últimas edições, o projeto foi considerado o mais buscado da folia nacional em São Paulo e fora da internet o público fez jus aos números: todas as apresentações tiveram seus ingressos esgotados. Com repertório sempre cheio de novidades que passa por sucessos de Olodum, Banda Eva, Ara Ketu, Daniela Mercury, Gilberto Gil, Caetano Veloso, Novos Baianos e Ivete Sangalo, o Bloco do Silva chega mais uma vez a Salvador com convidados especiais pra todo mundo dançar e cantar junto do começo ao fim.",
                    date = "16/05/2023",
                    img = "https://s2.glbimg.com/4Ck-vqz346D-qry9D7_IPZPxUWI=/0x0:2870x1980/1008x0/smart/filters:strip_icc()/i.s3.glbimg.com/v1/AUTH_59edd422c0c84a879bd37670ae4f538a/internal_photos/bs/2018/Z/I/8jAxnHRbSkLuCtRgiVYA/resgastedosoldadoryan.jpg",
                    status = "soldout",
                    tickets = arrayListOf(Ticket(ticketType = "full_price", price = "10.000,00")),
                    locations = arrayListOf("Rio de Janeiro", "Duque de Caxias", "Belford Roxo"),
                    hour = "14:00"
                ),
                EventResponse(
                    id = 14,
                    type = "natureza",
                    title = "Pedra Bonita",
                    description = "Em suas últimas edições, o projeto foi considerado o mais buscado da folia nacional em São Paulo e fora da internet o público fez jus aos números: todas as apresentações tiveram seus ingressos esgotados. Com repertório sempre cheio de novidades que passa por sucessos de Olodum, Banda Eva, Ara Ketu, Daniela Mercury, Gilberto Gil, Caetano Veloso, Novos Baianos e Ivete Sangalo, o Bloco do Silva chega mais uma vez a Salvador com convidados especiais pra todo mundo dançar e cantar junto do começo ao fim.",
                    date = "16/05/2023",
                    img = "https://upload.wikimedia.org/wikipedia/commons/0/0f/Pedra_Bonita_by_diego_Baravelli.jpg",
                    status = "warning",
                    locations = arrayListOf("Rio de Janeiro", "Minas Gerais")
                ),
            )
        )
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as MainActivity).apply {
            bottomBarHidden = false
            backBtnVisible = false
        }
    }

}