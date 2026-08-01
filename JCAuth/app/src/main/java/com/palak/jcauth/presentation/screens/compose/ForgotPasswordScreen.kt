package com.palak.jcauth.presentation.screens.compose

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
import androidx.compose.material.icons.outlined.Email
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
import com.palak.jcauth.presentation.screens.components.SignUpButton
import com.palak.jcauth.presentation.screens.components.SignUpTextField
import com.palak.jcauth.presentation.viewModel.ForgotPasswordViewModel

@Composable
fun ForgotPasswordScreen(navController: NavHostController) {
    val viewModel : ForgotPasswordViewModel = viewModel()
    val state by viewModel.uiState.collectAsState()
val context = LocalContext.current
    LaunchedEffect(state.linkSendSuccess) {
        if (state.linkSendSuccess){
            Toast.makeText(context,"Password reset link sent", Toast.LENGTH_SHORT).show()
        }
    }
    Scaffold() { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize().padding(paddingValues = innerPadding)
                .background(color = MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {

                Text(
                    "Reset Password",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "Enter your registered Email ID to receive a password reset link in your Email.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                SignUpTextField(
                    value = state.email,
                    onValueChange = viewModel::updateEmail,
                    placeholder = "enter email address",
                    icon = Icons.Outlined.Email,
                    label = "Email Address",
                    showTrailingIcon = false,
                    onPasswordVisibilityChange = {},
                    keyboardOption = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )
                )
                if (state.loading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(16.dp)
                            .size(42.dp)
                            .align(Alignment.CenterHorizontally),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        strokeWidth = 1.dp,
                    )
                } else {
                    SignUpButton(
                        text = "Send Link",
                        icon = Icons.Outlined.Send,
                        onClick = viewModel::forgotPassword
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