package com.example.myapplication.ui.transaction

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.room.TransactionDB
import com.example.myapplication.room.TransactionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TransactionViewModel(private val context: Context) : ViewModel() {
    private val transactionDB by lazy { TransactionDB(context) }

    private val _listTransaction: MutableLiveData<List<TransactionEntity>> = MutableLiveData()
    private val _transaction: MutableLiveData<TransactionEntity> = MutableLiveData()

    val listTransaction: LiveData<List<TransactionEntity>> = _listTransaction
    val transaction: LiveData<TransactionEntity> = _transaction

    fun getAllData(){
        viewModelScope.launch {
            val response = transactionDB.transactionDao().getAllTransactions()
            _listTransaction.apply { value = response }
        }
    }

    fun insertTransaction(transaction: TransactionEntity){
        viewModelScope.launch {
            transactionDB.transactionDao().addTransaction(transaction)
        }
    }

    fun getTransaction(id: Int){
        viewModelScope.launch {
            val response = transactionDB.transactionDao().getTransaction(id)
            _transaction.apply { value = response[0] }
        }
    }

    fun deleteTransaction(transaction: TransactionEntity){
        viewModelScope.launch {
            transactionDB.transactionDao().deleteTransaction(transaction)
        }
    }

    fun updateTransaction(transaction: TransactionEntity){
        viewModelScope.launch{
            transactionDB.transactionDao().updateTransaction(transaction)
        }
    }
}