# kotlin Android通过GPS定位/网络定位获取当前经纬度 #

# 添加权限 #

android6.0后需要动态获取权限

Androidmanifest:

```xml
	<!--这个权限用于进行网络定位 -->
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <!-- 这个权限用于访问GPS定位 -->
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
```

## 动态获取权限 ##

```kotlin
	/*
     * 获取权限
     * */
    fun checkPermission(){
       val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            
        }
        requestPermissionLauncher.launch(
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
        ))
    }
```

## 创建一个获取位置的工具类 ##

```kotlin
package com.hearme.uisheji

/*
* 来源 CSDN
* https://blog.csdn.net/weixin_51355264/article/details/126283959
* */
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import java.text.DecimalFormat
import kotlin.math.floor

class LocationUtils
    private constructor(private val mContext:Context){
    // 定位回调
    private var mLocationCallBack:LocationCallBack? = null
    // 定位管理实例
    var mLocationManager:LocationManager? = null

    /**
     * 获取定位
     * @param mLocationCallBack 定位回调
     * @return
     */
    fun getLocation(mLocationCallBack:LocationCallBack){
        this.mLocationCallBack = mLocationCallBack
        // 定位管理初始化
        mLocationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // 通过GPS定位
        val gpsProvider = mLocationManager!!.getProvider(LocationManager.GPS_PROVIDER)
        // 通过网络定位
        val netProvider = mLocationManager!!.getProvider(LocationManager.NETWORK_PROVIDER)
        // GPS定位优先
        if (gpsProvider != null) gpsLocation()
        else if (netProvider != null) netWorkLocation()
        else mLocationCallBack.setLocation(null)
    }

    /**
     * GPS定位
     * @return
     */
    @SuppressLint("MissingPermission")
    private fun gpsLocation(){
        if (mLocationManager == null)
            mLocationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        mLocationManager!!.requestLocationUpdates(
            GPS_LOCATION,MIN_TIME,MIN_DISTANCE,mLocationListener
        )
    }

    /**
     * 网络定位
     */
    @SuppressLint("MissingPermission")
    private fun netWorkLocation(){
        if (mLocationManager == null)
            mLocationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        mLocationManager!!.requestLocationUpdates(
            NETWORK_LOCATION,MIN_TIME,MIN_DISTANCE,mLocationListener
        )
    }

    // 定位监听
    private val mLocationListener:LocationListener = object: LocationListener{
        override fun onLocationChanged(location: Location) {
            if (mLocationCallBack != null)
                mLocationCallBack!!.setLocation(location)
        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {
            // 如果GPS不可用，改用网络定位
            if (provider == LocationManager.GPS_PROVIDER)
                netWorkLocation()
        }
    }

    /**
     * GPS坐标 转换成 角度
     * 例如 113.202222 转换成 113°12′8″
     *
     * @param location
     * @return
     */
    fun gpsToDegree(location: Double): String {
        val degree = floor(location)
        val minute_temp = (location - degree) * 60
        val minute = floor(minute_temp)
        val second = DecimalFormat("#.##").format((minute_temp - minute) * 60)
        return (degree.toInt()).toString() + "°" + minute.toInt() + "′" + second + "″"
    }

    fun removeListener() {
        if (mLocationManager != null)
            mLocationManager!!.removeUpdates(mLocationListener)
    }

    /**
     * @className: LocationCallBack
     * @classDescription: 定位回调
     */
    interface LocationCallBack{
        fun setLocation(location:Location?)
    }

    companion object {
        // GPS定位
        private const val GPS_LOCATION = LocationManager.GPS_PROVIDER
        // 网络定位
        private const val NETWORK_LOCATION = LocationManager.NETWORK_PROVIDER
        // 时间更新间隔，单位：ms
        private const val MIN_TIME: Long = 1000
        // 位置刷新距离，单位：m
        private const val MIN_DISTANCE = 0.01.toFloat()
        // singleton
        @SuppressLint("StaticFieldLeak")
        private var instance: LocationUtils? = null

        /**
         * singleton
         * @param mContext 上下文
         * @return
         */
        fun getInstance(mContext: Context): LocationUtils {
            if (instance == null) {
                instance = LocationUtils(mContext)
            }
            return instance!!
        }
    }
}
```

## 使用 ##

```kotlin
lateinit var ltu:LocationUtils
var location: Location? = null

    ltu = LocationUtils.getInstance(this)
    ltu!!.getLocation(object : LocationUtils.LocationCallBack {
        override fun setLocation(location: Location?) {
            if (location !=null){
                 location = location
             }
         }
    })

override fun onDestroy() {
    super.onDestroy()
    ltu.removeListener()
}
```

