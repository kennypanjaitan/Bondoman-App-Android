package com.example.myapplication.ui.statistics

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.models.CategoryEnum
import com.example.myapplication.room.TransactionDB
import com.example.myapplication.room.TransactionEntity
import kotlinx.coroutines.launch

class StatisticsViewModel(private val context: Context) : ViewModel() {
    private val transactionDB by lazy { TransactionDB(context) }

    private val _valueIncome = MutableLiveData<Int>()
    private val _valueExpense = MutableLiveData<Int>()

    val valueIncome: LiveData<Int> = _valueIncome
    val valueExpense: LiveData<Int> = _valueExpense

    fun incomeTransaction(){
        Log.d("edf", "apakah disnikah")
        viewModelScope.launch{
            Log.d("edf", "apakah disni")
            val response = transactionDB.transactionDao().getTypedTransaction(CategoryEnum.INCOME)
            var sum : Int = 0
            response.forEach{ transaction ->
                sum += transaction.nominal
            }
            Log.d("edf", "incomeResponse : " + sum)
            _valueIncome.apply {value = sum}
        }
    }

    fun expenseTransaction(){
        viewModelScope.launch{

            val response = transactionDB.transactionDao().getTypedTransaction(CategoryEnum.EXPENSE)
            var sum : Int = 0
            response.forEach{ transaction ->
                sum = transaction.nominal
            }
            Log.d("edf", "expenseResponse : " + sum)
            _valueExpense.apply { value = sum}
        }
    }


}