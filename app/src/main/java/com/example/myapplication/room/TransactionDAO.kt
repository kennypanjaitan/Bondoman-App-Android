package com.example.myapplication.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TransactionDAO {
    @Query("SELECT * FROM `transaction`")
    suspend fun getAllTransactions(): List<TransactionEntity>

    @Query("SELECT * FROM `transaction` WHERE id = :transactionID")
    suspend fun getTransaction(transactionID: Int): List<TransactionEntity>

    @Insert
    suspend fun addTransaction(transaction: TransactionEntity)

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)

}