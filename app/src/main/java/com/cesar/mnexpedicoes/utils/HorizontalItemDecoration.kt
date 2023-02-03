package com.cesar.mnexpedicoes.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

// Offset ZERO because i don't want to set margin between rcv item, but i want to let it in the code for remembering
class HorizontalItemDecoration(private var offset:Int = 0) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        val offsetDp = offset.dp

        val firstPosition = parent.getChildAdapterPosition(view) == 0
        val lastPosition = parent.getChildAdapterPosition(view) == state.itemCount -1

        if(state.itemCount == 2){
            outRect.left = offsetDp
            outRect.right = offsetDp
            if(firstPosition) {
                outRect.right = 0
            }
            return
        }


        if(firstPosition){
            outRect.left = offsetDp
            outRect.right = offsetDp/2
            return
        }
        if(lastPosition){
            outRect.left = offsetDp/2
            outRect.right = offsetDp
            return
        }

        outRect.left = offsetDp/2
        outRect.right = offsetDp/2
    }
}