package com.hearme.sqliteviewer.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hearme.sqliteviewer.Const
import com.hearme.sqliteviewer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()


        binding.btnOpendb.setOnClickListener {
            val intent = Intent(this, TableListActivity::class.java)
            val dbpath = "${Const.EXTERNAL_STORAGE_DIR}/20220825.db"
            intent.putExtra(Const.DBPathIntent,dbpath)
            startActivity(intent)
        }
        binding.btnQx.setOnClickListener {
            val intent = Intent(this, TableDataActivity::class.java)
            startActivity(intent)
        }

    }

    /*
    * 初始化toolbar
    */
    fun initToolbar(){
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {
//            it.setDisplayHomeAsUpEnabled(true)
            // it.setHomeAsUpIndicator()
        }
    }


}