package com.example.businesscalculator.`data`.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.businesscalculator.`data`.entity.DailyRecord
import javax.`annotation`.processing.Generated
import kotlin.Double
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class DailyRecordDao_Impl(
  __db: RoomDatabase,
) : DailyRecordDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfDailyRecord: EntityInsertAdapter<DailyRecord>

  private val __deleteAdapterOfDailyRecord: EntityDeleteOrUpdateAdapter<DailyRecord>
  init {
    this.__db = __db
    this.__insertAdapterOfDailyRecord = object : EntityInsertAdapter<DailyRecord>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `daily_records` (`id`,`date`,`investment`,`revenue`,`profit`,`itemizedSalesJson`) VALUES (nullif(?, 0),?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: DailyRecord) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.date)
        statement.bindDouble(3, entity.investment)
        statement.bindDouble(4, entity.revenue)
        statement.bindDouble(5, entity.profit)
        statement.bindText(6, entity.itemizedSalesJson)
      }
    }
    this.__deleteAdapterOfDailyRecord = object : EntityDeleteOrUpdateAdapter<DailyRecord>() {
      protected override fun createQuery(): String = "DELETE FROM `daily_records` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: DailyRecord) {
        statement.bindLong(1, entity.id)
      }
    }
  }

  public override suspend fun insertDailyRecord(record: DailyRecord): Unit = performSuspending(__db,
      false, true) { _connection ->
    __insertAdapterOfDailyRecord.insert(_connection, record)
  }

  public override suspend fun deleteDailyRecord(record: DailyRecord): Unit = performSuspending(__db,
      false, true) { _connection ->
    __deleteAdapterOfDailyRecord.handle(_connection, record)
  }

  public override fun getAllDailyRecordsDescending(): Flow<List<DailyRecord>> {
    val _sql: String = "SELECT * FROM daily_records ORDER BY date DESC, id DESC"
    return createFlow(__db, false, arrayOf("daily_records")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfInvestment: Int = getColumnIndexOrThrow(_stmt, "investment")
        val _columnIndexOfRevenue: Int = getColumnIndexOrThrow(_stmt, "revenue")
        val _columnIndexOfProfit: Int = getColumnIndexOrThrow(_stmt, "profit")
        val _columnIndexOfItemizedSalesJson: Int = getColumnIndexOrThrow(_stmt, "itemizedSalesJson")
        val _result: MutableList<DailyRecord> = mutableListOf()
        while (_stmt.step()) {
          val _item: DailyRecord
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpDate: String
          _tmpDate = _stmt.getText(_columnIndexOfDate)
          val _tmpInvestment: Double
          _tmpInvestment = _stmt.getDouble(_columnIndexOfInvestment)
          val _tmpRevenue: Double
          _tmpRevenue = _stmt.getDouble(_columnIndexOfRevenue)
          val _tmpProfit: Double
          _tmpProfit = _stmt.getDouble(_columnIndexOfProfit)
          val _tmpItemizedSalesJson: String
          _tmpItemizedSalesJson = _stmt.getText(_columnIndexOfItemizedSalesJson)
          _item =
              DailyRecord(_tmpId,_tmpDate,_tmpInvestment,_tmpRevenue,_tmpProfit,_tmpItemizedSalesJson)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getRecordByDate(date: String): DailyRecord? {
    val _sql: String = "SELECT * FROM daily_records WHERE date = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, date)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfInvestment: Int = getColumnIndexOrThrow(_stmt, "investment")
        val _columnIndexOfRevenue: Int = getColumnIndexOrThrow(_stmt, "revenue")
        val _columnIndexOfProfit: Int = getColumnIndexOrThrow(_stmt, "profit")
        val _columnIndexOfItemizedSalesJson: Int = getColumnIndexOrThrow(_stmt, "itemizedSalesJson")
        val _result: DailyRecord?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpDate: String
          _tmpDate = _stmt.getText(_columnIndexOfDate)
          val _tmpInvestment: Double
          _tmpInvestment = _stmt.getDouble(_columnIndexOfInvestment)
          val _tmpRevenue: Double
          _tmpRevenue = _stmt.getDouble(_columnIndexOfRevenue)
          val _tmpProfit: Double
          _tmpProfit = _stmt.getDouble(_columnIndexOfProfit)
          val _tmpItemizedSalesJson: String
          _tmpItemizedSalesJson = _stmt.getText(_columnIndexOfItemizedSalesJson)
          _result =
              DailyRecord(_tmpId,_tmpDate,_tmpInvestment,_tmpRevenue,_tmpProfit,_tmpItemizedSalesJson)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
