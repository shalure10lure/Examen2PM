package com.ucb.primerproyecto.portafolio.data.datasource

import com.ucb.primerproyecto.deposit.domain.model.DepositModel
import kotlinx.coroutines.flow.Flow

expect class FirebaseManager() {

    fun observeDeposits(): Flow<List<DepositModel>>
}