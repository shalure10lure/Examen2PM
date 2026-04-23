package com.ucb.primerproyecto.di

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.ucb.primerproyecto.core.data.db.AppDatabase
import com.ucb.primerproyecto.core.data.notification.AndroidLocalNotificationManager
import com.ucb.primerproyecto.core.data.notification.LocalNotificationManager
import com.ucb.primerproyecto.core.data.repository.AppEventRepository
import com.ucb.primerproyecto.core.data.repository.AppEventRepositoryImpl
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule = module {
    single<AppDatabase> {
        val context = androidContext() 
        val dbFile = context.getDatabasePath("dollar_db.db")
        
        Room.databaseBuilder<AppDatabase>(
            context = context,
            name = dbFile.absolutePath
        )
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .fallbackToDestructiveMigration()
            .build()
    }

    // Registro de DAOs
    single { get<AppDatabase>().getDao() }
    single { get<AppDatabase>().getAppEventDao() }
    single { get<AppDatabase>().getAppConfigDao() } // <--- Agregado para que funcione el RemoteConfigRepository

    // Servicios de plataforma
    single<LocalNotificationManager> { AndroidLocalNotificationManager(get()) }

    // Registro del repositorio de eventos (Firebase + Room)
    single { AppEventRepositoryImpl(get()) }.bind<AppEventRepository>()
}
