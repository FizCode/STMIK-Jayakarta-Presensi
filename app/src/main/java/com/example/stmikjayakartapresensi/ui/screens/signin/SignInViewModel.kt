package com.example.stmikjayakartapresensi.ui.screens.signin

import android.util.Patterns
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
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

    val shouldShowLoading: MutableState<Boolean> = mutableStateOf(false)
    val shouldShowError: MutableState<Boolean> = mutableStateOf(false)
    val errorMessage: MutableState<String> = mutableStateOf("")
    val shouldOpenHomePage: MutableLiveData<Boolean> = MutableLiveData()

    fun onChangeEmail(email: String) {
        this.email = email
    }

    fun onChangePassword(password: String) {
        this.password = password
    }

    fun onClickSignIn() {
        if (email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            shouldShowError.value = true
            errorMessage.value = "Email Tidak Valid"
        } else if (password.isNotEmpty() && password.length < 1) {
            shouldShowError.value = true
            errorMessage.value = "Password tidak valid"
        } else {
            signInFromAPI()
        }
    }

    private fun signInFromAPI() {
        shouldShowLoading.value = true
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
                } else {
                    shouldShowError.value = true
                    errorMessage.value = response.message().orEmpty()
                }
            }
            shouldShowLoading.value = false
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
                            nim = it.nim.orEmpty(),
                            major = it.major.orEmpty()
                        )
                        insertProfile(userEntity)
                    }
                } else {
                    val error =
                        Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                    shouldShowError.value = true
                    errorMessage.value = error.message.orEmpty() + " #${error.code}"
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
                    shouldShowError.value = true
                    errorMessage.value = "Maaf, gagal inset ke dalam ROOM. Harap lapor ke pengembang aplikasi!"
                }
            }
        }
    }
}