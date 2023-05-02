package com.bella.sub1mystoryapps.story

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.bella.sub1mystoryapps.R
import com.bella.sub1mystoryapps.UserPreferences
import com.bella.sub1mystoryapps.ViewModelFactory
import com.bella.sub1mystoryapps.api.ApiConfig
import com.bella.sub1mystoryapps.apirespon.AddStoryRespon
import com.bella.sub1mystoryapps.autentifikasi.login.dataStore
import com.bella.sub1mystoryapps.databinding.ActivityUploadBinding
import com.bella.sub1mystoryapps.main.ListStoryActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private lateinit var uploadViewModel: UploadViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.tambahStory)

        setupViewModel()

        binding.cameraXButton.setOnClickListener { startCameraX() }
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.fabUpload.setOnClickListener { uploadImage() }
    }

    private fun setupViewModel() {
        uploadViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[UploadViewModel::class.java]
    }

    private fun uploadImage() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)

            val description = binding.desc.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            uploadViewModel.getToken().observe(this) { user ->
                val token = user.token

                val retro = ApiConfig.getRetrofitClientInstance()
                showLoading(true)
                retro.uploadStory(
                    token = "Bearer $token",
                    imageMultiPart,
                    description
                ).enqueue(object : Callback<AddStoryRespon> {
                    override fun onResponse(
                        call: Call<AddStoryRespon>,
                        response: Response<AddStoryRespon>
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            if (responseBody != null && !responseBody.error) {
                                showLoading(false)
                                Toast.makeText(
                                    this@UploadActivity,
                                    responseBody.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(this@UploadActivity, ListStoryActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        } else {
                            showLoading(false)
                            Toast.makeText(
                                this@UploadActivity,
                                response.message(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<AddStoryRespon>, t: Throwable) {
                        showLoading(false)
                        Toast.makeText(
                            this@UploadActivity,
                            getString(R.string.failRetrofit),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
        }
        else {
            Toast.makeText(this@UploadActivity, getString(R.string.masukGambar), Toast.LENGTH_SHORT).show()
        }
    }



    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.pilihGambar))
        launcherIntentGallery.launch(chooser)
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private var getFile: File? = null
    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile
            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )
            binding.imgPreview.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@UploadActivity)
            getFile = myFile
            binding.imgPreview.setImageURI(selectedImg)
        }
    }

    private fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmPicByArray = bmpStream.toByteArray()
            streamLength = bmPicByArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val CAMERA_X_RESULT = 200
    }
}