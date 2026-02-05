package com.example.finals_activity3ramos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FilingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filing)

        val recyclerView = findViewById<RecyclerView>(R.id.RV_Products)
        val cartTotal = findViewById<TextView>(R.id.TV_CartTotal)
        val cartCount = findViewById<TextView>(R.id.TV_CartCount)
        val cart = findViewById<TextView>(R.id.TV_ViewCart)
        val home = findViewById<ImageView>(R.id.BTN_Home)


        cart.setOnClickListener {
            startActivity(Intent(this, ViewCartActivity::class.java))
        }

        home.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }




        recyclerView.layoutManager = LinearLayoutManager(this)



        // Get all filing products from InventoryManager
        val filingProducts = InventoryManager.getAllProducts() // implement this function

        val adapter = ProductAdapter(filingProducts) {
            // Update bottom bar when cart changes
            cartTotal.text = "₱${CartManager.getTotalPrice()}"
            cartCount.text = CartManager.getTotalQuantity().toString()
        }

        recyclerView.adapter = adapter
    }
    override fun onResume() {
        super.onResume()

        // Update Cart Count
        val cartCount = findViewById<TextView>(R.id.TV_CartCount)
        cartCount.text = CartManager.getTotalQuantity().toString()

        // Update Cart Total
        val cartTotal = findViewById<TextView>(R.id.TV_CartTotal)
        cartTotal.text = "₱${CartManager.getTotalPrice()}"
    }

}
