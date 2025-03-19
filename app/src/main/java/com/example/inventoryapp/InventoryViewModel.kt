package com.example.inventoryapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class InventoryViewModel(application: Application) : AndroidViewModel(application) {

    private val dao: InventoryDao = InventoryDatabase.getDatabase(application).inventoryDao()
    val allItems: LiveData<List<InventoryItem>> = dao.getAllItems()

    fun insertItem(item: InventoryItem) = viewModelScope.launch {
        dao.insertItem(item)
    }

    fun updateItem(item: InventoryItem) = viewModelScope.launch {
        dao.updateItem(item)
    }

    fun deleteItem(item: InventoryItem) = viewModelScope.launch {
        dao.deleteItem(item)
    }
}
