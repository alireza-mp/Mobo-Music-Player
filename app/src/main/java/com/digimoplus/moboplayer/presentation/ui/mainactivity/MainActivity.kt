package com.digimoplus.moboplayer.presentation.ui.mainactivity

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import com.digimoplus.moboplayer.presentation.theme.MusicApplicationTheme
import com.digimoplus.moboplayer.presentation.ui.home.HomePage
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

const val TAG = "AppD"

@ExperimentalMaterialApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setAppLanguage(this)
        setContent {
            MusicApplicationTheme {
                HomePage()
            }
        }
    }
}

private fun setAppLanguage(activity: Activity) {
    val locale = Locale("en")
    Locale.setDefault(locale)
    val config: Configuration = activity.resources.configuration
    config.setLocale(locale)
    activity.resources.updateConfiguration(config, activity.resources.displayMetrics)
}


