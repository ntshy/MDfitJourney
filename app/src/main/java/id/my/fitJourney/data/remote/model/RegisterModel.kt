package id.my.fitJourney.data.remote.model

import com.google.gson.annotations.SerializedName


data class RegisterModel(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("name") val name: String
)