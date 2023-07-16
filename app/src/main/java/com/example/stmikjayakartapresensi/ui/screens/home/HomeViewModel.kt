package com.example.stmikjayakartapresensi.ui.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stmikjayakartapresensi.data.api.classes.TodayClassesResponse
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val classesRepository: ClassesRepository
): ViewModel() {

    val todayClassesState = MutableStateFlow(TodayClassesResponse())
    val shouldShowUser: MutableLiveData<ProfileModel> = MutableLiveData()
    val shouldShowError: MutableState<Boolean> = mutableStateOf(false)
    val errorMessage: MutableState<String> = mutableStateOf("")

    fun onViewLoaded() {
        getUserProfile()
        getMyClasses()
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
            val token = "Bearer " + authRepository.getToken().toString()
            val studentsId = profileRepository.getProfile().id

            val formatter = DateTimeFormatter.ofPattern("EEEE", Locale("id"))
            val currentDay: String = LocalDate.now().format(formatter)

            val result = classesRepository.getTodayClass(token = token, studentsId = studentsId, day = currentDay)
            withContext(Dispatchers.Main) {
                if (result.isSuccessful) {
                    todayClassesState.value = result.body()!!
                } else {
                    shouldShowError.value = true
                    errorMessage.value = result.message().orEmpty()
                }
            }
        }
    }
}