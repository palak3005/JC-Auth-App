package com.palak.jcauth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.palak.jcauth.presentation.navigation.AppNavigation
import com.palak.jcauth.ui.theme.JCAuthTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JCAuthTheme {

                    AppNavigation()

            }
        }
    }
}

