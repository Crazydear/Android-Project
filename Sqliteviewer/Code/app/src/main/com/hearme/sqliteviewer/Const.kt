package com.hearme.sqliteviewer

import android.os.Environment
import java.io.File
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

object Const {

    const val DBPathIntent = "db_path"
    const val DBTableNameIntent = "db_table_name"
    const val TAG = "SQLiteViewer"

    val EXTERNAL_STORAGE_DIR = File(Environment.getExternalStorageDirectory(), "documents")
    val CSV_EXPORT_DIR: File = File(EXTERNAL_STORAGE_DIR, "CSV")

    fun getEncodingType(encoding: String?): Charset? {
        return when (encoding) {
            "UTF-8" -> StandardCharsets.UTF_8
            "UTF-16" -> StandardCharsets.UTF_16
            "UTF-16LE" -> StandardCharsets.UTF_16LE
            "UTF-16BE" -> StandardCharsets.UTF_16BE
            "US-ASCII" -> StandardCharsets.US_ASCII
            "ISO–8859–1" -> StandardCharsets.ISO_8859_1
            else -> StandardCharsets.UTF_8
        }
    }
}