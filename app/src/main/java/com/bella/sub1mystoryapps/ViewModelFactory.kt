package com.bella.sub1mystoryapps

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bella.sub1mystoryapps.autentifikasi.login.LoginViewModel
import com.bella.sub1mystoryapps.main.ListStoryViewModel
import com.bella.sub1mystoryapps.story.UploadViewModel

class ViewModelFactory(private val pref: UserPreferences) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(pref) as T
        }

        if (modelClass.isAssignableFrom(ListStoryViewModel::class.java)) {
            return ListStoryViewModel(pref) as T
        }

        if (modelClass.isAssignableFrom(UploadViewModel::class.java)) {
            return UploadViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}