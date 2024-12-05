package id.my.fitJourney.ui.detailfood

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.my.fitJourney.data.local.entity.FoodData
import id.my.fitJourney.data.repository.Repository
import kotlinx.coroutines.launch

class DetailFoodViewModel(private val repository: Repository) : ViewModel() {
    fun saveFavoriteFood(food: FoodData) {
        viewModelScope.launch {
            repository.setFavoriteFoods(food)
        }
    }

    fun deleteFavorite(name: String) {
        viewModelScope.launch {
            repository.deleteFavoriteFoodsById(name)
        }
    }

    fun isFoodFavorite(name: String): LiveData<Int> = repository.isFoodFavorite(name)
}