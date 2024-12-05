package id.my.fitJourney.data.remote.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("user")
    val user: User,

    @field:SerializedName("status")
    val status: Int
)

data class User(

    @field:SerializedName("createdAt")
    val createdAt: CreatedAt,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String
)

data class CreatedAt(

    @field:SerializedName("_nanoseconds")
    val nanoseconds: Int,

    @field:SerializedName("_seconds")
    val seconds: Int
)
