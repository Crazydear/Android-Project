package com.hearme.sqliteviewer.extension

import android.content.Context
import android.widget.Toast

fun String.showToast(context: Context, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(context, this, duration).show()
}

fun Int.showToast(context: Context, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(context, this, duration).show()
}