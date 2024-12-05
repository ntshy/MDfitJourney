package id.my.fitJourney.ui.register

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import id.my.fitJourney.ViewModelFactory
import id.my.fitJourney.data.remote.Result
import id.my.fitJourney.ui.login.LoginActivity
import id.my.fitJourney.R
import id.my.fitJourney.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerViewModel = obtainViewModel(this@RegisterActivity)

        auth = Firebase.auth

        binding.switchToLogin.setOnClickListener {
            switchToLogin()
        }

        binding.registerButton.setOnClickListener {
            if (isNetworkAvailable(this)) {
                onRegisterButtonClicked()
            } else {
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
            }
        }

        setupView()
    }

    private fun obtainViewModel(registerActivity: RegisterActivity): RegisterViewModel {
        val factory = ViewModelFactory.getInstance(registerActivity.application)
        return ViewModelProvider(registerActivity, factory)[RegisterViewModel::class.java]
    }

    private fun onRegisterButtonClicked() {
        val name = binding.nameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        val confirmPassword = binding.confirmPasswordEditText.text.toString()

        if (password == confirmPassword) {
            registerViewModel.postRegister(name, email, password).observe(this) { result ->
                when (result) {
                    is Result.Success -> {
                        showLoading(false)
                        Toast.makeText(
                            this@RegisterActivity,
                            result.data.message, Toast.LENGTH_SHORT
                        ).show()
                        switchToLogin()
                    }

                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(this@RegisterActivity, result.error, Toast.LENGTH_SHORT)
                            .show()
                    }

                    is Result.Loading -> {
                        showLoading(true)
                    }

                    is Result.Empty -> {
                        showLoading(false)
                        Toast.makeText(
                            this,
                            getString(R.string.response_data_is_empty),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } else {
            binding.registerResult.text = getString(R.string.error_confirm_password)
        }
    }

    private fun switchToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
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
}