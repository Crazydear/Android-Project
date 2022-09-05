package com.hearme.cnlunar

import android.util.Log
import com.hearme.cnlunar.Holidays.legalHolidaysDic
import com.hearme.cnlunar.Holidays.legalLunarHolidaysDic
import com.hearme.cnlunar.Holidays.legalsolarTermsHolidayDic
import com.hearme.cnlunar.Holidays.otherHolidaysList
import com.hearme.cnlunar.Holidays.otherLunarHolidaysList
import com.hearme.cnlunar.config.LEAPMONTH_NUM_BIT
import com.hearme.cnlunar.config.MONTH_DAY_BIT
import com.hearme.cnlunar.config.START_YEAR
import com.hearme.cnlunar.config.badGodDic
import com.hearme.cnlunar.config.chinese12DayGods
import com.hearme.cnlunar.config.chinese12DayOfficers
import com.hearme.cnlunar.config.chinese8Trigrams
import com.hearme.cnlunar.config.chineseZodiacNameList
import com.hearme.cnlunar.config.day8CharThing
import com.hearme.cnlunar.config.directionList
import com.hearme.cnlunar.config.eastZodiacList
import com.hearme.cnlunar.config.fetalGodList
import com.hearme.cnlunar.config.levelDic
import com.hearme.cnlunar.config.luckyGodDirection
import com.hearme.cnlunar.config.lunarDayNameList
import com.hearme.cnlunar.config.lunarMonthData
import com.hearme.cnlunar.config.lunarMonthNameList
import com.hearme.cnlunar.config.lunarNewYearList
import com.hearme.cnlunar.config.mascotGodDirection
import com.hearme.cnlunar.config.meridiansName
import com.hearme.cnlunar.config.moonNobleDirection
import com.hearme.cnlunar.config.officerThings
import com.hearme.cnlunar.config.pengTatooList
import com.hearme.cnlunar.config.solarTermsNameList
import com.hearme.cnlunar.config.sunNobleDirection
import com.hearme.cnlunar.config.the10HeavenlyStems
import com.hearme.cnlunar.config.the10HeavenlyStems5ElementsList
import com.hearme.cnlunar.config.the12EarthlyBranches
import com.hearme.cnlunar.config.the12EarthlyBranches5ElementsList
import com.hearme.cnlunar.config.the28StarsList
import com.hearme.cnlunar.config.the60HeavenlyEarth
import com.hearme.cnlunar.config.theHalf60HeavenlyEarth5ElementsList
import com.hearme.cnlunar.config.thingLevelDic
import com.hearme.cnlunar.config.twohourLuckyTimeList
import com.hearme.cnlunar.config.wealthGodDirection
import com.hearme.cnlunar.config.weekDay
import com.hearme.cnlunar.extension.boolean
import com.hearme.cnlunar.extension.int
import com.hearme.cnlunar.solar24.getTheYearAllSolarTermsList
import com.hearme.cnlunar.tools.day_add
import com.hearme.cnlunar.tools.diff_day_number
import com.hearme.cnlunar.tools.get_lunarYearCN
import com.hearme.cnlunar.tools.rfAdd
import com.hearme.cnlunar.tools.rfRemove
import com.hearme.cnlunar.tools.rlAdd
import java.lang.Math.abs
import java.lang.Math.pow
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/*// 1901~2100年农历数据表
// author: cuba3, github: https://github.com/opn48/pylunar
// base code by Yovey , https://www.jianshu.com/p/8dc0d7ba2c2a
// powered by Late Lee, http://www.latelee.org/python/python-yangli-to-nongli.html//comment-78
// other author:Chen Jian, http://www.cnblogs.com/chjbbs/p/5704326.html
// 数据来源: http://data.weather.gov.hk/gts/time/conversion1_text_c.htm
*/

class Lunar {

    var godType:String
    var date:Calendar
    var twohourNum: Int = 0
    var lunarYear = 0
    var lunarMonth = 1
    var lunarDay = 1
    // 阴历，农历
    var lunarYearCn:String = ""
    var lunarMonthCn:String = ""
    var lunarDayCn:String = ""
    var phaseOfMoona:String = ""    // 月相

    var isLunarLeapMonth = false
    var monthDaysList = ArrayList<Int>()
    var lunarMonthLong = false

    lateinit var thisYearSolarTermsDateList:ArrayList<Pair<Int,Int>>
    lateinit var thisYearSolarTermsDic : Map<String,Pair<Int,Int>>
    var nextSolarNum = 0
    var todaySolarTerms:String = ""
    var nextSolarTerm:String = ""
    var nextSolarTermDate:Pair<Int,Int> = Pair(0,0)
    var nextSolarTermYear:Int = 0
    // 八字部分
    var year8Char = ""
    var month8Char = ""
    var day8Char = ""
    var dayHeavenlyEarthNum = 0
    var yearEarthNum = 0
    var monthEarthNum = 0
    var dayEarthNum = 0
    var yearHeavenNum = 0
    var monthHeavenNum = 0
    var dayHeavenNum = 0
    var seasonType = 0
    var seasonNum = 0
    var lunarSeason = ""
    lateinit var twohour8CharList:List<String>
    var twohour8Char = ""
    var today12DayOfficer = ""
    var today12DayGod = ""
    // 生肖
    var chineseYearZodiac=""
    var chineseZodiacClash = ""
    var zodiacMark6 = ""        // 六合
    var zodiacMark3List = ArrayList<String>()       // 三合
    var zodiacWin = ""
    var zodiacLose = ""

    var weekDayCn = ""
    var starZodiac = ""
    var todayEastZodiac = ""    // 星次
    var today28Star = ""
    var content = ""
    var meridians = ""

    lateinit var goodGodName:ArrayList<String>
    lateinit var badGodName:ArrayList<String>
    var todayLevel = -1
    var todayLevelName = ""
    var isDe = false
    var thingLevelName = ""

    val formatter = SimpleDateFormat("yyyy-MM-dd")

    lateinit var goodThing:ArrayList<String>
    lateinit var badThing:ArrayList<String>
    lateinit var angelDemon:Pair<Pair<ArrayList<String>, ArrayList<String>>, Pair<ArrayList<String>, ArrayList<String>>>

