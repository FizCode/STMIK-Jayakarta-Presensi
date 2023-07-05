package com.example.stmikjayakartapresensi.ui.screens.classdetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stmikjayakartapresensi.data.api.classes.ClassesDetailsResponse
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

    val shouldShowUser: MutableLiveData<ProfileModel> = MutableLiveData()
    val classDetailState = MutableStateFlow(ClassesDetailsResponse())
    val presenceStatusState = MutableStateFlow(StudentPresenceResponse())
    val shouldShowError: MutableLiveData<String> = MutableLiveData()

    fun onViewLoaded(classesId: Int) {
        getClassesDetails(classesId = classesId)
        getUserProfile()
    }

    fun postStudentPresence(studentsId: Int, classesId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val token = "Bearer " + authRepository.getToken().toString()
            val result = classesRepository.postStudentPresence(token = token, studentsId = studentsId, classesId = classesId)
            withContext(Dispatchers.Main) {
                if (result.isSuccessful) {
                    presenceStatusState.value = result.body()!!
                } else {
                    shouldShowError.postValue(result.message().orEmpty())
                }
            }
        }
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
    private fun getClassesDetails(classesId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val token = "Bearer " + authRepository.getToken().toString()
            val result = classesRepository.getClassDetails(token = token, classes_id = classesId)
            withContext(Dispatchers.Main) {
                if (result.isSuccessful) {
                    classDetailState.value = result.body()!!
                } else {
                    shouldShowError.postValue(result.message().orEmpty())
                }
            }

        }
    }
}