package com.example.businesscalculator.data.repository

import com.example.businesscalculator.data.dao.DailyRecordDao
import com.example.businesscalculator.data.dao.MenuItemDao
import com.example.businesscalculator.data.entity.DailyRecord
import com.example.businesscalculator.data.entity.MenuItem
import kotlinx.coroutines.flow.Flow

class BusinessRepository(
    private val menuItemDao: MenuItemDao,
    private val dailyRecordDao: DailyRecordDao
) {
    val allMenuItems: Flow<List<MenuItem>> = menuItemDao.getAllMenuItems()
    val activeMenuItems: Flow<List<MenuItem>> = menuItemDao.getActiveMenuItems()
    val allDailyRecords: Flow<List<DailyRecord>> = dailyRecordDao.getAllDailyRecordsDescending()

    suspend fun insertMenuItem(item: MenuItem) {
        menuItemDao.insertMenuItem(item)
    }

    suspend fun updateMenuItem(item: MenuItem) {
        menuItemDao.updateMenuItem(item)
    }

    suspend fun deleteMenuItem(item: MenuItem) {
        menuItemDao.deleteMenuItem(item)
    }

    suspend fun insertDailyRecord(record: DailyRecord) {
        dailyRecordDao.insertDailyRecord(record)
    }

    suspend fun getRecordByDate(date: String): DailyRecord? {
        return dailyRecordDao.getRecordByDate(date)
    }

    suspend fun deleteDailyRecord(record: DailyRecord) {
        dailyRecordDao.deleteDailyRecord(record)
    }
}
