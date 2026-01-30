package com.example.finals_activity3ramos

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class FilingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filing)

        val btnPlus = findViewById<ImageView>(R.id.BTN_plus)
        val btnMinus = findViewById<ImageView>(R.id.BTN_minus)
        val txtQty = findViewById<TextView>(R.id.txt_quantity)
        val cartCount = findViewById<TextView>(R.id.TV_CartCount)
        val viewCart = findViewById<TextView>(R.id.TV_ViewCart)
        val stock = findViewById<TextView>(R.id.TV_StockStatus)

        val productId = "ARCH_FILE_A4"
        val productName = "ARCH FILE FOLDER A4, 2 RINGS (3\")"
        val price = 85.0

        btnPlus.setOnClickListener {

            val success = InventoryManager.decreaseStock(productId)

            if (success) {
                CartManager.addItem(
                    CartItem(productId, productName, price, 1)
                )
                updateStockUI(productId, stock)
                updateUI(txtQty, cartCount,productId)
                updateCartCount(cartCount)
            } else {
                Toast.makeText(this, "Out of stock", Toast.LENGTH_SHORT).show()
            }
        }


        btnMinus.setOnClickListener {

            val currentQty = CartManager.getItemQuantity(productId)

            if (currentQty > 0) {
                CartManager.removeItem(productId)
                InventoryManager.increaseStock(productId)
                updateStockUI(productId, stock)
                updateUI(txtQty, cartCount,productId)
                updateCartCount(cartCount)
            }
        }
        viewCart.setOnClickListener {
            val intent = Intent(this, ViewCartActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
    private fun updateCartCount(txtCartCount: TextView) {
        txtCartCount.text = CartManager.getTotalQuantity().toString()
    }

    private fun updateStockUI(
        productId: String,
        txtStock: TextView
    ) {
        val product = InventoryManager.getProduct(productId)

        if (product != null && product.stock > 0) {
            txtStock.text = "In stock (${product.stock})"
            txtStock.setTextColor(
                ContextCompat.getColor(this, android.R.color.holo_green_dark)
            )
        } else {
            txtStock.text = "Out of stock"
            txtStock.setTextColor(
                ContextCompat.getColor(this, android.R.color.holo_red_dark)
            )
        }
    }

    }


    private fun updateUI(
        txtQty: TextView,
        txtCartCount: TextView,
        productId: String
    ) {
        val item = CartManager.getItems().find { it.productId == productId }
        txtQty.text = item?.quantity?.toString() ?: "0"
        txtCartCount.text = CartManager.getTotalQuantity().toString()
    }