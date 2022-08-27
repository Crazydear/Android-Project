package com.hearme.sqliteviewer.ui

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.hearme.sqliteviewer.Adapter.MyTableViewAdapter
import com.hearme.sqliteviewer.Adapter.PagesListAdapter
import com.hearme.sqliteviewer.Const
import com.hearme.sqliteviewer.R
import com.hearme.sqliteviewer.database.DataBase
import com.hearme.sqliteviewer.databinding.ActivityTableDataBinding
import com.hearme.sqliteviewer.extension.showToast
import com.hearme.sqliteviewer.model.Cell
import com.hearme.sqliteviewer.model.ColumnHeader
import com.hearme.sqliteviewer.model.FieldModel
import com.hearme.sqliteviewer.model.RowHeader
import com.hearme.sqliteviewer.utils.SharedPreferenceManager
import de.siegmar.fastcsv.writer.CsvAppender
import de.siegmar.fastcsv.writer.CsvWriter
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.util.*
import kotlin.collections.ArrayList

class TableDataActivity : AppCompatActivity(){

    private lateinit var binding: ActivityTableDataBinding
    private lateinit var db:DataBase
    private lateinit var tableName:String
    private lateinit var tableData:List<List<Cell>>
    private lateinit var mTableViewAdapter:MyTableViewAdapter
    lateinit var mPagesListAdapter: PagesListAdapter
    private lateinit var context:TableDataActivity
    private var totalRows = 0L
    private lateinit var columnHeaders:ArrayList<ColumnHeader>
    private var RowHeader = ArrayList<RowHeader>()
    var dbName :String = ""
    private var tableViewRowCount:Long = 50
    private var offset = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTableDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DataBase.getInstance(this)
        if (!db.isDatabase()){
            R.string.not_database.showToast(this)
            finish()
        }

        if (intent != null && intent.hasExtra(Const.DBTableNameIntent))
            tableName = intent.getStringExtra(Const.DBTableNameIntent).toString()
        dbName = File(db.get_dbPath()).name

        initToolbar()
        initBottomBar()
        context = this

        val prefs = SharedPreferenceManager.getInstence(this)

        tableViewRowCount =
            prefs.getString("${packageName}_preferences",getString(R.string.preference_settings_table_row_count_key), "50").toLong()

        var fields = ArrayList<FieldModel>()
        tableData = generateTableData()
        fields = db.getFields(tableName)
        totalRows = db.getCount(tableName)

        if (tableData.size == 0)
            R.string.table_empty.showToast(this)

        columnHeaders = ArrayList<ColumnHeader>()
        for (field in fields)
            columnHeaders.add(ColumnHeader("1",field.headerName))

        RowHeader = generateRowHeader()

