package com.ucb.primerproyecto.portafolio.data.repository

import com.ucb.primerproyecto.core.data.dao.AppConfigDao
import com.ucb.primerproyecto.core.data.entity.AppConfigEntity
import com.ucb.primerproyecto.core.data.notification.LocalNotificationManager
import com.ucb.primerproyecto.portafolio.data.datasource.remoteconfig.RemoteConfigManager
import com.ucb.primerproyecto.portafolio.domain.repository.RemoteConfigRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class RemoteConfigRepositoryImpl(
    private val remoteConfig: RemoteConfigManager,
    private val appConfigDao: AppConfigDao,
    private val notificationManager: LocalNotificationManager
) : RemoteConfigRepository {

    override fun fetchConfig(onResult: (Boolean) -> Unit) {
        remoteConfig.fetchConfig { success ->
            if (success) {
                CoroutineScope(Dispatchers.IO).launch {
                    checkAndNotifyChange("maintenance_mode", remoteConfig.isMaintenanceMode().toString(), "El modo mantenimiento ha cambiado")
                    checkAndNotifyChange("deposit_enabled", remoteConfig.isDepositEnabled().toString(), "El estado de los depósitos ha cambiado")
                    checkAndNotifyChange("min_version", remoteConfig.getMinVersion(), "Hay una nueva versión mínima requerida: ${remoteConfig.getMinVersion()}")
                }
            }
            onResult(success)
        }
    }

    private suspend fun checkAndNotifyChange(key: String, newValue: String, message: String) {
        val oldValue = appConfigDao.getConfig(key)?.value
        if (oldValue != null && oldValue != newValue) {
            // Si el valor existía y es diferente, notificamos
            notificationManager.showNotification("Cambio en Configuración", message)
        }
        // Siempre actualizamos en Room
        appConfigDao.saveConfig(AppConfigEntity(key, newValue))
    }

    override fun isMaintenanceMode(): Boolean {
        return try {
            remoteConfig.isMaintenanceMode()
        } catch (e: Exception) {
            runBlocking {
                appConfigDao.getConfig("maintenance_mode")?.value?.toBoolean() ?: false
            }
        }
    }

    override fun isDepositEnabled(): Boolean {
        return try {
            remoteConfig.isDepositEnabled()
        } catch (e: Exception) {
            runBlocking {
                appConfigDao.getConfig("deposit_enabled")?.value?.toBoolean() ?: true
            }
        }
    }

    override fun getMinVersion(): String {
        return try {
            remoteConfig.getMinVersion()
        } catch (e: Exception) {
            runBlocking {
                appConfigDao.getConfig("min_version")?.value ?: "1.0.0"
            }
        }
    }
}
