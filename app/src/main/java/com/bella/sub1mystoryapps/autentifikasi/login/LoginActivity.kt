package com.bella.sub1mystoryapps.autentifikasi.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bella.sub1mystoryapps.DataPreferences
import com.bella.sub1mystoryapps.R
import com.bella.sub1mystoryapps.UserPreferences
import com.bella.sub1mystoryapps.ViewModelFactory
import com.bella.sub1mystoryapps.autentifikasi.register.RegistActivity
import com.bella.sub1mystoryapps.databinding.ActivityLoginBinding
import com.bella.sub1mystoryapps.main.ListStoryActivity
import com.google.android.material.snackbar.Snackbar

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
        playAnimation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[LoginViewModel::class.java]

        loginViewModel.getToken().observe(this){
            if(it.token != ""){
                finish()
                val intent = Intent(this@LoginActivity, ListStoryActivity::class.java)
                startActivity(intent)
            }
        }

    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            login()
        }

        binding.tvRegis.setOnClickListener {
            val intentRegister = Intent(this@LoginActivity, RegistActivity::class.java)
            startActivity(intentRegister)
        }

        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        loginViewModel.snackBarText.observe(this) {
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(
                    window.decorView.rootView,
                    snackBarText,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        loginViewModel.getLogin().observe(this) {
            if (it.error == false && it != null) {
                loginViewModel.saveToken(DataPreferences(
                    id = it.loginResult?.userId!!,
                    name = it.loginResult.name!!,
                    state = true,
                    token = it.loginResult.token!!
                )
                )

                val intent = Intent(this@LoginActivity, ListStoryActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            else {
                Toast.makeText(this@LoginActivity, getString(R.string.notRegister), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun login() {
        val email = binding.edtEmailLog.text.toString()
        val password = binding.edtPassLog.text.toString()
        when {
            email.isEmpty() && password.isEmpty() -> {
                binding.edtEmailLog.error = getString(R.string.isiEmail)
                binding.edtPassLog.error = getString(R.string.isiPass)
            }
            email.isEmpty() -> {
                binding.edtEmailLog.error = getString(R.string.isiEmail)
            }
            password.isEmpty() -> {
                binding.edtPassLog.error = getString(R.string.isiPass)
            }
            password.length < 8 -> {
                binding.edtPassLog.error = getString(R.string.passMin8)
                Toast.makeText(this, getString(R.string.passMin8), Toast.LENGTH_SHORT).show()
            }
            else -> {
                loginViewModel.setLogin(email, password)
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imgHome, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleLog, View.ALPHA, 1f).setDuration(500)
        val subtitle = ObjectAnimator.ofFloat(binding.subLogin, View.ALPHA, 1f).setDuration(500)
        val message = ObjectAnimator.ofFloat(binding.subLogin, View.ALPHA, 1f).setDuration(500)
        val emailEditText = ObjectAnimator.ofFloat(binding.edtEmailLog, View.ALPHA, 1f).setDuration(500)
        val passwordEditText = ObjectAnimator.ofFloat(binding.edtPassLog, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val haveNotEditText = ObjectAnimator.ofFloat(binding.blmAdaAkun, View.ALPHA, 1f).setDuration(500)
        val registerEditText = ObjectAnimator.ofFloat(binding.tvRegis, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title, message, emailEditText, passwordEditText, login, haveNotEditText, registerEditText)
            startDelay = 500
        }.start()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}