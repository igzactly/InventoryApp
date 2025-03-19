package com.example.inventoryapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import java.util.concurrent.Executors

class InventoryViewModel(application: Application) : AndroidViewModel(application) {

    private val dao: InventoryDao = InventoryDatabase.getDatabase(application).inventoryDao()
    val allItems: LiveData<List<InventoryItem>> = dao.getAllItems()

    // Create a single-thread executor for database operations
    private val executor = Executors.newSingleThreadExecutor()

    fun insertItem(item: InventoryItem) {
        executor.execute {
            dao.insertItem(item)
        }
    }

    fun updateItem(item: InventoryItem) {
        executor.execute {
            dao.updateItem(item)
        }
    }

    fun deleteItem(item: InventoryItem) {
        executor.execute {
            dao.deleteItem(item)
        }
    }
}
