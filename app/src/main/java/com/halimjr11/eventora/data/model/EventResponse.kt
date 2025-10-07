package com.halimjr11.eventora.data.model

import com.google.gson.annotations.SerializedName

data class EventResponse(
    @SerializedName("beginTime")
    val beginTime: String? = null,
    @SerializedName("category")
    val category: String? = null,
    @SerializedName("cityName")
    val cityName: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("endTime")
    val endTime: String? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("imageLogo")
    val imageLogo: String? = null,
    @SerializedName("link")
    val link: String? = null,
    @SerializedName("mediaCover")
    val mediaCover: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("ownerName")
    val ownerName: String? = null,
    @SerializedName("quota")
    val quota: Int? = null,
    @SerializedName("registrants")
    val registrants: Int? = null,
    @SerializedName("summary")
    val summary: String? = null
)