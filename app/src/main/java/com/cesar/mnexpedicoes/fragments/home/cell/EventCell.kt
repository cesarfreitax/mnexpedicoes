package com.cesar.mnexpedicoes.fragments.home.cell

import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cesar.mnexpedicoes.R
import com.cesar.mnexpedicoes.databinding.CellEventBinding
import com.cesar.mnexpedicoes.databinding.CellTripBinding
import com.cesar.mnexpedicoes.fragments.home.model.EventResponse
import com.cesar.mnexpedicoes.fragments.home.model.TripResponse
import com.cesar.mnexpedicoes.utils.*
import com.google.android.material.card.MaterialCardView
import io.github.enicolas.genericadapter.adapter.BaseCell

class EventCell(private val viewBinding: CellEventBinding) : BaseCell(viewBinding.root) {

    fun setupCell(event: EventResponse, fragment: Fragment) {

        viewBinding.imgEvent.load(event.img, fragment.requireContext())
        viewBinding.txtEventTitle.text = event.title
        try {
            viewBinding.txtEventDate.text = event.date?.formatDateEvent()
        } catch (e: java.lang.Exception) {
            Toast.makeText(fragment.context, "Ocorreu um erro inesperado...", Toast.LENGTH_SHORT).show()
        }

        viewBinding.txtEventLocation.text = event.location
        setStatus(
            event.status ?: "available",
            viewBinding.cdvStatus,
            viewBinding.txtStatus,
            fragment
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
                statusTxt.text = fragment.requireContext().getString(R.string.available)
            }
            Constants.WARNING -> {
                statusCdv.setCardBackgroundColor(fragment.context?.getColorStateList(R.color.yellow_warning))
                statusTxt.text = fragment.requireContext().getString(R.string.warning_tickets)
            }
            Constants.SOLD_OUT -> {
                statusCdv.setCardBackgroundColor(fragment.context?.getColorStateList(R.color.red_sold_out))
                statusTxt.text = fragment.requireContext().getString(R.string.sold_out)
            }
        }
    }

}

