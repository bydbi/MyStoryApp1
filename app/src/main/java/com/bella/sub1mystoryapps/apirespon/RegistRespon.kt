package com.bella.sub1mystoryapps.apirespon

import com.google.gson.annotations.SerializedName

data class RegistRespon(
    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)
