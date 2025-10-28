package com.example.counterplusplus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// ViewModel managing the state and logic for Counter++ app
class CounterViewModel : ViewModel() {

    // MutableStateFlow holding current counter value, exposed as immutable StateFlow
    private val _counter = MutableStateFlow(0)
    val counter: StateFlow<Int> = _counter

    // stateFlow to track whether Auto Mode is enabled
    private val _isAutoMode = MutableStateFlow(false)
    val isAutoMode: StateFlow<Boolean> = _isAutoMode

    //StateFlow to control visibility of settings screen
    private val _showSettings = MutableStateFlow(false)
    val showSettings: StateFlow<Boolean> = _showSettings

    //StateFlow for auto-increment interval in seconds, default to 3
    private val _interval = MutableStateFlow(3)
    val interval: StateFlow<Int> = _interval

    //Job reference for auto-increment coroutine, to allow cancellation
    private var autoJob: Job? = null

    // Increments counter by 1
    fun increment() {
        _counter.value += 1
    }

    // decrement counter by 1
    fun decrement() {
        _counter.value -= 1
    }

    // reset
    fun reset() {
        _counter.value = 0
    }

    //Toggles Auto Mode on/off and starts/stops auto-increment coroutine
    fun toggleAutoMode() {
        _isAutoMode.value = !_isAutoMode.value
        if (_isAutoMode.value) {
            startAutoIncrement()
        } else {
            autoJob?.cancel()
        }
    }

    // Coroutine that increments the counter every interval seconds while Auto Mode is on
    private fun startAutoIncrement() {
        autoJob?.cancel()
        autoJob = viewModelScope.launch {
            while (_isAutoMode.value) {
                delay(_interval.value * 1000L)
                _counter.value += 1
            }
        }
    }

    // Increases auto-increment interval, capped at 10 seconds
    fun increaseInterval() {
        _interval.value = (_interval.value + 1).coerceAtMost(10)
    }

    // Decreases auto-increment interval, floored at 1 second
    fun decreaseInterval() {
        _interval.value = (_interval.value - 1).coerceAtLeast(1)
    }

    //Toggles visibility of settings screen
    fun toggleSettings() {
        _showSettings.value = !_showSettings.value
    }
}
