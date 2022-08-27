package com.hearme.sqliteviewer.utils

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.hearme.sqliteviewer.Const
import com.hearme.sqliteviewer.extension.showToast
import com.hearme.sqliteviewer.ui.TableListActivity


class DatabaseIntentFilter:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uri: Uri? = intent.data
        if (uri != null) {
            Log.d(Const.TAG, "URI: " + uri.path)
            uri.path?.let { startDatabaseViewerActivity(it.substring(5,it.length)) }
        } else {
            "错误".showToast(this)
            finish()
        }
    }

    private fun startDatabaseViewerActivity(file: String) {
            val tableListIntent = Intent(this, TableListActivity::class.java)
            tableListIntent.putExtra(Const.DBPathIntent, file)
            startActivity(tableListIntent)
            finish()
    }
}