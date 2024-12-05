package id.my.fitJourney.ui.favorite

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageButton
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import id.my.fitJourney.ViewModelFactory
import id.my.fitJourney.data.local.entity.FoodData
import id.my.fitJourney.databinding.ActivityFavoriteFoodBinding
import id.my.fitJourney.ui.customfood.CustomFoodActivity
import id.my.fitJourney.ui.detailfood.DetailFoodActivity

class FavoriteFoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteFoodBinding
    private lateinit var viewModel: FavoriteFoodViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = obtainViewModel(this)

        binding.btnArrowBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setRecyclerView()
        setupView()

        viewModel.getAllFavoriteFood().observe(this) {
            setupFavoriteFoodData(it)
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteFoodViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteFoodViewModel::class.java]
    }

    private fun setRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvFavoriteFood.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvFavoriteFood.setHasFixedSize(true)
        binding.rvFavoriteFood.addItemDecoration(itemDecoration)
    }

    private fun setupFavoriteFoodData(foods: List<FoodData>) {
        if (foods.isNotEmpty()) {
            binding.tvEmptyFavorite.visibility = View.GONE
            binding.ivKitten.visibility = View.GONE
            binding.rvFavoriteFood.visibility = View.VISIBLE
            binding.btnCheckFoodNutrition.visibility = View.GONE

            val adapter = FavoriteAdapter()
            adapter.submitList(foods)
            binding.rvFavoriteFood.adapter = adapter

            adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
                override fun onItemClicked(data: FoodData) {
                    val intent = Intent(this@FavoriteFoodActivity, DetailFoodActivity::class.java)
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
            binding.btnCheckFoodNutrition.visibility = View.VISIBLE
            binding.btnCheckFoodNutrition.setOnClickListener {
                val intent = Intent(this, CustomFoodActivity::class.java)
                startActivity(intent)
            }
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
}