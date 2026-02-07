package com.example.finals_activity3ramos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ViewCartActivity : AppCompatActivity() {

    private lateinit var txtTotal: TextView
    private lateinit var edtPurpose: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CartAdapter
    private lateinit var btnback: ImageView
    private lateinit var btnCheckout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_cart)

        txtTotal = findViewById(R.id.TV_CartTotal)
        edtPurpose = findViewById(R.id.ET_Purpose)
        recyclerView = findViewById(R.id.RV_CartItems)
        btnback = findViewById(R.id.BTN_Back)
        btnCheckout = findViewById(R.id.BTN_Checkout)

        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = CartAdapter(
            CartManager.getItems().toMutableList()
        ) {
            refreshTotal()
        }

        recyclerView.adapter = adapter

        refreshTotal()

        btnback.setOnClickListener {
            onBackPressed()
        }

        btnCheckout.setOnClickListener {
            if (CartManager.getItems().isEmpty()) {
                Toast.makeText(this, "You must add items to your cart first!", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, ReceiptActivity::class.java)
                intent.putExtra("PURPOSE", edtPurpose.text.toString())
                startActivity(intent)
            }
        }
    }

    private fun refreshTotal() {
        txtTotal.text = "Total: ₱%.2f".format(CartManager.getTotalPrice())
    }
}
