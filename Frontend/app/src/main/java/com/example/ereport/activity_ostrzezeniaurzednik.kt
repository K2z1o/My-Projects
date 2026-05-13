package com.example.ereport

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ereport.api.Item

class activity_ostrzezeniaurzednik : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var itemList: ArrayList<Item>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ostrzezeniaurzednik)

        val returnButton = findViewById<ImageButton>(R.id.returnButton)
        returnButton.setOnClickListener {
            startActivity(Intent(this, activity_ostrzezostrzezenia::class.java))
        }

        recyclerView = findViewById(R.id.ostrzezeniaView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        itemList = arrayListOf(
            Item(
                "Awaria systemu",
                "IT",
                "10:30",
                "Wystąpiła awaria systemu logowania. Problem dotyczy części użytkowników."
            ),
            Item(
                "Brak prądu",
                "Techniczne",
                "11:15",
                "Zgłoszono chwilowy brak zasilania w budynku A."
            ),
            Item(
                "Alarm pożarowy",
                "Bezpieczeństwo",
                "12:05",
                "Uruchomiono alarm pożarowy na 2 piętrze. Trwa weryfikacja zgłoszenia."
            )
        )

        val adapter = ItemAdapter(itemList) { clickedItem ->
            val intent = Intent(this, activity_szczegolyurzednik::class.java)
            intent.putExtra("tytul", clickedItem.tytul)
            intent.putExtra("grupa", clickedItem.grupa)
            intent.putExtra("czas", clickedItem.czas)
            intent.putExtra("opis", clickedItem.opis)
            startActivity(intent)
        }

        recyclerView.adapter = adapter
    }
}