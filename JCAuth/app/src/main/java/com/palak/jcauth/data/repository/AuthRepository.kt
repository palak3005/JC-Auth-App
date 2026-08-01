package com.palak.jcauth.data.repository

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()

    suspend fun login(email: String, password: String): Boolean {
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            return true
        } catch (e: Exception) {
            return false
        }
    }

    suspend fun signup(email: String, password: String): Boolean {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun logout(): Boolean {
        try {
            auth.signOut()
            return true
        } catch (e: Exception) {
            return false
        }


    }

    suspend fun sendPasswordReset(email: String): Boolean {
        return try {
            auth.sendPasswordResetEmail(email).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun signInWithGoogle(context: Context, webClientId: String): Boolean {
        return try {
            val credentialManager =
                CredentialManager.create(context)
            val googleIdOption =
                GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(webClientId)
                    .setAutoSelectEnabled(false)
                    .build()

            val request = GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build()


            val result = credentialManager.getCredential(
                context, request
            )

            val credential = result.credential

            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                val firebaseCredential =
                    GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)

                auth.signInWithCredential(firebaseCredential).await()
                true
            } else {
                false
            }

        } catch (e: Exception) {
            return false
        }
    }

    fun sendOTP(
        activity: Activity,
        phoneNumber: String,
        resendToken: PhoneAuthProvider.ForceResendingToken? = null,
        onCodeSent: (String, PhoneAuthProvider.ForceResendingToken) -> Unit,
        onVerificationComplete: (PhoneAuthCredential) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val callbacks =object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    onVerificationComplete(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Log.e("OTP_DEBUG", e.message ?: "Failed")
                    onFailure(e.message ?: "Verification failed")
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    Log.d("OTP_DEBUG", "onCodeSent")
                    super.onCodeSent(verificationId,token)
                    onCodeSent(verificationId,token)
                }
            }
             val  builder = PhoneAuthOptions.newBuilder(auth)
                 .setPhoneNumber(phoneNumber)
                 .setTimeout(60L, TimeUnit.SECONDS)
                 .setActivity(activity)
                 .setCallbacks(callbacks)

        resendToken?.let {
            builder.setForceResendingToken(it)
        }


        PhoneAuthProvider.verifyPhoneNumber(builder.build())


    }


    suspend fun signInWithCredential(credential: PhoneAuthCredential): Boolean {
        return try {
            auth.signInWithCredential(credential).await()
            true
        } catch (e: Exception) {
            false
        }
    }


    suspend fun verifyOTP(
        verificationId : String,
        otp : String
    ): Boolean{
        return try {
            val credential =
                PhoneAuthProvider.getCredential(verificationId,otp)
            auth.signInWithCredential(credential).await()
            true
        }catch(e:Exception){
            false
        }
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}