package com.cesar.mnexpedicoes.fragments.home.cell

import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.cesar.mnexpedicoes.R
import com.cesar.mnexpedicoes.databinding.CellEventBinding
import com.cesar.mnexpedicoes.fragments.home.model.EventResponse
import com.cesar.mnexpedicoes.utils.*
import com.google.android.material.card.MaterialCardView
import io.github.enicolas.genericadapter.adapter.BaseCell

class EventCell(private val viewBinding: CellEventBinding) : BaseCell(viewBinding.root) {

    fun setupCell(
        event: EventResponse,
        fragment: Fragment,
        isVertical: Boolean = false,
        isFirstCell: Boolean = false
    ) {
        setLayoutParams(isVertical, isFirstCell)
        setupCellView(event, fragment)
    }

    private fun setLayoutParams(isVertical: Boolean, isFirstCell: Boolean) {
        setVerticalParams(isVertical)
        setHorizontalParams(isVertical, isFirstCell)
    }

    private fun setHorizontalParams(isVertical: Boolean, isFirstCell: Boolean) {
        if (!isVertical) {
            val lp = viewBinding.cdvEventContainer.layoutParams as ViewGroup.MarginLayoutParams
            lp.setMargins(16.dp, 0, 0, 0)
            viewBinding.cdvEventContainer.layoutParams = lp
        }
    }

    private fun setVerticalParams(isVertical: Boolean) {
        if (isVertical) {
            val layoutParams =
                viewBinding.cdvEventContainer.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.setMargins(0, 16.dp, 0, 0)
            viewBinding.cdvEventContainer.layoutParams = layoutParams
        }
    }

    private fun setupCellView(event: EventResponse, fragment: Fragment) {

        viewBinding.imgEvent.load(event.img, fragment.requireContext())
        viewBinding.txtEventTitle.text = event.title
        if (event.type == "trip") {
            viewBinding.txtEventDate.text =
                formatDateTrip(event.date.toString(), event.endDate.toString())
            viewBinding.txtEventLocation.text = formatLocationTrip(event.locations)

        } else {
            viewBinding.txtEventDate.text =
                formatDateEvent(event.date.toString(), event.hour.toString())
            viewBinding.txtEventLocation.text = event.locations.first().name
        }
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
                statusTxt.text =
                    fragment.requireContext().getString(R.string.generic_warning_tickets)
            }
            Constants.SOLD_OUT -> {
                statusCdv.setCardBackgroundColor(fragment.context?.getColorStateList(R.color.red_sold_out))
                statusTxt.text = fragment.requireContext().getString(R.string.generic_sold_out)
            }
        }
    }

}

