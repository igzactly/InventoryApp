package com.example.inventoryapp

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface InventoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(item: InventoryItem)

    @Update
    fun updateItem(item: InventoryItem)

    @Delete
    fun deleteItem(item: InventoryItem)

    @Query("SELECT * FROM inventory_table ORDER BY id ASC")
    fun getAllItems(): LiveData<List<InventoryItem>>
}
