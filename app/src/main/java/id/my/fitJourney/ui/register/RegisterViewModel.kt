package id.my.fitJourney.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import id.my.fitJourney.data.remote.model.RegisterModel
import id.my.fitJourney.data.remote.response.RegisterResponse
import id.my.fitJourney.data.repository.Repository
import id.my.fitJourney.data.remote.Result

class RegisterViewModel(private val repository: Repository) : ViewModel() {
    fun postRegister(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<RegisterResponse>> {
        val user = RegisterModel(email, password, name)
        return repository.postRegister(user)
    }
}