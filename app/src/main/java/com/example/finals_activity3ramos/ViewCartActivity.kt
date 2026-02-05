package com.example.finals_activity3ramos

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ViewCartActivity : AppCompatActivity() {

    private lateinit var txtTotal: TextView
    private lateinit var edtPurpose: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CartAdapter
    private lateinit var btnback: ImageView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_cart)


        txtTotal = findViewById(R.id.TV_CartTotal)
        edtPurpose = findViewById(R.id.ET_Purpose)
        recyclerView = findViewById(R.id.RV_CartItems)
        btnback = findViewById(R.id.BTN_Back)


        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = CartAdapter(
            CartManager.getItems().toMutableList()
        ) {
            refreshTotal()
        }

        recyclerView.adapter = adapter

        refreshTotal()

        btnback.setOnClickListener {
            startActivity(Intent(this, FilingActivity::class.java))
        }
    }



    private fun refreshTotal() {
        txtTotal.text =
            "Total: ₱%.2f".format(CartManager.getTotalPrice())
    }
}

