package com.example.inventoryapp

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val viewModel: InventoryViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: InventoryAdapter
    private lateinit var spinnerFilter: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        spinnerFilter = findViewById(R.id.spinnerFilter)

        adapter = InventoryAdapter(emptyList(),
            onUpdateClick = { item -> showUpdateDialog(item) },
            onDeleteClick = { item ->
                viewModel.deleteItem(item)
                Toast.makeText(this, "Deleted: ${item.name}", Toast.LENGTH_SHORT).show()
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Observe LiveData from ViewModel and update RecyclerView
        viewModel.allItems.observe(this) { items ->
            adapter.updateList(items)
            updateSpinner(items)  // Update filter options dynamically
        }

        // Set up Add Item Button
        val btnAddItem = findViewById<Button>(R.id.btnAddItem)
        btnAddItem.setOnClickListener {
            val etItemName = findViewById<EditText>(R.id.etItemName)
            val etItemType = findViewById<EditText>(R.id.etItemType)
            val etItemQuantity = findViewById<EditText>(R.id.etItemQuantity)

            val name = etItemName.text.toString().trim()
            val type = etItemType.text.toString().trim()
            val quantity = etItemQuantity.text.toString().toIntOrNull()

            if (name.isEmpty() || type.isEmpty() || quantity == null) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                val newItem = InventoryItem(name = name, type = type, quantity = quantity)
                viewModel.insertItem(newItem)
                etItemName.text.clear()
                etItemType.text.clear()
                etItemQuantity.text.clear()
            }
        }

        // Handle Spinner Selection for Filtering
        spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedType = parent?.getItemAtPosition(position).toString()
                filterItemsByType(selectedType)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                adapter.updateList(viewModel.allItems.value ?: emptyList()) // Show all if nothing is selected
            }
        }
    }

    // Update Spinner with Unique Item Types
    private fun updateSpinner(items: List<InventoryItem>) {
        val uniqueTypes = items.map { it.type }.distinct().sorted()
        val filterOptions = listOf("All") + uniqueTypes

        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, filterOptions)
        spinnerFilter.adapter = spinnerAdapter
    }

    // Filter Inventory Items by Selected Type
    private fun filterItemsByType(type: String) {
        val allItems = viewModel.allItems.value ?: emptyList()
        val filteredItems = if (type == "All") allItems else allItems.filter { it.type == type }
        adapter.updateList(filteredItems)
    }

    // Show Dialog to Update an Item
    private fun showUpdateDialog(item: InventoryItem) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_update_item, null)
        val etUpdateName = dialogView.findViewById<EditText>(R.id.etUpdateName)
        val etUpdateType = dialogView.findViewById<EditText>(R.id.etUpdateType)
        val etUpdateQuantity = dialogView.findViewById<EditText>(R.id.etUpdateQuantity)

        etUpdateName.setText(item.name)
        etUpdateType.setText(item.type)
        etUpdateQuantity.setText(item.quantity.toString())

        AlertDialog.Builder(this)
            .setTitle("Update Item")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                val updatedItem = InventoryItem(
                    id = item.id,
                    name = etUpdateName.text.toString(),
                    type = etUpdateType.text.toString(),
                    quantity = etUpdateQuantity.text.toString().toIntOrNull() ?: item.quantity
                )
                viewModel.updateItem(updatedItem)
                Toast.makeText(this, "Updated: ${updatedItem.name}", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
