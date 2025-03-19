package com.example.inventoryapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "inventory_table")
data class InventoryItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Auto-incremented ID
    val name: String,   // Item Name
    val type: String,   // Item Type (Electronics, Furniture, etc.)
    val quantity: Int   // Item Quantity
)
