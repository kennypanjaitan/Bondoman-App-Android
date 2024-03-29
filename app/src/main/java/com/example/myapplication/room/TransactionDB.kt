package com.example.myapplication.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TransactionEntity::class], version = 1)
abstract class TransactionDB : RoomDatabase() {
    abstract fun transactionDao() : TransactionDAO

    companion object {
        @Volatile
        private var instance: TransactionDB? = null

        fun getInstance(context: Context): TransactionDB {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context)
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            TransactionDB::class.java,
            "transaction.db"
        )
            .fallbackToDestructiveMigration()
            .build()
            .also { instance = it }
    }
}