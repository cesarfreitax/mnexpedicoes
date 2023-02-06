package com.cesar.mnexpedicoes.fragments.home.cell

import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.marginStart
import androidx.fragment.app.Fragment
import com.cesar.mnexpedicoes.R
import com.cesar.mnexpedicoes.databinding.CellEventBinding
import com.cesar.mnexpedicoes.fragments.home.model.EventResponse
import com.cesar.mnexpedicoes.utils.*
import com.google.android.material.card.MaterialCardView
import io.github.enicolas.genericadapter.adapter.BaseCell
import java.lang.Exception

class EventCell(private val viewBinding: CellEventBinding) : BaseCell(viewBinding.root) {

    fun setupCell(event: EventResponse, fragment: Fragment, isVertical: Boolean = false, isFirstCell: Boolean = false) {
        if (isVertical) {
            val layoutParams = viewBinding.cdvEventContainer.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.setMargins(0, 16.dp, 0, 0)
            viewBinding.cdvEventContainer.layoutParams = layoutParams
        }
        if (!isVertical && !isFirstCell) {
            val layoutParams = viewBinding.cdvEventContainer.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.setMargins(16.dp, 0, 0, 0)
            viewBinding.cdvEventContainer.layoutParams = layoutParams
        }
        setupCellView(event, fragment)
    }

    private fun setupCellView(event: EventResponse, fragment: Fragment) {

        viewBinding.imgEvent.load(event.img, fragment.requireContext())
        viewBinding.txtEventTitle.text = event.title
        if (event.type == "trip") {
            viewBinding.txtEventDate.text = formatDateTrip(event.startDate.toString(), event.endDate.toString())
            viewBinding.txtEventLocation.text = formatLocationTrip(event.locations!!)

        } else {
            viewBinding.txtEventDate.text = formatDateEvent(event.date.toString(), event.hour.toString())
            viewBinding.txtEventLocation.text = event.locations?.first()
        }
        setStatus(
            event.status ?: "available", viewBinding.cdvStatus, fragment
        )
    }

    private fun setStatus(
        statusString: String,
        statusCdv: MaterialCardView,
        fragment: Fragment
    ) {
        when (statusString) {
            Constants.AVAILABLE -> {
                statusCdv.setCardBackgroundColor(fragment.context?.getColorStateList(R.color.green_available))
            }
            Constants.WARNING -> {
                statusCdv.setCardBackgroundColor(fragment.context?.getColorStateList(R.color.yellow_warning))
            }
            Constants.SOLD_OUT -> {
                statusCdv.setCardBackgroundColor(fragment.context?.getColorStateList(R.color.red_sold_out))
            }
        }
    }

}

