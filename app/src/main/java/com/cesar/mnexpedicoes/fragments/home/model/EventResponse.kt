package com.cesar.mnexpedicoes.fragments.home.model

import java.io.Serializable

class EventResponse(
    val id: Int? = 0,
    val type: String? = "",
    val title: String? = "",
    val date: String? = "",
    val startDate: String? = "",
    val endDate: String? = "",
    val hour: String? = "",
    val img: String? = "",
    val status: String? = "available",
    val locations: ArrayList<String>,
    val description: String? = "",
    val included: ArrayList<String>? = arrayListOf(),
    val notIncluded: ArrayList<String>? = arrayListOf(),
    val tickets: ArrayList<Ticket>? = arrayListOf()
) : Serializable
