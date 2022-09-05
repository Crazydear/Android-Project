package com.hearme.cnlunar

import android.util.Log
import com.hearme.cnlunar.config.SOLAR_TERMS_DATA_LIST
import com.hearme.cnlunar.config.START_YEAR
import com.hearme.cnlunar.tools.abListMerge
import java.lang.Math.pow

/*# 24节气模块\节气数据16进制加解密
# author: cuba3
# github: https://github.com/OPN48/pyLunarCalendar
*/

object solar24 {
    // # 解压缩16进制用
    fun unZipSolarTermsList(data:Long,rangeEndNum:Int=24,charCountLen:Int=2):List<Int>{
        var list2 = arrayListOf<Int>()
        for (i in 1..rangeEndNum){
            val right = charCountLen * (rangeEndNum-i)
            val x = data shr right
            val c = pow(2.0, charCountLen.toDouble())
            list2.add((x % c).toInt())
        }
        list2.reverse()
        return abListMerge(list2)
    }

    // # 采集压缩用
//    fun zipSolarTermsList(inputList,charCountLen:Int=2){
//        val tempList = abListMerge(inputList, type=-1)
//        val data = 0
//        var num = 0
//        for (i in tempList){
//            data += i shl charCountLen*num
//            num+=1
//        }
//
//        return hex(data),len(tempList)
//    }

    fun getTheYearAllSolarTermsList(year:Int): List<Int>{
        return unZipSolarTermsList(SOLAR_TERMS_DATA_LIST[year - START_YEAR])
    }

}