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
import kotlin.jvm.java

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val result = findViewById<TextView>(R.id.TV_Result)
        val username = findViewById<EditText>(R.id.ET_Username)
        val password = findViewById<EditText>(R.id.ET_Password)
        val create = findViewById<Button>(R.id.BTN_Create)
        val cancel = findViewById<Button>(R.id.BTN_Cancel)
        val last = findViewById<EditText>(R.id.ET_Last)
        val first = findViewById<EditText>(R.id.ET_First)
        val middle = findViewById<EditText>(R.id.ET_Middle)
        val login = findViewById<TextView>(R.id.TV_Login)

        create.setOnClickListener {
            val user = username.text.toString()
            val pass = password.text.toString()
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("message",user)
            intent.putExtra("password",pass)
            startActivity(intent)
        }
        cancel.setOnClickListener {
            result.text = ""
            username.text.clear()
            password.text.clear()
        }
        login.setOnClickListener {
            val goBack = Intent(this, LoginActivity::class.java)
            startActivity(goBack)
        }
    }
}