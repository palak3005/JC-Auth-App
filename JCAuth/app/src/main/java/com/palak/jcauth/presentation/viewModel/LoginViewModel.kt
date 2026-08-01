package com.palak.jcauth.presentation.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.palak.jcauth.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val email :String ="",
    val password :String ="",
    val isLoading :Boolean = false,//login chal ra h ya mhai
    val loginSuccess : Boolean = false,//successful hua ya nhi
    val errorMessage :String? = null,
    val loading : Boolean= false,
    val passwordVisible : Boolean = false
)

class LoginViewModel: ViewModel() {

    private  val repository = AuthRepository()
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun updateEmail(email: String){
        _uiState.update {
            it.copy(email = email )
        }
    }

    fun updatePassword(password: String){
        _uiState.update {
            it.copy(password=password)
        }
    }

    fun togglePasswordVisibility(){
        _uiState.update {
            it.copy(passwordVisible = !it.passwordVisible)
        }
    }

    fun login(){
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true ,
                    errorMessage = null)

            }
          val success = repository.login(
              email = uiState.value.email,
              password = uiState.value.password
          )
            _uiState.update {
                it.copy(
                    isLoading = false,
                    loginSuccess = success,
                    errorMessage = if(success) null else "Invalid email or password"
                )
            }

        }
    }

    fun googleSignIn(context: Context,webClientId:String){
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    loading = true,
                    errorMessage = null

                )
            }
         val success = repository.signInWithGoogle(context = context, webClientId = webClientId)

            _uiState.update {
                it.copy(
                    loading = false,
                    loginSuccess = success,
                    errorMessage = if (success) null else "Google Sign In Failed"
                )
            }
        }


    }
}