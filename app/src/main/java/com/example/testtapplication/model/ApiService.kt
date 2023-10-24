package com.example.testtapplication.model

import TicketType
import retrofit2.http.GET

interface ApiService {
    @GET("ticketMgmt/dropdowns/getTicketTypes")
    suspend fun getTicketTypes(): List<TicketType>
}

