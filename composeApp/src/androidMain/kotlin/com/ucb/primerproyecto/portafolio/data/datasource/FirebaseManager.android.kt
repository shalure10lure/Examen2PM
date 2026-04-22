package com.ucb.primerproyecto.portafolio.data.datasource

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ucb.primerproyecto.deposit.domain.model.DepositModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

actual class FirebaseManager actual constructor() {

    private val db = FirebaseDatabase.getInstance().reference

    actual fun observeDeposits(): Flow<List<DepositModel>> = callbackFlow {

        val listener = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val list = snapshot.children.mapNotNull { child ->

                    val amount = child.child("amount")
                        .getValue(Double::class.java) ?: 0.0

                    val currency = child.child("currency")
                        .getValue(String::class.java) ?: ""

                    val timestamp = child.child("timestamp")
                        .getValue(Long::class.java) ?: 0L

                    DepositModel(
                        amount = amount,
                        currency = currency,
                        timestamp = timestamp
                    )
                }

                trySend(list)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        db.child("deposits").addValueEventListener(listener)

        awaitClose {
            db.child("deposits").removeEventListener(listener)
        }
    }
}