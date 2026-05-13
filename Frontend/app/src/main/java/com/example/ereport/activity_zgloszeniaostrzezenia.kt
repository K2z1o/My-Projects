package com.example.ereport

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class activity_zgloszeniaostrzezenia : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_zgloszeniaostrzezenia)

        val ostrzezeniaButton = findViewById<Button>(R.id.ostrzezeniaButton)
        ostrzezeniaButton.setOnClickListener {
            android.util.Log.d("NAV", "kliknięto ostrzezeniaButton")
            startActivity(Intent(this, activity_ostrzezenia::class.java))
        }

        val zgloszeniaButton = findViewById<Button>(R.id.zgloszeniaButton)
        zgloszeniaButton.setOnClickListener {
            startActivity(Intent(this, activity_zgloszenia::class.java))
        }

        val returnButton = findViewById<ImageButton>(R.id.returnButton)
        returnButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        val profilButton = findViewById<ImageButton>(R.id.profilButton)
        profilButton.setOnClickListener {
            startActivity(Intent(this, activity_profil::class.java))
        }

        val changeButton = findViewById<ImageButton>(R.id.changeButton)
        changeButton.setOnClickListener {
            startActivity(Intent(this, activity_ostrzezostrzezenia::class.java))
        }
    }
}