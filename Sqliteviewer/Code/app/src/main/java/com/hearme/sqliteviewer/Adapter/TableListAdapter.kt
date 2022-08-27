package com.hearme.sqliteviewer.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hearme.sqliteviewer.Const
import com.hearme.sqliteviewer.databinding.TableListItemsBinding
import com.hearme.sqliteviewer.ui.TableDataActivity

class TableListAdapter(val tables:ArrayList<String>):RecyclerView.Adapter<TableListAdapter.ViewHolder>() {

    inner class ViewHolder(binding: TableListItemsBinding):RecyclerView.ViewHolder(binding.root){
        val tableName : TextView = binding.tableListNameTv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TableListItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        val viewHolder = ViewHolder(binding)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val table = tables[position]
        holder.tableName.text = table

        holder.tableName.setOnClickListener {
            Log.d(Const.TAG,table)
            TableDataActivity.actionStart(holder.itemView.context,table)
        }
    }

    override fun getItemCount() = tables.size

}