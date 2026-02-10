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
    private lateinit var btnBack: ImageView
    private lateinit var btnCheckout: Button

    private lateinit var dbHelper: DatabaseHelper
    private var userId: Int = -1
    private lateinit var username: String




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_cart)

        userId = intent.getIntExtra("USER_ID", -1)

        if (userId == -1) {
            Toast.makeText(this, "User session error. Please login again.", Toast.LENGTH_LONG).show()
            finish()
            return
        }
        username = intent.getStringExtra("USERNAME") ?: "Unknown User"


        dbHelper = DatabaseHelper(this)

        txtTotal = findViewById(R.id.TV_CartTotal)
        edtPurpose = findViewById(R.id.ET_Purpose)
        recyclerView = findViewById(R.id.RV_CartItems)
        btnBack = findViewById(R.id.BTN_Back)
        btnCheckout = findViewById(R.id.BTN_Checkout)

        recyclerView.layoutManager = LinearLayoutManager(this)
        val cartItems = dbHelper.getCartItems(userId)

        adapter = CartAdapter(cartItems, dbHelper, userId) {
            refreshTotal()
        }

        recyclerView.adapter = adapter


        btnBack.setOnClickListener { finish() }

        btnCheckout.setOnClickListener {
            if (adapter.itemCount == 0) {
                Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val cartItems = dbHelper.getCartItems(userId)
            val total = dbHelper.getCartTotal(userId)


            val orderId = dbHelper.createOrder(userId, total)


            dbHelper.insertOrderItems(orderId, cartItems)


            val intent = Intent(this, ReceiptActivity::class.java)
            intent.putExtra("USER_ID", userId)
            intent.putExtra("ORDER_ID", orderId)
            intent.putExtra("USERNAME", username)
            intent.putExtra("PURPOSE", edtPurpose.text.toString())
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        refreshTotal()
    }



    private fun refreshTotal() {
        val total = dbHelper.getCartTotal(userId)
        txtTotal.text = "Total: ₱ %.2f".format(total)
    }
}
