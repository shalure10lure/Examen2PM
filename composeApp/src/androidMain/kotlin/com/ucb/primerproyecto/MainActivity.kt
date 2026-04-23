package com.ucb.primerproyecto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.ucb.primerproyecto.core.data.repository.AppEventRepository
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val appEventRepository: AppEventRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                100
            )
        }
        setContent {
            App()
        }
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            appEventRepository.registerEvent("OPEN")
        }
    }

    override fun onStop() {
        super.onStop()
        lifecycleScope.launch {
            appEventRepository.registerEvent("CLOSE")
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
