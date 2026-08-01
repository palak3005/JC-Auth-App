package com.palak.jcauth.presentation.screens.compose

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.NavigateNext
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.palak.jcauth.presentation.navigation.Routes
import com.palak.jcauth.presentation.screens.components.SignUpButton
import com.palak.jcauth.presentation.screens.components.SignUpTextField
import com.palak.jcauth.presentation.viewModel.PhoneLogInViewModel

@Composable
fun PhoneLoginScreen(navController: NavHostController,viewModel: PhoneLogInViewModel) {
    val state by viewModel.uiState.collectAsState()

    val context = LocalContext.current
    val activity = context as Activity

    LaunchedEffect(state.verificationId) {
        if (state.verificationId.isNotEmpty()){
            Toast.makeText(context,"OTP sent successfully",
                Toast.LENGTH_SHORT).show()
            navController.navigate(Routes.OTP)
        }
    }
    Scaffold() { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize().padding(paddingValues = innerPadding)
                .background(color = MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    "Sign In with Phone Number", lineHeight = 40.sp,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "Enter your phone number to receive a verification code.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                SignUpTextField(
                    value = state.phoneNumber,
                    onValueChange = {if (it.length<=10 && it.all { char->
                        char.isDigit()
                        }){viewModel.updatePhoneNumber(phone = it)}},
                    placeholder = "enter phone number",
                    icon = Icons.Outlined.Phone,
                    label = "Phone Number",
                    showTrailingIcon = false,
                    onPasswordVisibilityChange = {},
                    keyboardOption = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Done
                    )
                )

                state.errorMessage?.let {
                    Text(
                        text = it,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
                    if(state.loading){
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(16.dp)
                                .size(42.dp)
                                .align(Alignment.CenterHorizontally),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            strokeWidth = 1.dp,
                        )
                    }else {

                        SignUpButton(
                            text = "Send OTP",
                            enabled = state.phoneNumber.isNotBlank() && state.phoneNumber.length==10,
                            icon = Icons.Outlined.ArrowForward,
                            onClick ={viewModel.sendOTP(activity = activity)}
                        )
                    }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Back to Log In",
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable {
                            navController.popBackStack()
                        }
                    )

                }

            }
        }
    }
}