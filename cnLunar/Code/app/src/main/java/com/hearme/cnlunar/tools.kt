package com.hearme.cnlunar

import com.hearme.cnlunar.config.encryptionVectorList
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object tools {
    val formatter = SimpleDateFormat("yyyy-MM-dd")

    fun get_lunarYearCN(lunarYear:Int): String {
        // 年份转大写
        var _upper_year = ""
        val upperNum = listOf("〇", "一", "二", "三", "四", "五", "六", "七", "八", "九")
        for (i in lunarYear.toString()){
            _upper_year = "$_upper_year${upperNum[i.digitToInt()]}"
        }
        return _upper_year
    }

    fun diff_day_number(ends: Calendar, starts:String): Long {
        val end: Date = formatter.parse("${ends.get(Calendar.YEAR)}-${ends.get(Calendar.MONTH)+1}-${ends.get(
            Calendar.DATE)}") //结束时间
        val start: Date = formatter.parse(starts) //开始时间
        val diff: Long = end.getTime() - start.getTime()
        val apart = diff / (1000*3600*24)
        return apart
    }

    fun diff_day_number(ends: String, starts:String): Long {
        val end: Date = formatter.parse(ends) //结束时间
        val start: Date = formatter.parse(starts) //开始时间
        val diff: Long = end.getTime() - start.getTime()
        val apart = diff / (1000*3600*24)
        return apart
    }

    fun day_add(time:String,addDay:Int): List<Int> {
        var g = Calendar.getInstance()
        g.time = formatter.parse(time)
        g.add(Calendar.DATE, addDay)
        return listOf(g.get(Calendar.MONTH)+1,g.get(Calendar.DATE))
    }

    fun diff_day_number(ends: String, starts: Calendar): Int {
        val start: Date = formatter.parse("${ends.get(Calendar.YEAR)}-${ends.get(Calendar.MONTH)+1}-${ends.get(
            Calendar.DATE)}") //结束时间
        val end: Date = formatter.parse(ends) //结束时间
        val diff: Long = end.getTime() - start.getTime()
        val apart = diff / (1000*3600*24)
        return apart.toInt()
    }

    fun rfAdd(l:ArrayList<String>?,addList:List<String>?):ArrayList<String>{
        val temp = ArrayList<String>()
        l?.let { temp.addAll(it) }
        addList?.let { temp.addAll(it) }
        temp.toSortedSet().toList()
        return temp
    }

    fun rfRemove(l:ArrayList<String>,removeList:List<String>):ArrayList<String>{
        for (removeThing in l.toSortedSet().intersect(removeList.toSortedSet())){
            l.remove(removeThing)
        }
        return l
    }

    fun rlAdd(l: List<String>?, addList: List<String>?):ArrayList<String>{
        val temp = ArrayList<String>()
        l?.let { temp.addAll(it) }
        addList?.let { temp.addAll(it) }
        return temp
    }
    // 两个List合并对应元素相加或者相减，a[i]+b[i]:tpye=1 a[i]-b[i]:tpye=-1
    fun abListMerge(a:List<Int>, b: List<Int> =encryptionVectorList, type:Int=1): List<Int> {
        val c = ArrayList<Int>()
        for (i in a.indices) {
            c.add(a[i] + b[i] * type)
        }
        return c
    }




}