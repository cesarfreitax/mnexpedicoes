package com.cesar.mnexpedicoes.fragments.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.cesar.mnexpedicoes.R
import com.cesar.mnexpedicoes.fragments.home.model.TripResponse
import com.cesar.mnexpedicoes.utils.Constants
import com.cesar.mnexpedicoes.utils.getMonthString
import com.cesar.mnexpedicoes.utils.load
import com.google.android.material.card.MaterialCardView

class ViewPagerAdapter(var trips: List<TripResponse>, private val fragment: Fragment) :
    RecyclerView.Adapter<ViewPagerAdapter.PageViewHolder>() {

    private var formattedDateTxt = ""

    class PageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.img_trip)
        val statusCdv: MaterialCardView = itemView.findViewById(R.id.cdv_status)
        val statusTxt: TextView = itemView.findViewById(R.id.txt_status)
        val title: TextView = itemView.findViewById(R.id.txt_trip_title)
        val date: TextView = itemView.findViewById(R.id.txt_trip_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        return PageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.cell_trip, parent, false)
        )
    }

    override fun getItemCount(): Int = trips.size

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        setupCell(
            img = holder.img,
            statusCdv = holder.statusCdv,
            statusTxt = holder.statusTxt,
            title = holder.title,
            date = holder.date,
            trip = trips[position],
            fragment = fragment
        )
    }

    private fun setupCell(
        img: ImageView,
        statusCdv: MaterialCardView,
        statusTxt: TextView,
        title: TextView,
        date: TextView,
        trip: TripResponse,
        fragment: Fragment
    ) {
        title.text = trip.title
        val startDateSplitted = trip.startDate?.split("/")!!.toList()
        val endDateSplitted = trip.endDate?.split("/")!!.toList()
        formatDate(startDateSplitted, endDateSplitted)
        date.text = formattedDateTxt
        img.load(trip.img, fragment.requireContext())
        setStatus(trip.status!!, statusCdv, statusTxt, fragment)
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

    private fun formatDate(
        startDateSplitted: List<String>,
        endDateSplitted: List<String>
    ) {
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
    }
}