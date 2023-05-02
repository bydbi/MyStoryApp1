package com.bella.sub1mystoryapps.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bella.sub1mystoryapps.DataPreferences
import com.bella.sub1mystoryapps.R
import com.bella.sub1mystoryapps.UserPreferences
import com.bella.sub1mystoryapps.ViewModelFactory
import com.bella.sub1mystoryapps.adapter.ListStoryAdapter
import com.bella.sub1mystoryapps.apirespon.ListStoryItem
import com.bella.sub1mystoryapps.apirespon.StoryRespon
import com.bella.sub1mystoryapps.autentifikasi.login.dataStore
import com.bella.sub1mystoryapps.databinding.ActivityListStoryBinding
import com.bella.sub1mystoryapps.main.detail.DetailStoryActivity

class ListStoryActivity : AppCompatActivity() {

    private lateinit var rvStory: RecyclerView
    private val list = ArrayList<DataPreferences>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_story)

        rvStory = findViewById(R.id.rv_stories)
        rvStory.setHasFixedSize(true)

        list.addAll(getListStory())
        showRecyclerList()
    }

    private fun getListStory(): ArrayList<DataPreferences> {
        val dataName = resources.getStringArray(R.array.)
        val dataDescription = resources.getStringArray(R.array.)
        val dataPhoto = resources.getStringArray(R.array.)
        val listStory = ArrayList<DataPreferences>()
        for (i in dataName.indicies){
            val story = DataPreferences(dataName[i], dataDescription[i], dataPhoto.getResource(i, -1))
            listStory.add(story)
        }
        return listStory
    }

    private fun showRecyclerList(){
        rvStory.layoutManager = LinearLayoutManager(this)
        val listStoryAdapter = ListStoryAdapter(list)
        rvStory.adapter = listStoryAdapter
    }
}