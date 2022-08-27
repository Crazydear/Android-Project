package com.hearme.sqliteviewer.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hearme.sqliteviewer.databinding.PageItemBinding

class PagesListAdapter(val pages:ArrayList<String>):RecyclerView.Adapter<PagesListAdapter.ViewHolder>() {

    //1.定义接口类型的成员数据
    private var myItemClickListener : OnItemClickListener ?= null

    //2.定义接口
    interface OnItemClickListener{
        fun onItemClick(position:Int)
    }
    //3.set方法
    fun setOnItemClickListener(onItemClickListener : OnItemClickListener?){
        myItemClickListener = onItemClickListener
    }

    inner class ViewHolder(binding: PageItemBinding):RecyclerView.ViewHolder(binding.root){
        val tableName : TextView = binding.page
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PageItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        val viewHolder = ViewHolder(binding)
        return viewHolder
    }

    override fun onBindViewHolder(holder: PagesListAdapter.ViewHolder, position: Int) {
        val page = pages[position]
        holder.tableName.text = page

        //4.传入position
        holder.itemView.setOnClickListener {
            myItemClickListener!!.onItemClick(position)
        }
    }
    override fun getItemCount() = pages.size
}