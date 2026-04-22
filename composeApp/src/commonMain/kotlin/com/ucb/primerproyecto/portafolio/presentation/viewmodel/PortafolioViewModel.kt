package com.ucb.primerproyecto.portafolio.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.primerproyecto.firebase.getToken
import com.ucb.primerproyecto.portafolio.domain.repository.RemoteConfigRepository
import com.ucb.primerproyecto.portafolio.domain.usecase.GetPortafolioUseCase
import com.ucb.primerproyecto.portafolio.presentation.state.PortafolioEffect
import com.ucb.primerproyecto.portafolio.presentation.state.PortafolioEvent
import com.ucb.primerproyecto.portafolio.presentation.state.PortafolioUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class PortafolioViewModel(
    private val getPortfolioUseCase: GetPortafolioUseCase,
    private val remoteConfigRepository: RemoteConfigRepository
) : ViewModel() {

    var state by mutableStateOf(PortafolioUiState())
        private set

    // 🔥 EFFECTS (eventos de una sola vez)
    private val _effect = MutableSharedFlow<PortafolioEffect>()
    val effect = _effect.asSharedFlow()

    init {
        loadRemoteConfig()
        fetchFcmToken() // Se llama al iniciar el ViewModel
    }

    // 🚀 1. PRIMERO: cargar Remote Config
    private fun loadRemoteConfig() {

        state = state.copy(isLoading = true)

        remoteConfigRepository.fetchConfig { success ->

            println("🔥 Remote Config loaded: $success")

            val isMaintenance = remoteConfigRepository.isMaintenanceMode()
            val depositsEnabled = remoteConfigRepository.isDepositEnabled()

            println("🚧 Maintenance: $isMaintenance")
            println("💳 Deposits: $depositsEnabled")

            state = state.copy(
                isMaintenance = isMaintenance,
                depositEnabled = depositsEnabled
            )

            // 🚧 2. SI ESTÁ EN MANTENIMIENTO → BLOQUEAR TODO
            if (isMaintenance) {
                state = state.copy(
                    deposits = emptyList(),
                    totalBalance = 0.0,
                    isLoading = false
                )
                return@fetchConfig
            }

            // 💳 3. SI TODO OK → CARGAR PORTAFOLIO
            loadPortfolio(depositsEnabled)
        }
    }

    // 📊 CARGAR DATOS DESDE FIREBASE
    private fun loadPortfolio(depositsEnabled: Boolean) {
        // 1. validar config primero

        if (!depositsEnabled) {
            state = state.copy(
                deposits = emptyList(),
                isLoading = false
            )
            return
        }
        viewModelScope.launch {

            getPortfolioUseCase().collect { list ->

                val total = list.sumOf { it.amount }

                state = state.copy(
                    deposits = list,
                    totalBalance = total,
                    isLoading = false
                )
            }
        }
    }

    // 🎯 MANEJO DE EVENTOS
    fun onEvent(event: PortafolioEvent) {
        when (event) {

            PortafolioEvent.OnAddClick -> {
                navigateToDeposit()
            }
        }
    }

    // 🚀 NAVEGACIÓN
    private fun navigateToDeposit() {
        viewModelScope.launch {
            _effect.emit(PortafolioEffect.NavigateToDeposit)
        }
    }

    private fun fetchFcmToken() {
        viewModelScope.launch {
            try {
                // Llama a la función expect/actual
                val token = getToken()
                state = state.copy(fcmToken = token)
                println("FCM Token obtenido exitosamente: $token")
            } catch (e: Exception) {
                println("Error al obtener el token de FCM: ${e.message}")
            }
        }
    }

}


//bloquear la aplicacion , remote config
//bloquear en los sistemas a los clientes qq haccen operaciones cuando no se debe ,
//el remote config esta diseñado para eso, no solo sirve para android ,  bloquear solo targeta de credito, , o solo puede ser para android
//leer de la base de datos