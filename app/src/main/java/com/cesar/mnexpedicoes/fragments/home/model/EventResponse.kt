package com.cesar.mnexpedicoes.fragments.home.model

import java.io.Serializable

class EventResponse(
    val id_event: Int? = 0,
    val type: String? = "",
    val title: String? = "",
    val date: String? = "",
    val endDate: String? = "",
    val hour: String? = "",
    val img: String? = "",
    val status: String? = "available",
    val description: String? = "",
    val tickets: ArrayList<Ticket>? = arrayListOf(),
    val locations: ArrayList<Location> = arrayListOf(),
    val includeds: ArrayList<Included> = arrayListOf(),
    val notIncludeds: ArrayList<NotIncluded> = arrayListOf()
) : Serializable

class Ticket (
    val id_ticket: Int? = 0,
    val ticketType: String? = "",
    val ticketTitle: String? = "",
    val price: Double? = 0.00
)

class Location(
    val id_location: Int? = 0,
    val name: String? = ""
)

class Included(
    val id_included: Int? = 0,
    val description: String? = ""
)

class NotIncluded(
    val id_not_included: Int? = 0,
    val description: String? = ""
)