# Sqliteviewer 1.0.1 #

根据开源软件 [SQLiteViewer](https://gitlab.com/vijai/SqliteDBViewer) 制作而成，将原来的JAVA代码用Kotlin重新制作



build.gradle

```
android {
    ...
    buildFeatures {
            viewBinding true
        }
    repositories {
        flatDir {
            dirs 'libs'
        }
    }
}

dependencies {
    implementation 'androidx.core:core-ktx:1.3.1'
    implementation 'androidx.appcompat:appcompat:1.3.1'
    implementation 'com.google.android.material:material:1.3.0'

 	compile(name:"sqliteviewer",ext:"aar")
    implementation 'androidx.preference:preference:1.1.1'
    implementation 'androidx.recyclerview:recyclerview:1.2.1'   // RecyclerView
    implementation 'de.siegmar:fastcsv:1.0.2'
}
```

主题 theme修改为NoActionBar的

用法

```
		val intent = Intent(this,TableListActivity::class.java)
        intent.putExtra(Const.DBPathIntent,dbPath)
        startActivity(intent)
```

或者

```
		TableListActivity.actionStart(this,dbPath)
```

