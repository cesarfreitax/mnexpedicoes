package com.cesar.mnexpedicoes.fragments.events.cell

import androidx.fragment.app.Fragment
import com.cesar.mnexpedicoes.R
import com.cesar.mnexpedicoes.databinding.CellTicketBinding
import com.cesar.mnexpedicoes.fragments.home.model.Ticket
import com.cesar.mnexpedicoes.utils.toggleVisibility
import io.github.enicolas.genericadapter.adapter.BaseCell

class TicketCell(private val viewBinding: CellTicketBinding) : BaseCell(viewBinding.root) {

    fun setupCell(ticket: Ticket, isFirst: Boolean = false, fragment: Fragment) {
        viewBinding.viwSeparator.toggleVisibility(!isFirst)
        val ticketString = if (ticket.ticketType == "full_price") fragment.getString(R.string.generic_full_price) else fragment.getString(R.string.generic_half_price)
        viewBinding.txtTicketType.text = ticketString
        if (ticket.ticketTitle != "") {
            viewBinding.txtTicketTitle.toggleVisibility(true)
            viewBinding.txtTicketTitle.text = ticket.ticketTitle
        }

        viewBinding.txtTicketPrice.text = fragment.getString(R.string.generic_ticket_price, ticket.price)
    }

}

