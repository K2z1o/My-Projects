package com.example.ereport

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.ereport.api.ApiService
import com.example.ereport.model.Miasto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class activity_ostrzez : AppCompatActivity() {

    private lateinit var spinnerWoj: Spinner
    private lateinit var spinnerPowiat: Spinner
    private lateinit var spinnerGmina: Spinner
    private lateinit var spinnerMiejscowosc: Spinner

    private var idMiejscowosci: String? = null
    private lateinit var api: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ostrzez)

        val returnButton = findViewById<ImageButton>(R.id.returnButton)
        returnButton.setOnClickListener {
            startActivity(Intent(this, activity_ostrzezostrzezenia::class.java))
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(ApiService::class.java)

        spinnerWoj = findViewById(R.id.spinnerWoj)
        spinnerPowiat = findViewById(R.id.spinnerPowiat)
        spinnerGmina = findViewById(R.id.spinnerGmina)
        spinnerMiejscowosc = findViewById(R.id.spinnerMiejscowosc)

        val tytulEdit = findViewById<EditText>(R.id.tytulText)
        val czasEdit = findViewById<EditText>(R.id.czasText)
        val opisEdit = findViewById<EditText>(R.id.opisText)

        returnButton.setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.wyczyscButton).setOnClickListener {
            tytulEdit.setText("")
            czasEdit.setText("")
            opisEdit.setText("")
        }

        // WOJEWÓDZTWA
        val wojewodztwa = resources.getStringArray(R.array.wojewodztwa_array).toList()
        val wojAdapter = createAdapter(wojewodztwa)
        spinnerWoj.adapter = wojAdapter

        spinnerWoj.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                pobierzPowiaty(parent.getItemAtPosition(pos).toString())
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerPowiat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                pobierzGminy(parent.getItemAtPosition(pos).toString())
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerGmina.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                pobierzMiejscowosci(parent.getItemAtPosition(pos).toString())
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerMiejscowosc.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View?, pos: Int, id: Long
                ) {
                    val miasto = parent.getItemAtPosition(pos) as Miasto
                    idMiejscowosci = miasto.id
                }
                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
    }

    // ---------- API ----------
    private fun pobierzPowiaty(woj: String) {
        api.getPowiaty(woj).enqueue(object : Callback<List<String>> {
            override fun onResponse(c: Call<List<String>>, r: Response<List<String>>) {
                if (r.isSuccessful) spinnerPowiat.adapter = createAdapter(r.body() ?: emptyList())
            }
            override fun onFailure(c: Call<List<String>>, t: Throwable) {}
        })
    }

    private fun pobierzGminy(pow: String) {
        api.getGminy(pow).enqueue(object : Callback<List<String>> {
            override fun onResponse(c: Call<List<String>>, r: Response<List<String>>) {
                if (r.isSuccessful) spinnerGmina.adapter = createAdapter(r.body() ?: emptyList())
            }
            override fun onFailure(c: Call<List<String>>, t: Throwable) {}
        })
    }

    private fun pobierzMiejscowosci(gmina: String) {
        api.getMiejscowosci(gmina).enqueue(object : Callback<List<Miasto>> {
            override fun onResponse(c: Call<List<Miasto>>, r: Response<List<Miasto>>) {
                if (r.isSuccessful) spinnerMiejscowosc.adapter = createAdapter(r.body() ?: emptyList())
            }
            override fun onFailure(c: Call<List<Miasto>>, t: Throwable) {}
        })
    }

    // ---------- Adapter ----------
    private fun <T> createAdapter(data: List<T>): ArrayAdapter<T> =
        ArrayAdapter(this, R.layout.spinner_item, data).apply {
            setDropDownViewResource(R.layout.spinner_dropdown_item)
        }
}