    constructor(date: Calendar = Calendar.getInstance(), godType: String = "8char"){
        this.godType = godType
        this.date = date
        this.twohourNum = (date.get(Calendar.HOUR_OF_DAY) + 1) / 2
        init()
    }
    constructor(year: Int,month:Int,date:Int,godType: String="8char"){
        // 年份在 2090 年之后会报错
        this.godType = godType
        val cal = Calendar.getInstance()
        cal.set(year,month-1,date)
        this.date = cal
        this.twohourNum = (cal.get(Calendar.HOUR_OF_DAY) + 1) / 2
        init()
    }

    fun init(){
        val lunardate = get_lunarDateNum()
        lunarYear = lunardate.first
        lunarMonth = lunardate.second
        lunarDay = lunardate.third

        val lunarCn = get_lunarCN()
        lunarYearCn = lunarCn.first
        lunarMonthCn = lunarCn.second
        lunarDayCn = lunarCn.third
        phaseOfMoona = getPhaseOfMoon()
        todaySolarTerms = get_todaySolarTerms()

        val the8char = get_the8char()       // 八字
        year8Char = the8char.first
        month8Char = the8char.second
        day8Char = the8char.third

        get_earthNum();get_heavenNum();get_season()
        twohour8CharList = get_twohour8CharList()
        twohour8Char = get_twohour8Char()
        get_today12DayOfficer()

        chineseYearZodiac = get_chineseYearZodiac()
        chineseZodiacClash = get_chineseZodiacClash()
        weekDayCn = get_weekDayCn()
//        starZodiac = get_starZodiac()         // 星座，暂时不考虑
        todayEastZodiac = get_eastZodiac()
        thisYearSolarTermsDic = solarTermsNameList.zip(thisYearSolarTermsDateList).toMap()

        today28Star = get_the28Stars()      // 今日28星宿
        angelDemon = get_AngelDemon()
        meridians = meridiansName[twohourNum % 12]
    }

    fun get_lunarMonthCN(lunarMonth:Int):String{
        var lunarMonth = lunarMonthNameList[(lunarMonth - 1) % 12]
        var thisLunarMonthDays = this.monthDaysList[0]
        if (this.isLunarLeapMonth){
            lunarMonth = "闰" + lunarMonth
            thisLunarMonthDays = this.monthDaysList[2]
        }
        lunarMonthLong = thisLunarMonthDays >= 30
        val s =  if (lunarMonthLong) "(大)" else "(小)"
        return lunarMonth + s
    }

    fun get_lunarCN(): Triple<String,String,String> {
        return Triple(get_lunarYearCN(lunarYear),get_lunarMonthCN(lunarMonth), lunarDayNameList[(lunarDay-1) % 30])
    }

    /* 月相 */
    fun getPhaseOfMoon(): String {
        if ( lunarDay - lunarMonthLong.int == 15) return "望月"
        else if (lunarDay == 1) return "朔月"
        else if (lunarDay in 7..9) return "上弦月"
        else if (lunarDay in 22..24) return "下弦月"
        else return ""
    }

    /* 生肖 */
    fun get_chineseYearZodiac(): String {
        return chineseZodiacNameList[(lunarYear - 4) % 12]
    }

    fun get_chineseZodiacClash(): String {
        val zodiacNum = dayEarthNum
        val zodiacClashNum = (zodiacNum + 6) % 12
        zodiacMark6 = chineseZodiacNameList[(25 - zodiacNum) % 12]
        zodiacMark3List = arrayListOf(chineseZodiacNameList[(zodiacNum + 4) % 12],chineseZodiacNameList[(zodiacNum + 8) % 12])
        zodiacWin = chineseZodiacNameList[zodiacNum]
        zodiacLose = chineseZodiacNameList[zodiacClashNum]
        return zodiacWin + "日冲" + zodiacLose
    }

    /* 星期 */
    fun get_weekDayCn(): String {
        /*输出当前日期中文星期几
        :return: 星期三
        */
        return weekDay[date.get(Calendar.DAY_OF_WEEK)-1]
    }

    /* 农历月数 */
    fun getMonthLeapMonthLeapDays(): List<Int> {
        /* 计算阴历月天数
            Arg:
                type(_cn_year) int 2018 数字年份
                type(lunarMonthNameList) int 6 数字阴历月份
            Return:
                int 30或29,该年闰月，闰月天数【
         */
        // 闰几月，该月多少天 传入月份多少天
        var leapMonth = 0
        var leap_day = 0
        var month_day = 0
        val tmp = lunarMonthData[lunarYear-START_YEAR]          // // 获取16进制数据 12-1月份农历日数 0=29天 1=30天
        // 表示获取当前月份的布尔值:指定二进制1（假定真），向左移动月数-1，与当年全年月度数据合并取出2进制位作为判断
        month_day = if ((tmp and (1 shl lunarMonth - 1)).boolean) 30 else 29
        // 闰月
        leapMonth = (tmp shr  LEAPMONTH_NUM_BIT) and  0xf
        if (leapMonth.boolean){
            leap_day = if ((tmp and (1 shl MONTH_DAY_BIT)).boolean) 30 else 29
        }
        monthDaysList = arrayListOf(month_day,leapMonth,leap_day)
        return listOf(month_day,leapMonth,leap_day)
    }

