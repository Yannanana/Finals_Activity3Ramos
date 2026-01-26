package com.example.finals_activity3ramos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.appbar.MaterialToolbar

class ViewCartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_cart)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        val cartItemsText = findViewById<TextView>(R.id.cart_items)
        val cartTotalText = findViewById<TextView>(R.id.cart_total)
        val btnCheckout = findViewById<Button>(R.id.btn_checkout)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        // Get cart data from intent
        val cartData = intent.getStringExtra("cart_data") ?: ""
        val cartTotal = intent.getDoubleExtra("cart_total", 0.0)

        // Display cart items
        if (cartData.isNotEmpty()) {
            val items = cartData.split("|")
            val displayText = items.joinToString("\n\n")
            cartItemsText.text = displayText
        } else {
            cartItemsText.text = "Your cart is empty"
        }

        // Display total
        cartTotalText.text = "Total: $${String.format("%.2f", cartTotal)}"

        btnCheckout.setOnClickListener {
            // Add checkout logic here
            // For now, just show a message
            android.widget.Toast.makeText(this, "Checkout feature coming soon!", Toast.LENGTH_SHORT).show()
        }
    }
}