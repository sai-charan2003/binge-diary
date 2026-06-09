

package com.charan.bingediary

import com.charan.bingediary.Screen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.charan.bingediary.presentation.theme.BingeDiaryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BingeDiaryTheme {
                Screen()
            }
        }
    }
}