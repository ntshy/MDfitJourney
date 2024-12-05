package id.my.fitJourney.ui.checkfoodnutrition

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import id.my.fitJourney.data.remote.Result
import id.my.fitJourney.data.remote.response.FoodDetectResponse
import id.my.fitJourney.data.repository.Repository
import okhttp3.MultipartBody

class CheckFoodNutritionViewModel(private val repository: Repository) : ViewModel() {
    fun postFoodDetect(
        multipartBody: MultipartBody.Part,
    ): LiveData<Result<FoodDetectResponse>> =
        repository.postFoodDetect(multipartBody)
}