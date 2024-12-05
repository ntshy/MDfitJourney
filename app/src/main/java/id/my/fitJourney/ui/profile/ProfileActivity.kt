package id.my.fitJourney.ui.profile

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import id.my.fitJourney.ui.editprofile.EditProfileActivity
import id.my.fitJourney.ui.login.LoginActivity
import id.my.fitJourney.R
import id.my.fitJourney.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        val user = auth.currentUser

        user?.let {
            binding.tvProfileName.text = it.displayName
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

        binding.btnEditProfile.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }
        binding.btnLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
        binding.btnLogout.setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.logout))
        builder.setMessage(getString(R.string.logout_message))
        builder.setPositiveButton(getString(R.string.yes)) { _: DialogInterface, _: Int ->
            logout()
        }
        builder.setNegativeButton(getString(R.string.no)) { dialog: DialogInterface, _: Int ->
            dialog.dismiss()
        }

        val alertDialog = builder.create()
        alertDialog.setOnShowListener {
            alertDialog.findViewById<TextView>(android.R.id.message)?.setTextAppearance(
                R.style.DialogTextStyle
            )
            alertDialog.findViewById<TextView>(android.R.id.title)?.setTextAppearance(
                R.style.DialogTextStyle
            )
        }

        alertDialog.show()
    }

    private fun logout() {
        Firebase.auth.signOut()
        startActivity(Intent(this@ProfileActivity, LoginActivity::class.java))
        finish()
    }
}