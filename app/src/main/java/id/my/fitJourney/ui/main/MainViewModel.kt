package id.my.fitJourney.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.my.fitJourney.data.local.entity.FoodData
import id.my.fitJourney.data.remote.Result
import id.my.fitJourney.data.remote.response.NewsResponse
import id.my.fitJourney.data.repository.Repository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {
    fun getInsightData(
    ): LiveData<Result<NewsResponse>> =
        repository.getNews()

    fun deleteFavorite(name: String) {
        viewModelScope.launch {
            repository.deleteFavoriteFoodsById(name)
        }
    }

    fun getAllFavoriteFood(): LiveData<List<FoodData>> =
        repository.getFavoriteFoods()
}