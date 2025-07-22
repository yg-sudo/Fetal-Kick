package com.yg.fetalkick

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class KickViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = KickRepository(application)
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    private val _selectedDate = MutableLiveData(LocalDate.now())
    val selectedDate: LiveData<LocalDate> = _selectedDate

    private val _breakfastCount = MutableLiveData(0)
    val breakfastCount: LiveData<Int> = _breakfastCount
    private val _lunchCount = MutableLiveData(0)
    val lunchCount: LiveData<Int> = _lunchCount
    private val _snacksCount = MutableLiveData(0)
    val snacksCount: LiveData<Int> = _snacksCount
    private val _dinnerCount = MutableLiveData(0)
    val dinnerCount: LiveData<Int> = _dinnerCount

    // Stopwatch states (seconds)
    private val _breakfastTimer = MutableLiveData(0)
    val breakfastTimer: LiveData<Int> = _breakfastTimer
    private val _lunchTimer = MutableLiveData(0)
    val lunchTimer: LiveData<Int> = _lunchTimer
    private val _snacksTimer = MutableLiveData(0)
    val snacksTimer: LiveData<Int> = _snacksTimer
    private val _dinnerTimer = MutableLiveData(0)
    val dinnerTimer: LiveData<Int> = _dinnerTimer

    // Stopwatch running flags
    private val _breakfastRunning = MutableLiveData(false)
    val breakfastRunning: LiveData<Boolean> = _breakfastRunning
    private val _lunchRunning = MutableLiveData(false)
    val lunchRunning: LiveData<Boolean> = _lunchRunning
    private val _snacksRunning = MutableLiveData(false)
    val snacksRunning: LiveData<Boolean> = _snacksRunning
    private val _dinnerRunning = MutableLiveData(false)
    val dinnerRunning: LiveData<Boolean> = _dinnerRunning

    private var breakfastJob: Job? = null
    private var lunchJob: Job? = null
    private var snacksJob: Job? = null
    private var dinnerJob: Job? = null

    init {
        loadEntryForDate(_selectedDate.value!!)
    }

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
        loadEntryForDate(date)
    }

    private fun loadEntryForDate(date: LocalDate) {
        viewModelScope.launch {
            val entry = repository.getEntryByDate(date.format(dateFormatter))
            _breakfastCount.value = entry?.breakfastCount ?: 0
            _lunchCount.value = entry?.lunchCount ?: 0
            _snacksCount.value = entry?.snacksCount ?: 0
            _dinnerCount.value = entry?.dinnerCount ?: 0
            // Reset timers and running states
            _breakfastTimer.value = 0
            _lunchTimer.value = 0
            _snacksTimer.value = 0
            _dinnerTimer.value = 0
            _breakfastRunning.value = false
            _lunchRunning.value = false
            _snacksRunning.value = false
            _dinnerRunning.value = false
            stopAllTimers()
        }
    }

    private fun saveEntry() {
        val date = _selectedDate.value?.format(dateFormatter) ?: return
        val entry = KickEntry(
            date = date,
            breakfastCount = _breakfastCount.value ?: 0,
            lunchCount = _lunchCount.value ?: 0,
            snacksCount = _snacksCount.value ?: 0,
            dinnerCount = _dinnerCount.value ?: 0
        )
        viewModelScope.launch { repository.insertOrUpdate(entry) }
    }

    // Count operations
    fun setBreakfastCount(count: Int) {
        _breakfastCount.value = count.coerceAtLeast(0)
        saveEntry()
    }
    fun setLunchCount(count: Int) {
        _lunchCount.value = count.coerceAtLeast(0)
        saveEntry()
    }
    fun setSnacksCount(count: Int) {
        _snacksCount.value = count.coerceAtLeast(0)
        saveEntry()
    }
    fun setDinnerCount(count: Int) {
        _dinnerCount.value = count.coerceAtLeast(0)
        saveEntry()
    }
    fun incrementBreakfast() = setBreakfastCount((_breakfastCount.value ?: 0) + 1)
    fun decrementBreakfast() = setBreakfastCount((_breakfastCount.value ?: 0) - 1)
    fun incrementLunch() = setLunchCount((_lunchCount.value ?: 0) + 1)
    fun decrementLunch() = setLunchCount((_lunchCount.value ?: 0) - 1)
    fun incrementSnacks() = setSnacksCount((_snacksCount.value ?: 0) + 1)
    fun decrementSnacks() = setSnacksCount((_snacksCount.value ?: 0) - 1)
    fun incrementDinner() = setDinnerCount((_dinnerCount.value ?: 0) + 1)
    fun decrementDinner() = setDinnerCount((_dinnerCount.value ?: 0) - 1)

    // Stopwatch logic
    fun toggleBreakfastTimer() {
        if (_breakfastRunning.value == true) stopBreakfastTimer() else startBreakfastTimer()
    }
    fun toggleLunchTimer() {
        if (_lunchRunning.value == true) stopLunchTimer() else startLunchTimer()
    }
    fun toggleSnacksTimer() {
        if (_snacksRunning.value == true) stopSnacksTimer() else startSnacksTimer()
    }
    fun toggleDinnerTimer() {
        if (_dinnerRunning.value == true) stopDinnerTimer() else startDinnerTimer()
    }

    private fun startBreakfastTimer() {
        _breakfastRunning.value = true
        breakfastJob = viewModelScope.launch {
            while (_breakfastRunning.value == true) {
                delay(1000)
                _breakfastTimer.value = (_breakfastTimer.value ?: 0) + 1
            }
        }
    }
    private fun stopBreakfastTimer() {
        _breakfastRunning.value = false
        breakfastJob?.cancel()
        saveEntry() // Save on stop
    }
    private fun startLunchTimer() {
        _lunchRunning.value = true
        lunchJob = viewModelScope.launch {
            while (_lunchRunning.value == true) {
                delay(1000)
                _lunchTimer.value = (_lunchTimer.value ?: 0) + 1
            }
        }
    }
    private fun stopLunchTimer() {
        _lunchRunning.value = false
        lunchJob?.cancel()
        saveEntry()
    }
    private fun startSnacksTimer() {
        _snacksRunning.value = true
        snacksJob = viewModelScope.launch {
            while (_snacksRunning.value == true) {
                delay(1000)
                _snacksTimer.value = (_snacksTimer.value ?: 0) + 1
            }
        }
    }
    private fun stopSnacksTimer() {
        _snacksRunning.value = false
        snacksJob?.cancel()
        saveEntry()
    }
    private fun startDinnerTimer() {
        _dinnerRunning.value = true
        dinnerJob = viewModelScope.launch {
            while (_dinnerRunning.value == true) {
                delay(1000)
                _dinnerTimer.value = (_dinnerTimer.value ?: 0) + 1
            }
        }
    }
    private fun stopDinnerTimer() {
        _dinnerRunning.value = false
        dinnerJob?.cancel()
        saveEntry()
    }
    private fun stopAllTimers() {
        _breakfastRunning.value = false
        _lunchRunning.value = false
        _snacksRunning.value = false
        _dinnerRunning.value = false
        breakfastJob?.cancel()
        lunchJob?.cancel()
        snacksJob?.cancel()
        dinnerJob?.cancel()
    }
}