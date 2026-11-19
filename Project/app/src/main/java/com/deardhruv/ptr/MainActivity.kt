package com.deardhruv.ptr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.deardhruv.ptr.navigation.AppNavigation
import com.deardhruv.ptr.ui.theme.PTRTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            PTRTheme {
                AppNavigation()
            }
        }
    }
}
