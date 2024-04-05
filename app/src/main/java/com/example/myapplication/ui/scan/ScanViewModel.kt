package com.example.myapplication.ui.scan

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.room.TransactionDB
import com.example.myapplication.room.TransactionEntity
import kotlinx.coroutines.launch


class ScanViewModel(context: Context) : ViewModel() {

    private val transactionDB by lazy { TransactionDB(context) }
    private val _scannedTransactionList: MutableLiveData<List<TransactionEntity>> = MutableLiveData()
    val scannedTransactionList: LiveData<List<TransactionEntity>> = _scannedTransactionList

    fun scanTransaction(transaction: TransactionEntity){
        viewModelScope.launch {
            val currentList = _scannedTransactionList.value.orEmpty().toMutableList()
            currentList.add(transaction)
            _scannedTransactionList.apply { value = currentList }
        }
    }

    fun submitTransactions(){
        viewModelScope.launch {
            _scannedTransactionList.value?.forEach { transaction ->
                transactionDB.transactionDao().addTransaction(transaction)
            }
        }
    }
}