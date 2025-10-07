package com.halimjr11.eventora.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.halimjr11.eventora.BuildConfig

@Entity(tableName = BuildConfig.ENTITY_NAME)
data class EventEntity(
    @PrimaryKey()
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