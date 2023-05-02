package com.bella.sub1mystoryapps.apirespon

import com.google.gson.annotations.SerializedName

data class AddStoryRespon(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)
