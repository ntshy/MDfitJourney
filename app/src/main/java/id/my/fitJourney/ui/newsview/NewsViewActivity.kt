package id.my.fitJourney.ui.newsview

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import id.my.fitJourney.R
import id.my.fitJourney.databinding.ActivityNewsViewBinding

class NewsViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsViewBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url = intent.getStringExtra(EXTRA_NEWS_URL)

        val webView = binding.webView
        if (url != null) {
            webView.loadUrl(url)
            binding.progressBar.visibility = View.VISIBLE
        }
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(
                    this@NewsViewActivity,
                    getString(R.string.web_success_announce), Toast.LENGTH_LONG
                ).show()
            }
        }

        setupView()
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
        const val EXTRA_NEWS_URL = "extra news url"
    }
}