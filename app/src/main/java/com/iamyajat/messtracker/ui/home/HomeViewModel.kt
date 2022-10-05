package com.iamyajat.messtracker.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iamyajat.messtracker.model.Meal
import com.iamyajat.messtracker.repository.MealRepository
import com.iamyajat.messtracker.util.Constants.MAX_CREDITS
import com.iamyajat.messtracker.util.DateFunctions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: MealRepository) : ViewModel() {

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
            val availBal = MAX_CREDITS - exp
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

    private val _meals = MutableLiveData<List<Meal>>().apply {
        value = ArrayList()
    }
    val meals: LiveData<List<Meal>> = _meals

    private fun getMeals() {
        viewModelScope.launch {
            _meals.postValue(
                repository.getPeriodMeals(
                    DateFunctions.getTodayStart(),
                    DateFunctions.getTodayEnd()
                )
            )
        }
    }

    fun deleteMeal(id: Long) {
        viewModelScope.launch {
            repository.deleteMeal(id)
            initVM()
        }
    }


    fun addValue(mealName: String, amount: Long, date: Date) {
        viewModelScope.launch {
            repository.insertMeal(
                Meal(
                    mealName = mealName,
                    description = "",
                    addedOn = date,
                    amount = amount,
                    rating = 0
                )
            )

        }
    }

    fun initVM() {
        getMeals()
        todayExpValue()
        monthExpValue()
        todayBalValue()
    }

    init {
        initVM()
    }

}