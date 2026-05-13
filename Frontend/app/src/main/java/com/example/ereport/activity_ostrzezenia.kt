package com.example.ereport

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ereport.adapters.OstrzezenieAdapter
import com.example.ereport.api.ApiService
import com.example.ereport.api.RetrofitClient
import com.example.ereport.viewmodel.OstrzezeniaViewModel

import android.widget.Button
import com.example.ereport.model.Ostrzezenie
import com.example.ereport.viewmodel.OstrzezeniaViewModelFactory

class activity_ostrzezenia : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OstrzezenieAdapter
    private lateinit var viewModel: OstrzezeniaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        android.util.Log.d("NAV", "activity_ostrzezenia onCreate")
        enableEdgeToEdge()
        setContentView(R.layout.activity_ostrzezenia)

        val returnButton = findViewById<ImageButton>(R.id.returnButton)
        returnButton.setOnClickListener {
            startActivity(Intent(this, activity_zgloszeniaostrzezenia::class.java))
        }

        recyclerView = findViewById(R.id.ostrzezeniaView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(false) // false bo lista zmienia rozmiar dynamicznie

        adapter = OstrzezenieAdapter(
            items = mutableListOf(),
            onDelete = { item ->
                val miejscowoscId = getSharedPreferences("session", MODE_PRIVATE)
                    .getString("miejscowoscId", "") ?: ""
                viewModel.delete(item.id ?: return@OstrzezenieAdapter, miejscowoscId)
            },
            onClick = { item ->
                val intent = Intent(this, activity_szczegoly::class.java)
                intent.putExtra("tytul", item.Tytul)
                intent.putExtra("grupa", item.Grupa)
                intent.putExtra("opis", item.Opis)
                startActivity(intent)

            }
        )

        recyclerView.adapter = adapter


        val apiService = RetrofitClient.ostrzezenieApi
        viewModel = ViewModelProvider(
            this,
            OstrzezeniaViewModelFactory(apiService)
        )[OstrzezeniaViewModel::class.java]
        // obserwuj listę — adapter aktualizuje się automatycznie przy każdej zmianie
        viewModel.items.observe(this) { lista ->
            adapter.updateItems(lista)
        }

        // pobierz ostrzeżenia dla miejscowości zalogowanego użytkownika
        val miejscowoscId = getSharedPreferences("session", MODE_PRIVATE)
            .getString("miejscowoscId", "") ?: ""

        android.util.Log.d("SESJA", "ładuję ostrzeżenia dla: '$miejscowoscId'")
        viewModel.load(miejscowoscId)
        val sortTytulButton = findViewById<Button>(R.id.sortTytulButton)
        val sortCzasButton = findViewById<Button>(R.id.sortCzasButton)

        sortTytulButton.setOnClickListener {
            viewModel.sortByTytul()
        }
        sortCzasButton.setOnClickListener {
            viewModel.sortByCzas()
        }
        viewModel.load(miejscowoscId)

    }

}