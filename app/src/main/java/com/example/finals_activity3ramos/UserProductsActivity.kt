package com.example.finals_activity3ramos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class UserProductsActivity : AppCompatActivity() {

    private lateinit var rvProducts: RecyclerView
    private lateinit var adapter: ProductAdapter
    private lateinit var dbHelper: DatabaseHelper
    private var categoryId: Int = -1
    private var categoryName: String = ""
    private var userId: Int = 1 // Set this from your logged-in user session
    private lateinit var tvItemCount: TextView
    private lateinit var tvTotal: TextView
    private lateinit var btnViewCart: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_products)

        title = categoryName
        categoryId = intent.getIntExtra("CATEGORY_ID", -1)
        categoryName = intent.getStringExtra("CATEGORY_NAME") ?:""



        if (categoryId == -1 || categoryName == null) {
            Toast.makeText(this, "Invalid category", Toast.LENGTH_SHORT).show()
            finish()
            return
        }


        dbHelper = DatabaseHelper(this)
        tvItemCount = findViewById(R.id.TV_CartCount)
        tvTotal = findViewById(R.id.TV_CartTotal)
        btnViewCart = findViewById(R.id.TV_ViewCart)

        btnViewCart.setOnClickListener {
            val intent = Intent(this, ViewCartActivity::class.java)
            startActivity(intent)
        }


        // Setup RecyclerView
        rvProducts = findViewById(R.id.RV_Products)
        rvProducts.layoutManager = LinearLayoutManager(this)


        // Initialize adapter with empty list first
        adapter = ProductAdapter(this, mutableListOf(), userId) {
            updateCartSummary()
        }
        rvProducts.adapter = adapter

        // Load products from DB
        loadProducts()

        updateCartSummary()

    }
    private fun updateCartSummary() {
        val itemCount = dbHelper.getCartCount(userId)
        val totalAmount = dbHelper.getCartTotal(userId)

        tvItemCount.text = "$itemCount"
        tvTotal.text = "₱ %.2f".format(totalAmount)
        btnViewCart.isEnabled = itemCount > 0
    }

    private fun loadProducts() {
        val products = dbHelper.getProductsByCategory(categoryId)

        Log.d("USER_PRODUCTS", "Products size = ${products.size}")

        if (products.isEmpty()) {
            Toast.makeText(this, "No products yet", Toast.LENGTH_SHORT).show()
        }

        adapter.updateList(products.toMutableList())
    }




}
