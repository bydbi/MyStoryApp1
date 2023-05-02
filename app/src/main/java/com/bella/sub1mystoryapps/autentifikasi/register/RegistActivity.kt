package com.bella.sub1mystoryapps.autentifikasi.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bella.sub1mystoryapps.R
import com.bella.sub1mystoryapps.autentifikasi.login.LoginActivity
import com.bella.sub1mystoryapps.databinding.ActivityRegistBinding

class RegistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistBinding
    private lateinit var registerViewModel: RegistViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupView()
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
        registerViewModel = ViewModelProvider(
            this, ViewModelProvider.NewInstanceFactory()
        )[RegistViewModel::class.java]
    }

    private fun setupAction() {
        binding.btnRegist.setOnClickListener {
            registerAction()
            registerViewModel.getRegister().observe(this) {
                if (it.error == true) {
                    showLoading(false)
                    Toast.makeText(
                        this@RegistActivity,
                        getString(R.string.emailAda),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    showLoading(false)
                    Toast.makeText(this@RegistActivity, getString(R.string.daftarSukses), Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@RegistActivity, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun registerAction() {
        val name = binding.edtNamaReg.text.toString()
        val email = binding.edtEmailReg.text.toString()
        val password = binding.edtPassReg1.text.toString()
        val password2 = binding.edtPassReg2.text.toString()
        when {
            name.isEmpty() && email.isEmpty() && password.isEmpty() && password2.isEmpty() -> {
                binding.edtNamaReg.error = getString(R.string.nama)
                binding.edtEmailReg.error = getString(R.string.emailReg)
                binding.edtPassReg1.error = getString(R.string.passReg)
                binding.edtPassReg2.error = getString(R.string.konfirmPass)
            }
            name.isEmpty() -> {
                binding.edtNamaReg.error = getString(R.string.nama)
            }
            email.isEmpty() -> {
                binding.edtEmailReg.error = getString(R.string.emailReg)
            }
            password.isEmpty() -> {
                binding.edtPassReg1.error = getString(R.string.passReg)
            }
            password2.isEmpty() -> {
                binding.edtPassReg2.error = getString(R.string.konfirmPass)
            }
            password2.length < 8 -> {
                binding.edtPassReg2.error = getString(R.string.passMin8)
            }
            password.length < 8 -> {
                binding.edtPassReg1.error = getString(R.string.passMin8)
            }
            password != password2 -> {
                binding.edtPassReg2.error = getString(R.string.passSama)
            }
            name.isEmpty() && password.isEmpty() -> {
                binding.edtNamaReg.error = getString(R.string.nama)
                binding.edtPassReg1.error = getString(R.string.passReg)
            }
            else -> {
                showLoading(true)
                registerViewModel.setRegister(name, email, password)
            }
        }
    }

    private fun playAnimation() {

        val title = ObjectAnimator.ofFloat(binding.titleReg, View.ALPHA, 1f).setDuration(500)
        val subtitle = ObjectAnimator.ofFloat(binding.subtitleReg, View.ALPHA, 1f).setDuration(500)
        val nameEditText = ObjectAnimator.ofFloat(binding.edtNamaReg, View.ALPHA, 1f).setDuration(500)
        val emailEditText = ObjectAnimator.ofFloat(binding.edtEmailReg, View.ALPHA, 1f).setDuration(500)
        val passwordEditText = ObjectAnimator.ofFloat(binding.edtPassReg1, View.ALPHA, 1f).setDuration(500)
        val passwordEditText2 = ObjectAnimator.ofFloat(binding.edtPassReg2, View.ALPHA, 1f).setDuration(500)
        val register = ObjectAnimator.ofFloat(binding.btnRegist, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title, nameEditText, emailEditText, passwordEditText, passwordEditText2, register)
            startDelay = 500
        }.start()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}