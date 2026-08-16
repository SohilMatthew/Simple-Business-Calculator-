package com.example.businesscalculator.`data`.database

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.example.businesscalculator.`data`.dao.DailyRecordDao
import com.example.businesscalculator.`data`.dao.DailyRecordDao_Impl
import com.example.businesscalculator.`data`.dao.MenuItemDao
import com.example.businesscalculator.`data`.dao.MenuItemDao_Impl
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class BusinessDatabase_Impl : BusinessDatabase() {
  private val _menuItemDao: Lazy<MenuItemDao> = lazy {
    MenuItemDao_Impl(this)
  }

  private val _dailyRecordDao: Lazy<DailyRecordDao> = lazy {
    DailyRecordDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1,
        "c685f339d0492758ef54d2bb8c70f431", "3b489f2bf73f46d11ff3286693fe51a4") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `menu_items` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `isActive` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `daily_records` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `date` TEXT NOT NULL, `investment` REAL NOT NULL, `revenue` REAL NOT NULL, `profit` REAL NOT NULL, `itemizedSalesJson` TEXT NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'c685f339d0492758ef54d2bb8c70f431')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `menu_items`")
        connection.execSQL("DROP TABLE IF EXISTS `daily_records`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection):
          RoomOpenDelegate.ValidationResult {
        val _columnsMenuItems: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsMenuItems.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsMenuItems.put("name", TableInfo.Column("name", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsMenuItems.put("isActive", TableInfo.Column("isActive", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysMenuItems: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesMenuItems: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoMenuItems: TableInfo = TableInfo("menu_items", _columnsMenuItems,
            _foreignKeysMenuItems, _indicesMenuItems)
        val _existingMenuItems: TableInfo = read(connection, "menu_items")
        if (!_infoMenuItems.equals(_existingMenuItems)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |menu_items(com.example.businesscalculator.data.entity.MenuItem).
              | Expected:
              |""".trimMargin() + _infoMenuItems + """
              |
              | Found:
              |""".trimMargin() + _existingMenuItems)
        }
        val _columnsDailyRecords: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsDailyRecords.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsDailyRecords.put("date", TableInfo.Column("date", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsDailyRecords.put("investment", TableInfo.Column("investment", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsDailyRecords.put("revenue", TableInfo.Column("revenue", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsDailyRecords.put("profit", TableInfo.Column("profit", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsDailyRecords.put("itemizedSalesJson", TableInfo.Column("itemizedSalesJson", "TEXT",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysDailyRecords: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesDailyRecords: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoDailyRecords: TableInfo = TableInfo("daily_records", _columnsDailyRecords,
            _foreignKeysDailyRecords, _indicesDailyRecords)
        val _existingDailyRecords: TableInfo = read(connection, "daily_records")
        if (!_infoDailyRecords.equals(_existingDailyRecords)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |daily_records(com.example.businesscalculator.data.entity.DailyRecord).
              | Expected:
              |""".trimMargin() + _infoDailyRecords + """
              |
              | Found:
              |""".trimMargin() + _existingDailyRecords)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "menu_items", "daily_records")
  }

  public override fun clearAllTables() {
    super.performClear(false, "menu_items", "daily_records")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(MenuItemDao::class, MenuItemDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(DailyRecordDao::class, DailyRecordDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override
      fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>):
      List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun menuItemDao(): MenuItemDao = _menuItemDao.value

  public override fun dailyRecordDao(): DailyRecordDao = _dailyRecordDao.value
}
