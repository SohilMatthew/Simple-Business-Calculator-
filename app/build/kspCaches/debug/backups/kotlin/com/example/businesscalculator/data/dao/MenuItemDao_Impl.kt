package com.example.businesscalculator.`data`.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.businesscalculator.`data`.entity.MenuItem
import javax.`annotation`.processing.Generated
import kotlin.Boolean
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
public class MenuItemDao_Impl(
  __db: RoomDatabase,
) : MenuItemDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfMenuItem: EntityInsertAdapter<MenuItem>

  private val __deleteAdapterOfMenuItem: EntityDeleteOrUpdateAdapter<MenuItem>

  private val __updateAdapterOfMenuItem: EntityDeleteOrUpdateAdapter<MenuItem>
  init {
    this.__db = __db
    this.__insertAdapterOfMenuItem = object : EntityInsertAdapter<MenuItem>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `menu_items` (`id`,`name`,`isActive`) VALUES (nullif(?, 0),?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: MenuItem) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.name)
        val _tmp: Int = if (entity.isActive) 1 else 0
        statement.bindLong(3, _tmp.toLong())
      }
    }
    this.__deleteAdapterOfMenuItem = object : EntityDeleteOrUpdateAdapter<MenuItem>() {
      protected override fun createQuery(): String = "DELETE FROM `menu_items` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: MenuItem) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfMenuItem = object : EntityDeleteOrUpdateAdapter<MenuItem>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `menu_items` SET `id` = ?,`name` = ?,`isActive` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: MenuItem) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.name)
        val _tmp: Int = if (entity.isActive) 1 else 0
        statement.bindLong(3, _tmp.toLong())
        statement.bindLong(4, entity.id)
      }
    }
  }

  public override suspend fun insertMenuItem(item: MenuItem): Unit = performSuspending(__db, false,
      true) { _connection ->
    __insertAdapterOfMenuItem.insert(_connection, item)
  }

  public override suspend fun deleteMenuItem(item: MenuItem): Unit = performSuspending(__db, false,
      true) { _connection ->
    __deleteAdapterOfMenuItem.handle(_connection, item)
  }

  public override suspend fun updateMenuItem(item: MenuItem): Unit = performSuspending(__db, false,
      true) { _connection ->
    __updateAdapterOfMenuItem.handle(_connection, item)
  }

  public override fun getAllMenuItems(): Flow<List<MenuItem>> {
    val _sql: String = "SELECT * FROM menu_items ORDER BY id ASC"
    return createFlow(__db, false, arrayOf("menu_items")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfIsActive: Int = getColumnIndexOrThrow(_stmt, "isActive")
        val _result: MutableList<MenuItem> = mutableListOf()
        while (_stmt.step()) {
          val _item: MenuItem
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpIsActive: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsActive).toInt()
          _tmpIsActive = _tmp != 0
          _item = MenuItem(_tmpId,_tmpName,_tmpIsActive)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getActiveMenuItems(): Flow<List<MenuItem>> {
    val _sql: String = "SELECT * FROM menu_items WHERE isActive = 1 ORDER BY name ASC"
    return createFlow(__db, false, arrayOf("menu_items")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfIsActive: Int = getColumnIndexOrThrow(_stmt, "isActive")
        val _result: MutableList<MenuItem> = mutableListOf()
        while (_stmt.step()) {
          val _item: MenuItem
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpIsActive: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsActive).toInt()
          _tmpIsActive = _tmp != 0
          _item = MenuItem(_tmpId,_tmpName,_tmpIsActive)
          _result.add(_item)
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
