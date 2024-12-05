package id.my.fitJourney.data.remote.retrofit

import id.my.fitJourney.data.remote.model.CustomFoodRequest
import id.my.fitJourney.data.remote.response.CustomFoodResponseItem
import id.my.fitJourney.data.remote.response.FoodDetectResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MLApiService {
    @Multipart
    @POST("prediction")
    suspend fun foodDetect(
        @Part image: MultipartBody.Part,
    ): FoodDetectResponse

    @POST("form")
    suspend fun customFood(@Body request: CustomFoodRequest): List<CustomFoodResponseItem>
}