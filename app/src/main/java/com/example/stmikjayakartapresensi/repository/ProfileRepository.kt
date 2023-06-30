package com.example.stmikjayakartapresensi.repository

import com.example.stmikjayakartapresensi.model.ProfileModel
import com.example.stmikjayakartapresensi.data.local.auth.UserDAO
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val dao: UserDAO
) {
    suspend fun getProfile(): ProfileModel {
        return dao.getUser().let {
            ProfileModel(
                id = it?.id.hashCode(),
                name = it?.name.orEmpty(),
                email = it?.email.orEmpty(),
                nim = it?.nim.orEmpty()
            )
        }
    }
}