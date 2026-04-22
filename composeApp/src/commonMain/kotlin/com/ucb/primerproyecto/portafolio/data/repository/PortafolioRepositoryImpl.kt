package com.ucb.primerproyecto.portafolio.data.repository

import com.ucb.primerproyecto.deposit.domain.model.DepositModel
import com.ucb.primerproyecto.portafolio.data.datasource.FirebaseManager
import com.ucb.primerproyecto.portafolio.domain.repository.PortafolioRepository
import kotlinx.coroutines.flow.Flow

class PortafolioRepositoryImpl(
    private val firebase: FirebaseManager
) : PortafolioRepository {

    override fun getDeposits(): Flow<List<DepositModel>> {
        return firebase.observeDeposits()
    }
}