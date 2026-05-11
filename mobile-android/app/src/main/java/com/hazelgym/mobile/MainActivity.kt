package com.hazelgym.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.hazelgym.mobile.ui.HazelGymMobileApp
import com.hazelgym.mobile.ui.theme.HazelGymTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HazelGymTheme {
                HazelGymMobileApp()
            }
        }
    }
}
