package com.bella.sub1mystoryapps.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.bella.sub1mystoryapps.DataPreferences
import com.bella.sub1mystoryapps.UserPreferences
import com.bella.sub1mystoryapps.apirespon.ListStoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListStoryViewModel(private val pref: UserPreferences) : ViewModel() {

    fun getStories(token: String) : LiveData<RecyclerView<ListStoryItem>> {
        return storyRepository.getStories(token).cachedIn(viewModelScope)
    }

    fun getToken(): LiveData<DataPreferences> {
        return pref.readDataStore.asLiveData()
    }

    fun clear(){
        viewModelScope.launch(Dispatchers.IO) {
            pref.clearToken()
        }
    }

}