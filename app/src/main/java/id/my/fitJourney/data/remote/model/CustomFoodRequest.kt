package id.my.fitJourney.data.remote.model

import com.google.gson.annotations.SerializedName

data class CustomFoodRequest(
    @SerializedName("Calories") val calories: Int,
    @SerializedName("FatContent") val fatContent: Int,
    @SerializedName("SaturatedFatContent") val saturatedFatContent: Int,
    @SerializedName("CholesterolContent") val cholesterolContent: Int,
    @SerializedName("SodiumContent") val sodiumContent: Int,
    @SerializedName("CarbohydrateContent") val carbohydrateContent: Int,
    @SerializedName("FiberContent") val fiberContent: Int,
    @SerializedName("SugarContent") val sugarContent: Int,
    @SerializedName("ProteinContent") val proteinContent: Int
)