    /* 基础 */
    fun get_lunarDateNum(): Triple<Int, Int, Int> {
        /* 获取数字形式的农历日期
            Args:
                _date = datetime(year, month, day)
            Return:
                lunarYear, lunarMonth, lunarDay
                返回的月份，高4bit为闰月月份，低4bit为其它正常月份
        */
        lunarYear = date.get(Calendar.YEAR)
        val _code_year = lunarNewYearList[lunarYear - START_YEAR]
        """ 获取当前日期与当年春节的差日 """
        val chunjie = SimpleDateFormat("$lunarYear-${((_code_year shr 5) and 0x3)}-${((_code_year shr 0) and 0x1f)}")
        var _span_days = diff_day_number(date,chunjie.toLocalizedPattern())
        if (_span_days >= 0) {
            """ 新年后推算日期，差日依序减月份天数，直到不足一个月，剪的次数为月数，剩余部分为日数 """
            """ 先获取闰月 """
            //  可从迭代递归修改为数学加和 待优化
            var (_monthDays, _leap_month, _leap_day) = getMonthLeapMonthLeapDays()
            while (_span_days >= _monthDays) {
                """ 获取当前月份天数，从差日中扣除 """
                _span_days -= _monthDays
                if (lunarMonth == _leap_month) {
                    """ 如果当月还是闰月 """
                    _monthDays = _leap_day
                    if (_span_days < _monthDays) {
                        """ 指定日期在闰月中 ???"""
                        // lunarMonth = _leap_month
                        isLunarLeapMonth = true
                        break
                    }
                    """ 否则扣除闰月天数，月份加一 """
                    _span_days -= _monthDays
                }
                lunarMonth += 1
                _monthDays = getMonthLeapMonthLeapDays()[0]
            }
            lunarDay += _span_days.toInt()
            return Triple(lunarYear, lunarMonth, lunarDay)
        }else{
            """ 新年前倒推去年日期 """
            lunarMonth = 12
            lunarYear -= 1
            var(_monthDays, _leap_month, _leap_day) = getMonthLeapMonthLeapDays()
            while (abs(_span_days) > _monthDays){
                _span_days += _monthDays
                lunarMonth -= 1
                if (lunarMonth == _leap_month) {
                    _monthDays = _leap_day
                    //// 指定日期在闰月中
                    if (abs(_span_days) <= _monthDays) {
                        isLunarLeapMonth = true
                        //// lunarMonth = lunarMonth | (_leap_month << 4)
                        break
                    }
                    _span_days += _monthDays
                }
                _monthDays = getMonthLeapMonthLeapDays()[0]
            }
            lunarDay += (_monthDays + _span_days.toInt())  // // 从月份总数中倒扣 得到天数
            return Triple(lunarYear,lunarMonth,lunarDay)
        }
    }

    /* 24 节气部分 */
    fun getSolarTermsDateList(year:Int): ArrayList<Pair<Int,Int>> {
        val solarTermsList =  getTheYearAllSolarTermsList(year)
        val solarTermsDateList = ArrayList<Pair<Int,Int>>()
        for (i in 0..solarTermsList.size-1){
            val day = solarTermsList[i]
            val month = i / 2 + 1
            solarTermsDateList.add(Pair(month,day))
        }
        return solarTermsDateList
    }

    fun getNextNum(findDate:Pair<Int,Int>, solarTermsDateList:ArrayList<Pair<Int,Int>>,remainder:Int=24): Int {
        val tempList = ArrayList<Pair<Int,Int>>()
        for (item in solarTermsDateList){
            if (item.first < findDate.first){
                tempList.add(item)
            }else if (item.first == findDate.first){
                if (item.second <= findDate.second){
                    tempList.add(item)
                }
            }
        }
        val nextSolarNum = tempList.size % remainder
        return nextSolarNum
    }

    /* 获取今日节气 */
    fun get_todaySolarTerms(): String {
        /* :return:节气 */
        var year = date.get(Calendar.YEAR)
        var todaySolarTerm = "无"
        var solarTermsDateList = getSolarTermsDateList(year)
        thisYearSolarTermsDateList = solarTermsDateList
        val findDate = Pair(date.get(Calendar.MONTH)+1, date.get(Calendar.DATE))
        nextSolarNum = getNextNum(findDate, solarTermsDateList)
        if (findDate in solarTermsDateList){
            todaySolarTerm = solarTermsNameList[solarTermsDateList.indexOf(findDate)]
        } else {
            todaySolarTerm = "无"
        }
        // 次年节气
        if (findDate.first == solarTermsDateList.last().first){
            if (findDate.second >= solarTermsDateList.last().second){
                year += 1
                year += 1
                solarTermsDateList = getSolarTermsDateList(year)
            }
        }
        nextSolarTerm = solarTermsNameList[nextSolarNum]
        nextSolarTermDate = solarTermsDateList[nextSolarNum]
        nextSolarTermYear = year
        return todaySolarTerm
    }

    /* 星次 */
    fun get_eastZodiac(): String {
        val todayEastZodiac = eastZodiacList[(solarTermsNameList.indexOf(nextSolarTerm) - 1) % 24 / 2]
        return todayEastZodiac
    }

    /* 八字部分 */
    fun get_year8Char(): String {
        val str = the10HeavenlyStems[(lunarYear - 4) % 10] + the12EarthlyBranches[(lunarYear - 4) % 12]
        return str
    }

    /* 月八字与节气相关 */
    fun get_month8Char(): String {
        /* findDate = (date.month, date.day)
           solarTermsDateList = getSolarTermsDateList(date.year)
        */
        var nextNum = nextSolarNum
        // 2019年正月为丙寅月
        if (nextNum == 0){
            if (date.get(Calendar.MONTH) +1 == 12){
                nextNum = 24
            }
        }
        val apartNum = (nextNum + 1) / 2
        var indexs = ((date.get(Calendar.YEAR) - 2019) * 12 + apartNum)
        while (indexs<0) indexs += 60
        // (year-2019)*12+apartNum每年固定差12个月回到第N年月柱，2019小寒甲子，加上当前过了几个节气除以2+(nextNum-1)//2，减去1
        month8Char = the60HeavenlyEarth[indexs % 60]
        return month8Char
    }

    fun get_day8Char(): String {
        var apart = diff_day_number(date,"2019-1-29")
        var baseNum = the60HeavenlyEarth.indexOf("丙寅")
        // 超过23点算第二天，为防止溢出，在baseNum上操作+1
        if (twohourNum == 12){
            baseNum += 1
        }
        dayHeavenlyEarthNum = (apart.toInt() + baseNum) % 60
        if (dayHeavenlyEarthNum<0) dayHeavenlyEarthNum += 60
        return the60HeavenlyEarth[dayHeavenlyEarthNum]
    }

    fun get_twohour8CharList(): List<String> {
        // 北京时间离长安时间差1小时，一天24小时横跨13个时辰,清单双循环
        val begin = (the60HeavenlyEarth.indexOf(day8Char) * 12) % 60
        return (the60HeavenlyEarth + the60HeavenlyEarth).slice(begin..begin+12)
    }

    fun get_twohour8Char(): String {
        return twohour8CharList[twohourNum % 12]
    }

    fun get_the8char(): Triple<String, String, String> {
        return Triple(get_year8Char(), get_month8Char(), get_day8Char())
    }

    fun get_earthNum(): Triple<Int, Int, Int> {
        yearEarthNum = the12EarthlyBranches.indexOf(year8Char[1].toString())
        monthEarthNum = the12EarthlyBranches.indexOf(month8Char[1].toString())
        dayEarthNum = the12EarthlyBranches.indexOf(day8Char[1].toString())
        return Triple(yearEarthNum, monthEarthNum,dayEarthNum)
    }

