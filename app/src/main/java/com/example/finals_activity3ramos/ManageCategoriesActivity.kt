package com.example.finals_activity3ramos

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finals_activity3ramos.adapters.CategoryAdapter
import com.example.finals_activity3ramos.models.Category
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.widget.Toast

class ManageCategoriesActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CategoryAdapter
    private lateinit var fabAddCategory: FloatingActionButton
    private var categoryList = mutableListOf<Category>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_categories)

        db = DatabaseHelper(this)
        recyclerView = findViewById(R.id.RV_Categories)
        fabAddCategory = findViewById(R.id.fabAddCategory)

        setupRecyclerView()
        loadCategories()

        fabAddCategory.setOnClickListener { showAddCategoryDialog() }
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CategoryAdapter(
            categoryList,
            onEdit = { category -> showEditCategoryDialog(category) },
            onDelete = { category ->
                if (db.deleteCategory(category.id)) {
                    Toast.makeText(this, "Category deleted", Toast.LENGTH_SHORT).show()
                    loadCategories()
                } else {
                    Toast.makeText(this, "Failed to delete", Toast.LENGTH_SHORT).show()
                }
            }
        )
        recyclerView.adapter = adapter
    }

    private fun loadCategories() {
        categoryList.clear()
        val cursor = db.getAllCategories()
        if (cursor.moveToFirst()) {
            do {
                val category = Category(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("category_id")),
                    name = cursor.getString(cursor.getColumnIndexOrThrow("category_name"))
                )
                categoryList.add(category)
            } while (cursor.moveToNext())
        }
        cursor.close()
        adapter.updateList(categoryList)
    }

    private fun showAddCategoryDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_category, null)
        val etName = dialogView.findViewById<EditText>(R.id.etCategoryName)
        val btnSave = dialogView.findViewById<Button>(R.id.btnSave)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialog.show()

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            if (name.isNotEmpty()) {
                if (db.addCategory(name)) {
                    Toast.makeText(this, "Category added", Toast.LENGTH_SHORT).show()
                    loadCategories()
                    dialog.dismiss()
                } else {
                    Toast.makeText(this, "Failed to add category", Toast.LENGTH_SHORT).show()
                }
            } else {
                etName.error = "Category name cannot be empty"
            }
        }

        btnCancel.setOnClickListener { dialog.dismiss() }
    }

    private fun showEditCategoryDialog(category: Category) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_category, null)
        val etName = dialogView.findViewById<EditText>(R.id.etCategoryName)
        val btnSave = dialogView.findViewById<Button>(R.id.btnSave)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)

        etName.setText(category.name)
        btnSave.text = "UPDATE"

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialog.show()

        btnSave.setOnClickListener {
            val newName = etName.text.toString().trim()
            if (newName.isNotEmpty()) {
                if (db.updateCategory(category.id, newName)) {
                    Toast.makeText(this, "Category updated", Toast.LENGTH_SHORT).show()
                    loadCategories()
                    dialog.dismiss()
                } else {
                    Toast.makeText(this, "Failed to update category", Toast.LENGTH_SHORT).show()
                }
            } else {
                etName.error = "Category name cannot be empty"
            }
        }

        btnCancel.setOnClickListener { dialog.dismiss() }
    }
}
