package com.halimjr11.eventora.domain.model

data class EventDomain(
    val id: Int = 0,
    val name: String = "",
    val summary: String = "",
    val description: String = "",
    val imageLogo: String = "",
    val mediaCover: String = "",
    val category: String = "",
    val ownerName: String = "",
    val cityName: String = "",
    val quota: Int = 0,
    val beginTime: String = "",
    val endTime: String = "",
    val link: String = ""
)