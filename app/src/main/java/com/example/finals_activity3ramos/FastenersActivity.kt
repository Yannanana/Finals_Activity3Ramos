package com.example.finals_activity3ramos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class FastenersActivity : AppCompatActivity() {

    // Product data with prices
    private val productPrices = mapOf(
        "binder_clip_1" to 2.50,
        "binder_clip_2" to 3.00,
        "staple_wire" to 1.75,
        "paper_clip_big" to 1.25,
        "push_pin" to 1.50
    )

    private val quantities = mutableMapOf<String, Int>().apply {
        productPrices.keys.forEach { key -> this[key] = 0 }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.category_fasteners)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        val fabCart = findViewById<ExtendedFloatingActionButton>(R.id.fab_cart)

        // Setup toolbar navigation
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        // Setup quantity buttons for each product
        setupProductButtons()

        // Setup cart button
        fabCart.setOnClickListener {
            if (getTotalItems() > 0) {
                val intent = Intent(this, ViewCartActivity::class.java)
                intent.putExtra("cart_data", getCartData())
                intent.putExtra("cart_total", getCartTotal())
                startActivity(intent)
            } else {
                Toast.makeText(this, "Add items to cart first!", Toast.LENGTH_SHORT).show()
            }
        }

        // Update cart total initially
        updateCartTotal()
    }

    private fun setupProductButtons() {
        // Binder Clip 1.5/8"
        setupProductControls(
            plusButtonId = R.id.btn_plus_binder_clip_1,
            minusButtonId = R.id.btn_minus_binder_clip_1,
            quantityTextId = R.id.quantity_binder_clip_1,
            productKey = "binder_clip_1",
            stockStatus = "In Stock"
        )

        // Binder Clip Jumbo 2"
        setupProductControls(
            plusButtonId = R.id.btn_plus_binder_clip_2,
            minusButtonId = R.id.btn_minus_binder_clip_2,
            quantityTextId = R.id.quantity_binder_clip_2,
            productKey = "binder_clip_2",
            stockStatus = "In Stock"
        )

        // Staple Wire
        setupProductControls(
            plusButtonId = R.id.btn_plus_staple_wire,
            minusButtonId = R.id.btn_minus_staple_wire,
            quantityTextId = R.id.quantity_staple_wire,
            productKey = "staple_wire",
            stockStatus = "In Stock"
        )

        // Paper Clip Big
        setupProductControls(
            plusButtonId = R.id.btn_plus_paper_clip_big,
            minusButtonId = R.id.btn_minus_paper_clip_big,
            quantityTextId = R.id.quantity_paper_clip_big,
            productKey = "paper_clip_big",
            stockStatus = "In Stock"
        )

        // Push Pin
        setupProductControls(
            plusButtonId = R.id.btn_plus_push_pin,
            minusButtonId = R.id.btn_minus_push_pin,
            quantityTextId = R.id.quantity_push_pin,
            productKey = "push_pin",
            stockStatus = "In Stock"
        )

        // Products out of stock
        disableOutOfStockProducts()
    }

    private fun setupProductControls(
        plusButtonId: Int,
        minusButtonId: Int,
        quantityTextId: Int,
        productKey: String,
        stockStatus: String
    ) {
        val btnPlus = findViewById<ImageView>(plusButtonId)
        val btnMinus = findViewById<ImageView>(minusButtonId)
        val quantityText = findViewById<TextView>(quantityTextId)

        btnPlus.setOnClickListener {
            if (stockStatus == "In Stock") {
                val currentQuantity = quantities[productKey] ?: 0
                quantities[productKey] = currentQuantity + 1
                quantityText.text = quantities[productKey].toString()
                updateCartTotal()
            }
        }

        btnMinus.setOnClickListener {
            if (stockStatus == "In Stock") {
                val currentQuantity = quantities[productKey] ?: 0
                if (currentQuantity > 0) {
                    quantities[productKey] = currentQuantity - 1
                    quantityText.text = quantities[productKey].toString()
                    updateCartTotal()
                }
            }
        }

        // Initialize quantity display
        quantityText.text = quantities[productKey].toString()
    }

    private fun disableOutOfStockProducts() {
    }

    private fun updateCartTotal() {
        val fabCart = findViewById<ExtendedFloatingActionButton>(R.id.fab_cart)
        val total = getCartTotal()
        val items = getTotalItems()

        if (items > 0) {
            fabCart.text = "View your cart $${String.format("%.2f", total)}"
        } else {
            fabCart.text = "View your cart $0.00"
        }
    }

    private fun getCartTotal(): Double {
        var total = 0.0
        for ((key, quantity) in quantities) {
            val price = productPrices[key] ?: 0.0
            total += price * quantity
        }
        return total
    }

    private fun getTotalItems(): Int {
        return quantities.values.sum()
    }

    private fun getCartData(): String {
        val cartItems = mutableListOf<String>()

        // Map of product keys to display names
        val productNames = mapOf(
            "binder_clip_1" to "BINDER CLIP 1.5/8\", 12'S/80X",
            "binder_clip_2" to "BINDER CLIP JUMBO 2\", 12'S/80X",
            "staple_wire" to "STAPLE WIRE MAX #35 (28/6)",
            "paper_clip_big" to "PAPER CLIP, PRINCE, BIG (VINYL COATED)",
            "push_pin" to "PUSH PIN, 100'S/PAKO"
        )

        for ((key, quantity) in quantities) {
            if (quantity > 0) {
                val name = productNames[key] ?: key
                val price = productPrices[key] ?: 0.0
                val itemTotal = price * quantity
                cartItems.add("$name x$quantity = $${String.format("%.2f", itemTotal)}")
            }
        }

        return cartItems.joinToString("|")
    }
}