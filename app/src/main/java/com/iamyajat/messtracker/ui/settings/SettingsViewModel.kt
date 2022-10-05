package com.iamyajat.messtracker.ui.settings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iamyajat.messtracker.model.Day
import com.iamyajat.messtracker.repository.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val repository: MealRepository) : ViewModel() {


    fun addValue(day: Day) {
        viewModelScope.launch {
            repository.insertDay(day)
        }
    }

    fun updateValue(day: Day) {
        viewModelScope.launch {
            Log.d("UPDATE_DAY", day.toString())
            repository.updateDay(day)
            Log.d("UPDATE_DAY", day.toString())
        }
    }


    private val _days = MutableLiveData<List<Day>>().apply {
        value = ArrayList()
    }
    val days: LiveData<List<Day>> = _days

    fun getDayInfo(id: Long) {
        viewModelScope.launch {
            return repository.getDayInfo(id)
        }
    }

    fun initVM() {
    }

    init {
        initVM()
    }

}