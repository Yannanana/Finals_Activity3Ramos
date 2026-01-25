package com.example.finals_activity3ramos

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HomeActivity : AppCompatActivity() {

    private var isPanelOpen = false
    private var panelWidth = 0
    private var initialX = 0f
    private var panelInitialX = 0f
    private var isDragging = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val burgerButton = findViewById<ImageView>(R.id.burger_button)
        val sidePanel = findViewById<View>(R.id.side_panel)
        val checkoutButton = findViewById<Button>(R.id.checkout_button)

        // Get panel width once it's measured
        sidePanel.post {
            panelWidth = sidePanel.width
        }

        burgerButton.setOnClickListener {
            toggleSidePanel()
        }

        // ONLY ONE touch listener - remove the duplicate!
        @SuppressLint("ClickableViewAccessibility")
        sidePanel.setOnTouchListener { view, event ->
            handlePanelDrag(view, event)
        }

        // Handle checkout button
        checkoutButton.setOnClickListener {
            val intent = Intent(this, ViewCartActivity::class.java)
            startActivity(intent)
        }

        // Set up category click listeners
        setupCategoryClicks()
    }

    private fun handlePanelDrag(view: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // Only allow dragging when panel is open
                if (!isPanelOpen) return false

                // Record initial positions
                initialX = event.rawX
                panelInitialX = view.translationX
                isDragging = true
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                if (!isDragging) return false

                // Calculate how much finger has moved
                val deltaX = event.rawX - initialX

                // Calculate new panel position
                var newTranslation = panelInitialX + deltaX

                // Constrain movement:
                // - Can't pull right beyond open position (0)
                // - Can't pull left beyond closed position (-panelWidth)
                if (newTranslation > 0f) {
                    newTranslation = 0f
                } else if (newTranslation < -panelWidth.toFloat()) {
                    newTranslation = -panelWidth.toFloat()
                }

                // Move panel in REAL-TIME with finger
                view.translationX = newTranslation

                // Optional: Add visual feedback (fade effect)
                val progress = 1 - (-newTranslation / panelWidth.toFloat())
                view.alpha = 0.8f + (0.2f * progress)

                return true
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (!isDragging) return false

                isDragging = false

                val deltaX = event.rawX - initialX
                val currentTranslation = view.translationX
                val swipeThreshold = panelWidth * 0.3f // 30% of panel width

                // Decide whether to close or open based on:
                // 1. Current position
                // 2. Swipe distance
                val shouldClose = when {
                    // Swiped left significantly
                    deltaX < -swipeThreshold -> true
                    // Panel is more than halfway closed
                    currentTranslation < -panelWidth / 2 -> true
                    // Default: return to open
                    else -> false
                }

                if (shouldClose) {
                    closePanelWithAnimation(view)
                } else {
                    openPanelWithAnimation(view)
                }

                return true
            }
        }
        return false
    }

    private fun toggleSidePanel() {
        val sidePanel = findViewById<View>(R.id.side_panel)
        if (isPanelOpen) {
            closePanelWithAnimation(sidePanel)
        } else {
            openPanelWithAnimation(sidePanel)
        }
    }

    private fun openPanelWithAnimation(panel: View? = null) {
        val sidePanel = panel ?: findViewById<View>(R.id.side_panel)
        sidePanel.visibility = View.VISIBLE

        sidePanel.animate()
            .translationX(0f)
            .alpha(1f)
            .setDuration(300)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .withStartAction {
                // Reset alpha when starting animation
                sidePanel.alpha = 0.8f
            }
            .start()

        isPanelOpen = true
    }

    private fun closePanelWithAnimation(panel: View? = null) {
        val sidePanel = panel ?: findViewById<View>(R.id.side_panel)

        sidePanel.animate()
            .translationX(-panelWidth.toFloat())
            .alpha(0.8f)
            .setDuration(300)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .withEndAction {
                sidePanel.visibility = View.GONE
                sidePanel.alpha = 1f // Reset alpha
            }
            .start()

        isPanelOpen = false
    }

    private fun setupCategoryClicks() {
        val sidePanel = findViewById<View>(R.id.side_panel)

        val categoryIds = listOf(
            R.id.activity_home,
            R.id.category_fasteners,
            R.id.category_cutting,
            R.id.category_writing,
            R.id.category_ink,
            R.id.category_paper,
            R.id.category_tapes
        )

        categoryIds.forEach { id ->
            val categoryView = findViewById<View?>(id)
            categoryView?.setOnClickListener {
                when (id) {
                    R.id.activity_home -> {
                        Toast.makeText(this, "Filing clicked", Toast.LENGTH_SHORT).show()
                        // TODO: Show filing content
                    }
                    R.id.category_fasteners -> {
                        Toast.makeText(this, "Fasteners clicked", Toast.LENGTH_SHORT).show()
                        // TODO: Show fasteners content
                    }
                    R.id.category_cutting -> {
                        Toast.makeText(this, "Cutting clicked", Toast.LENGTH_SHORT).show()
                        // TODO: Show cutting content
                    }
                    R.id.category_writing -> {
                        Toast.makeText(this, "Writing clicked", Toast.LENGTH_SHORT).show()
                        // TODO: Show writing content
                    }
                    R.id.category_ink -> {
                        Toast.makeText(this, "Ink clicked", Toast.LENGTH_SHORT).show()
                        // TODO: Show ink content
                    }
                    R.id.category_paper -> {
                        Toast.makeText(this, "Paper clicked", Toast.LENGTH_SHORT).show()
                        // TODO: Show paper content
                    }
                    R.id.category_tapes -> {
                        Toast.makeText(this, "Tapes clicked", Toast.LENGTH_SHORT).show()
                        // TODO: Show tapes content
                    }
                }

                closePanelWithAnimation(sidePanel)
            }
        }
    }

    override fun onBackPressed() {
        if (isPanelOpen) {
            closePanelWithAnimation()
        } else {
            super.onBackPressed()
        }
    }
}