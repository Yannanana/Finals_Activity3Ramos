package com.example.finals_activity3ramos

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AdminDashboardActivity : AppCompatActivity() {

    private var isPanelOpen = false
    private var panelWidth = 0
    private var startX = 0f

    @SuppressLint("ClickableViewAccessibility")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_dashboard)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val categoryButton = findViewById<Button>(R.id.BTN_MCategory)
        val productButton = findViewById<Button>(R.id.BTN_MProduct)
        val logoutButton = findViewById<Button>(R.id.BTN_Logout)
        val burgerButton = findViewById<ImageView>(R.id.BTN_Burger)
        val sidePanel = findViewById<View>(R.id.side_panel)

        categoryButton.setOnClickListener {
            startActivity(Intent(this, ManageCategoriesActivity::class.java))
        }

        productButton.setOnClickListener {
            startActivity(Intent(this, ManageProductsActivity::class.java))
        }

        logoutButton.setOnClickListener {
            finish()
        }

        sidePanel.post {
            panelWidth = sidePanel.width
        }

        // Burger button click
        burgerButton.setOnClickListener {
            if (isPanelOpen) {
                closePanel()
            } else {
                openPanel()
            }
        }

        sidePanel.setOnTouchListener { view, event ->
            handleTouch(view, event)
        }

        // Logout button - goes to MainActivity
        logoutButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Close current activity
        }

        // Close panel when clicking outside
        findViewById<View>(R.id.main).setOnClickListener {
            if (isPanelOpen) {
                closePanel()
            }
        }
    }

    private fun handleTouch(view: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!isPanelOpen) return false
                startX = event.rawX
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                if (!isPanelOpen) return false

                val deltaX = event.rawX - startX
                var newX = deltaX

                if (newX > 0) newX = 0f
                if (newX < -panelWidth) newX = -panelWidth.toFloat()

                view.translationX = newX
                return true
            }

            MotionEvent.ACTION_UP -> {
                if (!isPanelOpen) return false

                val currentX = view.translationX
                if (currentX < -panelWidth / 2) {
                    closePanel()
                } else {
                    openPanel()
                }
                return true
            }
        }
        return false
    }

    private fun openPanel() {
        val sidePanel = findViewById<View>(R.id.side_panel)
        sidePanel.visibility = View.VISIBLE
        sidePanel.translationX = 0f
        isPanelOpen = true
    }

    private fun closePanel() {
        val sidePanel = findViewById<View>(R.id.side_panel)
        sidePanel.translationX = -panelWidth.toFloat()
        sidePanel.visibility = View.GONE
        isPanelOpen = false
    }

    override fun onBackPressed() {
        if (isPanelOpen) {
            closePanel()
        } else {
            super.onBackPressed()
        }
    }
}


