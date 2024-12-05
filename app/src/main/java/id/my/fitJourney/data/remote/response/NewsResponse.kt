package id.my.fitJourney.data.remote.response

import com.google.gson.annotations.SerializedName

data class NewsResponse(

    @field:SerializedName("data")
    val data: List<DataItem>,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: Int
)

data class DataItem(

    @field:SerializedName("image")
    val image: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("sort")
    val sort: Int,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("url")
    val url: String
)
