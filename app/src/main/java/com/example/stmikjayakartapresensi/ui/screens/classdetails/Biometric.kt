package com.example.stmikjayakartapresensi.ui.screens.classdetails

import android.content.Context
import android.os.Build
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.Executor

object Biometric {
    fun statusName(context: Context): String {
        val biometricManager = BiometricManager.from(context)
        var result = 0

        result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            biometricManager.canAuthenticate(BiometricManager.Authenticators.DEVICE_CREDENTIAL or BiometricManager.Authenticators.BIOMETRIC_STRONG)
        } else {
            biometricManager.canAuthenticate()
        }
        return when (result) {
            BiometricManager.BIOMETRIC_SUCCESS -> "BIOMETRIC_SUCCESS"
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> "BIOMETRIC_ERROR_NO_HARDWARE"
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> "BIOMETRIC_ERROR_HW_UNAVAILABLE"
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> "BIOMETRIC_ERROR_NONE_ENROLLED"
            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> "BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED"
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> "BIOMETRIC_ERROR_UNSUPPORTED"
            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> "BIOMETRIC_STATUS_UNKNOWN"
            else -> {""}
        }
    }

    fun status(context: Context): Boolean {
        var result = false
        val biometricManager = BiometricManager.from(context)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            when (biometricManager.canAuthenticate(BiometricManager.Authenticators.DEVICE_CREDENTIAL or BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
                BiometricManager.BIOMETRIC_SUCCESS -> result = true
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> result = false
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> result = false
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> result = false
                BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> result = true
                BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> result = true
                BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> result = false
            }
        } else {
            when (biometricManager.canAuthenticate()) {
                BiometricManager.BIOMETRIC_SUCCESS -> result = true
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> result = false
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> result = false
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> result = false
                BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> result = true
                BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> result = true
                BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> result = false
            }
        }
        return  result
    }

    fun authenticate(
        activity: FragmentActivity,
        context: Context,
        title: String,
        subtitle: String,
        description: String,
        negativeText: String,
        onSuccess: (BiometricPrompt.AuthenticationResult) -> Unit,
        onError: (Int, CharSequence) -> Unit,
        onFailed: ()-> Unit
    ) {
        val executor: Executor = ContextCompat.getMainExecutor(context)
        val biometricPrompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess(result)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    onFailed()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onError(errorCode, errString)
                }
            }
        )
        val promptInfo: BiometricPrompt.PromptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setDescription(description)
            .setNegativeButtonText(negativeText)
            .build()
        biometricPrompt.authenticate(promptInfo)
    }
}