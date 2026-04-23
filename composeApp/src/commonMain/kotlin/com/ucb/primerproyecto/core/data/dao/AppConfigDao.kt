package com.ucb.primerproyecto.core.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ucb.primerproyecto.core.data.entity.AppConfigEntity

@Dao
interface AppConfigDao {
    @Query("SELECT * FROM app_config WHERE `key` = :key")
    suspend fun getConfig(key: String): AppConfigEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveConfig(config: AppConfigEntity)
}
