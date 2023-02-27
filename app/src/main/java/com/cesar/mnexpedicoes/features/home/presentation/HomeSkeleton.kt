package com.cesar.mnexpedicoes.features.home.presentation

import android.view.View
import com.cesar.mnexpedicoes.R
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen

class HomeSkeleton(private val view: View) {

    private var skeleton: SkeletonScreen? = null

    fun showSkeleton() {
        skeleton = Skeleton.bind(view)
            .load(R.layout.skeleton_home)
            .duration(375)
            .angle(30)
            .color(R.color.semi_transparent)
            .show()
    }

    fun hideSkeleton() {
        skeleton?.hide()
    }
}