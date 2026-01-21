package com.example.finals_activity3ramos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val signup = findViewById<TextView>(R.id.TV_Signup)
        val username = findViewById<EditText>(R.id.ET_Username)
        val password = findViewById<EditText>(R.id.ET_Password)
        val login = findViewById<Button>(R.id.BTN_Login)
        val cancel = findViewById<Button>(R.id.BTN_Cancel)

        login.setOnClickListener {
            val user = username.text.toString()
            val pass = password.text.toString()
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("message", user)
            intent.putExtra("password", pass)
            startActivity(intent)
        }
        cancel.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        signup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}