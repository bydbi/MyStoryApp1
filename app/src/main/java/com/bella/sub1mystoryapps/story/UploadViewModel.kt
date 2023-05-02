package com.bella.sub1mystoryapps.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bella.sub1mystoryapps.DataPreferences
import com.bella.sub1mystoryapps.UserPreferences

class UploadViewModel(private val pref: UserPreferences): ViewModel() {

    fun getToken(): LiveData<DataPreferences> {
        return pref.readDataStore.asLiveData()
    }
}