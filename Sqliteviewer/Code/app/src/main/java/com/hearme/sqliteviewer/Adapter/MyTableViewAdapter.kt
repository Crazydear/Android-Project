package com.hearme.sqliteviewer.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.evrencoskun.tableview.adapter.AbstractTableAdapter
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.hearme.sqliteviewer.R
import com.hearme.sqliteviewer.databinding.MyCellLayoutBinding
import com.hearme.sqliteviewer.databinding.TableViewColumnHeaderLayoutBinding
import com.hearme.sqliteviewer.databinding.TableViewRowHeaderLayoutBinding
import com.hearme.sqliteviewer.model.Cell
import com.hearme.sqliteviewer.model.ColumnHeader
import com.hearme.sqliteviewer.model.RowHeader

class MyTableViewAdapter(context: Context): AbstractTableAdapter<ColumnHeader, RowHeader, Cell>() {
    val context = context

    fun refreshData(tableData:List<List<Cell>>,rowHeaders: List<RowHeader>){
        setCellItems(tableData)
        setRowHeaderItems(rowHeaders)
        notifyDataSetChanged()
    }

    /**
     * This is where you create your custom Cell ViewHolder. This method is called when Cell
     * RecyclerView of the TableView needs a new RecyclerView.ViewHolder of the given type to
     * represent an item.
     *
     * @param viewType : This value comes from #getCellItemViewType method to support different type
     *                 of viewHolder as a Cell item.
     * @see #getCellItemViewType(int);
     */
    override fun onCreateCellViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        val binding = MyCellLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyCellViewHolder(binding.root)
    }

    override fun onBindCellViewHolder(
        holder: AbstractViewHolder,
        cellItemModel: Cell?,
        columnPosition: Int,
        rowPosition: Int
    ) {
        val cell:Cell = cellItemModel as Cell
        val viewHolder:MyCellViewHolder = holder as MyCellViewHolder
        viewHolder.cell_textview.text = cell.getData().toString()

        // If your TableView should have auto resize for cells & columns.
        // Then you should consider the below lines. Otherwise, you can ignore them.

        // It is necessary to remeasure itself.
        viewHolder.ItemView.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
        viewHolder.cell_textview.requestLayout()
    }

    /**
     * This is where you create your custom Column Header ViewHolder. This method is called when
     * Column Header RecyclerView of the TableView needs a new RecyclerView.ViewHolder of the given
     * type to represent an item.
     *
     * @param viewType : This value comes from "getColumnHeaderItemViewType" method to support
     *                 different type of viewHolder as a Column Header item.
     * @see #getColumnHeaderItemViewType(int);
     */
    override fun onCreateColumnHeaderViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder {
        val binding = TableViewColumnHeaderLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyColumnHeaderViewHolder(binding.root)
    }

    /**
     * That is where you set Column Header View Model data to your custom Column Header ViewHolder.
     * This method is Called by ColumnHeader RecyclerView of the TableView to display the data at
     * the specified position. This method gives you everything you need about a column header
     * item.
     *
     * @param holder                : This is one of your column header ViewHolders that was created on
     *                              ```onCreateColumnHeaderViewHolder``` method. In this example we have created
     *                              "MyColumnHeaderViewHolder" holder.
     * @param columnHeaderItemModel : This is the column header view model located on this X position. In this
     *                              example, the model class is "ColumnHeader".
     * @param position              : This is the X (Column) position of the column header item.
     * @see #onCreateColumnHeaderViewHolder(ViewGroup, int) ;
     */
    override fun onBindColumnHeaderViewHolder(
        holder: AbstractViewHolder,
        columnHeaderItemModel: ColumnHeader?,
        columnPosition: Int
    ) {
        val columnHeader = columnHeaderItemModel as ColumnHeader
        // Get the holder to update cell item text
        val columnHeaderViewHolder = holder as MyColumnHeaderViewHolder
        columnHeaderViewHolder.column_header_textview.text = columnHeader.getData() as String?
        // If your TableView should have auto resize for cells & columns.
        // Then you should consider the below lines. Otherwise, you can ignore them.

        // It is necessary to remeasure itself.
        columnHeaderViewHolder.column_header_container.layoutParams.width =
            LinearLayout.LayoutParams.WRAP_CONTENT
        columnHeaderViewHolder.column_header_textview.requestLayout()
    }

    /**
     * This is where you create your custom Row Header ViewHolder. This method is called when
     * Row Header RecyclerView of the TableView needs a new RecyclerView.ViewHolder of the given
     * type to represent an item.
     *
     * @param viewType : This value comes from "getRowHeaderItemViewType" method to support
     *                 different type of viewHolder as a row Header item.
     * @see #getRowHeaderItemViewType(int);
     */
    override fun onCreateRowHeaderViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        val binding = TableViewRowHeaderLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyRowHeaderViewHolder(binding.root)
    }

    /**
     * That is where you set Row Header View Model data to your custom Row Header ViewHolder. This
     * method is Called by RowHeader RecyclerView of the TableView to display the data at the
     * specified position. This method gives you everything you need about a row header item.
     *
     * @param holder             : This is one of your row header ViewHolders that was created on
     *                           ```onCreateRowHeaderViewHolder``` method. In this example we have created
     *                           "MyRowHeaderViewHolder" holder.
     * @param rowHeaderItemModel : This is the row header view model located on this Y position. In this
     *                           example, the model class is "RowHeader".
     * @param position           : This is the Y (row) position of the row header item.
     * @see #onCreateRowHeaderViewHolder(ViewGroup, int) ;
     */
    override fun onBindRowHeaderViewHolder(
        holder: AbstractViewHolder,
        rowHeaderItemModel: RowHeader?,
        rowPosition: Int
    ) {
        val rowHeader = rowHeaderItemModel as RowHeader

        // Get the holder to update row header item text
        val rowHeaderViewHolder = holder as MyRowHeaderViewHolder
        rowHeaderViewHolder.row_header_textview.text = rowHeader.getData().toString()
    }

    override fun onCreateCornerView(parent: ViewGroup): View {
        return LayoutInflater.from(context).inflate(R.layout.table_view_corner_layout,null)
    }

    override fun getColumnHeaderItemViewType(position: Int): Int {
        // The unique ID for this type of column header item
        // If you have different items for Cell View by X (Column) position,
        // then you should fill this method to be able create different
        // type of CellViewHolder on "onCreateCellViewHolder"
        return 0
    }

    override fun getRowHeaderItemViewType(position: Int): Int {
        // The unique ID for this type of row header item
        // If you have different items for Row Header View by Y (Row) position,
        // then you should fill this method to be able create different
        // type of RowHeaderViewHolder on "onCreateRowHeaderViewHolder"
        return 0
    }

    override fun getCellItemViewType(position: Int): Int {
        // The unique ID for this type of cell item
        // If you have different items for Cell View by X (Column) position,
        // then you should fill this method to be able create different
        // type of CellViewHolder on "onCreateCellViewHolder"
        return 0
    }

    /**
     * This is sample CellViewHolder class
     * This viewHolder must be extended from AbstractViewHolder class instead of RecyclerView.ViewHolder.
     */
    class MyCellViewHolder(itemView: View) : AbstractViewHolder(itemView) {
        var cell_textview: TextView
        var ItemView: LinearLayout

        init {
            cell_textview = itemView.findViewById(R.id.cell_data)
            ItemView = itemView.findViewById(R.id.cell_container)
        }
    }

    /**
     * This is sample CellViewHolder class.
     * This viewHolder must be extended from AbstractViewHolder class instead of RecyclerView.ViewHolder.
     */
    inner class MyColumnHeaderViewHolder(itemView: View) :
        AbstractViewHolder(itemView) {
        val column_header_textview: TextView
        val column_header_container: LinearLayout

        init {
            column_header_textview = itemView.findViewById(R.id.column_header_textView)
            column_header_container = itemView.findViewById(R.id.column_header_container)
        }
    }

    /**
     * This is sample CellViewHolder class.
     * This viewHolder must be extended from AbstractViewHolder class instead of RecyclerView.ViewHolder.
     */
    inner class MyRowHeaderViewHolder(itemView: View) :
        AbstractViewHolder(itemView) {
        val row_header_textview: TextView

        init {
            row_header_textview = itemView.findViewById(R.id.row_header_textview)
        }
    }
}