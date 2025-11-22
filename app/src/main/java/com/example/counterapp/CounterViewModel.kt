package com.example.counterapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CounterViewModel(application: Application): AndroidViewModel(application) {
    private val repo = CounterRepository(application)

    private val _today = MutableStateFlow(0)
    val today = _today.asStateFlow()

    private val _weekly = MutableStateFlow(mapOf<String,Int>())
    val weekly = _weekly.asStateFlow()

    private val _history = MutableStateFlow(mapOf<String,Int>())
    val history = _history.asStateFlow()

    init { loadAll() }

    fun addClick() {
        viewModelScope.launch {
            repo.addClickForToday()
            checkAndSendSummary()
            loadAll()
        }
    }

    private suspend fun checkAndSendSummary() {
        val hist = repo.getHistory()
        val todayKey = getTodayKey()
        val todayCount = hist[todayKey] ?: 0
        val goal = repo.getDailyGoal()
        if (!repo.wasSummaryNotifiedToday() && todayCount >= (goal * 0.8).toInt()) {
            NotificationHelper.send(getApplication(), "Daily Progress", "You reached 80% of your goal today.")
            repo.markSummaryNotifiedForToday()
        }
    }

    fun loadAll() {
        viewModelScope.launch {
            val hist = repo.getHistory()
            _history.value = hist
            _today.value = hist[getTodayKey()] ?: 0
            _weekly.value = getLast7(hist)
        }
    }

    fun dailyGoal(): Int = repo.getDailyGoal()

    private fun getTodayKey(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun getLast7(hist: Map<String, Int>): Map<String, Int> {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val out = LinkedHashMap<String, Int>()
        val cal = Calendar.getInstance()
        for (i in 6 downTo 0) {
            cal.time = Date()
            cal.add(Calendar.DATE, -i)
            val key = sdf.format(cal.time)
            out[key] = hist[key] ?: 0
        }
        return out
    }
}
