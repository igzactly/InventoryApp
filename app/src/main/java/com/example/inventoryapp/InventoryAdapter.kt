package com.example.inventoryapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class InventoryAdapter(
    private var itemList: List<InventoryItem>,
    private val onUpdateClick: (InventoryItem) -> Unit,
    private val onDeleteClick: (InventoryItem) -> Unit
) : RecyclerView.Adapter<InventoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvItemName: TextView = view.findViewById(R.id.tvItemName)
        val tvItemType: TextView = view.findViewById(R.id.tvItemType)
        val tvItemQuantity: TextView = view.findViewById(R.id.tvItemQuantity)
        val btnUpdate: Button = view.findViewById(R.id.btnUpdate)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_inventory, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.tvItemName.text = item.name
        holder.tvItemType.text = item.type
        holder.tvItemQuantity.text = "Qty: ${item.quantity}"

        holder.btnUpdate.setOnClickListener { onUpdateClick(item) }
        holder.btnDelete.setOnClickListener { onDeleteClick(item) }
    }

    override fun getItemCount() = itemList.size

    fun updateList(newList: List<InventoryItem>) {
        itemList = newList
        notifyDataSetChanged()
    }
}
