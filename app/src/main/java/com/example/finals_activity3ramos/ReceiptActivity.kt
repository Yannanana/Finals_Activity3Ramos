package com.example.finals_activity3ramos

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class ReceiptActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private var userId: Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt)

        // Views inside receipt
        val tvUsername = findViewById<TextView>(R.id.tvUsername)
        val tvPurpose = findViewById<TextView>(R.id.tvPurpose)
        val tvTotal = findViewById<TextView>(R.id.tvTotal)
        val tvOrder = findViewById<TextView>(R.id.tvOrderNumber)
        val itemsContainer = findViewById<LinearLayout>(R.id.itemsContainer)
        val receiptContainer = findViewById<FrameLayout>(R.id.receiptContainer)
        val btnDownload = findViewById<Button>(R.id.BTN_Download)
        val btnHome = findViewById<ImageView>(R.id.BTN_Home)

        // Intent data
        val orderId = intent.getIntExtra("ORDER_ID", -1)
        val username = intent.getStringExtra("USERNAME") ?: "Unknown User"
        val purpose = intent.getStringExtra("PURPOSE") ?: "No purpose"

        dbHelper = DatabaseHelper(this)

        userId = intent.getIntExtra("USER_ID", -1)
        if (userId == -1) {
            Toast.makeText(this, "User session error. Please login again.", Toast.LENGTH_LONG).show()
            finish()
            return
        }
        val cartItems = dbHelper.getCartItems(userId)



        // Date formatting
        val dateOnly = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(Date())

        // Set header data
        tvUsername.text = username
        tvPurpose.text = purpose
        tvOrder.text = "Order #: $orderId\nDate: $dateOnly"


        // Populate items
        var total = dbHelper.getCartTotal(userId)

        itemsContainer.removeAllViews()
        cartItems.forEach { item ->
            val tv = TextView(this).apply {
                text = "${item.quantity}x ${item.name} — ₱%.2f".format(item.price)
                textSize = 14f
            }
            itemsContainer.addView(tv)
        }

        tvTotal.text = "Total: ₱%.2f".format(dbHelper.getCartTotal(userId))

        // Download receipt (ONLY paper)
        btnDownload.setOnClickListener {
            saveReceiptAsImage(receiptContainer)

            dbHelper.clearCart(userId)

            Toast.makeText(this, "Checkout complete. Cart cleared.", Toast.LENGTH_SHORT).show()

        }


        // Home button
        btnHome.setOnClickListener {
            finish()
        }
    }

    private fun saveReceiptAsImage(view: View) {
        // Ensure view is measured
        view.post {
            val bitmap = Bitmap.createBitmap(
                view.width,
                view.height,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            view.draw(canvas)

            val filename = "NU_Needs_Receipt_${System.currentTimeMillis()}.png"

            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                put(
                    MediaStore.Images.Media.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES + "/NU_Needs"
                )
            }

            val resolver = contentResolver
            val uri = resolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )

            uri?.let {
                resolver.openOutputStream(it)?.use { stream ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                }
                Toast.makeText(
                    this,
                    "Receipt saved to Gallery",
                    Toast.LENGTH_LONG
                ).show()
            }

        }

    }
}
