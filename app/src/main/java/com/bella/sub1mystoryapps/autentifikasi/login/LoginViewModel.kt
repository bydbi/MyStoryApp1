package com.bella.sub1mystoryapps.autentifikasi.login

import android.util.Log
import androidx.lifecycle.*
import com.bella.sub1mystoryapps.DataPreferences
import com.bella.sub1mystoryapps.Event
import com.bella.sub1mystoryapps.UserPreferences
import com.bella.sub1mystoryapps.api.ApiConfig
import com.bella.sub1mystoryapps.apirespon.LoginRespon
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: UserPreferences) : ViewModel() {

    val userLogin = MutableLiveData<LoginRespon>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackBarText = MutableLiveData<Event<String>>()
    val snackBarText: LiveData<Event<String>> = _snackBarText

    fun setLogin(email: String, password: String) {
        _isLoading.value = true
        val retro = ApiConfig.getRetrofitClientInstance()
        retro.postLogin(email, password).enqueue(object : Callback<LoginRespon> {
            override fun onResponse(call: Call<LoginRespon>, response: Response<LoginRespon>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.error != true) {
                        userLogin.postValue(response.body())
                        _snackBarText.value = Event(response.body()?.message.toString())
                        Log.e(TAG, "Connection success data is valid")
                    }
                    else {
                        Log.e(TAG, response.message())
                    }
                }
                else {
                    _snackBarText.value = Event("Sorry your Email or Password maybe wrong")
                    Log.e(TAG, response.body()?.message.toString())
                }
            }

            override fun onFailure(call: Call<LoginRespon>, t: Throwable) {
                _isLoading.value = false
                _snackBarText.value = Event("Sorry, Please connect your internet")
                Log.e(TAG, "error in failure ${t.message.toString()}")
            }

        })
    }

    fun getLogin(): LiveData<LoginRespon> {
        return userLogin
    }

    fun saveToken(user: DataPreferences) {
        viewModelScope.launch {
            pref.saveToken(user)
        }
    }

    fun getToken() : LiveData<DataPreferences> {
        return pref.readDataStore.asLiveData()
    }


    companion object {
        private const val TAG = "LoginViewModel"
    }
}