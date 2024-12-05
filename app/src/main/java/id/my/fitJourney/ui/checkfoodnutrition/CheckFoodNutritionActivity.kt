package id.my.fitJourney.ui.checkfoodnutrition

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import id.my.fitJourney.ViewModelFactory
import id.my.fitJourney.data.remote.Result
import id.my.fitJourney.ui.camera.CameraActivity
import id.my.fitJourney.ui.camera.CameraActivity.Companion.EXTRA_URI_IMAGE
import id.my.fitJourney.ui.checkfoodresult.CheckFoodResultActivity
import id.my.fitJourney.util.reduceFileImage
import id.my.fitJourney.util.uriToFile
import id.my.fitJourney.R
import id.my.fitJourney.databinding.ActivityCheckFoodNutritionBinding
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class CheckFoodNutritionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckFoodNutritionBinding
    private lateinit var viewModel: CheckFoodNutritionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckFoodNutritionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = obtainViewModel(this)

        setupView()
        setImage()
        binding.btnCamera.setOnClickListener {
            moveToCamera()
        }
        binding.btnGallery.setOnClickListener {
            startGallery()
        }
        binding.btnArrowBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun obtainViewModel(activity: CheckFoodNutritionActivity): CheckFoodNutritionViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[CheckFoodNutritionViewModel::class.java]
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val intent = Intent(this, CheckFoodNutritionActivity::class.java)
            intent.putExtra(EXTRA_URI_IMAGE, uri.toString())
            startActivity(intent)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun setImage() {
        val imgUri = intent.getStringExtra(EXTRA_URI_IMAGE)
        imgUri?.toUri().let {
            Log.d("Image URI", "showImage: $it")
            binding.ivFoodCheck.setImageURI(it)
        }
        if (imgUri != null) {
            binding.btnGetResult.alpha = 1f
            binding.btnGetResult.setOnClickListener {
                uploadImage(imgUri.toUri())
            }
        } else {
            binding.btnGetResult.alpha = 0f
        }
    }

    private fun uploadImage(uri: Uri) {
        val imgFile = uriToFile(uri, this).reduceFileImage()
        val requestImageFile = imgFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "image",
            imgFile.name,
            requestImageFile
        )
        Log.d(CheckFoodNutritionActivity::class.java.simpleName, requestImageFile.toString())
        viewModel.postFoodDetect(multipartBody).observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    showLoading(false)
                    val intent = Intent(this, CheckFoodResultActivity::class.java)
                    intent.putExtra(
                        CheckFoodResultActivity.EXTRA_CHECK_FOOD_RESULT,
                        result.data.data
                    )
                    startActivity(intent)
                }

                is Result.Error -> {
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT)
                        .show()
                    showLoading(false)
                }

                is Result.Empty -> {
                    showLoading(false)
                }

                is Result.Loading -> {
                    binding.btnGetResult.isEnabled = false
                    binding.btnGallery.isEnabled = false
                    binding.btnCamera.isEnabled = false
                    showLoading(true)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(
                    this,
                    getString(R.string.permission_request_granted), Toast.LENGTH_LONG
                ).show()
                moveToCamera()
            } else {
                Toast.makeText(this, getString(R.string.rejected_permission), Toast.LENGTH_LONG)
                    .show()
            }
        }

    private fun moveToCamera() {
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
        if (allPermissionsGranted()) {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }
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

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}