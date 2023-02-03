package com.cesar.mnexpedicoes.fragments.home.model

import java.io.Serializable

class EventResponse(
    val id: Int? = 0,
    val title: String? = "",
    val date: String? = "",
    val hour: String? = "",
    val img: String? = "",
    val status: String? = "available",
    val location: String? = "",
    val description: String? = "",
    val included: MutableList<String>? = arrayListOf(),
    val notIncluded: MutableList<String>? = arrayListOf()
) : Serializable