    fun get_heavenNum(): Triple<Int, Int, Int> {
        yearHeavenNum = the10HeavenlyStems.indexOf(year8Char[0].toString())
        monthHeavenNum = the10HeavenlyStems.indexOf(month8Char[0].toString())
        dayHeavenNum = the10HeavenlyStems.indexOf(day8Char[0].toString())
        return Triple(yearHeavenNum,monthHeavenNum,dayHeavenNum)
    }

    /* 月令 */
    fun get_season(){
        seasonType = monthEarthNum % 3
        seasonNum = ((monthEarthNum - 2) % 12) / 3
        lunarSeason = "仲季孟"[seasonType].toString() + "春夏秋冬"[seasonNum].toString()
    }

    /* 星座 */
//    fun get_starZodiac(){
//        return starZodiacName[len(list(filter(lambda y: y <= (date.month, date.day), starZodiacDate))) % 12]
//    }

    /* 节日 */
    fun get_legalHolidays():String{
        var temp = ""
        if (todaySolarTerms in legalsolarTermsHolidayDic){
            temp += legalsolarTermsHolidayDic[todaySolarTerms] + " "
        }
        if (Pair(date.get(Calendar.MONTH)+1, date.get(Calendar.DATE)) in legalHolidaysDic.keys){
            temp += legalHolidaysDic[Pair(date.get(Calendar.MONTH)+1, date.get(Calendar.DATE))] + " "
        }
        if (!(lunarMonth > 12)) {
            if (Pair(lunarMonth, lunarDay) in legalLunarHolidaysDic.keys){
                temp += legalLunarHolidaysDic[Pair(lunarMonth, lunarDay)]
            }
        }
        return temp.replace("\\s".toRegex(), "").replace(" ", ",")
    }

    fun get_otherHolidays(): ArrayList<String> {
        val tempList = ArrayList<String>()
        val m = date.get(Calendar.MONTH) + 1
        val d = date.get(Calendar.DATE)
        val holidayDic = otherHolidaysList[m - 1]
        if (d in holidayDic.keys){
            tempList.add(holidayDic[d]!!)
        }
        return tempList
    }

    fun get_otherLunarHolidays(): String {
        if (!(lunarMonth > 12)) {
            val holidayDic = otherLunarHolidaysList[lunarMonth - 1]
            if (lunarDay in holidayDic){
                return holidayDic[lunarDay]!!
            }
        }
        return ""
    }

    /* 彭祖百忌 */
    fun get_pengTaboo(long:Int=9,dilimit:String=" "): String {
        return "${pengTatooList[dayHeavenNum].subSequence(0,long)}$dilimit${pengTatooList[dayEarthNum + 10].subSequence(0,long)}"
    }

    /* 建除十二神，《淮南子》曰：正月建寅，则寅为建，卯为除，辰为满，巳为平，主生；
    午为定，未为执，主陷；申为破，主衡；酉为危，主杓；戍为成，主小德；
    亥为收，主大备；子为开，主太阳；丑为闭，主太阴。*/
    fun get_today12DayOfficer(): Triple<String, String, String> {
        var men: Int = if (godType == "cnlunar"){
            // 使用农历月份与八字日柱算神煞（辨方书文字） 农历(1-12)，-1改编号，[0-11]，+2位移，% 12 防止溢出
            val lmn = lunarMonth
            (lmn - 1 + 2) % 12
        }else {
            // 使用八字月柱与八字日柱算神煞（辨方书配图和部分文字）
            monthEarthNum
        }
        // thisMonthStartGodNum = (lunarMonth -1 + 2) % 12
        val thisMonthStartGodNum = men % 12
        var apartNum = dayEarthNum - thisMonthStartGodNum
        if (apartNum<0) apartNum += 12
        today12DayOfficer = chinese12DayOfficers[apartNum % 12].toString()
        /* 青龙定位口诀：子午寻申位，丑未戌上亲；寅申居子中，卯酉起于寅；辰戌龙位上，巳亥午中寻。
         [申戌子寅辰午]
         十二神凶吉口诀：道远几时通达，路遥何日还乡
         辶为吉神(0, 1, 4, 5, 7, 10)
         为黄道吉日*/
        var eclipticGodNum = (dayEarthNum - listOf(8, 10, 0, 2, 4, 6, 8, 10, 0, 2, 4, 6)[men]) % 12
        if (eclipticGodNum<0) eclipticGodNum += 12
        today12DayGod = chinese12DayGods[eclipticGodNum % 12]
        val dayName = if (eclipticGodNum in listOf(0, 1, 4, 5, 7, 10)) "黄道日" else "黑道日"
        return Triple(today12DayOfficer, today12DayGod, dayName)
    }

    /* 八字与五行 */
    fun get_the28Stars():String{
        var apart = diff_day_number(date,"2019-1-17")
        while (apart<0) apart += 28
        return the28StarsList[apart.toInt() % 28]
    }

    /* 纳音 */
    fun get_nayin(): String {
        return theHalf60HeavenlyEarth5ElementsList[the60HeavenlyEarth.indexOf(day8Char) / 2]
    }

    fun get_today5Elements(): List<Any> {
        val nayin = get_nayin()
        val tempList = listOf("天干", day8Char[0], "属" + the10HeavenlyStems5ElementsList[dayHeavenNum],
            "地支", day8Char[1], "属" + the12EarthlyBranches5ElementsList[dayEarthNum],
            "纳音", nayin.last(), "属" + nayin.last(),
            "廿八宿", today28Star[0], "宿",
            "十二神", today12DayOfficer, "日"
        )
        return tempList
    }

    /* 九宫飞星 */
    fun get_the9FlyStar(): ArrayList<String> {
        val apartNum = diff_day_number(date,"2019-1-17")
        val startNumList = listOf(7, 3, 5, 6, 8, 1, 2, 4, 9)
        val flyStarList = ArrayList<String>()
            for (i in startNumList){
                var temp = (i - 1 - apartNum) % 9 + 1
                if (temp <1) temp += 9
            flyStarList.add(temp.toString())
        }
        return flyStarList
    }

