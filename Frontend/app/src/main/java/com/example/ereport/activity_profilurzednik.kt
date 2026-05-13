package com.example.ereport

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class activity_profilurzednik : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profilurzednik)

        val returnButton = findViewById<ImageButton>(R.id.returnButton)
        returnButton.setOnClickListener {
            startActivity(Intent(this, activity_ostrzezostrzezenia::class.java))
        }

        val edytujButton = findViewById<Button>(R.id.edytujButton)
        edytujButton.setOnClickListener {
            startActivity(Intent(this, activity_editprofilurzednik::class.java))
        }
    }
}