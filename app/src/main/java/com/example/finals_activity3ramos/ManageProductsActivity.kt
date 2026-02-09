package com.example.finals_activity3ramos

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.finals_activity3ramos.models.Category

class ManageProductsActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private lateinit var spinnerCategories: Spinner
    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAddProduct: FloatingActionButton

    private var categoryList = mutableListOf<Category>()
    private var productList = mutableListOf<Product>()
    private lateinit var adapter: ProductAdapter
    private var selectedCategoryId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_manage_products)

        // Edge-to-edge padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fabAddProduct = findViewById(R.id.fabAddProduct)
        fabAddProduct.setOnClickListener {
            showAddProductDialog()
        }

        db = DatabaseHelper(this)
        spinnerCategories = findViewById(R.id.spinnerCategories)
        recyclerView = findViewById(R.id.RV_ManageProducts)

        setupSpinner()
        setupRecyclerView()
    }

    private fun setupSpinner() {
        categoryList.clear()
        val cursor = db.getAllCategories()
        if (cursor.moveToFirst()) {
            do {
                categoryList.add(
                    Category(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("category_id")),
                        name = cursor.getString(cursor.getColumnIndexOrThrow("category_name"))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()

        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            categoryList.map { it.name }
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategories.adapter = spinnerAdapter

        spinnerCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                selectedCategoryId = categoryList[position].id
                loadProducts(selectedCategoryId)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ProductAdapter(
            this,
            productList,
            userId = 0, // admin mode
            onCartUpdated = {}
        )
        recyclerView.adapter = adapter
    }

    private fun loadProducts(categoryId: Int) {
        productList.clear()
        productList.addAll(db.getProductsByCategory(categoryId))
        adapter.updateList(productList)
    }


    private fun showAddProductDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_product, null)
        val etName = dialogView.findViewById<EditText>(R.id.etProductName)
        val etDesc = dialogView.findViewById<EditText>(R.id.etDescription)
        val etPrice = dialogView.findViewById<EditText>(R.id.etPrice)
        val etStock = dialogView.findViewById<EditText>(R.id.etStock)
        val btnSave = dialogView.findViewById<Button>(R.id.btnSave)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()
        dialog.show()

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val desc = etDesc.text.toString().trim()
            val price = etPrice.text.toString().toDoubleOrNull()
            val stock = etStock.text.toString().toIntOrNull()

            if (name.isEmpty() || desc.isEmpty() || price == null || stock == null) {
                Toast.makeText(this, "All fields must be filled correctly", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedCategoryId == -1) {
                Toast.makeText(this, "Select a category first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (db.addProduct(selectedCategoryId, name, desc, price, stock, null)) {
                Toast.makeText(this, "Product added", Toast.LENGTH_SHORT).show()
                loadProducts(selectedCategoryId)
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Failed to add product", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel.setOnClickListener { dialog.dismiss() }
    }
}
