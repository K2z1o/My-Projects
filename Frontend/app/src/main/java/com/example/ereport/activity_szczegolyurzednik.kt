package com.example.ereport

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class activity_szczegolyurzednik : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_szczegolyurzednik)

        val returnButton = findViewById<ImageButton>(R.id.returnButton)
        returnButton.setOnClickListener {
            startActivity(Intent(this, activity_ostrzezeniaurzednik::class.java))
        }

        val tytul = intent.getStringExtra("tytul")
        val grupa = intent.getStringExtra("grupa")
        val czas = intent.getStringExtra("czas")
        val opis = intent.getStringExtra("opis")

        val detailTytul = findViewById<TextView>(R.id.titleText)
        val detailGrupa = findViewById<TextView>(R.id.valueGrupa)
        val detailCzas = findViewById<TextView>(R.id.valueCzas)
        val detailOpis = findViewById<TextView>(R.id.valueOpis)

        detailTytul.text = tytul
        detailGrupa.text = "$grupa"
        detailCzas.text = "$czas"
        detailOpis.text = "$opis"
    }
}