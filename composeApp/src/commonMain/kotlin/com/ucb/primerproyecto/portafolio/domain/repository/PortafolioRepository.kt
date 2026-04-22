package com.ucb.primerproyecto.portafolio.domain.repository

import com.ucb.primerproyecto.deposit.domain.model.DepositModel
import kotlinx.coroutines.flow.Flow

interface PortafolioRepository {
    fun getDeposits(): Flow<List<DepositModel>>

}