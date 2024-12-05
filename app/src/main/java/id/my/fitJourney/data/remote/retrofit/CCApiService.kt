package id.my.fitJourney.data.remote.retrofit

import id.my.fitJourney.data.remote.model.RegisterModel
import id.my.fitJourney.data.remote.response.NewsResponse
import id.my.fitJourney.data.remote.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CCApiService {

    @POST("auth/signup")
    suspend fun register(
        @Body request: RegisterModel
    ): RegisterResponse

    @GET("news")
    suspend fun getNews(): NewsResponse
}