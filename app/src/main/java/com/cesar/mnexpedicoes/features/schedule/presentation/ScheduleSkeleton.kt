package com.cesar.mnexpedicoes.features.schedule.presentation

import android.view.View
import com.cesar.mnexpedicoes.R
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen

class ScheduleSkeleton(private val view: View) {

    private var skeleton: SkeletonScreen? = null

    fun showSkeleton() {
        skeleton = Skeleton.bind(view)
            .load(R.layout.skeleton_schedule)
            .duration(375)
            .color(R.color.semi_transparent)
            .angle(30)
            .show()
    }

    fun hideSkeleton() {
        skeleton?.hide()
    }
}