package com.mahesa0004.smartdiscount

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mahesa0004.smartdiscount.ui.screen.MainScreen
import com.mahesa0004.smartdiscount.ui.theme.SmartDiscountTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartDiscountTheme {
               MainScreen()
            }
        }
    }
}

