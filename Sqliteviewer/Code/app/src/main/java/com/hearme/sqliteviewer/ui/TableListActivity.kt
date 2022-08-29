package com.hearme.sqliteviewer.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hearme.sqliteviewer.Adapter.TableListAdapter
import com.hearme.sqliteviewer.Const
import com.hearme.sqliteviewer.R
import com.hearme.sqliteviewer.database.DataBase
import com.hearme.sqliteviewer.databinding.ActivityTableListBinding
import com.hearme.sqliteviewer.extension.showToast
import java.io.File

class TableListActivity : AppCompatActivity() {

    lateinit var binding: ActivityTableListBinding
    lateinit var dbPath:String
    lateinit var adapter: TableListAdapter
    lateinit var tables: ArrayList<String>
    lateinit var db: DataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTableListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPermission()
        initData()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.table_list_topmenu,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
            R.id.settings -> {
                startActivity(Intent(this,SettingsActivity::class.java))
            }
        }
        return true
    }

    /*
    * 初始化toolbar
    */
    fun initToolbar(){
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setTitle(File(dbPath).name)
        }
    }

    /*
    * 获取读写权限
    * */
    fun checkPermission(){
        val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                // User allow the permission.
            } else {
                "拒绝了读写权限".showToast(this)
            }
        }
        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    /*
    * 初始化RecyclerView
    * */
    private fun setupRecyclerView(){
        val layoutManager = LinearLayoutManager(this)
        binding.tableRv.layoutManager = layoutManager
        binding.tableRv.setHasFixedSize(true)
        adapter = TableListAdapter(tables)
        binding.tableRv.adapter = adapter
    }

    /*
    * 初始化数据
    * */
    private fun initData(){
        if (getIntent()!=null && intent.hasExtra(Const.DBPathIntent))
            dbPath = intent.getStringExtra(Const.DBPathIntent).toString()
        else{
            R.string.no_database_path_found.showToast(this)
            finish()
        }
        Log.d(Const.TAG,dbPath)
        initToolbar()

        db = DataBase.getInstance(this)
        val isDb = db.setDatabase(dbPath)
        if (isDb){
            tables = db.getTables()
            setupRecyclerView()
        }else{
            R.string.not_database.showToast(this)
        }
    }

    companion object{
        fun actionStart(context: Context, dbPath: String) {
            val intent = Intent(context, TableListActivity::class.java)
            intent.putExtra(Const.DBPathIntent, dbPath)
            context.startActivity(intent)
        }
    }

}