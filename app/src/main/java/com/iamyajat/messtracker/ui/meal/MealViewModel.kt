package com.iamyajat.messtracker.ui.meal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iamyajat.messtracker.model.Meal
import com.iamyajat.messtracker.repository.MealRepository
import com.iamyajat.messtracker.util.Constants
import com.iamyajat.messtracker.util.DateFunctions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MealViewModel @Inject constructor(private val repository: MealRepository) : ViewModel() {

    private val _amount = MutableLiveData<Long>().apply {
        value = 0
    }
    val amount: LiveData<Long> = _amount

    fun changeAmount(amt: Long) {
        _amount.postValue(amt)
    }

    fun addValue(mealName: String) {
        viewModelScope.launch {
            repository.insertMeal(
                Meal(
                    mealName = mealName,
                    description = "",
                    addedOn = Date(),
                    amount = amount.value!!,
                    rating = 0
                )
            )

        }
    }


    private val _todayExp = MutableLiveData<Long>().apply {
        value = 0L
    }
    val todayExp: LiveData<Long> = _todayExp

    private fun todayExpValue() {
        viewModelScope.launch {
            var exp: Long? = repository.getPeriodExpense(
                DateFunctions.getTodayStart(),
                DateFunctions.getTodayEnd()
            )
            if (exp == null) {
                exp = 0
            }
            _todayExp.postValue(exp)
            todayBalValue()
        }
    }

    private val _todayBal = MutableLiveData<Long>().apply {
        value = 0L
    }
    val todayBal: LiveData<Long> = _todayBal

    private fun todayBalValue() {
        viewModelScope.launch {
            var exp: Long? = repository.getPeriodExpense(
                DateFunctions.getMonthStart(),
                DateFunctions.getTodayStart()
            )
            if (exp == null) {
                exp = 0
            }
            val availBal = Constants.MAX_CREDITS - exp
            var perDay = availBal / DateFunctions.getRemainingDays()
            perDay -= if (todayExp.value == null) 0 else todayExp.value!!
            _todayBal.postValue(perDay)
        }
    }

    private val _monthExp = MutableLiveData<Long>().apply {
        value = 0L
    }
    val monthExp: LiveData<Long> = _monthExp

    private fun monthExpValue() {
        viewModelScope.launch {
            var exp: Long? = repository.getPeriodExpense(
                DateFunctions.getMonthStart(),
                DateFunctions.getMonthEnd()
            )
            if (exp == null) {
                exp = 0
            }
            _monthExp.postValue(exp)
        }
    }


    fun initVM() {
        todayExpValue()
        monthExpValue()
        todayBalValue()
    }

    init {
        initVM()
    }

}