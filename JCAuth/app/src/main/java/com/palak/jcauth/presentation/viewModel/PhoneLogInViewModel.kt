package com.palak.jcauth.presentation.viewModel

import android.app.Activity
import android.provider.ContactsContract
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.palak.jcauth.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PhoneLoginUiState(
    val phoneNumber: String = "",
    val verificationId: String = "",
    val loading: Boolean = false,
    val loginSuccess: Boolean = false,
    val errorMessage: String? = null,
    //otp screen
    val otp: String = "",
    val resendLoading : Boolean = false,
    val otpSent: Boolean = false
)

class PhoneLogInViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PhoneLoginUiState())
    val uiState = _uiState.asStateFlow()

   private val repository = AuthRepository()
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    fun updatePhoneNumber(phone: String) {
        _uiState.update {
            it.copy(phoneNumber = phone)
        }
    }

    fun sendOTP(activity: Activity,isResend :Boolean= false) {
        val phone = "+91${_uiState.value.phoneNumber}"
        _uiState.update {
            it.copy(
                loading = !isResend,
                resendLoading = isResend,
                errorMessage = null,
                otpSent = false

            )
        }
        repository.sendOTP(
            activity = activity, phoneNumber = phone,
            resendToken = if (isResend) resendToken else null,

            onCodeSent = { verificationId,token ->
                resendToken  = token

                _uiState.update {
                    it.copy(
                        verificationId = verificationId,
                        loading = false,
                        resendLoading = false,
                        otpSent = true
                    )
                }
            },
            onVerificationComplete = { credential ->
                viewModelScope.launch {
                    val success = repository.signInWithCredential(credential)

                        _uiState.update {
                            it.copy(
                                loginSuccess = success,
                                loading = false,
                                resendLoading = false,
                                errorMessage = if (success) null else "Login Failed"
                            )
                        }



                }

            },
            onFailure = { message ->
                _uiState.update {
                    it.copy(
                        loading = false,
                        resendLoading = false,
                        errorMessage = message
                    )
                }

            }
        )
    }

    fun clearVerification() {
        _uiState.update {
            it.copy(
                verificationId = "",
                otp = "",
                loginSuccess = false,
                errorMessage = null,
                otpSent = false,
                loading = false,
                resendLoading = false
            )
        }
    }

    //otp screen

    fun updateOTP(otp: String) {
        _uiState.update {
            it.copy(
                otp = otp
            )
        }
    }

    fun verifyOTP() {
        if (_uiState.value.verificationId.isBlank()) {
            _uiState.update {
                it.copy(errorMessage = "Please request OTP first")
            }
            return
        }
        if (_uiState.value.otp.isBlank()) {
            _uiState.update {
                it.copy(errorMessage = "Please enter OTP")
            }
            return
        }
        if (_uiState.value.otp.length != 6) {
            _uiState.update {
                it.copy(errorMessage = "OTP must be of 6 digits")
            }
            return
        }


        _uiState.update {
            it.copy(
                loading = true,
                errorMessage = null
            )
        }
        viewModelScope.launch {
            val success = repository.verifyOTP(
                verificationId = _uiState.value.verificationId,
                otp = _uiState.value.otp
            )
            _uiState.update {
                it.copy(
                    loading = false,
                    loginSuccess = success,
                    errorMessage = if (success) null else "Invalid OTP"
                )
            }

        }
    }
}

