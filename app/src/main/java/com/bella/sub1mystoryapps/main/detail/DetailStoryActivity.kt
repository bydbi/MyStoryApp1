package com.bella.sub1mystoryapps.main.detail

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.bella.sub1mystoryapps.R
import com.bella.sub1mystoryapps.databinding.ActivityDetailStoryBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()

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

    private fun setupAction() {
        showLoading(true)
        val name = intent.getStringExtra(EXTRA_USERNAME)
        val photo = intent.getStringExtra(EXTRA_PHOTO)
        val description = intent.getStringExtra(EXTRA_DESCRIPTION)

        binding.apply {
            tvDetailUsername.text = name
            Glide.with(this@DetailStoryActivity)
                .load(photo)
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.baseline_photo_24)
                .into(detailPhoto)
            detailDesc.text = description
        }
        showLoading(false)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_USERNAME = "extra username"
        const val EXTRA_PHOTO = "extra photo"
        const val EXTRA_DESCRIPTION = "extra description"
    }
}