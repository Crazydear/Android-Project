package com.hearme.sqliteviewer.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences


@SuppressLint("StaticFieldLeak")
object SharedPreferenceManager {

    private var prefs: SharedPreferenceManager? = null
    private var mContext: Context? = null

    fun getInstence(context: Context): SharedPreferenceManager {
        if (prefs == null) {
            prefs = SharedPreferenceManager
            mContext = context
        }
        return prefs as SharedPreferenceManager
    }

    private fun getSharedPrefernces(preference:String):SharedPreferences{
        return mContext!!.getSharedPreferences(preference,Context.MODE_PRIVATE)
    }

    fun getString(preference: String,key:String,defaultValue:String): String {
        return getSharedPrefernces(preference).getString(key,defaultValue)!!
    }

    fun setString(preference: String,key: String,value:String){
        val editor = getSharedPrefernces(preference).edit()
        editor.putString(key, value)
        editor.apply()
    }

}