package com.halimjr11.eventora.data.service

import com.halimjr11.eventora.data.model.BaseResponse
import com.halimjr11.eventora.data.model.EventResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EventService {
    @GET("events")
    suspend fun getEvents(
        @Query("active") active: Int = -1,
        @Query("q") keyword: String? = null,
        @Query("limit") limit: Int? = null
    ): BaseResponse<List<EventResponse>>

    @GET("events/{id}")
    suspend fun getEventDetail(
        @Path("id") id: Int
    ): BaseResponse<EventResponse>
}