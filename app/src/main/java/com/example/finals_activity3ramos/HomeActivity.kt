package com.example.finals_activity3ramos

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

class HomeActivity : AppCompatActivity() {

    private var isPanelOpen = false
    private var panelWidth = 0
    private var startX = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Edge-to-edge replacement
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContentView(R.layout.activity_home)

        // Adjust main layout padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val burgerButton = findViewById<ImageView>(R.id.BTN_Burger)
        val sidePanel = findViewById<View>(R.id.side_panel)
        val logoutButton = findViewById<Button>(R.id.BTN_Logout)
        val filing = findViewById<Button>(R.id.BTN_Filings)
        val fastener = findViewById<Button>(R.id.BTN_Fasteners)
        val cutting = findViewById<Button>(R.id.BTN_Cutting)
        val writing = findViewById<Button>(R.id.BTN_Writing)
        val paper = findViewById<Button>(R.id.BTN_Paper)
        val tapes = findViewById<Button>(R.id.BTN_Tapes)

        filing.setOnClickListener {
            startActivity(Intent(this, FilingActivity::class.java))
            finish()
        }

        sidePanel.post {
            panelWidth = sidePanel.width
        }

        // Burger button click
        burgerButton.setOnClickListener {
            if (isPanelOpen) closePanel() else openPanel()
        }

        // Panel touch listener for dragging
        sidePanel.setOnTouchListener { view, event ->
            handleTouch(view, event)
        }

        // Logout button - goes to MainActivity
        logoutButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // Close panel when clicking outside
        findViewById<View>(R.id.main).setOnClickListener {
            if (isPanelOpen) closePanel()
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
                if (currentX < -panelWidth / 2) closePanel() else openPanel()
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
        if (isPanelOpen) closePanel() else super.onBackPressed()
    }
}
