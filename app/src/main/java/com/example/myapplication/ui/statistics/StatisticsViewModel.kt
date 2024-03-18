package com.example.myapplication.ui.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StatisticsViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    private val _valueIncome = MutableLiveData<Int>(0)
    private val _valueExpense = MutableLiveData<Int>(0)
    val valueIncome : LiveData<Int>
        get() = _valueIncome
    val valueExpense : LiveData<Int>
        get() = _valueExpense

    fun updateValue(isIncome: Boolean) {
        if (isIncome) {
            _valueIncome.value = (_valueIncome.value)?.plus(1)
        } else {
            _valueExpense.value = (_valueExpense.value)?.plus(1)
        }
    }

}