        binding.nowPage.text = "第1页\n共${totalRows / tableViewRowCount + 1}页"
        mTableViewAdapter = MyTableViewAdapter(this)
        binding.tableDataContent.setAdapter(mTableViewAdapter)
        binding.tableDataContent.cellRecyclerView.addOnScrollListener(onScrollListener)
        mTableViewAdapter.setAllItems(columnHeaders,RowHeader,tableData)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
            R.id.pageselect -> binding.drawerLayout.openDrawer(Gravity.RIGHT)
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_tabledata,menu)
        var item = menu?.findItem(R.id.pageselect)
        if (shouldShowPageSelect()){
           refreshPageRecy()
        }else
            item?.isVisible = false
        refreshPageRecy()
        return true
    }

    private fun refreshPageRecy() {
        val pages = addValuesToPageRecy()
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.pagesList.layoutManager = layoutManager
        mPagesListAdapter = PagesListAdapter(pages)
        binding.pagesList.adapter = mPagesListAdapter
        mPagesListAdapter.setOnItemClickListener(object:PagesListAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val temp = pages[position].toLong()-1
                offset = temp * tableViewRowCount
                refreshTableViewData()
                binding.nowPage.text = "第${temp+1}页\n共${pages.size}页"
            }
        })
    }

    private fun addValuesToPageRecy(): ArrayList<String> {
        val divident = totalRows / tableViewRowCount + 1
        val pagesItems: ArrayList<String> = ArrayList()
        for (i in 1..divident)
            pagesItems.add(i.toString())
        return pagesItems
    }

    private fun shouldShowPageSelect():Boolean{
        val divident = totalRows / tableViewRowCount
        return divident > 0
    }

    /*
    * 初始化toolbar
    */
    fun initToolbar(){
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = "${striExtension(dbName)}.$tableName"
        }
    }

    private val onScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 && binding.bottomNavigationView.isShown) {
                    binding.bottomNavigationView.animate()
                        .translationY(binding.bottomNavigationView.height.toFloat())
                        .duration = 800
                } else if (dy < 0) {
                    binding.bottomNavigationView.animate().translationY(0f).duration = 800
                }
            }
        }

    private fun generateTableData(): List<List<Cell>> {
        return db.getTableData(tableName,tableViewRowCount,offset)
    }

    private fun generateRowHeader(): ArrayList<RowHeader> {
        val rowHeader = ArrayList<RowHeader>()
        var localOffset = offset
        for (i in tableData.indices) {
            rowHeader.add(RowHeader(i.toString(), (localOffset + 1).toString()))
            localOffset += 1
        }
        return rowHeader
    }

    private fun striExtension(str:String): String? {
        if (str == null)
            return null
        val pos = str.lastIndexOf(".")
        if (pos == -1)
            return str
        return str.substring(0,pos)
    }

    private fun refreshTableViewData(){
        tableData = generateTableData()
        mTableViewAdapter.refreshData(tableData,generateRowHeader())
    }

    private fun formatFileName(fileName:String): String {
        return if (fileName.lowercase(Locale.getDefault()).endsWith(".csv")) fileName else "$fileName.csv"
    }

    private fun exportToCSV(fileName: String){
        val csvExportDir = Const.CSV_EXPORT_DIR
        if (csvExportDir.exists() || csvExportDir.mkdirs()) {
            val csv = File(csvExportDir, formatFileName(fileName))
            WriteToCSV(csv).execute()
        }
    }

    fun initBottomBar(){
        binding.bottomNavigationView.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when(item.itemId){
                    R.id.action_next->{
                        if (offset + tableViewRowCount < totalRows) {
                            offset += tableViewRowCount
                            refreshTableViewData()
                        }
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.action_previous ->{
                        if (offset < totalRows && offset != 0L) {
                            offset = if (offset - tableViewRowCount > 0)
                                offset - tableViewRowCount
                            else 0
                            refreshTableViewData()
                        }
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.action_export_csv ->{
                        exportToCSV("今日数据.csv")
                        "数据已导出".showToast(this)
                        return@OnNavigationItemSelectedListener true
                    }
                }
                false
            })
    }

    inner class WriteToCSV(val targetFile: File): AsyncTask<Unit, Int, Boolean>() {

        val db = DataBase.getInstance(context)
        val pref = SharedPreferenceManager.getInstence(context)
        val maxQueryCount = pref.getString("${packageName}_preferences",
                context.getString(R.string.preference_max_export_query_count_key),
                "50").toLong()
        val charset: Charset = Const.getEncodingType(pref.getString("${packageName}_preferences",
            context.getString(R.string.preference_export_charset_key),
            "UTF-8"))!!
        val dialog = ProgressDialog(context)
        var limit = -1L
        var exportedRowCount = 0

        override fun onPreExecute() {
            super.onPreExecute()
            dialog.setTitle(getString(R.string.export_progress_dialog_title))
            dialog.setMessage(getString(R.string.export_progress_dialog_message))
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            dialog.isIndeterminate = true
            dialog.setCancelable(false)
            dialog.show()
        }

        override fun doInBackground(vararg p0: Unit?): Boolean {
            val totalRows = db.getCount(tableName)
            dialog.max = totalRows.toInt()
            val csvWriter = CsvWriter()
            try {
                csvWriter.append(targetFile, charset).use { br ->
                    for (i in columnHeaders.indices) {
                        br.appendField(columnHeaders[i].getData().toString())
                    }
                    br.endLine()
                    br.flush()
                    if (totalRows > maxQueryCount) {
                        limit = maxQueryCount
                        do {
                            writeToCSV(br, generateData())
                            offset += limit
                        } while (offset < totalRows)
                    } else writeToCSV(br, generateData())
                }
            } catch (e: IOException) {
                R.string.toast_message_failed_export_message.showToast(context)
            }
            return true
        }

        override fun onProgressUpdate(vararg values: Int?) {
            if (dialog.isIndeterminate){
                dialog.isIndeterminate = false
            }
            dialog.progress = values[0] ?: 0
        }

        override fun onPostExecute(result: Boolean?) {
            dialog.dismiss()
            R.string.toast_message_export_success.showToast(context)
            super.onPostExecute(result)
            AlertDialog.Builder(context).apply {
                setTitle("导出成功")
                setMessage("保存位置：\n$targetFile")
                setPositiveButton("确定"){dialog,which->}
                show()
            }
        }

        @Throws(IOException::class)
        private fun writeToCSV(br: CsvAppender, data: List<List<Cell>>) {
            for (rowCount in data.indices) {
                val row = data[rowCount]
                for (col in row.indices) {
                    if (row[col].getData() == null) {
                        br.appendField(" ")
                    } else br.appendField(row[col].getData().toString())
                }
                br.endLine()
                br.flush()
                exportedRowCount++
            }
            publishProgress(exportedRowCount)
        }
        private fun generateData():List<List<Cell>>{
            return db.getTableData(tableName, limit,offset)
        }
    }

    companion object {
        fun actionStart(context: Context, dbTableName: String) {
            val intent = Intent(context, TableDataActivity::class.java)
            intent.putExtra(Const.DBTableNameIntent, dbTableName)
            context.startActivity(intent)
        }
    }

}