package id.my.fitJourney.ui.customfoodresult

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageButton
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import id.my.fitJourney.ViewModelFactory
import id.my.fitJourney.data.local.entity.FoodData
import id.my.fitJourney.data.remote.response.CustomFoodResponseItem
import id.my.fitJourney.ui.detailfood.DetailFoodActivity
import id.my.fitJourney.ui.main.MainActivity
import id.my.fitJourney.R
import id.my.fitJourney.databinding.ActivityCustomFoodResultBinding

class CustomFoodResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCustomFoodResultBinding
    private lateinit var viewModel: CustomFoodResultViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomFoodResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = obtainViewModel(this)

        setRecyclerView()
        setFoodsData()
        setupView()

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@CustomFoodResultActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)

        binding.btnArrowBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): CustomFoodResultViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[CustomFoodResultViewModel::class.java]
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

    private fun setRecyclerView() {
        val layoutManger =
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) GridLayoutManager(
                this,
                5
            ) else GridLayoutManager(this, 2)
        binding.rvFoodResultItem.layoutManager = layoutManger
        binding.rvFoodResultItem.setHasFixedSize(true)
    }

    private fun setFoodsData() {
        val adapter = CustomFoodAdapter()
        binding.rvFoodResultItem.adapter = adapter

        val foodData = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableArrayListExtra(EXTRA_FOOD_DATA, CustomFoodResponseItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableArrayListExtra(EXTRA_FOOD_DATA)
        }

        val foodNames = foodData?.map { it.name ?: "" }

        val isFavoriteMap = mutableMapOf<String, Boolean>()
        foodNames?.let {

            for (foodName in it) {
                viewModel.isFoodFavorite(foodName)
                    .observe(this@CustomFoodResultActivity) { number ->
                        isFavoriteMap[foodName] = number > 0
                        adapter.setIsFavoriteMap(isFavoriteMap)
                    }
            }

        }

        adapter.submitList(foodData)

        adapter.setOnItemClickCallback(object : CustomFoodAdapter.OnItemClickCallback {
            override fun onItemClicked(data: CustomFoodResponseItem) {
                val intent = Intent(this@CustomFoodResultActivity, DetailFoodActivity::class.java)
                intent.putExtra(DetailFoodActivity.EXTRA_FOOD_DATA, data)
                startActivity(intent)
            }

            override fun onFavoriteClicked(data: CustomFoodResponseItem, btnFavorite: ImageButton) {
                val food = FoodData(
                    name = data.name,
                    description = data.description,
                    recipeIngredientParts = data.recipeIngredientParts,
                    recipeIngredientQuantities = data.recipeIngredientQuantities,
                    images = data.images,
                    recipeInstructions = data.recipeInstructions,
                    recipeServings = data.recipeServings,
                    recipeCategory = data.recipeCategory,
                    totalTime = data.totalTime,
                    sugarContent = data.sugarContent,
                    cholesterolContent = data.cholesterolContent,
                    saturatedFatContent = data.saturatedFatContent,
                    proteinContent = data.proteinContent,
                    sodiumContent = data.sodiumContent,
                    calories = data.calories,
                    carbohydrateContent = data.carbohydrateContent,
                    fatContent = data.fatContent,
                    fiberContent = data.fiberContent
                )
                data.name?.let {

                    var isFavorite = isFavoriteMap[it] ?: false

                    viewModel.isFoodFavorite(it).observe(this@CustomFoodResultActivity) { number ->
                        isFavorite = number > 0
                    }

                    if (isFavorite) {
                        btnFavorite.setImageDrawable(
                            ContextCompat.getDrawable(
                                btnFavorite.context,
                                R.drawable.baseline_favorite_border_24
                            )
                        )
                        viewModel.deleteFavorite(data.name)
                    } else {
                        btnFavorite.setImageDrawable(
                            ContextCompat.getDrawable(
                                btnFavorite.context,
                                R.drawable.baseline_favorite_24
                            )
                        )
                        viewModel.saveFavoriteFood(food)
                    }

                    isFavoriteMap[data.name] = !isFavorite
                }

            }
        })
    }

    companion object {
        const val EXTRA_FOOD_DATA = "food_data"
    }
}
