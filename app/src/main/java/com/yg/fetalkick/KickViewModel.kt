package com.yg.fetalkick

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlinx.coroutines.isActive

class KickViewModel(application: Application) : AndroidViewModel(application) {

    // Selected Date
    private val _selectedDate = MutableLiveData<LocalDate>(LocalDate.now())
    val selectedDate: LiveData<LocalDate> = _selectedDate

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
        // TODO: Here you would also load the kick counts for the newly selected date
    }

    // --- Breakfast ---
    private val _breakfastCount = MutableStateFlow(0)
    val breakfastCount: StateFlow<Int> = _breakfastCount.asStateFlow()
    private val _breakfastTimerLD = MutableLiveData(0) // Seconds
    val breakfastTimerLD: LiveData<Int> = _breakfastTimerLD
    private val _breakfastRunning = MutableStateFlow(false)
    val breakfastRunning: StateFlow<Boolean> = _breakfastRunning.asStateFlow()
    private var breakfastJob: Job? = null

    fun incrementBreakfast() { _breakfastCount.value++ }
    fun decrementBreakfast() { if (_breakfastCount.value > 0) _breakfastCount.value-- }
    fun setBreakfastCount(count: Int) { if (count >= 0) _breakfastCount.value = count }
    fun toggleBreakfastTimer() {
        if (_breakfastRunning.value) {
            _breakfastRunning.value = false
            breakfastJob?.cancel()
        } else {
            _breakfastTimerLD.value = 0 // Reset timer
            _breakfastRunning.value = true
            breakfastJob = viewModelScope.launch {
                while (isActive && _breakfastRunning.value) { // Use isActive from coroutine scope
                    delay(1000)
                    _breakfastTimerLD.postValue((_breakfastTimerLD.value ?: 0) + 1)
                }
            }
        }
    }

    // --- Lunch (Similar structure) ---
    private val _lunchCount = MutableStateFlow(0)
    val lunchCount: StateFlow<Int> = _lunchCount.asStateFlow()
    private val _lunchTimerLD = MutableLiveData(0)
    val lunchTimerLD: LiveData<Int> = _lunchTimerLD
    private val _lunchRunning = MutableStateFlow(false)
    val lunchRunning: StateFlow<Boolean> = _lunchRunning.asStateFlow()
    private var lunchJob: Job? = null

    fun incrementLunch() { _lunchCount.value++ }
    fun decrementLunch() { if (_lunchCount.value > 0) _lunchCount.value-- }
    fun setLunchCount(count: Int) { if (count >= 0) _lunchCount.value = count }
    fun toggleLunchTimer() {
        if (_lunchRunning.value) {
            _lunchRunning.value = false
            lunchJob?.cancel()
        } else {
            _lunchTimerLD.value = 0 // Reset timer
            _lunchRunning.value = true
            lunchJob = viewModelScope.launch {
                while (isActive && _lunchRunning.value) {
                    delay(1000)
                    _lunchTimerLD.postValue((_lunchTimerLD.value ?: 0) + 1)
                }
            }
        }
    }

    // --- Snacks (Similar structure) ---
    private val _snacksCount = MutableStateFlow(0)
    val snacksCount: StateFlow<Int> = _snacksCount.asStateFlow()
    private val _snacksTimerLD = MutableLiveData(0)
    val snacksTimerLD: LiveData<Int> = _snacksTimerLD
    private val _snacksRunning = MutableStateFlow(false)
    val snacksRunning: StateFlow<Boolean> = _snacksRunning.asStateFlow()
    private var snacksJob: Job? = null

    fun incrementSnacks() { _snacksCount.value++ }
    fun decrementSnacks() { if (_snacksCount.value > 0) _snacksCount.value-- }
    fun setSnacksCount(count: Int) { if (count >= 0) _snacksCount.value = count }
    fun toggleSnacksTimer() {
        if (_snacksRunning.value) {
            _snacksRunning.value = false
            snacksJob?.cancel()
        } else {
            _snacksTimerLD.value = 0 // Reset timer
            _snacksRunning.value = true
            snacksJob = viewModelScope.launch {
                while (isActive && _snacksRunning.value) {
                    delay(1000)
                    _snacksTimerLD.postValue((_snacksTimerLD.value ?: 0) + 1)
                }
            }
        }
    }

    // --- Dinner (Similar structure) ---
    private val _dinnerCount = MutableStateFlow(0)
    val dinnerCount: StateFlow<Int> = _dinnerCount.asStateFlow()
    private val _dinnerTimerLD = MutableLiveData(0)
    val dinnerTimerLD: LiveData<Int> = _dinnerTimerLD
    private val _dinnerRunning = MutableStateFlow(false)
    val dinnerRunning: StateFlow<Boolean> = _dinnerRunning.asStateFlow()
    private var dinnerJob: Job? = null

    fun incrementDinner() { _dinnerCount.value++ }
    fun decrementDinner() { if (_dinnerCount.value > 0) _dinnerCount.value-- }
    fun setDinnerCount(count: Int) { if (count >= 0) _dinnerCount.value = count }
    fun toggleDinnerTimer() {
        if (_dinnerRunning.value) {
            _dinnerRunning.value = false
            dinnerJob?.cancel()
        } else {
            _dinnerTimerLD.value = 0 // Reset timer
            _dinnerRunning.value = true
            dinnerJob = viewModelScope.launch {
                while (isActive && _dinnerRunning.value) {
                    delay(1000)
                    _dinnerTimerLD.postValue((_dinnerTimerLD.value ?: 0) + 1)
                }
            }
        }
    }
}
        