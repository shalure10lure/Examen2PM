package com.ucb.primerproyecto.core.data.repository

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.ucb.primerproyecto.core.data.dao.AppEventDao
import com.ucb.primerproyecto.core.data.entity.AppEventEntity
import kotlinx.coroutines.tasks.await

class AppEventRepositoryImpl(
    private val appEventDao: AppEventDao
) : AppEventRepository {

    private val db = FirebaseDatabase.getInstance().reference

    override suspend fun registerEvent(type: String) {
        val timestamp = System.currentTimeMillis()
        val event = AppEventEntity(timestamp = timestamp, type = type)

        appEventDao.insertEvent(event)
        Log.d("EVENT_TRACKER", " Evento [$type] guardado en Room")

        try {
            val firebaseEvent = mapOf(
                "timestamp" to timestamp,
                "type" to type,
                "device" to "Android"
            )
            db.child("app_events").push().setValue(firebaseEvent).await()
            Log.d("EVENT_TRACKER", "️ Evento [$type] sincronizado con Firebase")
        } catch (e: Exception) {
            Log.e("EVENT_TRACKER", " Sin conexión: Evento [$type] guardado solo localmente")
        }
    }
}
