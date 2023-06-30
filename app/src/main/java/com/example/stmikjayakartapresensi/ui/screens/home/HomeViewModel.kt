package com.example.stmikjayakartapresensi.ui.screens.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stmikjayakartapresensi.data.api.classes.MyClassesResponse
import com.example.stmikjayakartapresensi.model.ProfileModel
import com.example.stmikjayakartapresensi.repository.AuthRepository
import com.example.stmikjayakartapresensi.repository.ClassesRepository
import com.example.stmikjayakartapresensi.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val classesRepository: ClassesRepository
): ViewModel() {
    val myClassesState = MutableStateFlow(MyClassesResponse())
    val shouldShowUser: MutableLiveData<ProfileModel> = MutableLiveData()
    val shouldShowError: MutableLiveData<String> = MutableLiveData()

    fun onViewLoaded() {
        getUserProfile()
        // getMyClasses()
    }



    private fun getUserProfile() {
        CoroutineScope(Dispatchers.IO).launch {
            val profile = profileRepository.getProfile()
            withContext(Dispatchers.Main) {
                profile.let {
                    shouldShowUser.postValue(it)
                }
            }
        }
    }

    private fun getMyClasses() {
        CoroutineScope(Dispatchers.IO).launch {
            val token = authRepository.getToken().toString()
            val studentsId = profileRepository.getProfile().id
            val result = classesRepository.getMyClasses(token = token, studentsId = studentsId)
            withContext(Dispatchers.Main) {
                if (result.isSuccessful) {
                    myClassesState.value = result.body()!!
                } else {
                    shouldShowError.postValue(token)
                }
            }
        }
    }
}