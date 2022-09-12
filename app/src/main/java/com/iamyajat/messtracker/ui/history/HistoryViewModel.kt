package com.iamyajat.messtracker.ui.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iamyajat.messtracker.model.Meal
import com.iamyajat.messtracker.repository.MealRepository
import com.iamyajat.messtracker.util.DateFunctions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val repository: MealRepository) : ViewModel() {

    private val _meals = MutableLiveData<List<Meal>>().apply {
        value = ArrayList()
    }
    val meals: LiveData<List<Meal>> = _meals

    fun getMeals() {
        viewModelScope.launch {
            val mealsList = repository.getAllMeals()
            Log.d("MEALS", mealsList.toString())
            _meals.postValue(mealsList)
        }
    }

    private val _monthExp = MutableLiveData<Long>().apply {
        value = 0L
    }
    val monthExp: LiveData<Long> = _monthExp

    fun monthExpValue() {
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


    init {
        getMeals()
        monthExpValue()
    }
}