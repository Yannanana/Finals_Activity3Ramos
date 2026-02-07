package com.example.finals_activity3ramos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SignupActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = DatabaseHelper(this)

        val firstName = findViewById<EditText>(R.id.ET_First)
        val lastName = findViewById<EditText>(R.id.ET_Last)
        val middleName = findViewById<EditText>(R.id.ET_Middle)
        val username = findViewById<EditText>(R.id.ET_Username)
        val password = findViewById<EditText>(R.id.ET_Password)
        val create = findViewById<Button>(R.id.BTN_Create)
        val cancel = findViewById<Button>(R.id.BTN_Cancel)
        val login = findViewById<TextView>(R.id.TV_Login)


        create.setOnClickListener {
            val fName = firstName.text.toString()
            val lName = lastName.text.toString()
            val mName = middleName.text.toString()
            val user = username.text.toString()
            val pass = password.text.toString()

            if (fName.isEmpty() || lName.isEmpty() || user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show()
            } else {
                val role = if (user == "admin@nu.edu.ph") "ADMIN" else "USER"
                val success = dbHelper.addUser(fName, lName, mName, user, pass, role)
                if (success) {
                    Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
        cancel.setOnClickListener {
            finish()
        }
        login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}