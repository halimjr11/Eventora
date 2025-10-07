package com.halimjr11.eventora.data.service

import com.halimjr11.eventora.data.model.BaseResponse
import com.halimjr11.eventora.data.model.EventResponse
import com.halimjr11.eventora.utils.Constants.EVENT_LIMIT
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EventService {
    @GET("events")
    suspend fun getActiveEvents(
        @Query("active") active: Int = 1,
        @Query("limit") limit: Int = EVENT_LIMIT
    ): BaseResponse<List<EventResponse>>

    @GET("events")
    suspend fun getFinishedEvents(
        @Query("active") active: Int = 0,
        @Query("limit") limit: Int = EVENT_LIMIT
    ): BaseResponse<List<EventResponse>>

    @GET("events")
    suspend fun searchEvents(
        @Query("active") active: Int = -1,
        @Query("q") keyword: String
    ): BaseResponse<List<EventResponse>>

    @GET("events/{id}")
    suspend fun getEventDetail(
        @Path("id") id: Int
    ): BaseResponse<EventResponse>
}