package com.ucb.primerproyecto.core.data.repository

interface AppEventRepository {
    suspend fun registerEvent(type: String)
}
