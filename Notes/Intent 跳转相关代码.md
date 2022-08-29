# Intent 跳转相关代码 #

## 1. 跳转到本应用信息界面 ##

```kotlin
val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
intent.data = Uri.parse("package:" + packageName)
startActivity(intent)
```

## 2. 跳转到位置设置 ##

```kotlin
val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
startActivity(intent)
```

