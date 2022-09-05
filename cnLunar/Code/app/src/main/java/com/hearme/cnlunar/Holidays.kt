package com.hearme.cnlunar

object Holidays {

    val legalsolarTermsHolidayDic = mapOf<String, String>("清明" to "清明节")
    val legalHolidaysDic = mapOf<Pair<Int, Int>, String>(
        Pair(1, 1) to "元旦节",
        Pair(5, 1) to "国际劳动节",
        Pair(10, 1) to "国庆节"
    )
    val legalLunarHolidaysDic =
        mapOf<Pair<Int, Int>, String>(Pair(1, 1) to "春节", Pair(5, 5) to "端午节", Pair(8, 15) to "中秋节")

    val otherHolidaysList = listOf<Map<Int, String>>(
        mapOf<Int, String>(
            8 to "周恩来逝世纪念日"
        ), //1月
        mapOf<Int, String>(
            19 to "邓小平逝世纪念日"
        ),
        mapOf<Int, String>(
            5 to "周恩来诞辰纪念日",
            8 to "国际劳动妇女节",
            12 to "孙中山逝世纪念日,中国植树节",
            22 to "世界水日"
        ),
        mapOf<Int, String>(
            7 to "世界卫生日"
        ),
        mapOf<Int, String>(
            4 to "中国青年节",
            23 to "世界读书日"
        ),
        mapOf<Int, String>(
            1 to "国际儿童节",
            5 to "世界环境日"
        ),
        mapOf<Int, String>(
            1 to "中国共产党诞生日,香港回归纪念日",
            7 to "中国人民抗日战争纪念日"
        ),
        mapOf<Int, String>(
            1 to "中国人民解放军建军节",
            22 to "邓小平诞辰纪念日"
        ),
        mapOf<Int, String>(
            3 to "中国抗日战争胜利纪念日",
            9 to "毛泽东逝世纪念日",
            10 to "中国教师节",
            18 to "“九·一八”事变纪念日"
        ),
        mapOf<Int, String>(10 to "辛亥革命纪念日", 13 to "中国少年先锋队诞辰日", 25 to "抗美援朝纪念日"),
        mapOf<Int, String>(12 to "孙中山诞辰纪念日", 28 to "恩格斯诞辰纪念日"),
        mapOf<Int, String>(
            12 to "西安事变纪念日",
            13 to "南京大屠杀纪念日",
            26 to "毛泽东诞辰纪念日"
        ) // #12月
    )

    //#复活节:每年春分后月圆第一个星期天  母亲节:每年5月份的第2个星期日  父亲节:每年6月份的第3个星期天感恩节 每年11月最后一个星期四
    //# otherEastHolidaysList=mapOf<Int,String>(5:(2,7,"母亲节"),6:(3,7,"父亲节"))
    val otherLunarHolidaysList = listOf<Map<Int, String>>(
        mapOf<Int, String>(1 to "弥勒佛圣诞", 8 to "五殿阎罗天子诞", 9 to "玉皇上帝诞", 15 to "元宵节"),
        mapOf<Int, String>(
            1 to "一殿秦广王诞",
            2 to "春龙节-福德土地正神诞",
            3 to "文昌帝君诞",
            6 to "东华帝君诞",
            8 to "释迦牟尼佛出家",
            15 to "释迦牟尼佛般涅槃",
            17 to "东方杜将军诞",
            18 to "至圣先师孔子讳辰",
            19 to "观音大士诞",
            21 to "普贤菩萨诞"
        ),
        mapOf<Int, String>(
            1 to "二殿楚江王诞",
            3 to "三月三-玄天上帝诞",
            8 to "六殿卞城王诞",
            15 to "昊天上帝诞",
            16 to "准提菩萨诞",
            19 to "中岳大帝诞",
            20 to "子孙娘娘诞",
            27 to "七殿泰山王诞",
            28 to "苍颉至圣先师诞"
        ),
        mapOf<Int, String>(
            1 to "八殿都市王诞",
            4 to "文殊菩萨诞",
            8 to "释迦牟尼佛诞",
            14 to "纯阳祖师诞",
            15 to "钟离祖师诞",
            17 to "十殿转轮王诞",
            18 to "紫徽大帝诞",
            20 to "眼光圣母诞"
        ),
        mapOf<Int, String>(
            1 to "南极长生大帝诞",
            8 to "南方五道诞",
            11 to "天下都城隍诞",
            12 to "炳灵公诞",
            13 to "关圣降",
            16 to "天地元气造化万物之辰",
            18 to "张天师诞",
            22 to "孝娥神诞"
        ),
        mapOf<Int, String>(
            19 to "观世音菩萨成道日",
            24 to "关帝诞"
        ),
        mapOf<Int, String>(
            7 to "七夕-魁星诞",
            13 to "长真谭真人诞-大势至菩萨诞",
            15 to "中元节",
            18 to "西王母诞",
            19 to "太岁诞",
            22 to "增福财神诞",
            29 to "杨公忌",
            30 to "地藏菩萨诞"
        ),
        mapOf<Int, String>(
            1 to "许真君诞",
            3 to "司命灶君诞",
            5 to "雷声大帝诞",
            10 to "北斗大帝诞",
            12 to "西方五道诞",
            15 to "太阴星君诞",
            16 to "天曹掠刷真君降",
            18 to "天人兴福之辰",
            20 to "燃灯佛圣诞",
            23 to "汉恒候张显王诞",
            24 to "灶君夫人诞",
            29 to "至圣先师孔子诞"
        ),
        mapOf<Int, String>(
            1 to "北斗九星降世",
            3 to "五瘟神诞",
            9 to "重阳节-酆都大帝诞",
            13 to "孟婆尊神诞",
            17 to "金龙四大王诞",
            19 to "观世音菩萨出家",
            30 to "药师琉璃光佛诞"
        ),
        mapOf<Int, String>(1 to "寒衣节", 3 to "三茅诞", 5 to "达摩祖师诞", 8 to "佛涅槃日", 15 to "下元节"),
        mapOf<Int, String>(
            4 to "至圣先师孔子诞",
            6 to "西岳大帝诞",
            11 to "太乙救苦天尊诞",
            17 to "阿弥陀佛诞",
            19 to "太阳日宫诞",
            23 to "张仙诞",
            26 to "北方五道诞"
        ),
        mapOf<Int, String>(
            8 to "腊八节-释迦如来成佛之辰",
            16 to "南岳大帝诞",
            21 to "天猷上帝诞",
            23 to "小年",
            24 to "子时灶君上天朝玉帝",
            29 to "华严菩萨诞",
            30 to "除夕"
        )
    )

}