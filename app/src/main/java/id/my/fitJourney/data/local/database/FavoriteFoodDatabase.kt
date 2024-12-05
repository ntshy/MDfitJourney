package id.my.fitJourney.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import id.my.fitJourney.data.local.dao.FavoriteFoodDao
import id.my.fitJourney.data.local.entity.FoodData
import id.my.fitJourney.util.ConverterUtils

@Database(
    entities = [FoodData::class],
    version = 1
)
@TypeConverters(ConverterUtils::class)
abstract class FavoriteFoodDatabase : RoomDatabase() {
    abstract fun favoriteFoodDao(): FavoriteFoodDao

    companion object {
        @Volatile
        private var INSTANCE: FavoriteFoodDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): FavoriteFoodDatabase {
            if (INSTANCE == null) {
                synchronized(FavoriteFoodDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FavoriteFoodDatabase::class.java, "favorite_food"
                    )
                        .build()
                }
            }
            return INSTANCE as FavoriteFoodDatabase
        }
    }
}