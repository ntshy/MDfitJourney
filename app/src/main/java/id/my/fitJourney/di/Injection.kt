package id.my.fitJourney.di

import android.content.Context
import id.my.fitJourney.data.local.database.FavoriteFoodDatabase
import id.my.fitJourney.data.remote.retrofit.ApiConfig
import id.my.fitJourney.data.repository.Repository

object Injection {
    fun provideRepository(context: Context): Repository {
        val ccApiService = ApiConfig.getCCApiService()
        val ccAuthApiService = ApiConfig.getCCApiServiceForAuth()
        val mlApiService = ApiConfig.getMLApiService()
        val database = FavoriteFoodDatabase.getDatabase(context)
        val favoriteFoodDao = database.favoriteFoodDao()
        return Repository.getInstance(ccApiService, ccAuthApiService, mlApiService, favoriteFoodDao)
    }
}