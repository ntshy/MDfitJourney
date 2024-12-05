package id.my.fitJourney.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CustomFoodResponseItem(

    @field:SerializedName("Description")
    val description: String? = null,

    @field:SerializedName("AuthorName")
    val authorName: String? = null,

    @field:SerializedName("RecipeYield")
    val recipeYield: String? = null,

    @field:SerializedName("RecipeId")
    val recipeId: Int? = null,

    @field:SerializedName("SugarContent")
    val sugarContent: Double? = null,

    @field:SerializedName("CholesterolContent")
    val cholesterolContent: Double? = null,

    @field:SerializedName("Name")
    val name: String? = null,

    @field:SerializedName("RecipeIngredientQuantities")
    val recipeIngredientQuantities: List<String>,

    @field:SerializedName("RecipeServings")
    val recipeServings: Double? = null,

    @field:SerializedName("TotalTime")
    val totalTime: String? = null,

    @field:SerializedName("SaturatedFatContent")
    val saturatedFatContent: Double? = null,

    @field:SerializedName("ReviewCount")
    val reviewCount: Double? = null,

    @field:SerializedName("ProteinContent")
    val proteinContent: Double? = null,

    @field:SerializedName("CookTime")
    val cookTime: String? = null,

    @field:SerializedName("AuthorId")
    val authorId: Int? = null,

    @field:SerializedName("RecipeInstructions")
    val recipeInstructions: String? = null,

    @field:SerializedName("Images")
    val images: String? = null,

    @field:SerializedName("RecipeIngredientParts")
    val recipeIngredientParts: List<String>,

    @field:SerializedName("SodiumContent")
    val sodiumContent: Double? = null,

    @field:SerializedName("Calories")
    val calories: Double? = null,

    @field:SerializedName("DatePublished")
    val datePublished: String? = null,

    @field:SerializedName("AggregatedRating")
    val aggregatedRating: Double? = null,

    @field:SerializedName("PrepTime")
    val prepTime: String? = null,

    @field:SerializedName("RecipeCategory")
    val recipeCategory: String? = null,

    @field:SerializedName("CarbohydrateContent")
    val carbohydrateContent: Double? = null,

    @field:SerializedName("FatContent")
    val fatContent: Double? = null,

    @field:SerializedName("FiberContent")
    val fiberContent: Double? = null
) : Parcelable
