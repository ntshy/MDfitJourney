package id.my.fitJourney.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ConverterUtils {
    @TypeConverter
    fun toJson(list: List<String>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromJson(value: String): List<String> {
        return Gson().fromJson(value, object : TypeToken<List<String>>() {}.type)
    }
}