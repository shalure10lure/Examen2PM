package com.ucb.primerproyecto.core.data.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.ucb.primerproyecto.core.data.dao.AppConfigDao
import com.ucb.primerproyecto.core.data.entity.AppConfigEntity
import com.ucb.primerproyecto.dollar.data.dao.DollarDao
import com.ucb.primerproyecto.dollar.data.entity.DollarEntity

@Database(entities = [DollarEntity::class, AppConfigEntity::class], version = 2)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): DollarDao
    abstract fun getAppConfigDao(): AppConfigDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

expect fun getDatabaseBuilder(ctx: Any? = null): RoomDatabase.Builder<AppDatabase>