    /*喜神方位*/
    fun get_luckyGodsDirection(): List<String> {
        val todayNum = dayHeavenNum
        val direction = listOf(
            "财神" + directionList[chinese8Trigrams.indexOf(wealthGodDirection[todayNum])],
            "喜神" + directionList[chinese8Trigrams.indexOf(luckyGodDirection[todayNum])],
            "福神" + directionList[chinese8Trigrams.indexOf(mascotGodDirection[todayNum])],
            "阳贵" + directionList[chinese8Trigrams.indexOf(sunNobleDirection[todayNum])],
            "阴贵" + directionList[chinese8Trigrams.indexOf(moonNobleDirection[todayNum])],
        )
        return direction
    }

    /* 今日胎神 */
    fun get_fetalGod(): String {
        return fetalGodList[the60HeavenlyEarth.indexOf(day8Char)]
    }

    // 每日时辰凶吉（子时分前后）
    fun get_twohourLuckyList(): List<String> {
        fun tmp2List(tmp:Int): ArrayList<String> {
            val lisg = ArrayList<String>()
            for(i in 1..12){
                if ((tmp and pow(2.0,12-i.toDouble()).toInt() > 0)){
                    lisg.add("凶")
                }else lisg.add("吉")
            }
            return lisg
        }
        val todayNum = dayHeavenlyEarthNum
        val tomorrowNum = (dayHeavenlyEarthNum + 1) % 60
        val outputList = (tmp2List(twohourLuckyTimeList[todayNum]) + tmp2List(twohourLuckyTimeList[tomorrowNum]))
        return outputList.subList(0,13)
    }

    /* 宜忌等第表 计算凶吉 */
    fun getTodayThingLevel(): Int {
        /*
        :return thingLevel
        */
        val todayAllGodName = goodGodName + badGodName + arrayListOf<String>(today12DayOfficer + "日")
        var l = -1
        for (gnoItem in todayAllGodName){
            if (gnoItem in badGodDic.keys){
                for (item in badGodDic[gnoItem]!!){
                    if (month8Char[1] in item.first){
                        for (godname in item.second){
                            if ((godname in todayAllGodName) and (item.third > l)){
                                l = item.third
                                break
                            }
                        }
                    }
                }
            }
        }
        /* 今日宜忌等第 */
        todayLevel = l
        todayLevelName = levelDic[l]!!
        for (i in goodGodName){
            if (i in listOf("岁德", "岁德合", "月德", "月德合", "天德", "天德合")){
                isDe = true
                break
            }
        }
        var thingLevel = when (l){
            5 -> 3                      /* 下下：凶叠大凶，遇德亦诸事皆忌；卯酉月 灾煞遇 月破、月厌  月厌遇灾煞、月破 */
            4 -> {if (isDe) 2 else 3}   /* 下：凶又逢凶，遇德从忌不从宜，不遇诸事皆忌；*/
            3 -> {if (isDe) 1 else 2}   /* 中次：凶胜于吉，遇德从宜亦从忌，不遇从忌不从宜；*/
            2 -> {if (isDe) 0 else 2}   /* 中：吉不抵凶，遇德从宜不从忌，不遇从忌不从宜；*/
            1 -> {if (isDe) 0 else 1}   /* 上次：吉足抵凶，遇德从宜不从忌，不遇从宜亦从忌；*/
            0 -> 0                      /* 上：吉足胜凶，从宜不从忌;*/
            else -> 1                   /* 无，例外 从宜不从忌 */
        }
        thingLevelName = thingLevelDic[thingLevel]!!
        return thingLevel
    }

