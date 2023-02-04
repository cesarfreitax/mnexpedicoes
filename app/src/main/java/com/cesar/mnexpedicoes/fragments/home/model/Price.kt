package com.cesar.mnexpedicoes.fragments.home.model

import java.io.Serializable

class Ticket(
    val ticketType: String? = "",
    val ticketTitle: String? = "",
    val price: String? = ""
) : Serializable
