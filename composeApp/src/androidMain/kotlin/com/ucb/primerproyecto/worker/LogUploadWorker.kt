package com.ucb.primerproyecto.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ucb.primerproyecto.portafolio.domain.repository.RemoteConfigRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.resume

class LogUploadWorker(
    appContext: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(appContext, workerParameters), KoinComponent {

    private val remoteConfigRepository: RemoteConfigRepository by inject()

    override suspend fun doWork(): Result {
        Log.d("WORKER", "Iniciando comprobación de Remote Config...")

        return try {
            // Usamos suspendCancellableCoroutine para esperar a que la descarga termine
            val isSuccess = suspendCancellableCoroutine<Boolean> { continuation ->
                remoteConfigRepository.fetchConfig { success ->
                    if (!continuation.isCompleted) {
                        continuation.resume(success)
                    }
                }
            }

            if (isSuccess) {
                Log.d("WORKER", "Sincronización completada")
                Result.success()
            } else {
                Log.e("WORKER", "Error al descargar de Firebase")
                Result.retry()
            }
        } catch (e: Exception) {
            Log.e("WORKER", "Error crítico: ${e.message}")
            Result.failure()
        }
    }
}
