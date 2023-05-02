package com.bella.sub1mystoryapps.autentifikasi.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bella.sub1mystoryapps.api.ApiConfig
import com.bella.sub1mystoryapps.apirespon.RegistRespon
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistViewModel: ViewModel() {

    val userRegister= MutableLiveData<RegistRespon>()

    fun setRegister(name: String, email:String, password:String){
        val retro = ApiConfig.getRetrofitClientInstance()
        retro.postRegister(name, email, password).enqueue(object : Callback<RegistRespon> {
            override fun onResponse(
                call: Call<RegistRespon>,
                response: Response<RegistRespon>
            ) {
                if (response.isSuccessful){
                    userRegister.postValue(response.body())
                    Log.e(TAG, "Success to connect Api to register")
                }
            }

            override fun onFailure(call: Call<RegistRespon>, t: Throwable) {
                Log.e(TAG, "error ${t.message.toString()}")
                Log.e(TAG, "please, connect your internet")
            }

        })
    }

    fun getRegister(): LiveData<RegistRespon> {
        return userRegister
    }

    companion object {
        const val TAG = "RegisterViewModel"
    }
}