package com.example.stmikjayakartapresensi.ui.screens.signin

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stmikjayakartapresensi.data.ErrorResponse
import com.example.stmikjayakartapresensi.data.api.auth.SignInRequest
import com.example.stmikjayakartapresensi.data.local.auth.UserEntity
import com.example.stmikjayakartapresensi.repository.AuthRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private var email: String = ""
    private var password: String = ""

    val shouldShowError: MutableLiveData<String> = MutableLiveData()
    val shouldOpenHomePage: MutableLiveData<Boolean> = MutableLiveData()
    // val shouldShowLoading: MutableLiveData<Boolean> = MutableLiveData()

    fun onChangeEmail(email: String) {
        this.email = email
    }

    fun onChangePassword(password: String) {
        this.password = password
    }

    fun onClickSignIn() {
        if (email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            shouldShowError.postValue("Email tidak valid")
        } else if (password.isNotEmpty() && password.length < 1) {
            shouldShowError.postValue("Password tidak valid")
        } else {
            // shouldShowError.postValue("email: ${email}, password: ${password}")
            signInFromAPI() // error local network
        }
    }

    private fun signInFromAPI() {
        CoroutineScope(Dispatchers.IO).launch {
            val request = SignInRequest(
                email = email,
                password = password
            )
            val response = authRepository.signIn(request)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val signInResponse = response.body()
                    signInResponse?.let {
                        val token = it.token.orEmpty()
                        insertToken(token = token)
                        getUserData(token = token)
                    }
                }
            }
        }
    }

    private fun insertToken(token: String) {
        if (token.isNotEmpty()) {
            viewModelScope.launch {
                authRepository.updateToken(token)
            }
        }
    }

    private fun getUserData(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = authRepository.getUser(token = "Bearer $token")
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val getUserResponse = response.body()
                    getUserResponse?.let {
                        val userEntity = UserEntity(
                            id = it.id.hashCode(),
                            name = it.name.orEmpty(),
                            email = it.email.orEmpty(),
                            nim = it.nim.orEmpty()
                        )
                        insertProfile(userEntity)
                    }
                } else {
                    val error =
                        Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                    shouldShowError.postValue(error.message.orEmpty() + " #${error.code}")
                }
            }
        }
    }

    private fun insertProfile(userEntity: UserEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = authRepository.insertUser(userEntity)
            withContext(Dispatchers.Main) {
                if (result != 0L) {
                    shouldOpenHomePage.postValue(true)
                } else {
                    shouldShowError.postValue("Maaf, gagal inset ke dalam ROOM. Harap lapor ke pengembang aplikasi!")
                }
            }
        }
    }
}