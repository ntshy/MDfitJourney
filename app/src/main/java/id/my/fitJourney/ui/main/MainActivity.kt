package id.my.fitJourney.ui.main

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import id.my.fitJourney.ViewModelFactory
import id.my.fitJourney.data.local.entity.FoodData
import id.my.fitJourney.ui.customfood.CustomFoodActivity
import id.my.fitJourney.ui.detailfood.DetailFoodActivity
import id.my.fitJourney.ui.favorite.FavoriteAdapter
import id.my.fitJourney.ui.favorite.FavoriteFoodActivity
import id.my.fitJourney.ui.login.LoginActivity
import id.my.fitJourney.ui.newsview.NewsViewActivity
import android.app.AlertDialog
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import id.my.fitJourney.ui.profile.ProfileActivity
import id.my.fitJourney.R
import id.my.fitJourney.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        viewModel = obtainViewModel(this)

        setupView()
        setupFavoriteRecyclerView()
        setBindingView()
        setupProfile()

        viewModel.getAllFavoriteFood().observe(this) {
            setupFavoriteFoodData(it)
        }
    }


    private fun setupProfile() {
        val user = auth.currentUser
        user?.let {
            binding.tvHomeTitle.text = getString(R.string.home_title, it.displayName)
            Glide.with(this)
                .load(it.photoUrl)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .error(R.drawable.profile_placeholder_50)
                .into(binding.ivProfile)
        }
        binding.ivProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): MainViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[MainViewModel::class.java]
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

    private fun setupFavoriteRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvFavoriteFood.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvFavoriteFood.setHasFixedSize(true)
        binding.rvFavoriteFood.addItemDecoration(itemDecoration)
    }

    private fun setupFavoriteFoodData(foodData: List<FoodData>) {
        if (foodData.isNotEmpty()) {
            binding.tvEmptyFavorite.visibility = View.GONE
            binding.ivKitten.visibility = View.GONE
            binding.rvFavoriteFood.visibility = View.VISIBLE
            val foods = foodData.take(5)

            val adapter = FavoriteAdapter()
            adapter.submitList(foods)
            binding.rvFavoriteFood.adapter = adapter

            adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
                override fun onItemClicked(data: FoodData) {
                    val intent = Intent(this@MainActivity, DetailFoodActivity::class.java)
                    intent.putExtra(DetailFoodActivity.EXTRA_FAV_FOOD_DATA, data)
                    startActivity(intent)
                }

                override fun onFavoriteClicked(data: FoodData, btnFavorite: ImageButton) {
                    data.name?.let { viewModel.deleteFavorite(it) }
                }

            })
        } else {
            binding.tvEmptyFavorite.visibility = View.VISIBLE
            binding.rvFavoriteFood.visibility = View.GONE
            binding.ivKitten.visibility = View.VISIBLE
        }
    }


    private fun intentNews(url: String) {
        val intent = Intent(this, NewsViewActivity::class.java)
        intent.putExtra(NewsViewActivity.EXTRA_NEWS_URL, url)
        startActivity(intent)
    }

    private fun setBindingView() {
        binding.ibLogout.setOnClickListener {
            showLogoutDialog()
        }
        binding.btnCustomFood.setOnClickListener {
            startActivity(Intent(this, CustomFoodActivity::class.java))
        }
        binding.btnFavoriteFoodTitle.setOnClickListener {
            startActivity(Intent(this, FavoriteFoodActivity::class.java))
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
        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        finish()
    }
}