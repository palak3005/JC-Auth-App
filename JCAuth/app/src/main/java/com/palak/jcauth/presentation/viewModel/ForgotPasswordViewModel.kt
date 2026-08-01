package com.palak.jcauth.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.palak.jcauth.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ForgotPasswordUiState(
    val email: String = "",
    val loading: Boolean = false,
    val linkSendSuccess: Boolean = false,
    val errorMessage: String? = null
)

class ForgotPasswordViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState = _uiState.asStateFlow()


    fun updateEmail(email: String){
        _uiState.update {
            it.copy(email = email)
        }
    }

    fun forgotPassword() {


            if(uiState.value.email.isBlank()){
                _uiState.update {
                    it.copy(errorMessage = "Please enter your email")
                }
                return
            }
        viewModelScope.launch {

            _uiState.update {
                it.copy(loading = true,
                    errorMessage = null)
            }
            val success = repository.sendPasswordReset(uiState.value.email)

            _uiState.update {

                it.copy(
                    loading = false,
                    linkSendSuccess = success,
                    errorMessage = if (success) null else "Failed to send password reset email"
                )
            }
        }
    }


}