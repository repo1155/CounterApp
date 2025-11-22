package com.example.counterapp

import android.content.Context
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.*

class CounterRepository(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("counter_app_prefs", Context.MODE_PRIVATE)

    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    // Increment today's click count
    fun addClickForToday() {
        val key = getTodayKey()
        val todayCount = prefs.getInt(key, 0)
        prefs.edit().putInt(key, todayCount + 1).apply()
    }

    // Return all stored counts
    fun getHistory(): Map<String, Int> {
        val map = mutableMapOf<String, Int>()
        prefs.all.forEach { (k, v) ->
            if (v is Int) map[k] = v
        }
        return map.toMap()
    }

    // Return daily goal (can be made configurable)
    fun getDailyGoal(): Int = 10

    // Check if daily summary was already notified
    fun wasSummaryNotifiedToday(): Boolean {
        val key = getTodayKey() + "_summary"
        return prefs.getBoolean(key, false)
    }

    // Mark that today's summary has been notified
    fun markSummaryNotifiedForToday() {
        val key = getTodayKey() + "_summary"
        prefs.edit().putBoolean(key, true).apply()
    }

    // Helper: get today's date as string key
    private fun getTodayKey(): String = sdf.format(Date())
}
