package id.my.fitJourney.data.remote.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: Int
)
