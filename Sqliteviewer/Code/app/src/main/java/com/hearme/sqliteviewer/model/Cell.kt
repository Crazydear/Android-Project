package com.hearme.sqliteviewer.model

import com.evrencoskun.tableview.filter.IFilterableModel
import com.evrencoskun.tableview.sort.ISortableModel

open class Cell:ISortableModel,IFilterableModel {
    private var mId: String = ""
    private var mData: Any? = null
    private var mFilterKeyword: String = ""

    constructor(id: String){mId=id}

    constructor(id: String,data: Any){
        mId = id
        mData = data
        mFilterKeyword = data.toString()
    }

    override fun getId() = mId

    override fun getContent() = mData


    fun getData(): Any? {
        return mData
    }

    fun setData(data: String?) {
        mData = data
    }

    fun getFilterKeyword(): String? {
        return mFilterKeyword
    }

    fun setFilterKeyword(filterKeyword: String) {
        mFilterKeyword = filterKeyword
    }

    override fun getFilterableKeyword() = mFilterKeyword
}