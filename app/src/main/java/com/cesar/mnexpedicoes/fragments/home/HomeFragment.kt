package com.cesar.mnexpedicoes.fragments.home

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
import com.cesar.mnexpedicoes.fragments.home.model.Ticket
import com.cesar.mnexpedicoes.utils.PageItemSnapHelper
import io.github.enicolas.genericadapter.AdapterHolderType
import io.github.enicolas.genericadapter.adapter.GenericRecyclerAdapter
import io.github.enicolas.genericadapter.adapter.GenericRecylerAdapterDelegate

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val adapter = GenericRecyclerAdapter()
    private val trips = mutableListOf<EventResponse>()
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
        binding.vpgTrips.adapter = ViewPagerAdapter(trips, parentFragmentManager, this@HomeFragment)
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
                    c.setupCell(event, this@HomeFragment)
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
        trips.clear()
        trips.addAll(
            listOf(
                EventResponse(id = 1, type = "trip",title = "Jalapão", description = "Em suas últimas edições, o projeto foi considerado o mais buscado da folia nacional em São Paulo e fora da internet o público fez jus aos números: todas as apresentações tiveram seus ingressos esgotados. Com repertório sempre cheio de novidades que passa por sucessos de Olodum, Banda Eva, Ara Ketu, Daniela Mercury, Gilberto Gil, Caetano Veloso, Novos Baianos e Ivete Sangalo, o Bloco do Silva chega mais uma vez a Salvador com convidados especiais pra todo mundo dançar e cantar junto do começo ao fim.", startDate = "25/05/2023", endDate = "29/05/2023", img = "https://viagemeturismo.abril.com.br/wp-content/uploads/2016/12/fervedouro-jalapao-tocantins.jpeg?quality=70&strip=info&w=1024&resize=1200,800", status = "available", tickets = arrayListOf(Ticket(ticketType = "full_price", price = "10.000,00")), location = arrayListOf("Rio de Janeiro", "Duque de Caxias", "Belford Roxo")),
                EventResponse(id = 2, type = "trip", title = "Fernando de Noronha", description = "Em suas últimas edições, o projeto foi considerado o mais buscado da folia nacional em São Paulo e fora da internet o público fez jus aos números: todas as apresentações tiveram seus ingressos esgotados. Com repertório sempre cheio de novidades que passa por sucessos de Olodum, Banda Eva, Ara Ketu, Daniela Mercury, Gilberto Gil, Caetano Veloso, Novos Baianos e Ivete Sangalo, o Bloco do Silva chega mais uma vez a Salvador com convidados especiais pra todo mundo dançar e cantar junto do começo ao fim.", startDate = "10/02/2023", endDate = "14/02/2023", img = "https://www.viajali.com.br/wp-content/uploads/2017/03/fernando-de-noronha-0.jpg", status = "available", tickets = arrayListOf(Ticket(ticketType = "full_price", price = "10.000,00")), location = arrayListOf("Rio de Janeiro", "Duque de Caxias", "Belford Roxo")),
                EventResponse(id = 3, type = "trip", title = "Capitólio, Serra da Canastra e Tiradentes", description = "Em suas últimas edições, o projeto foi considerado o mais buscado da folia nacional em São Paulo e fora da internet o público fez jus aos números: todas as apresentações tiveram seus ingressos esgotados. Com repertório sempre cheio de novidades que passa por sucessos de Olodum, Banda Eva, Ara Ketu, Daniela Mercury, Gilberto Gil, Caetano Veloso, Novos Baianos e Ivete Sangalo, o Bloco do Silva chega mais uma vez a Salvador com convidados especiais pra todo mundo dançar e cantar junto do começo ao fim.", startDate = "04/03/2023", endDate = "12/03/2023", img = "https://imgmd.net/images/v1/guia/1613817/canion-de-furnas.jpg", status = "warning", tickets = arrayListOf(Ticket(ticketType = "full_price", price = "10.000,00")), location = arrayListOf("Rio de Janeiro", "Duque de Caxias", "Belford Roxo")),
                EventResponse(id = 4, type = "trip", title = "Caraíva, Abrolhos e Porto Seguro", description = "Em suas últimas edições, o projeto foi considerado o mais buscado da folia nacional em São Paulo e fora da internet o público fez jus aos números: todas as apresentações tiveram seus ingressos esgotados. Com repertório sempre cheio de novidades que passa por sucessos de Olodum, Banda Eva, Ara Ketu, Daniela Mercury, Gilberto Gil, Caetano Veloso, Novos Baianos e Ivete Sangalo, o Bloco do Silva chega mais uma vez a Salvador com convidados especiais pra todo mundo dançar e cantar junto do começo ao fim.", startDate = "15/07/2023", endDate = "24/07/2023", img = "https://a.cdn-hotels.com/gdcs/production196/d546/d00e74a4-31ed-405a-87c7-96af472077c9.jpg?impolicy=fcrop&w=800&h=533&q=medium", status = "soldout", tickets = arrayListOf(Ticket(ticketType = "full_price", price = "10.000,00")), location = arrayListOf("Rio de Janeiro", "Duque de Caxias", "Belford Roxo")),
                EventResponse(id = 5, type = "trip", title = "Paris", description = "Em suas últimas edições, o projeto foi considerado o mais buscado da folia nacional em São Paulo e fora da internet o público fez jus aos números: todas as apresentações tiveram seus ingressos esgotados. Com repertório sempre cheio de novidades que passa por sucessos de Olodum, Banda Eva, Ara Ketu, Daniela Mercury, Gilberto Gil, Caetano Veloso, Novos Baianos e Ivete Sangalo, o Bloco do Silva chega mais uma vez a Salvador com convidados especiais pra todo mundo dançar e cantar junto do começo ao fim.", startDate = "20/04/2024", endDate = "27/04/2024", img = "https://www.passagenspromo.com.br/blog/wp-content/uploads/2019/04/viagem-para-paris.jpg", status = "warning"),
            )

        )

        events.clear()
        events.addAll(
            listOf(
                EventResponse(id = 1, type = "show", title = "Roberto Carlos", date = "10/03/2023", img = "https://veja.abril.com.br/wp-content/uploads/2022/06/ROBERTO-CARLOS-1248.jpg?quality=70&strip=info&w=733&resize=1200,800", status = "available", location = arrayListOf("Vivo Rio"), hour = "16:00", description = "Em suas últimas edições, o projeto foi considerado o mais buscado da folia nacional em São Paulo e fora da internet o público fez jus aos números: todas as apresentações tiveram seus ingressos esgotados. Com repertório sempre cheio de novidades que passa por sucessos de Olodum, Banda Eva, Ara Ketu, Daniela Mercury, Gilberto Gil, Caetano Veloso, Novos Baianos e Ivete Sangalo, o Bloco do Silva chega mais uma vez a Salvador com convidados especiais pra todo mundo dançar e cantar junto do começo ao fim.", included = arrayListOf("Transporte porta a porta", "Fotografia", "Guia", "Guia Auxiliar"), notIncluded = arrayListOf("Extras de qualquer natureza", "Bebidas alcoólicas"), tickets = arrayListOf(Ticket("full_price", price = "120,00"), Ticket("full_price", ticketTitle = "VIP", price = "300,00"), Ticket("half_price", price = "60,00"))),
                EventResponse(id = 2, type = "show", title = "Alcione", date = "05/04/2023", img = "https://diariodorio.com/wp-content/uploads/2022/10/Alcione-2019-PROMO-AO-VIVO-fotos-@MarcosHermes-4-1-1.jpg", status = "warning", location = arrayListOf("Espaço Ribalta"), hour = "16:00", description = "Em suas últimas edições, o projeto foi considerado o mais buscado da folia nacional em São Paulo e fora da internet o público fez jus aos números: todas as apresentações tiveram seus ingressos esgotados. Com repertório sempre cheio de novidades que passa por sucessos de Olodum, Banda Eva, Ara Ketu, Daniela Mercury, Gilberto Gil, Caetano Veloso, Novos Baianos e Ivete Sangalo, o Bloco do Silva chega mais uma vez a Salvador com convidados especiais pra todo mundo dançar e cantar junto do começo ao fim.", included = arrayListOf("Transporte porta a porta", "Fotografia", "Guia", "Guia Auxiliar"), notIncluded = arrayListOf("Extras de qualquer natureza", "Bebidas alcoólicas"), tickets = arrayListOf(Ticket(ticketType = "full_price", price = "200"))),
                EventResponse(id = 3, type = "show", title = "Elvis Experience", date = "20/06/2023", img = "https://journalmetro.com/wp-content/uploads/2022/08/CreditNathalieLemelin-CourtoisieLCQProductions.jpg?resize=1051%2C591", status = "available", location = arrayListOf("Teatro das Artes"), hour = "16:00", description = "Em suas últimas edições, o projeto foi considerado o mais buscado da folia nacional em São Paulo e fora da internet o público fez jus aos números: todas as apresentações tiveram seus ingressos esgotados. Com repertório sempre cheio de novidades que passa por sucessos de Olodum, Banda Eva, Ara Ketu, Daniela Mercury, Gilberto Gil, Caetano Veloso, Novos Baianos e Ivete Sangalo, o Bloco do Silva chega mais uma vez a Salvador com convidados especiais pra todo mundo dançar e cantar junto do começo ao fim.", included = arrayListOf("Transporte porta a porta", "Fotografia", "Guia", "Guia Auxiliar"), notIncluded = arrayListOf("Extras de qualquer natureza", "Bebidas alcoólicas"), tickets = arrayListOf(Ticket(ticketType = "full_price", price = "160,00"))),
                EventResponse(id = 4, type = "show", title = "Roupa Nova", date = "30/08/2023", img = "https://i0.statig.com.br/bancodeimagens/4u/in/vm/4uinvm448ru9lhs5521avfy4f.jpg", status = "soldout", location = arrayListOf("Jeunesse Arena"), hour = "16:00", description = "Em suas últimas edições, o projeto foi considerado o mais buscado da folia nacional em São Paulo e fora da internet o público fez jus aos números: todas as apresentações tiveram seus ingressos esgotados. Com repertório sempre cheio de novidades que passa por sucessos de Olodum, Banda Eva, Ara Ketu, Daniela Mercury, Gilberto Gil, Caetano Veloso, Novos Baianos e Ivete Sangalo, o Bloco do Silva chega mais uma vez a Salvador com convidados especiais pra todo mundo dançar e cantar junto do começo ao fim.", included = arrayListOf("Transporte porta a porta", "Fotografia", "Guia", "Guia Auxiliar"), notIncluded = arrayListOf("Extras de qualquer natureza", "Bebidas alcoólicas"), tickets = arrayListOf(Ticket(ticketType = "full_price", price = "300,00")))
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