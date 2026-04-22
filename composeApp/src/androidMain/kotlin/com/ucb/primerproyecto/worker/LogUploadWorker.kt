package com.ucb.primerproyecto.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ucb.primerproyecto.portafolio.domain.usecase.GetPortafolioUseCase
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class LogUploadWorker(
    appContext: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(appContext, workerParameters), KoinComponent {

    private val getPortafolioUseCase: GetPortafolioUseCase by inject()

    override suspend fun doWork(): Result {

        println("🚀 Ejecutando trabajo en segundo plano")

        return try {
            //  aquí iría tu lógica real:
            // - enviar logs
            // - sincronizar Firebase
            // - actualizar datos

            val lista = getPortafolioUseCase().first()

            println($$"📦 depositos recibidos: $${lista.size}")

            println("✅ Trabajo completado")

            Result.success()

        } catch (e: Exception) {
            println("❌ Error en worker: ${e.message}")
            Result.retry()
        }
    }
}