package com.example.ereport

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.ereport.api.ApiService
import com.example.ereport.api.RetrofitClient
import com.example.ereport.api.UserApi
import com.example.ereport.model.Miasto
import com.example.ereport.model.RegisterResponse
import com.example.ereport.model.UserRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class activity_register : AppCompatActivity() {

    private lateinit var spinnerWoj: Spinner
    private lateinit var spinnerPowiat: Spinner
    private lateinit var spinnerGmina: Spinner
    private lateinit var spinnerMiejscowosc: Spinner

    private var idMiejscowosci: String? = null
    private lateinit var api: UserApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        // ---------- UI ----------
        spinnerWoj = findViewById(R.id.wojewodztwo)
        spinnerPowiat = findViewById(R.id.spinnerPowiat)
        spinnerGmina = findViewById(R.id.spinnerGmina)
        spinnerMiejscowosc = findViewById(R.id.spinnerMiejscowosc)

        val imieEdit = findViewById<EditText>(R.id.imieText)
        val nazwiskoEdit = findViewById<EditText>(R.id.nazwiskoText)
        val emailEdit = findViewById<EditText>(R.id.emailText)
        val telefonEdit = findViewById<EditText>(R.id.telefonText)
        val hasloEdit = findViewById<EditText>(R.id.editTextTextPassword)

        findViewById<ImageButton>(R.id.returnButton).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        findViewById<Button>(R.id.wyczyscButton).setOnClickListener {
            imieEdit.setText("")
            nazwiskoEdit.setText("")
            emailEdit.setText("")
            telefonEdit.setText("")
            hasloEdit.setText("")
        }

        // ---------- Retrofit ----------
        api = RetrofitClient.userApi

        // ---------- Województwa ----------
        val wojewodztwa = resources.getStringArray(R.array.wojewodztwa_array).toList()
        val wojAdapter = createAdapter(wojewodztwa)
        spinnerWoj.adapter = wojAdapter

        spinnerWoj.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                pobierzPowiaty(parent.getItemAtPosition(pos).toString())
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // ---------- Powiat ----------
        spinnerPowiat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                pobierzGminy(parent.getItemAtPosition(pos).toString())
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // ---------- Gmina ----------
        spinnerGmina.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                pobierzMiejscowosci(parent.getItemAtPosition(pos).toString())
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // ---------- Miejscowość ----------
        spinnerMiejscowosc.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                    val miasto = parent.getItemAtPosition(pos) as Miasto
                    idMiejscowosci = miasto.id
                }
                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

        // ---------- Rejestracja ----------
        findViewById<Button>(R.id.dalejButton).setOnClickListener {

            if (idMiejscowosci == null) {
                Toast.makeText(this, "Wybierz miejscowość", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (hasloEdit.text.length < 6) {
                Toast.makeText(this, "Hasło za krótkie", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = UserRequest(
                typ = "O",
                imie = imieEdit.text.toString(),
                nazwisko = nazwiskoEdit.text.toString(),
                email = emailEdit.text.toString(),
                telefon = telefonEdit.text.toString(),
                haslo = hasloEdit.text.toString(),
                miejscowoscId = idMiejscowosci!!
            )

            RetrofitClient.userApi.register(request)
                .enqueue(object : Callback<RegisterResponse> {
                    override fun onResponse(
                        call: Call<RegisterResponse>,
                        response: Response<RegisterResponse>
                    ) {
                        if (response.isSuccessful && response.body()?.success == true) {
                            Toast.makeText(this@activity_register, "Zarejestrowano", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@activity_register, "Błąd rejestracji", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        Toast.makeText(this@activity_register, t.message, Toast.LENGTH_LONG).show()
                    }
                })
        }
    }
    private fun pobierzPowiaty(woj: String) {
        Log.d("SPINNER", "Pobieram powiaty dla: $woj")
        api.getPowiaty(woj).enqueue(object : Callback<List<String>> {
            override fun onResponse(c: Call<List<String>>, r: Response<List<String>>) {
                Log.d("SPINNER", "Powiaty response code: ${r.code()}")
                Log.d("SPINNER", "Powiaty body: ${r.body()}")
                Log.d("SPINNER", "Powiaty error: ${r.errorBody()?.string()}")
                if (r.isSuccessful) spinnerPowiat.adapter = createAdapter(r.body() ?: emptyList())
            }
            override fun onFailure(c: Call<List<String>>, t: Throwable) {
                Log.e("SPINNER", "Powiaty FAIL: ${t.message}", t)
            }
        })
    }

    private fun pobierzGminy(pow: String) {
        Log.d("SPINNER", "Pobieram gminy dla: $pow")
        api.getGminy(pow).enqueue(object : Callback<List<String>> {
            override fun onResponse(c: Call<List<String>>, r: Response<List<String>>) {
                Log.d("SPINNER", "Gminy response code: ${r.code()}")
                Log.d("SPINNER", "Gminy body: ${r.body()}")
                Log.d("SPINNER", "Gminy error: ${r.errorBody()?.string()}")
                if (r.isSuccessful) spinnerGmina.adapter = createAdapter(r.body() ?: emptyList())
            }
            override fun onFailure(c: Call<List<String>>, t: Throwable) {
                Log.e("SPINNER", "Gminy FAIL: ${t.message}", t)
            }
        })
    }

    private fun pobierzMiejscowosci(gmina: String) {
        Log.d("SPINNER", "Pobieram miejscowosci dla: $gmina")
        api.getMiejscowosci(gmina).enqueue(object : Callback<List<Miasto>> {
            override fun onResponse(c: Call<List<Miasto>>, r: Response<List<Miasto>>) {
                Log.d("SPINNER", "Miejscowosci response code: ${r.code()}")
                Log.d("SPINNER", "Miejscowosci body: ${r.body()}")
                Log.d("SPINNER", "Miejscowosci error: ${r.errorBody()?.string()}")
                if (r.isSuccessful) spinnerMiejscowosc.adapter = createAdapter(r.body() ?: emptyList())
            }
            override fun onFailure(c: Call<List<Miasto>>, t: Throwable) {
                Log.e("SPINNER", "Miejscowosci FAIL: ${t.message}", t)
            }
        })
    }

    // ---------- Adapter ----------
    private fun <T> createAdapter(data: List<T>): ArrayAdapter<T> =
        ArrayAdapter(this, R.layout.spinner_item, data).apply {
            setDropDownViewResource(R.layout.spinner_dropdown_item)
        }
}