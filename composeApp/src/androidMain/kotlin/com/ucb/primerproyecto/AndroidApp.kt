package com.ucb.primerproyecto

import android.app.Application
import com.ucb.primerproyecto.di.getModules
import com.ucb.primerproyecto.worker.LogScheduler
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

class AndroidApp: Application() {

    override fun onCreate() {
        super.onCreate()

        // 1. Iniciamos Koin primero para que todas las dependencias estén disponibles
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@AndroidApp)
            modules(getModules())
        }

        // 2. Programamos el trabajo en segundo plano después de iniciar Koin
        LogScheduler(this).schedulePeriodicaUpload()
    }
}