    fun  get_AngelDemon(): Pair<Pair<ArrayList<String>, ArrayList<String>>, Pair<ArrayList<String>, ArrayList<String>>> {
        /*
        the10HeavenlyStems =["甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"]
        the12EarthlyBranches = ["子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"]
        相冲+6
        四绝日：一切用事皆忌之，立春，立夏，立秋，立冬前一日。忌出行、赴任、嫁娶、进人、迁移、开市、立券、祭祀
        四离日：春分、秋分、夏至、冬至前一天。日值四离，大事勿用
        杨公13忌日：忌开张、动工、嫁娶、签订合同
        红纱日、正红纱日：四孟金鸡（酉）四仲蛇（巳），四季丑日是红纱，惟有孟仲合吉用 ，季月逢之俱不佳（正红纱日）
        凤凰日、麒麟日：凤凰压朱雀，麒麟制白虎，克制朱雀白虎中效果。春井，夏尾，秋牛，冬壁，是麒麟日，春危，夏昴，秋胃，冬毕是凤凰日
        月德、月德合:申子辰三合，阳水为壬，月德合丁；巳酉丑三合，阳金为庚，月德合乙；寅午戌三合，阳火为丙，月德合辛；亥卯未三合，阳木为甲，月德合己
        天德、天德合:《子平赋》说：印绶得同天德，官刑不至，至老无灾.
        岁德、岁德合:《协纪辨方书·义例一·岁德》：曾门经曰：岁德者，岁中德神也。https://www.jianshu.com/p/ec0432f31060
        月恩:正月逢丙是月恩，二月见丁三庚真，四月己上五月戊，六辛七壬八癸成，九月庚上十月乙，冬月甲上腊月辛。
        天恩:四季何时是天恩，甲子乙丑丙寅建。丁卯戊辰兼己卯，庚辰辛巳壬午言，癸未隔求己酉日，庚戌辛亥亦同联，壬子癸丑无差误，此是天恩吉日传
        */
        var gbDic = GbDic(arrayListOf(), arrayListOf(),
            officerThings[today12DayOfficer]!!.first,
            officerThings[today12DayOfficer]!!.second
        )
        val tmd = day_add(formatter.format(date.time), 1)
        var t4l = arrayListOf<Pair<Int, Int>>()          // 四个节气的日期
        for (i in listOf("春分", "夏至", "秋分", "冬至"))
            t4l.add(thisYearSolarTermsDic[i]!!)
        var t4j = arrayListOf<Pair<Int, Int>>()          // 四个节气的日期
        for (i in listOf("立春", "立夏", "立秋", "立冬"))
            t4j.add(thisYearSolarTermsDic[i]!!)
        val twys = t4j[getNextNum(Pair(tmd[0], tmd[1]), t4j, 4)]
        val s = today28Star
        val o = today12DayOfficer
        val d = day8Char
        val den = dayEarthNum
        val dhen = dayHeavenlyEarthNum
        val sn = seasonNum                              // 季节
        // st=seasonType
        val yhn = yearHeavenNum
        val yen = yearEarthNum
        val ldn = lunarDay
        val lmn = lunarMonth
        //# 使用农历月份与八字日柱算神煞（辨方书文字） 农历(1-12)，-1改编号，[0-11]，+2位移，% 12 防止溢出
        //# 使用八字月柱与八字日柱算神煞（辨方书配图和部分文字）
        val men = if (godType == "cnlunar") (lmn - 1 + 2) % 12 else monthEarthNum
        //# item参数规则，（name,当日判断结果,判断规则,宜事,忌事）
        for (i in day8CharThing.keys) {
            if (i in d) {
                gbDic.goodThing!!.plus(day8CharThing[i]!!.first)
                gbDic.badThing!!.plus(day8CharThing[i]!!.second)
            }
        }
        /* 插入卷十一拆解后遗留内容
            节气间差类
            [("小寒", 0), ("大寒", 1), ("立春", 2), ("雨水", 3), ("惊蛰", 4), ("春分", 5), ("清明", 6), ("谷雨", 7), ("立夏", 8), ("小满", 9), ("芒种", 10), ("夏至", 11), ("小暑", 12), ("大暑", 13), ("立秋", 14), ("处暑", 15), ("白露", 16), ("秋分", 17), ("寒露", 18), ("霜降", 19), ("立冬", 20), ("小雪", 21), ("大雪", 22), ("冬至", 23)]
            雨水后立夏前执日、危日、收日 宜 取鱼 */
        if ((nextSolarNum in 4..8) and (o in listOf("执", "危", "收"))) {
            gbDic.goodThing = rfAdd(gbDic.goodThing, arrayListOf("取鱼"))
        }
        //# 霜降后立春前执日、危日、收日 宜 畋猎
        if ((nextSolarNum in 20..23) and (nextSolarNum in 0..2) and (o in listOf("执", "危", "收"))) {
            gbDic.goodThing = rfAdd(gbDic.goodThing, listOf("畋猎"))
        }
        //# 立冬后立春前危日 午日 申日 宜 伐木
        if ((nextSolarNum in 21..23) and (nextSolarNum in 0..2) and ((o in listOf("危"))
                    or (d in listOf("午", "申")))) {
            gbDic.goodThing = rfAdd(gbDic.goodThing, listOf("伐木"))
        }
        //#   每月一日 六日 十五 十九日 二十一日 二十三日 忌 整手足甲
        if (ldn in listOf(1, 6, 15, 19, 21, 23)) {
            gbDic.badThing = rfAdd(gbDic.badThing, listOf("整手足甲"))
        }
        //# 每月十二日 十五日 忌 整容剃头
        if (ldn in listOf(12, 15))
            gbDic.badThing = rfAdd(gbDic.badThing, listOf("整容", "剃头"))
        //# 每月十五日 朔弦望月 忌 求医疗病
        if ((ldn in listOf(15)) or (phaseOfMoona != "")) {
            gbDic.badThing = rfAdd(gbDic.badThing, listOf("求医疗病"))
        }
        //# 由于正月建寅，men参数使用排序是从子开始，所以对照书籍需要将循环八字列向右移两位，也就是映射正月的是在第三个字
        val angels = get_angels(yhn,d, men, s, sn, den, dhen)
        //# 配合angel、demon的数据结构的吉神凶神筛选
        val demons = get_demons(den,yen,men,d,ldn,lmn,sn,tmd,t4j,t4l,nextSolarTermYear,twys,date)

        fun getTodayGoodBadThing(dic: GbDic): GbDic {
            for (godItem in angels) {
                // # print(x, x[1] , x[2]) 输出当日判断结果x[1]，看x[1]是否落在判断范围x[2]里面
                if (godItem.contece) {
                    dic.goodName = rlAdd(dic.goodName, arrayListOf(godItem.a))
                    dic.goodThing = rlAdd(dic.goodThing, godItem.d)
                    dic.badThing = rlAdd(dic.badThing, godItem.e)
                }
            }
            for (godItem in demons) {
                if (godItem.contece) {
                    dic.badName = rlAdd(dic.badName, arrayListOf(godItem.a))
                    dic.goodThing = rlAdd(dic.goodThing, godItem.d)
                    dic.badThing = rlAdd(dic.badThing, godItem.e)
                }
            }
            //# 宜列、忌列分别去重
            dic.goodThing = ArrayList(dic.goodThing.toSortedSet())
            dic.badThing = ArrayList(dic.badThing.toSortedSet())
            return dic
        }
        gbDic = getTodayGoodBadThing(gbDic)
        goodGodName = gbDic.goodName
        badGodName = gbDic.badName

        /*# 第一方案：《钦定协纪辨方书》古书影印版，宜忌等第表
        # 凡铺注《万年历》、《通书》，先依用事次第察其所宜忌之日，于某日下注宜某事，某日下注忌某事，次按宜忌，较量其凶吉之轻重，以定去取。
        # 从忌亦从宜*/
        fun badDrewGood(dic: GbDic): GbDic {
            for (removeThing in dic.goodThing.toSortedSet().intersect(dic.badThing.toSortedSet())) {
                dic.goodThing.remove(removeThing)
                dic.badThing.remove(removeThing)
            }
            return dic
        }

        //# 从忌不从宜
        fun badOppressGood(dic: GbDic): GbDic {
            for (removeThing in (dic.goodThing.toSortedSet()
                .intersect(dic.badThing.toSortedSet()))) {
                dic.goodThing.remove(removeThing)
            }
            return dic
        }

        //# 从宜不从忌
        fun goodOppressBad(dic: GbDic): GbDic {
            for (removeThing in dic.goodThing.toSortedSet().intersect(dic.badThing.toSortedSet())) {
                dic.badThing.remove(removeThing)
            }
            return dic
        }

        //# 诸事不宜
        fun nothingGood(dic: GbDic): GbDic {
            dic.goodThing = arrayListOf("诸事不宜")
            dic.badThing = arrayListOf("诸事不宜")
            return dic
        }
        // # 今日凶吉判断
        val thingLevel = getTodayThingLevel()
        //# 0:'从宜不从忌',1:'从宜亦从忌',2:'从忌不从宜',3:'诸事皆忌'
        gbDic = when (thingLevel) {
            3 -> nothingGood(gbDic)
            2 -> badOppressGood(gbDic)
            1 -> badDrewGood(gbDic)
            else -> goodOppressBad(gbDic)
        }
        goodThing = gbDic.goodThing
        badThing = gbDic.badThing
        // # 遇德犹忌之事
        var deIsBadThingDic = mutableMapOf<String, List<String>>()
        for (i in angels.subList(0, 6)) {
            deIsBadThingDic.put(i.a, i.e)
        }
        var deIsBadThing = arrayListOf<String>()
        if (isDe) {
            for (i in goodGodName) {
                if (i in deIsBadThingDic) {
                    deIsBadThing = rlAdd(deIsBadThing, deIsBadThingDic[i])
                }
            }
        }
        deIsBadThing = ArrayList(deIsBadThing.toSortedSet())
        if (thingLevel != 3) {
            //# 凡宜宣政事，布政事之日，只注宜宣政事。
            if (("宣政事" in goodThing) and ("布政事" in goodThing)) {
                goodThing.remove("布政事")
            }
            //# 凡宜营建宫室、修宫室之日，只注宜营建宫室。
            if (("营建宫室" in goodThing) and ("修宫室" in goodThing))
                goodThing.remove("修宫室")
            //# 凡德合、赦愿、月恩、四相、时德等日，不注忌进人口、安床、经络、酝酿、开市、立券、交易、纳财、开仓库、出货财。如遇德犹忌，及从忌不从宜之日，则仍注忌。
            var temp = false
            for (i in goodGodName) {
                if (i in listOf("岁德合", "月德合", "天德合", "天赦", "天愿", "月恩", "四相", "时德")) {
                    temp = true
                    break
                }
                if (temp) {
                    //# 如遇德犹忌，及从忌不从宜之日，则仍注忌。
                    if (!(i in deIsBadThing) or (thingLevel != 2)) {
                        badThing = rfRemove(
                            badThing,
                            listOf("进人口", "安床", "经络", "酝酿", "开市", "立券交易", "纳财", "开仓库", "出货财")
                        )
                    }
                }
            }
            //# 凡天狗寅日忌祭祀，不注宜求福、祈嗣。
            if (("天狗" in goodGodName) or ("寅" in d)) {
                badThing = rfAdd(badThing, addList = listOf("祭祀"))
                goodThing = rfRemove(goodThing, removeList = listOf("祭祀"))
                goodThing = rfRemove(goodThing, removeList = listOf("求福", "祈嗣"))
            }
            //# 凡卯日忌穿井，不注宜开渠。壬日忌开渠，不注宜穿井。
            if ("卯" in d) {
                badThing = rfAdd(badThing, listOf("穿井"))
                goodThing = rfRemove(goodThing, listOf("穿井"))
                goodThing = rfRemove(goodThing, listOf("开渠"))
            }
            if ("壬" in d) {
                badThing = rfAdd(badThing, listOf("开渠"))
                goodThing = rfRemove(goodThing, listOf("开渠"))
                goodThing = rfRemove(goodThing, listOf("穿井"))
            }
            //# 凡巳日忌出行，不注宜出师、遣使。
            if ("巳" in d) {
                badThing = rfAdd(badThing, listOf("出行"))
                goodThing = rfRemove(goodThing, listOf("出行"))
                goodThing = rfRemove(goodThing, listOf("出师", "遣使"))
            }
            //# 凡酉日忌宴会，亦不注宜庆赐、赏贺。
            if ("酉" in d) {
                if (!("宴会" in badThing))
                    badThing = rfAdd(badThing, listOf("宴会"))
                goodThing = rfRemove(goodThing, listOf("宴会"))
                goodThing = rfRemove(goodThing, listOf("庆赐", "赏贺"))
            }
            //# 凡丁日忌剃头，亦不注宜整容。
            if ("丁" in d) {
                badThing = rfAdd(badThing, listOf("剃头"))
                goodThing = rfRemove(goodThing, listOf("剃头"))
                goodThing = rfRemove(goodThing, listOf("整容"))
            }
            //# 凡吉足胜凶，从宜不从忌者，如遇德犹忌之事，则仍注忌。
            if ((todayLevel == 0) and (thingLevel == 0))
                badThing = rfAdd(badThing, addList = deIsBadThing)
            //# 凡吉凶相抵，不注宜亦不注忌者，如遇德犹忌之事，则仍注忌。
            if (todayLevel == 1) {
                badThing = rfAdd(badThing, addList = deIsBadThing)
                //# 凡吉凶相抵，不注忌祈福，亦不注忌求嗣。
                if (!("祈福" in badThing)) {
                    badThing = rfRemove(badThing, listOf("求嗣"))
                }
                //# 凡吉凶相抵，不注忌结婚姻，亦不注忌冠带、纳采问名、嫁娶、进人口，如遇德犹忌之日则仍注忌。
                if (!("结婚姻" in badThing) and !isDe) {
                    badThing = rfRemove(badThing, listOf("冠带", "纳采问名", "嫁娶", "进人口"))
                }

                //# 凡吉凶相抵，不注忌嫁娶，亦不注忌冠带、结婚姻、纳采问名、进人口、搬移、安床，如遇德犹忌之日，则仍注忌。
                if (!("嫁娶" in badThing) and !isDe) {
                    //# 遇不将而不注忌嫁娶者，亦仍注忌。
                    if ("不将" in goodGodName) {
                    } else {
                        badThing =
                            rfRemove(badThing, listOf("冠带", "纳采问名", "结婚姻", "进人口", "搬移", "安床"))
                    }
                }
            }
            //# 遇亥日、厌对、八专、四忌、四穷而仍注忌嫁娶者，只注所忌之事，其不忌者仍不注忌。【未妥善解决】
            if ("亥" in d) {
                badThing = rfAdd(badThing, listOf("嫁娶"))
            }
            //# 凡吉凶相抵，不注忌搬移，亦不注忌安床。不注忌安床，亦不注忌搬移。如遇德犹忌之日，则仍注忌。
            if ((todayLevel == 1) and !isDe) {
                if (!("搬移" in badThing))
                    badThing = rfRemove(badThing, listOf("安床"))
                if (!("安床" in badThing))
                    badThing = rfRemove(badThing, listOf("搬移"))
                //# 凡吉凶相抵，不注忌解除，亦不注忌整容、剃头、整手足甲。如遇德犹忌之日，则仍注忌。
                if (!("解除" in badThing))
                    badThing = rfRemove(badThing, listOf("整容", "剃头", "整手足甲"))
                //# 凡吉凶相抵，不注忌修造动土、竖柱上梁，亦不注忌修宫室、缮城郭、筑提防、修仓库、鼓铸、苫盖、修置产室、开渠穿井、安碓硙、补垣塞穴、修饰垣墙、平治道涂、破屋坏垣。如遇德犹忌之日，则仍注忌。
                if (!("修造" in badThing) or !("竖柱上梁" in badThing))
                    badThing = rfRemove(
                        badThing,
                        listOf(
                            "修宫室", "缮城郭", "整手足甲", "筑提", "修仓库", "鼓铸", "苫盖", "修置产室", "开渠穿井",
                            "安碓硙", "补垣塞穴", "修饰垣墙", "平治道涂", "破屋坏垣"
                        )
                    )
            }
            //# 凡吉凶相抵，不注忌开市，亦不注忌立券交易、纳财。不注忌纳财，亦不注忌开市、立券交易。不注忌立券交易，亦不注忌开市、纳财。
            //# 凡吉凶相抵，不注忌开市、立券交易，亦不注忌开仓库、出货财。
            if (todayLevel == 1) {
                if (!("开市" in badThing))
                    badThing = rfRemove(badThing, listOf("立券交易", "纳财", "开仓库", "出货财"))
                if (!("纳财" in badThing))
                    badThing = rfRemove(badThing, listOf("立券交易", "开市"))
                if (!("立券交易" in badThing))
                    badThing = rfRemove(badThing, listOf("纳财", "开市", "开仓库", "出货财"))
            }
            //# 如遇专忌之日，则仍注忌。【未妥善解决】
            //# 凡吉凶相抵，不注忌牧养，亦不注忌纳畜。不注忌纳畜，亦不注忌牧养。
            if (todayLevel == 1) {
                if (!("牧养" in badThing))
                    badThing = rfRemove(badThing, listOf("纳畜"))
                if (!("纳畜" in badThing))
                    badThing = rfRemove(badThing, listOf("牧养"))
                //# 凡吉凶相抵，有宜安葬不注忌启攒，有宜启攒不注忌安葬。
                if ("安葬" in goodThing)
                    badThing = rfRemove(badThing, listOf("启攒"))
                if ("启攒" in goodThing)
                    badThing = rfRemove(badThing, listOf("安葬"))
            }
            //# 凡忌诏命公卿、招贤，不注宜施恩、封拜、举正直、袭爵受封。    #本版本无 封拜 袭爵受封
            if (("诏命公卿" in badThing) or ("招贤" in badThing))
                goodThing = rfRemove(goodThing, listOf("施恩", "举正直"))
            //# 凡忌施恩、封拜、举正直、袭爵受封，亦不注宜诏命公卿、招贤。
            if (("施恩" in badThing) or ("举正直" in badThing))
                goodThing = rfRemove(goodThing, listOf("诏命公卿", "招贤"))
            //# 凡宜宣政事之日遇往亡则改宣为布。
            if (("宣政事" in goodThing) and ("往亡" in badGodName)) {
                goodThing.remove("宣政事")
                goodThing = rfAdd(goodThing, listOf("布政事"))
            }
            // # 凡月厌忌行幸、上官，不注宜颁诏、施恩封拜、诏命公卿、招贤、举正直。遇宜宣政事之日，则改宣为布。
            if ("月厌" in badGodName) {
                goodThing = rfRemove(goodThing, listOf("颁诏", "施恩", "招贤", "举正直", "宣政事"))
                goodThing = rfAdd(goodThing, listOf("布政事"))
                //# 凡土府、土符、地囊，只注忌补垣，亦不注宜塞穴。
                badThing = rfAdd(badThing, listOf("补垣"))
                if (("土府" in badGodName) or ("土符" in badGodName) or ("地囊" in badGodName))
                    goodThing = rfRemove(goodThing, listOf("塞穴"))
            }
            //# 凡开日，不注宜破土、安葬、启攒，亦不注忌。遇忌则注。
            if ("开" in today12DayOfficer)
                goodThing = rfRemove(goodThing, listOf("破土", "安葬", "启攒"))
            //# 凡四忌、四穷只忌安葬。如遇鸣吠、鸣吠对亦不注宜破土、启攒。
            if (("四忌" in badGodName) or ("四忌" in badGodName)) {
                badThing = rfAdd(badThing, listOf("安葬"))
                goodThing = rfRemove(goodThing, listOf("破土", "启攒"))
            }
            if (("鸣吠" in badGodName) or ("鸣吠对" in badGodName))
                goodThing = rfRemove(goodThing, listOf("破土", "启攒"))
            //# 凡天吏、大时不以死败论者，遇四废、岁薄、逐阵仍以死败论。
            //# 凡岁薄、逐阵日所宜事，照月厌所忌删，所忌仍从本日。、
            //# 二月甲戌、四月丙申、六月甲子、七月戊申、八月庚辰、九月辛卯、十月甲子、十二月甲子，德和与赦、愿所汇之辰，诸事不忌。
            if (listOf("空", "甲戌", "空", "丙申", "空", "甲子", "戊申",
                    "庚辰", "辛卯", "甲子", "空", "甲子")[lmn - 1] in d)
                badThing = arrayListOf("诸事不忌")
            if (((goodGodName.toSortedSet().intersect(listOf("岁德合", "月德合", "天德合")).size) and
                        (goodGodName.toSortedSet().intersect(listOf("天赦", "天愿")).size)).boolean
            ) {
                badThing = arrayListOf("诸事不忌")
            }
        }
        //# 书中未明注忌不注宜
        var rmThing = arrayListOf<String>("")
        for (thing in badThing) {
            if (thing in goodThing)
                rmThing.add(thing)
        }

        if ((rmThing.size == 1) and ("诸事" in rmThing[0])) {
        } else {
            goodThing = rfRemove(goodThing, removeList = rmThing)
        }
        //# 为空清理
        if (badThing.isEmpty()) {
            badThing = arrayListOf("诸事不忌")
        }
        if (goodThing.isEmpty()) {
            goodThing = arrayListOf("诸事不宜")
        }
        //# 输出排序调整
        badThing.sort()
        goodThing.sort()
        return Pair(Pair(goodGodName, badGodName), Pair(goodThing, badThing))
    }
}