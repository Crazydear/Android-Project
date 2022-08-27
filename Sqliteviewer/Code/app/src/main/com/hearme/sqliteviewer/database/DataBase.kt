package com.hearme.sqliteviewer.database

import android.annotation.SuppressLint
import android.content.Context
import android.database.DatabaseUtils
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.hearme.sqliteviewer.Const
import com.hearme.sqliteviewer.extension.showToast
import com.hearme.sqliteviewer.model.Cell
import com.hearme.sqliteviewer.model.FieldModel
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader


@SuppressLint("StaticFieldLeak")
object DataBase {

    private var _db: SQLiteDatabase? = null
    private lateinit var _cont: Context
    private var dbInstance: DataBase? = null
    private var isDatabase = false
    private var isRoot = false
    private lateinit var _dbPath: String
    private lateinit var originalDBPath: String


    fun getInstance(context: Context):DataBase{
        if (dbInstance != null)
            return dbInstance!!
        _cont = context
        dbInstance = DataBase
        return DataBase
    }

    fun isDatabase(): Boolean {
        return isDatabase
    }

    fun setDatabase(dbPath:String):Boolean{
        _dbPath = dbPath
        originalDBPath =dbPath
        try {
            if (testDBFile(_dbPath)){
                // Here we know it is a SQLite 3 file
                Log.d(Const.TAG, "Trying to open (RW): $_dbPath")
                _db = SQLiteDatabase.openDatabase(_dbPath,null,SQLiteDatabase.OPEN_READONLY)
                isDatabase = true
            }else
                isDatabase = false
        }catch (e:Exception){
            Log.d(Const.TAG, "Trying to open Exception: " + e.message)
            e.printStackTrace()
            // It is not a database
            isDatabase = false
        }
        return isDatabase
    }

    private fun testDBFile(dbPath: String): Boolean {
        val backupFile = File(dbPath)
        if (!backupFile.canRead()){
            isRoot = true
        }
        if (backupFile.canRead()){
            try {
                val reader = FileReader(dbPath)
                val buffer = CharArray(16)
                reader.read(buffer, 0, 16)
                val str = String(buffer)
                reader.close()
                return str == "SQLite format 3\u0000"
            }catch (e: FileNotFoundException){
                Log.e(Const.TAG, "File not found")
                e.printStackTrace()
            }catch (e:Exception){
                Log.e(Const.TAG, "IO error")
                e.printStackTrace()
            }
            return false
        }
        return false
    }

    private fun testDB(){
        if (_db == null){
            Log.d(Const.TAG, "TestDB database is null")
            if (_dbPath != null) {  //then Content probably also null
                try {
                    _db = SQLiteDatabase.openDatabase(_dbPath, null, SQLiteDatabase.OPEN_READONLY)
                } catch (e: java.lang.Exception) {
                    Log.e(Const.TAG, "testDB " + e.message)
                    e.printStackTrace()
                }
            } else {
                "Something strange happened! Try again later".showToast(_cont)
            }
        } else if (!_db!!.isOpen) {
            Log.d(Const.TAG, "TestDB database not open")
            if (_dbPath == null) {
                "Something strange happened! Try again later".showToast(_cont)
            } else {
                try {
                    _db = SQLiteDatabase.openDatabase(_dbPath, null, SQLiteDatabase.OPEN_READWRITE)
                } catch (e: Exception) {
                    Log.e(Const.TAG, "testDB " + e.message)
                    e.printStackTrace()
                }
            }
        } else {
        }
    }

    fun getTables():ArrayList<String>{
        testDB()
        val sql = "select name from sqlite_master where type = 'table' order by name"
        val res = _db!!.rawQuery(sql, null)
        //int recs = res.getCount();
        //String[] tables = new String[recs + 1];
        val tables = ArrayList<String>()
        //int i = 1;
        tables.add("sqlite_master")
        //Utils.logD("Tables: " + recs);
        while (res.moveToNext()) {
            if (res.getString(0) == "android_metadata")
                continue
            tables.add(res.getString(0))
            //i++;
        }
        res.close()
        return tables
    }

    fun get_dbPath(): String {
        return originalDBPath
    }

    fun getFields(table:String):ArrayList<FieldModel>{
        // Get field type
        // SELECT typeof(sql) FROM sqlite_master where typeof(sql) <> "null" limit 1
        testDB()
        val sql = "pragma table_info([$table])"
        val res = _db!!.rawQuery(sql, null)

        val fields = java.util.ArrayList<FieldModel>()
        // getting field names
        while (res.moveToNext()) {
            val field = FieldModel()
            field.fieldName = res.getString(1)
            field.fieldType = res.getString(2)
            field.notNull = res.getInt(3)
            field.def = res.getString(4)
            field.pk = res.getInt(5)
            fields.add(field)
        }
        res.close()
        return fields
    }

    fun getFieldsNames(table: String): Array<String?> {
        testDB()
        val sql = "pragma table_info([$table])"
        val res = _db!!.rawQuery(sql, null) //TODO 3.3 NullPointerException here
        val cols = res.count
        val fields = arrayOfNulls<String>(cols)
        var i = 0
        while (res.moveToNext()){
            fields[i] = res.getString(1)
            i++
        }
        res.close()
        return fields
    }

    fun getCount(tableName: String): Long {
        return DatabaseUtils.queryNumEntries(_db,tableName)
    }

    @SuppressLint("Range")
    fun getTableData(tableName: String, limit: Long, offsetFrom: Long): List<List<Cell>> {
        val cols = getFieldsNames(tableName)
        val sql = StringBuilder().append("Select ")
        for (i in cols.indices) {
            sql.append(cols[i])
            if (i < cols.size - 1) sql.append(", ")
        }
        sql.append(" FROM ").append(tableName).append(" LIMIT ").append(limit)
            .append(" OFFSET ").append(offsetFrom)

        Log.d(Const.TAG, "Query $sql")

        val cursor = _db!!.rawQuery(sql.toString(), null)
        val Tabledata: ArrayList<List<Cell>> = ArrayList<List<Cell>>()
        while (cursor.moveToNext()) {
            val colData: ArrayList<Cell> = ArrayList<Cell>()
            var data: String = ""
            for (col in cols) {
                val colIndex = cursor.getColumnIndex(col)
                try {
                    data = cursor.getString(cursor.getColumnIndex(col))
                } catch (e: SQLException) {
                    //e.printStackTrace();
                    if (e.message!!.contains("Unable to convert BLOB to string")) data = "(BLOB)"
                }
                colData.add(Cell(colIndex.toString(), data))
            }
            Tabledata.add(colData)
        }
        cursor.close()
        //dumpData(Tabledata);
        return Tabledata
    }


}