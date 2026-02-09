package com.example.finals_activity3ramos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class UserProductsActivity : AppCompatActivity() {

    private lateinit var rvProducts: RecyclerView
    private lateinit var adapter: ProductAdapter
    private lateinit var dbHelper: DatabaseHelper
    private var categoryId: Int = 0
    private var categoryName: String = ""
    private var userId: Int = 1 // Set this from your logged-in user session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_products)

        // Get category info from intent
        categoryId = intent.getIntExtra("CATEGORY_ID", 0)
        categoryName = intent.getStringExtra("CATEGORY_NAME") ?: ""
        title = categoryName

        dbHelper = DatabaseHelper(this)

        // Setup RecyclerView
        rvProducts = findViewById(R.id.RV_Products)
        rvProducts.layoutManager = LinearLayoutManager(this)

        // Initialize adapter with empty list first
        adapter = ProductAdapter(this, mutableListOf(), userId) {
            // Optional cart update callback
        }
        rvProducts.adapter = adapter

        // Load products from DB
        loadProducts()
    }

    private fun loadProducts() {
        // Fetch products for this category
        val products = dbHelper.getProductsByCategory(categoryId)
        adapter.updateList(products.toMutableList())
    }
}
