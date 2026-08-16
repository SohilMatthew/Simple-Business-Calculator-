package com.example.businesscalculator.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_records")
data class DailyRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String, // Stored as YYYY-MM-DD
    val investment: Double,
    val revenue: Double,
    val profit: Double,
    val itemizedSalesJson: String // Stored as JSON string mapping item name -> quantity sold
)
