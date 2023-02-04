package com.cesar.mnexpedicoes.fragments.home.cell

import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cesar.mnexpedicoes.R
import com.cesar.mnexpedicoes.databinding.CellEventBinding
import com.cesar.mnexpedicoes.fragments.home.model.EventResponse
import com.cesar.mnexpedicoes.utils.*
import com.google.android.material.card.MaterialCardView
import io.github.enicolas.genericadapter.adapter.BaseCell
import java.lang.Exception

class EventCell(private val viewBinding: CellEventBinding) : BaseCell(viewBinding.root) {

    fun setupCell(event: EventResponse, fragment: Fragment) {
        setupCellView(event, fragment)
    }

    private fun setupCellView(event: EventResponse, fragment: Fragment) {
        viewBinding.imgEvent.load(event.img, fragment.requireContext())
        viewBinding.txtEventTitle.text = event.title
        try {
            viewBinding.txtEventDate.text = event.date?.formatDateEvent()
        } catch (e: Exception) {
            Toast.makeText(fragment.context, "Ocorreu um erro inesperado...", Toast.LENGTH_SHORT)
                .show()
        }
        viewBinding.txtEventLocation.text = event.location?.first()
        setStatus(
            event.status ?: "available", viewBinding.cdvStatus, viewBinding.txtStatus, fragment
        )
    }

    private fun setStatus(
        statusString: String,
        statusCdv: MaterialCardView,
        statusTxt: TextView,
        fragment: Fragment
    ) {
        when (statusString) {
            Constants.AVAILABLE -> {
                statusCdv.setCardBackgroundColor(fragment.context?.getColorStateList(R.color.green_available))
                statusTxt.text = fragment.requireContext().getString(R.string.generic_available)
            }
            Constants.WARNING -> {
                statusCdv.setCardBackgroundColor(fragment.context?.getColorStateList(R.color.yellow_warning))
                statusTxt.text = fragment.requireContext().getString(R.string.generic_warning_tickets)
            }
            Constants.SOLD_OUT -> {
                statusCdv.setCardBackgroundColor(fragment.context?.getColorStateList(R.color.red_sold_out))
                statusTxt.text = fragment.requireContext().getString(R.string.generic_sold_out)
            }
        }
    }

}

