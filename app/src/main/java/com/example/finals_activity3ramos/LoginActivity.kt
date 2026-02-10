package com.example.finals_activity3ramos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
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
                val adminEmail = "jglazatin@nu-clark.edu.ph"
                val password = "1"

                if (user == adminEmail && pass == password) {
                    startActivity(Intent(this, AdminDashboardActivity::class.java))
                    finish()
                } else {
                    val isUserValid = dbHelper.checkUser(user, pass)
                    if (isUserValid) {
                        val userId = dbHelper.getUserId(user)

                        if (userId == -1) {
                            Toast.makeText(
                                this,
                                "User session error. Please login again.",
                                Toast.LENGTH_LONG
                            ).show()
                            return@setOnClickListener
                        }

                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, HomeActivity::class.java)
                        intent.putExtra("USER_ID", dbHelper.getUserId(user))
                        intent.putExtra("USERNAME", user)

                        startActivity(intent)
                        finish()

                    }
                }
            }

            cancel.setOnClickListener {
                val goBack = Intent(this, MainActivity::class.java)
                startActivity(goBack)
            }
            signup.setOnClickListener {
                val intent = Intent(this, SignupActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
