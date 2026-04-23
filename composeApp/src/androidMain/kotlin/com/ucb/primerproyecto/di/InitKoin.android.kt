package com.ucb.primerproyecto.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.ucb.primerproyecto.core.data.db.AppDatabase
import com.ucb.primerproyecto.core.data.notification.AndroidLocalNotificationManager
import com.ucb.primerproyecto.core.data.notification.LocalNotificationManager
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
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
            .fallbackToDestructiveMigration() // <--- ESTO SOLUCIONA EL ERROR
            .build()
    }

    single { get<AppDatabase>().getDao() }
    single { get<AppDatabase>().getAppConfigDao() }
    single<LocalNotificationManager> { AndroidLocalNotificationManager(androidContext()) }
}
