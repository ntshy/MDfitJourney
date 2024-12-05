package id.my.fitJourney.ui.detailfood

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.WindowInsets
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import id.my.fitJourney.ViewModelFactory
import id.my.fitJourney.data.local.entity.FoodData
import id.my.fitJourney.data.remote.response.CustomFoodResponseItem
import id.my.fitJourney.R
import id.my.fitJourney.databinding.ActivityDetailFoodBinding


class DetailFoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailFoodBinding
    private lateinit var viewModel: DetailFoodViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = obtainViewModel(this)

        setupView()
        binding.btnArrowBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val foodData: CustomFoodResponseItem? = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_FOOD_DATA, CustomFoodResponseItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_FOOD_DATA)
        }

        val favFoodData: FoodData? = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_FAV_FOOD_DATA, FoodData::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_FAV_FOOD_DATA)
        }

        if (foodData != null) setupFoodData(foodData) else favFoodData?.let { setupFavData(it) }

    }

    private fun setupFoodData(foodData: CustomFoodResponseItem) {
        val listSteps = foodData.recipeInstructions?.split(".,")?.map { it.trim() + "." }
        var isFavorite = false
        foodData.name?.let { name ->
            viewModel.isFoodFavorite(name).observe(this) {
                isFavorite = it > 0
                if (isFavorite) {
                    binding.btnFavorite.setImageDrawable(
                        ContextCompat.getDrawable(
                            binding.btnFavorite.context,
                            R.drawable.baseline_favorite_24
                        )
                    )
                } else {
                    binding.btnFavorite.setImageDrawable(
                        ContextCompat.getDrawable(
                            binding.btnFavorite.context,
                            R.drawable.baseline_favorite_border_24
                        )
                    )
                }
            }
        }

        val favData = FoodData(
            name = foodData.name,
            description = foodData.description,
            recipeIngredientParts = foodData.recipeIngredientParts,
            recipeIngredientQuantities = foodData.recipeIngredientQuantities,
            images = foodData.images,
            recipeInstructions = foodData.recipeInstructions,
            recipeServings = foodData.recipeServings,
            recipeCategory = foodData.recipeCategory,
            totalTime = foodData.totalTime,
            sugarContent = foodData.sugarContent,
            cholesterolContent = foodData.cholesterolContent,
            saturatedFatContent = foodData.saturatedFatContent,
            proteinContent = foodData.proteinContent,
            sodiumContent = foodData.sodiumContent,
            calories = foodData.calories,
            carbohydrateContent = foodData.carbohydrateContent,
            fatContent = foodData.fatContent,
            fiberContent = foodData.fiberContent
        )

        binding.tvFoodName.text = foodData.name
        Glide.with(this)
            .load(foodData.images)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .placeholder(R.drawable.ic_place_holder)
            .error(R.drawable.ic_place_holder)
            .into(binding.ivFoodResult)

        binding.btnFavorite.setOnClickListener {
            if (isFavorite) {
                foodData.name?.let { name -> viewModel.deleteFavorite(name) }
            } else {
                viewModel.saveFavoriteFood(favData)
            }
        }

        binding.pbCalories.progress = foodData.calories?.toInt() ?: 0
        binding.pbFat.progress = foodData.fatContent?.toInt() ?: 0
        binding.pbSaturatedFat.progress = foodData.saturatedFatContent?.toInt() ?: 0
        binding.pbCholesterol.progress = foodData.cholesterolContent?.toInt() ?: 0
        binding.pbSodium.progress = foodData.sodiumContent?.toInt() ?: 0
        binding.pbCarbohydrate.progress = foodData.carbohydrateContent?.toInt() ?: 0
        binding.pbFiber.progress = foodData.fiberContent?.toInt() ?: 0
        binding.pbSugar.progress = foodData.sugarContent?.toInt() ?: 0
        binding.pbProtein.progress = foodData.proteinContent?.toInt() ?: 0

        binding.quantityCalories.text =
            getString(R.string.calories_quantity, foodData.calories.toString())
        binding.quantityFat.text = getString(R.string.fat_quantity, foodData.fatContent.toString())
        binding.quantitySaturatedFat.text =
            getString(R.string.saturatedfat_quantity, foodData.saturatedFatContent.toString())
        binding.quantityCholesterol.text =
            getString(R.string.cholesterol_quantity, foodData.cholesterolContent.toString())
        binding.quantitySodium.text =
            getString(R.string.sodium_quantity, foodData.sodiumContent.toString())
        binding.quantityCarbohydrate.text =
            getString(R.string.carbohydrate_quantity, foodData.carbohydrateContent.toString())
        binding.quantityFiber.text =
            getString(R.string.fiber_quantity, foodData.fiberContent.toString())
        binding.quantitySugar.text =
            getString(R.string.sugar_quantity, foodData.sugarContent.toString())
        binding.quantityProtein.text =
            getString(R.string.protein_quantity, foodData.proteinContent.toString())
    }

    private fun setupFavData(favData: FoodData) {
        val listSteps = favData.recipeInstructions?.split(".,")?.map { it.trim() + "." }
        var isFavorite = false
        favData.name?.let { name ->
            viewModel.isFoodFavorite(name).observe(this) {
                isFavorite = it > 0
                if (isFavorite) {
                    binding.btnFavorite.setImageDrawable(
                        ContextCompat.getDrawable(
                            binding.btnFavorite.context,
                            R.drawable.baseline_favorite_24
                        )
                    )
                } else {
                    binding.btnFavorite.setImageDrawable(
                        ContextCompat.getDrawable(
                            binding.btnFavorite.context,
                            R.drawable.baseline_favorite_border_24
                        )
                    )
                }
            }
        }

        binding.tvFoodName.text = favData.name
        Glide.with(this)
            .load(favData.images)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .placeholder(R.drawable.ic_place_holder)
            .error(R.drawable.ic_place_holder)
            .into(binding.ivFoodResult)

        binding.btnFavorite.setOnClickListener {
            if (isFavorite) {
                favData.name?.let { name -> viewModel.deleteFavorite(name) }
            } else {
                viewModel.saveFavoriteFood(favData)
            }
        }

        binding.pbCalories.progress = favData.calories?.toInt() ?: 0
        binding.pbFat.progress = favData.fatContent?.toInt() ?: 0
        binding.pbSaturatedFat.progress = favData.saturatedFatContent?.toInt() ?: 0
        binding.pbCholesterol.progress = favData.cholesterolContent?.toInt() ?: 0
        binding.pbSodium.progress = favData.sodiumContent?.toInt() ?: 0
        binding.pbCarbohydrate.progress = favData.carbohydrateContent?.toInt() ?: 0
        binding.pbFiber.progress = favData.fiberContent?.toInt() ?: 0
        binding.pbSugar.progress = favData.sugarContent?.toInt() ?: 0
        binding.pbProtein.progress = favData.proteinContent?.toInt() ?: 0

        binding.quantityCalories.text =
            getString(R.string.calories_quantity, favData.calories.toString())
        binding.quantityFat.text = getString(R.string.fat_quantity, favData.fatContent.toString())
        binding.quantitySaturatedFat.text =
            getString(R.string.saturatedfat_quantity, favData.saturatedFatContent.toString())
        binding.quantityCholesterol.text =
            getString(R.string.cholesterol_quantity, favData.cholesterolContent.toString())
        binding.quantitySodium.text =
            getString(R.string.sodium_quantity, favData.sodiumContent.toString())
        binding.quantityCarbohydrate.text =
            getString(R.string.carbohydrate_quantity, favData.carbohydrateContent.toString())
        binding.quantityFiber.text =
            getString(R.string.fiber_quantity, favData.fiberContent.toString())
        binding.quantitySugar.text =
            getString(R.string.sugar_quantity, favData.sugarContent.toString())
        binding.quantityProtein.text =
            getString(R.string.protein_quantity, favData.proteinContent.toString())
    }

    private fun obtainViewModel(activity: DetailFoodActivity): DetailFoodViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailFoodViewModel::class.java]
    }

    private fun textListBuilder(list: List<String>?): SpannableStringBuilder {
        val builder = SpannableStringBuilder()

        if (list != null) {
            for (item in list) {
                val formattedText = "• $item\n"
                builder.append(formattedText)
            }
        }
        return builder
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
        const val EXTRA_FOOD_DATA = "extra_food"
        const val EXTRA_FAV_FOOD_DATA = "extra_fav_food"
    }
}