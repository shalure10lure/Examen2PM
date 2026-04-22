package com.ucb.primerproyecto.portafolio.domain.usecase

import com.ucb.primerproyecto.deposit.domain.model.DepositModel
import com.ucb.primerproyecto.portafolio.domain.repository.PortafolioRepository
import kotlinx.coroutines.flow.Flow

class GetPortafolioUseCase(
    private val repository: PortafolioRepository
) {
    operator fun invoke(): Flow<List<DepositModel>> {
        return repository.getDeposits()
    }
}