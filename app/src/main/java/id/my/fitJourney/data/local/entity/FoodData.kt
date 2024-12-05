package id.my.fitJourney.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "food_data")
@Parcelize
data class FoodData(
    @PrimaryKey(autoGenerate = true)
    @field:ColumnInfo(name = "id")
    val id: Int = 0,

    @field:ColumnInfo(name = "name")
    val name: String? = null,

    @field:ColumnInfo("Description")
    val description: String? = null,

    @field:ColumnInfo("Images")
    val images: String? = null,

    @field:ColumnInfo("RecipeIngredientQuantities")
    val recipeIngredientQuantities: List<String>,

    @field:ColumnInfo("RecipeIngredientParts")
    val recipeIngredientParts: List<String>,

    @field:ColumnInfo("RecipeInstructions")
    val recipeInstructions: String? = null,

    @field:ColumnInfo("RecipeServings")
    val recipeServings: Double? = null,

    @field:ColumnInfo("RecipeCategory")
    val recipeCategory: String? = null,

    @field:ColumnInfo("TotalTime")
    val totalTime: String? = null,

    @field:ColumnInfo("SugarContent")
    val sugarContent: Double? = null,

    @field:ColumnInfo("CholesterolContent")
    val cholesterolContent: Double? = null,

    @field:ColumnInfo("SaturatedFatContent")
    val saturatedFatContent: Double? = null,

    @field:ColumnInfo("ProteinContent")
    val proteinContent: Double? = null,

    @field:ColumnInfo("SodiumContent")
    val sodiumContent: Double? = null,

    @field:ColumnInfo("Calories")
    val calories: Double? = null,

    @field:ColumnInfo("CarbohydrateContent")
    val carbohydrateContent: Double? = null,

    @field:ColumnInfo("FatContent")
    val fatContent: Double? = null,

    @field:ColumnInfo("FiberContent")
    val fiberContent: Double? = null
) : Parcelable