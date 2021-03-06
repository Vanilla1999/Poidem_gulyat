package com.example.poidem_gulyat.crypto.Biometry

import androidx.biometric.auth.AuthPromptCallback
import androidx.biometric.auth.AuthPromptErrorException
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.CancellableContinuation
import kotlin.coroutines.resumeWithException

internal class CoroutineAuthPromptCallback(
    private val continuation: CancellableContinuation<androidx.biometric.BiometricPrompt.AuthenticationResult>
) : AuthPromptCallback() {

    override fun onAuthenticationError(
        activity: FragmentActivity?,
        errorCode: Int,
        errString: CharSequence
    ) {
        continuation.resumeWithException(AuthPromptErrorException(errorCode, errString))
    }

    override fun onAuthenticationSucceeded(
        activity: FragmentActivity?,
        result: androidx.biometric.BiometricPrompt.AuthenticationResult
    ) {
        continuation.resumeWith(Result.success(result))
    }

    override fun onAuthenticationFailed(activity: FragmentActivity?) {
        // Stub
    }
}