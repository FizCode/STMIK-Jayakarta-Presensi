package com.example.stmikjayakartapresensi.ui.screens.classdetails

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stmikjayakartapresensi.data.api.classes.ClassesDetailsResponse
import com.example.stmikjayakartapresensi.data.api.classes.MyPresenceStatusResponse
import com.example.stmikjayakartapresensi.data.api.classes.StudentPresenceResponse
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
class ClassDetailsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val classesRepository: ClassesRepository,
    private val profileRepository: ProfileRepository
): ViewModel() {

    private val presenceStatusState = MutableStateFlow(StudentPresenceResponse())
    val shouldShowUser: MutableLiveData<ProfileModel> = MutableLiveData()
    val classDetailState = MutableStateFlow(ClassesDetailsResponse())
    val myPresenceStatusState = MutableStateFlow(MyPresenceStatusResponse())
    val shouldShowError: MutableState<Boolean> = mutableStateOf(false)
    val errorMessage: MutableState<String> = mutableStateOf("")

    fun onViewLoaded(classesId: Int) {
        getUserProfile()
        getClassesDetails(classesId = classesId)
    }

    fun postStudentPresence(studentsId: Int, classesId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val token = "Bearer " + authRepository.getToken().toString()
            val result = classesRepository.postStudentPresence(token = token, studentsId = studentsId, classesId = classesId)
            withContext(Dispatchers.Main) {
                if (result.isSuccessful) {
                    presenceStatusState.value = result.body()!!
                } else {
                    shouldShowError.value = true
                    errorMessage.value = result.message().orEmpty()
                }
            }
        }
    }

    private fun getUserProfile() {
        CoroutineScope(Dispatchers.IO).launch {
            val profile = profileRepository.getProfile()
            withContext(Dispatchers.Main) {
                profile.let {
                    shouldShowUser.value = it
                }
            }
        }
    }
    private fun getClassesDetails(classesId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val token = "Bearer " + authRepository.getToken().toString()
            val result = classesRepository.getClassDetails(token = token, classes_id = classesId)
            withContext(Dispatchers.Main) {
                if (result.isSuccessful) {
                    classDetailState.value = result.body()!!
                } else {
                    shouldShowError.value = true
                    errorMessage.value = result.message().orEmpty()
                }
            }

        }
    }

    fun getMyPresenceStatus(studentsId: Int, classesId: Int) {
        CoroutineScope(Dispatchers.IO).launch{
            val token = "Bearer " + authRepository.getToken().toString()
            val result = classesRepository.getMyPresence(token = token, studentsId = studentsId, classesId = classesId)
            withContext(Dispatchers.Main) {
                if (result.isSuccessful) {
                    myPresenceStatusState.value = result.body()!!
                } else {
                    shouldShowError.value = true
                    errorMessage.value = result.message().orEmpty()
                }
            }
        }
    }
}