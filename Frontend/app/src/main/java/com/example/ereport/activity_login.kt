package com.example.ereport

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.ereport.api.RetrofitClient
import com.example.ereport.model.LoginRequest
import com.example.ereport.model.LoginResponse
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

class activity_login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val loginButton = findViewById<Button>(R.id.loginButton)
        val logButton = findViewById<ImageButton>(R.id.logButton)
        val returnButton = findViewById<ImageButton>(R.id.returnButton)

        returnButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        logButton.setOnClickListener {
            startActivity(Intent(this, activity_zgloszeniaostrzezenia::class.java))
        }

        val emailInput = findViewById<EditText>(R.id.emailText)
        val passwordInput = findViewById<EditText>(R.id.hasloText)
        val phoneInput = findViewById<EditText>(R.id.telefonText)

        val wyczyscButton = findViewById<Button>(R.id.wyczyscButton)
        wyczyscButton.setOnClickListener {
            emailInput.setText("")
            phoneInput.setText("")
            passwordInput.setText("")
        }

        loginButton.setOnClickListener {

            val request = LoginRequest(
                email = emailInput.text.toString(),
                telefon = phoneInput.text.toString(),
                haslo = passwordInput.text.toString()
            )


            RetrofitClient.userApi.login(request)
                .enqueue(object : Callback<LoginResponse> {

                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        android.util.Log.d("LOGIN", "kod: ${response.code()}")
                        android.util.Log.d("LOGIN", "body: ${response.body()}")
                        android.util.Log.d("LOGIN", "success: ${response.body()?.success}")
                        android.util.Log.d("LOGIN", "miejscowoscId: ${response.body()?.miejscowoscId}")

                        if (response.isSuccessful && response.body()?.success == true) {
                            val miejscowoscId = response.body()?.miejscowoscId ?: ""

                            getSharedPreferences("session", MODE_PRIVATE)
                                .edit()
                                .putString("miejscowoscId", miejscowoscId)
                                .apply()

                            android.util.Log.d("LOGIN", "przed startActivity")

                            runOnUiThread {
                                android.util.Log.d("LOGIN", "w runOnUiThread")
                                Toast.makeText(this@activity_login, "Zalogowano", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@activity_login, activity_zgloszeniaostrzezenia::class.java))
                                finish()
                            }
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        android.util.Log.d("LOGIN", "błąd: ${t.message}")
                        Toast.makeText(this@activity_login, "Błąd połączenia", Toast.LENGTH_SHORT).show()
                    }

                })
        }
    }
}
