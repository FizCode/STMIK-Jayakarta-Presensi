package com.example.stmikjayakartapresensi.ui.screens.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stmikjayakartapresensi.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
private val authRepository: AuthRepository
): ViewModel() {
    val shouldOpenSignIn: MutableLiveData<Boolean> = MutableLiveData()
    val shouldOpenHome: MutableLiveData<Boolean> = MutableLiveData()
    var userToken: MutableLiveData<String> = MutableLiveData()

    fun onViewLoaded() {
        getUserToken()
    }

    private fun getUserToken() {
        viewModelScope.launch {
            val result = authRepository.getToken()
            withContext(Dispatchers.Main) {
                if (result.isNullOrEmpty()) {
                    shouldOpenSignIn.postValue(true)
                } else {
                    shouldOpenHome.postValue(true)
                }
            }
        }
    }
}