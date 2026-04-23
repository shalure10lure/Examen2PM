package com.ucb.primerproyecto.core.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ucb.primerproyecto.core.data.entity.AppEventEntity

@Dao
interface AppEventDao {
    @Insert
    suspend fun insertEvent(event: AppEventEntity)

    @Query("SELECT * FROM app_events ORDER BY timestamp DESC")
    suspend fun getAllEvents(): List<AppEventEntity>
}
