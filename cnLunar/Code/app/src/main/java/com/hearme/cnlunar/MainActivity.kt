package com.hearme.cnlunar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.TextView
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val text = findViewById<TextView>(R.id.texty)
        text.movementMethod = ScrollingMovementMethod.getInstance()
        val cal = Calendar.getInstance()
//        cal.set(2010,2-1,25)
        val lunars = Lunar(2089,5,18)
        val nlsz = "农历数字:" + lunars.lunarYear +" "+ lunars.lunarMonth+ " "+ lunars.lunarDay +  if (lunars.isLunarLeapMonth) "闰" else ""
        val nl = "农历: " + lunars.lunarYearCn+"年 " + lunars.year8Char +  lunars.chineseYearZodiac+ "年" + lunars.lunarMonthCn+ lunars.lunarDayCn
        val xq = "星期: " + lunars.weekDayCn
        val bz = "八字: "+ lunars.year8Char+lunars.month8Char+lunars.day8Char+lunars.twohour8Char
        val jrjq = "今日节气: " + lunars.todaySolarTerms
        val xyjq = "下一节气: " + lunars.nextSolarTerm+ lunars.nextSolarTermDate+lunars.nextSolarTermYear
        val jnjqb = "今年节气表: " + lunars.thisYearSolarTermsDic
        val jj = "月令: " + lunars.lunarSeason
        val jrsc = "今日时辰: "+lunars.twohour8CharList
        val scjx = "时辰凶吉: " + lunars.get_twohourLuckyList()
        val sxcs = "生肖冲煞:"+ lunars.chineseZodiacClash
        val pzbj = "彭祖百忌: " + lunars.get_pengTaboo()
        val pzbjjj = "彭祖百忌精简: " + lunars.get_pengTaboo(long=4)
        val ses = "十二神:" + lunars.get_today12DayOfficer()
        val esbx = "廿八宿: " + lunars.get_the28Stars()
        val jrsh = "今日三合: " + lunars.zodiacMark3List
        val jrlh = "今日六合: " + lunars.zodiacMark6
        val jrwx = "今日五行: " + lunars.get_today5Elements()
        val ny = "纳音:" + lunars.get_nayin()
        val jgfx = "九宫飞星: " + lunars.get_the9FlyStar()
        val jsfw = "吉神方位: "+ lunars.get_luckyGodsDirection()
        val jrts = "今日胎神: " + lunars.get_fetalGod()
        val ssyj = "神煞宜忌: " + lunars.angelDemon.toString()
        val jrjs = "今日吉神: " + lunars.goodGodName
        val jrxs = "今日凶煞: " + lunars.badGodName
        val yjdd = "宜忌等第: " + lunars.todayLevelName
        val y = "宜: " + lunars.goodThing
        val j = "忌: " + lunars.badThing
        val scjl = "时辰经络: " + lunars.meridians
        val todayHoliday = "今日节日：" + lunars.get_otherLunarHolidays()
        val xc = "星次：" + lunars.todayEastZodiac
        text.text = "$nlsz\n$nl\n$xq\n$bz\n$jrjq\n$xyjq\n$jnjqb\n$jj\n$jrsc\n$scjx\n$pzbj\n$pzbjjj\n$ses\n$esbx" +
                "\n$sxcs\n$jrsh\n$jrlh\n$jrwx\n$ny\n$jgfx\n$jsfw\n$jrts\n$scjl\n$todayHoliday\n$xc" +
                "\n$jrjs\n$jrxs\n$yjdd\n$y\n$j\n$ssyj"
    }
}