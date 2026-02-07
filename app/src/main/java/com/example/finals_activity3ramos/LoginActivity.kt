package com.example.finals_activity3ramos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Make content go edge-to-edge safely
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContentView(R.layout.activity_login)

        // Apply padding for system bars
        val rootLayout = findViewById<ConstraintLayout>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(rootLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = DatabaseHelper(this)

        val signup = findViewById<TextView>(R.id.TV_Signup)
        val username = findViewById<EditText>(R.id.ET_Username)
        val password = findViewById<EditText>(R.id.ET_Password)
        val login = findViewById<Button>(R.id.BTN_Login)
        val cancel = findViewById<Button>(R.id.BTN_Cancel)

        login.setOnClickListener {
            val user = username.text.toString()
            val pass = password.text.toString()

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show()
            } else {

                // Check for the special admin account first
                if (user == "jglazatin@nu-clark.edu.ph" && pass == "admin123") {
                    Toast.makeText(this, "Admin login successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, AdminHomeActivity::class.java))
                    finish()
                    return@setOnClickListener
                }
                else {
                    // Regular user login
                    val role = dbHelper.loginUser(user, pass)
                    if (role != null) {
                        // User exists, login successful
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, UserHomeActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }


        cancel.setOnClickListener {
            finish() // just go back
        }

        signup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}
