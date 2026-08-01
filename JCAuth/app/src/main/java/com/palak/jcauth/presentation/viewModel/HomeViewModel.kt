package com.palak.jcauth.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.palak.jcauth.data.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val loading: Boolean =false,
    val logoutSuccess : Boolean = false,
    val errorMessage: String? = null
)
class HomeViewModel : ViewModel(){

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState =_uiState.asStateFlow()
    private val repository = AuthRepository()
    fun logout(){

        viewModelScope.launch {
            _uiState.update {
                it.copy(loading = true)
            }

             val success = repository.logout()
            delay(2000)

            _uiState.update {
                it.copy(loading = false,
                    logoutSuccess = success,
                    errorMessage = if(success) null else "Logout failed try again"
                )
            }

        }
    }
}