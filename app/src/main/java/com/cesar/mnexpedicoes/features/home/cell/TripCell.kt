package com.cesar.mnexpedicoes.features.home.cell

import androidx.fragment.app.Fragment
import com.cesar.mnexpedicoes.databinding.CellTripBinding
import com.cesar.mnexpedicoes.features.home.model.TripResponse
import com.cesar.mnexpedicoes.utils.getMonthString
import com.cesar.mnexpedicoes.utils.load
import io.github.enicolas.genericadapter.adapter.BaseCell

class TripCell(private val viewBinding: CellTripBinding) : BaseCell(viewBinding.root) {

    private var formattedDateTxt = ""
    fun setupCell(trip: TripResponse, fragment: Fragment) {
        viewBinding.txtTripTitle.text = trip.title
        val startDateSplitted = trip.startDate?.split("/")!!.toList()
        val endDateSplitted = trip.endDate?.split("/")!!.toList()

        formattedDateTxt = if (startDateSplitted[1] == endDateSplitted[1]) {
            "De ${startDateSplitted[0]} a ${endDateSplitted[0]} de ${
                getMonthString(
                    startDateSplitted[1]
                )
            }"
        } else {
            "De ${startDateSplitted[0]} de ${getMonthString(startDateSplitted[1])} a ${endDateSplitted[0]} de ${
                getMonthString(
                    startDateSplitted[1]
                )
            }"
        }

        viewBinding.txtTripDate.text = formattedDateTxt
        val img = trip.img
        viewBinding.imgTrip.load(img, fragment.requireContext())

    }

}

