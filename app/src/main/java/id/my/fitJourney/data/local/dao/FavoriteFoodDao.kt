package id.my.fitJourney.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.my.fitJourney.data.local.entity.FoodData

@Dao
interface FavoriteFoodDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(foodData: FoodData)

    @Query("DELETE FROM food_data WHERE name =:name")
    suspend fun delete(name: String)

    @Query("SELECT * from food_data")
    fun getAllFavorites(): LiveData<List<FoodData>>

    @Query("SELECT COUNT(*) FROM food_data WHERE name = :name")
    fun isFoodFavoriteByName(name: String): LiveData<Int>
}