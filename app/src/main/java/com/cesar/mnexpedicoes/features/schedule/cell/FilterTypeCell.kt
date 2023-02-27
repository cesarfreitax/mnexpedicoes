package com.cesar.mnexpedicoes.features.schedule.cell

import android.graphics.Typeface
import androidx.fragment.app.Fragment
import com.cesar.mnexpedicoes.R
import com.cesar.mnexpedicoes.databinding.CellFilterTypeBinding
import com.cesar.mnexpedicoes.features.schedule.model.FilterResponse
import io.github.enicolas.genericadapter.adapter.BaseCell

class FilterTypeCell(private val viewBinding: CellFilterTypeBinding) : BaseCell(viewBinding.root) {

    fun setupCell(filter: FilterResponse, fragment: Fragment) {
        when (filter.type) {
            "all" -> {
                setCellView(R.drawable.ic_filter_all, "Todos")
            }
            "show" -> {
                setCellView(R.drawable.ic_filter_show, "Show")
            }
            "theater" -> {
                setCellView(R.drawable.ic_filter_theater, "Teatro")
            }
            "nature" -> {
                setCellView(R.drawable.ic_filter_nature, "Natureza")
            }
            "trip" -> {
                setCellView(R.drawable.ic_filter_trip, "Viagem")
            }
            "cinema" -> {
                setCellView(R.drawable.ic_filter_cinema, "Cinema")
            }
        }

        if (filter.isSelected) {
            viewBinding.cdvFilter.alpha = 1f
            viewBinding.txtFilter.setTextColor(fragment.requireContext().getColor(R.color.black))
            viewBinding.txtFilter.typeface = Typeface.DEFAULT_BOLD
        } else {
            viewBinding.cdvFilter.alpha = 0.5f
            viewBinding.txtFilter.setTextColor(fragment.requireContext().getColor(R.color.grey))
            viewBinding.txtFilter.typeface = Typeface.DEFAULT
        }
    }

    private fun setCellView(img: Int, txt: String) {
        viewBinding.imgFilter.setImageResource(img)
        viewBinding.txtFilter.text = txt
    }

}

