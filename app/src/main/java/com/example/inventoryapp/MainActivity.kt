package com.example.inventoryapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val viewModel: InventoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find UI elements
        val etItemName = findViewById<EditText>(R.id.etItemName)
        val etItemType = findViewById<EditText>(R.id.etItemType)
        val etItemQuantity = findViewById<EditText>(R.id.etItemQuantity)
        val btnAddItem = findViewById<Button>(R.id.btnAddItem)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // Setup RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = InventoryAdapter(emptyList(),
            onUpdateClick = { item ->
                // For now, show a Toast for update. Implement an update dialog as needed.
                Toast.makeText(this, "Update item: ${item.name}", Toast.LENGTH_SHORT).show()
            },
            onDeleteClick = { item ->
                viewModel.deleteItem(item)
                Toast.makeText(this, "Deleted item: ${item.name}", Toast.LENGTH_SHORT).show()
            }
        )
        recyclerView.adapter = adapter

        // Observe LiveData from ViewModel
        viewModel.allItems.observe(this) { items ->
            adapter.updateList(items)
        }

        // Add new item when button is clicked
        btnAddItem.setOnClickListener {
            val name = etItemName.text.toString().trim()
            val type = etItemType.text.toString().trim()
            val quantityStr = etItemQuantity.text.toString().trim()

            if (name.isEmpty() || type.isEmpty() || quantityStr.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                val quantity = quantityStr.toIntOrNull()
                if (quantity == null) {
                    Toast.makeText(this, "Please enter a valid quantity", Toast.LENGTH_SHORT).show()
                } else {
                    val newItem = InventoryItem(name = name, type = type, quantity = quantity)
                    viewModel.insertItem(newItem)
                    etItemName.text.clear()
                    etItemType.text.clear()
                    etItemQuantity.text.clear()
                }
            }
        }
    }
}
