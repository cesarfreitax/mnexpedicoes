package com.cesar.mnexpedicoes.features.events.cell

import androidx.fragment.app.Fragment
import com.cesar.mnexpedicoes.R
import com.cesar.mnexpedicoes.databinding.CellItemIncludedBinding
import io.github.enicolas.genericadapter.adapter.BaseCell

class ItemIncludedCell(private val viewBinding: CellItemIncludedBinding) : BaseCell(viewBinding.root) {

    fun setupCell(txt: String, isIncluded: Boolean = true, fragment: Fragment) {
        viewBinding.txtIncludedItem.text = txt
        if (!isIncluded) {
            viewBinding.txtIncludedItem.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_block, 0, 0, 0)
            viewBinding.txtIncludedItem.compoundDrawableTintList = fragment.context?.getColorStateList(R.color.red_sold_out)
        }
    }

}

