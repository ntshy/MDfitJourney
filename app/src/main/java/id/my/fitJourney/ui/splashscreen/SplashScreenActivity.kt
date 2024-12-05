package id.my.fitJourney.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import id.my.fitJourney.R
import id.my.fitJourney.ui.login.LoginActivity
import id.my.fitJourney.ui.main.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        auth = Firebase.auth

        Handler(Looper.getMainLooper()).postDelayed({
            redirectToAppropriateActivity()
        }, 1000)
    }

    private fun redirectToAppropriateActivity() {
        val currentUser = auth.currentUser
        val intent = if (currentUser == null) {
            Intent(this@SplashScreenActivity, LoginActivity::class.java)
        } else {
            Intent(this@SplashScreenActivity, MainActivity::class.java)
        }

        startActivity(intent)
        finish()
    }
}
