package com.bella.sub1mystoryapps

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataPreferences(
    var id: String = "",
    var name: String = "",
    var state: Boolean = false,
    var token: String = "",
) : Parcelable