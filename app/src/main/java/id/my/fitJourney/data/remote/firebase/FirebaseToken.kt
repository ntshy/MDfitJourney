package id.my.fitJourney.data.remote.firebase

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.auth.auth
import java.util.concurrent.CompletableFuture

object FirebaseToken {
    fun getToken(): String {
        val auth: FirebaseAuth = Firebase.auth
        val user = auth.currentUser
        var token: String

        val future = CompletableFuture<String>()

        user?.getIdToken(true)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result: GetTokenResult? = task.result
                    token = result?.token ?: ""
                    future.complete(token)
                } else {
                    future.completeExceptionally(task.exception)
                }
            }

        return future.get()
    }
}