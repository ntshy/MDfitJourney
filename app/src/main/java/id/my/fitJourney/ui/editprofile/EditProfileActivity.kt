package id.my.fitJourney.ui.editprofile

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import id.my.fitJourney.ui.main.MainActivity
import id.my.fitJourney.R
import id.my.fitJourney.databinding.ActivityEditProfileBinding


class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var auth: FirebaseAuth
    private var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        val user = auth.currentUser

        user?.let {
            imageUri = it.photoUrl
            binding.nameEditText.setText(it.displayName)
            Glide.with(this)
                .load(it.photoUrl)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .placeholder(R.drawable.profile_placeholder)
                .error(R.drawable.profile_placeholder)
                .into(binding.ivProfile)
        }

        binding.btnArrowBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.ivProfile.setOnClickListener {
            startGallery()
        }

        binding.btnSave.setOnClickListener {
            showLoading(true)
            binding.btnSave.isEnabled = false
            val profileUpdates = userProfileChangeRequest {
                displayName = binding.nameEditText.text.toString()
                photoUri = imageUri
            }

            user!!.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    showLoading(false)
                    if (task.isSuccessful) {
                        binding.btnSave.isEnabled = true
                        Toast.makeText(this, "User profile updated", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "User profile updated.")

                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    } else {
                        binding.btnSave.isEnabled = true
                        Toast.makeText(this, "User profile update unsuccessful", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            uri.let {
                imageUri = it
                Log.d("Image URI", "showImage: $it")
                Glide.with(this)
                    .load(uri)
                    .apply(RequestOptions.bitmapTransform(CircleCrop()))
                    .into(binding.ivProfile)
            }
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val TAG = "EditProfileActivity"
    }
}