package com.bella.sub1mystoryapps

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>){

    suspend fun saveToken(user: DataPreferences) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = user.name
            preferences[STATE_KEY] = user.state
            preferences[TOKEN_KEY] = user.token
            preferences[ID_KEY] = user.id
        }
    }

    val readDataStore : Flow<DataPreferences> = dataStore.data
        .catch { exception ->
            if(exception is IOException){
                Log.d("error", exception.message.toString())
                emit(emptyPreferences())
            }else{
                throw exception
            }
        }.map {
            val name = it[NAME_KEY] ?: ""
            val token = it[TOKEN_KEY] ?: ""
            val userid = it[ID_KEY] ?: ""
            val state = it[STATE_KEY] ?: false
            DataPreferences(name = name, id = userid,token = token, state = state)
        }

    suspend fun clearToken() {
        dataStore.edit {
            it.clear()
        }
    }


    companion object{
        @Volatile
        private var INSTANCE: UserPreferences? = null

        private val NAME_KEY = stringPreferencesKey("name")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val ID_KEY = stringPreferencesKey("id")
        private val STATE_KEY = booleanPreferencesKey("error")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}