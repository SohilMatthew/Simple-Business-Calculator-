package com.example.businesscalculator.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.businesscalculator.data.entity.DailyRecord
import com.example.businesscalculator.data.entity.MenuItem
import com.example.businesscalculator.data.repository.BusinessRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class AnalyticsState(
    val estimatedMonthlyProfit: Double = 0.0,
    val revenueDiffVsYesterday: Double? = null,
    val revenueDiffVs7DaysAgo: Double? = null,
    val last7DaysTrend: List<Pair<String, Double>> = emptyList()
)

class BusinessViewModel(
    private val repository: BusinessRepository
) : ViewModel() {

    private val gson = Gson()

    val activeMenuItems: StateFlow<List<MenuItem>> = repository.activeMenuItems.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allMenuItems: StateFlow<List<MenuItem>> = repository.allMenuItems.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val dailyRecords: StateFlow<List<DailyRecord>> = repository.allDailyRecords.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _lastSavedProfit = MutableStateFlow<Double?>(null)
    val lastSavedProfit: StateFlow<Double?> = _lastSavedProfit.asStateFlow()

    // Derived Analytics Flow
    val analyticsState: StateFlow<AnalyticsState> = dailyRecords.combine(MutableStateFlow(Unit)) { records, _ ->
        calculateAnalytics(records)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AnalyticsState()
    )

    // Save Daily Entry
    fun saveDailyEntry(
        investmentStr: String,
        revenueStr: String,
        itemQuantities: Map<String, Int>,
        onSuccess: (Double) -> Unit,
        onError: (String) -> Unit
    ) {
        val investment = investmentStr.toDoubleOrNull()
        val revenue = revenueStr.toDoubleOrNull()

        if (investment == null || revenue == null) {
            onError("Please enter valid numbers for Investment and Revenue.")
            return
        }

        val profit = revenue - investment
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val jsonSales = gson.toJson(itemQuantities)

        val record = DailyRecord(
            date = currentDate,
            investment = investment,
            revenue = revenue,
            profit = profit,
            itemizedSalesJson = jsonSales
        )

        viewModelScope.launch {
            repository.insertDailyRecord(record)
            _lastSavedProfit.value = profit
            onSuccess(profit)
        }
    }

    // Catalog Management
    fun addMenuItem(name: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            repository.insertMenuItem(MenuItem(name = name.trim(), isActive = true))
        }
    }

    fun toggleMenuItemActive(item: MenuItem) {
        viewModelScope.launch {
            repository.updateMenuItem(item.copy(isActive = !item.isActive))
        }
    }

    fun deleteMenuItem(item: MenuItem) {
        viewModelScope.launch {
            repository.deleteMenuItem(item)
        }
    }

    fun deleteDailyRecord(record: DailyRecord) {
        viewModelScope.launch {
            repository.deleteDailyRecord(record)
        }
    }

    // JSON Helper for Itemized Sales display
    fun parseItemizedSales(json: String): Map<String, Int> {
        return try {
            val type = object : TypeToken<Map<String, Int>>() {}.type
            gson.fromJson(json, type) ?: emptyMap()
        } catch (e: Exception) {
            emptyMap()
        }
    }

    // Analytics Helper Calculations
    private fun calculateAnalytics(records: List<DailyRecord>): AnalyticsState {
        if (records.isEmpty()) return AnalyticsState()

        // 1. Estimated Monthly Profit: (Avg daily profit) * 30
        val avgDailyProfit = records.map { it.profit }.average()
        val estimatedMonthlyProfit = avgDailyProfit * 30.0

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()

        val todayStr = dateFormat.format(calendar.time)

        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val yesterdayStr = dateFormat.format(calendar.time)

        calendar.time = Date()
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val sevenDaysAgoStr = dateFormat.format(calendar.time)

        val latestRecord = records.firstOrNull()
        val yesterdayRecord = records.find { it.date == yesterdayStr } ?: records.getOrNull(1)
        val sevenDaysAgoRecord = records.find { it.date == sevenDaysAgoStr } ?: records.getOrNull(6)

        val revDiffYesterday = if (latestRecord != null && yesterdayRecord != null) {
            latestRecord.revenue - yesterdayRecord.revenue
        } else null

        val revDiff7Days = if (latestRecord != null && sevenDaysAgoRecord != null) {
            latestRecord.revenue - sevenDaysAgoRecord.revenue
        } else null

        // 7 Day trend (last 7 entries or last 7 calendar days)
        val trend = records.take(7).reversed().map { record ->
            val shortDate = try {
                val d = dateFormat.parse(record.date)
                SimpleDateFormat("MM/dd", Locale.getDefault()).format(d ?: Date())
            } catch (e: Exception) {
                record.date
            }
            Pair(shortDate, record.revenue)
        }

        return AnalyticsState(
            estimatedMonthlyProfit = estimatedMonthlyProfit,
            revenueDiffVsYesterday = revDiffYesterday,
            revenueDiffVs7DaysAgo = revDiff7Days,
            last7DaysTrend = trend
        )
    }
}
