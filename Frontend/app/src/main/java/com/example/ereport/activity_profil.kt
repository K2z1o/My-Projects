package com.example.ereport

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class activity_profil : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profil)

        val returnButton = findViewById<ImageButton>(R.id.returnButton)
        returnButton.setOnClickListener {
            startActivity(Intent(this, activity_zgloszeniaostrzezenia::class.java))
        }

        val edytujButton = findViewById<Button>(R.id.edytujButton)
        edytujButton.setOnClickListener {
            startActivity(Intent(this, activity_editprofil::class.java))
        }
    }
}