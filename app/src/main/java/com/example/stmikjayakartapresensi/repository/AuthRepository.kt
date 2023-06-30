package com.example.stmikjayakartapresensi.repository

import com.example.stmikjayakartapresensi.data.api.auth.AuthAPI
import com.example.stmikjayakartapresensi.data.api.auth.GetUserResponse
import com.example.stmikjayakartapresensi.data.api.auth.SignInRequest
import com.example.stmikjayakartapresensi.data.api.auth.SignInResponse
import com.example.stmikjayakartapresensi.data.local.auth.UserDAO
import com.example.stmikjayakartapresensi.data.local.auth.UserEntity
import com.example.stmikjayakartapresensi.datastore.AuthDataStoreManager
import kotlinx.coroutines.flow.firstOrNull
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authDataStore: AuthDataStoreManager,
    private val api: AuthAPI,
    private val dao: UserDAO
) {
    suspend fun getToken(): String? {
        return authDataStore.getToken().firstOrNull()
    }

    suspend fun updateToken(value: String) {
        authDataStore.setToken(value)
    }

//    suspend fun clearToken() {
//        updateToken("")
//    }

    suspend fun signIn(request: SignInRequest): Response<SignInResponse> {
        return api.signIn(request)
    }

    suspend fun getUser(token: String): Response<GetUserResponse> {
        return api.getUser(token = token)
    }

    suspend fun insertUser(userEntity: UserEntity): Long {
        return dao.insertUser(userEntity)
    }
}