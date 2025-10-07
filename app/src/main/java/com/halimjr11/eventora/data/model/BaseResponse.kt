package com.halimjr11.eventora.data.model

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("error")
    val error: Boolean? = null,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName(value = "data", alternate = ["event", "listEvents"])
    val data: T? = null
)