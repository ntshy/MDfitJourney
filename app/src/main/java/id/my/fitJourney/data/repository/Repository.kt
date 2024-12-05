package id.my.fitJourney.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import id.my.fitJourney.data.local.dao.FavoriteFoodDao
import id.my.fitJourney.data.local.entity.FoodData
import id.my.fitJourney.data.remote.model.RegisterModel
import id.my.fitJourney.data.remote.response.RegisterResponse
import id.my.fitJourney.data.remote.retrofit.CCApiService
import id.my.fitJourney.data.remote.Result
import id.my.fitJourney.data.remote.model.CustomFoodRequest
import id.my.fitJourney.data.remote.response.CustomFoodResponseItem
import id.my.fitJourney.data.remote.response.ErrorResponse
import id.my.fitJourney.data.remote.response.FoodDetectResponse
import id.my.fitJourney.data.remote.response.NewsResponse
import id.my.fitJourney.data.remote.retrofit.MLApiService
import okhttp3.MultipartBody
import retrofit2.HttpException

class Repository private constructor(
    private val ccApiService: CCApiService,
    private val ccAuthApiService: CCApiService,
    private val mlApiService: MLApiService,
    private val favoriteFoodDao: FavoriteFoodDao,
) {
    fun postRegister(registerData: RegisterModel): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response =
                ccAuthApiService.register(registerData)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            Log.d(Repository::class.java.simpleName, "postRegister: ${e.message.toString()}")
            emit(Result.Error(errorMessage))
        }
    }

    fun getNews(): LiveData<Result<NewsResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = ccApiService.getNews()
            if (response.data.isNotEmpty()) {
                emit(Result.Success(response))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun postFoodDetect(
        multipartBody: MultipartBody.Part,
    ): LiveData<Result<FoodDetectResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = mlApiService.foodDetect(multipartBody)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            Log.d(Repository::class.java.simpleName, "postRegister: ${e.message.toString()}")
            emit(Result.Error(errorMessage))
        }
    }

    fun postCustomFood(
        calories: Int,
        fatContent: Int,
        saturatedFatContent: Int,
        cholesterolContent: Int,
        sodiumContent: Int,
        carbohydrateContent: Int,
        fiberContent: Int,
        sugarContent: Int,
        proteinContent: Int
    ): LiveData<Result<List<CustomFoodResponseItem>>> = liveData {
        emit(Result.Loading)
        try {
            val customFoodRequest = CustomFoodRequest(
                calories,
                fatContent,
                saturatedFatContent,
                cholesterolContent,
                sodiumContent,
                carbohydrateContent,
                fiberContent,
                sugarContent,
                proteinContent
            )
            val response = mlApiService.customFood(customFoodRequest)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            Log.d(Repository::class.java.simpleName, "postRegister: ${e.message.toString()}")
            emit(Result.Error(errorMessage))
        }
    }

    fun getFavoriteFoods(): LiveData<List<FoodData>> = favoriteFoodDao.getAllFavorites()

    suspend fun deleteFavoriteFoodsById(name: String) {
        favoriteFoodDao.delete(name)
    }

    suspend fun setFavoriteFoods(foodData: FoodData) {
        favoriteFoodDao.insert(foodData)
    }

    fun isFoodFavorite(name: String): LiveData<Int> = favoriteFoodDao.isFoodFavoriteByName(name)

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            ccApiService: CCApiService,
            ccAuthApiService: CCApiService,
            mlApiService: MLApiService,
            favoriteFoodDao: FavoriteFoodDao
        ): Repository = instance ?: synchronized(this) {
            instance ?: Repository(ccApiService, ccAuthApiService, mlApiService, favoriteFoodDao)
        }.also { instance = it }
    }
}