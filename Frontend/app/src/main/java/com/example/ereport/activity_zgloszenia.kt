package com.example.ereport

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

// Bartek
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import android.util.Log
import com.example.ereport.model.AppDatabase
import com.example.ereport.model.ReportDao
import com.example.ereport.ui.reports.ReportsViewModel
import com.google.android.gms.location.LocationServices
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ereport.data.network.NetworkUtils
import com.example.ereport.data.sync.SyncService
import com.example.ereport.model.LocalReport
import com.example.ereport.model.ReportStatus

class activity_zgloszenia : AppCompatActivity() {

    private var selectedImageUri: Uri? = null
    private var imageUrl: String? = null

    //Bartek
    private var lat: Double? = null
    private var lon: Double? = null
    private lateinit var viewModel: ReportsViewModel
    private lateinit var dao: ReportDao

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                selectedImageUri = it
                findViewById<ImageView>(R.id.zdjecieImageView)
                    .setImageURI(it)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_zgloszenia)

        val returnButton = findViewById<ImageButton>(R.id.returnButton)
        val dodajZdjecieButton = findViewById<Button>(R.id.zdjecieButton)
        val dalejButton = findViewById<Button>(R.id.dalejButton)
        val wyczyscButton = findViewById<Button>(R.id.wyczyscButton)

        val opisText = findViewById<EditText>(R.id.opisText)
        val kategoriaRadioGroup = findViewById<RadioGroup>(R.id.kategoriaRadioGroup)
        val rozmiarRadioGroup = findViewById<RadioGroup>(R.id.rozmiarRadioGroup)
        val tytulText = findViewById<TextView>(R.id.tytulText)

        //Bartek
        val db = AppDatabase.getDatabase(this)
        dao = db.reportDao()
        viewModel = ReportsViewModel(dao)

        returnButton.setOnClickListener {
            startActivity(
                Intent(this, activity_zgloszeniaostrzezenia::class.java)
            )
        }

        wyczyscButton.setOnClickListener {
            tytulText.setText("")
            opisText.setText("")
        }

        kategoriaRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.radioButton1) {
                rozmiarRadioGroup.visibility = View.VISIBLE
                tytulText.visibility = View.GONE
            } else {
                rozmiarRadioGroup.visibility = View.GONE
                tytulText.visibility = View.VISIBLE
                rozmiarRadioGroup.clearCheck()
            }
        }

        dodajZdjecieButton.setOnClickListener {
            pickImage.launch("image/*")
        }

        dalejButton.setOnClickListener {

            if (selectedImageUri == null) {
                Toast.makeText(
                    this,
                    "Dodaj zdjęcie",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            wyslijZgloszenieDoBackendu(selectedImageUri.toString())
        }

        // LOKALIZACJA - Bartek
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1001
            )
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                lat = location.latitude
                lon = location.longitude
            }
        }   

        // INFO DO UI - Bartek
        lifecycleScope.launch {
            viewModel.reports.collectLatest { list ->
                val last = list.firstOrNull() ?: return@collectLatest
                when (last.status) {
                    ReportStatus.PENDING -> showToast("Zapisano offline")
                    ReportStatus.FAILED -> showToast("Nie udało się wysłać")
                    ReportStatus.SENT -> showToast("Wysłano")
                }
            }
        }

        // Sprawdzanie czy mamy internet - Bartek
        lifecycleScope.launch(Dispatchers.IO) {
            val syncService = SyncService(applicationContext)
            while (true) {
                val hasInternet = NetworkUtils.isInternetAvailable(applicationContext)
                if (hasInternet) {
                    Log.d("SYNC", "Internet OK → sync")
                    syncService.syncReports()
                } else {
                    Log.d("SYNC", "Brak internetu")
                }
                delay(30000)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun uploadImage(
        imageUri: Uri,
        onSuccess: (String) -> Unit,
        onError: () -> Unit
    ) {
        val storage = FirebaseStorage.getInstance()
        val ref = storage.reference
            .child("zdjecia/${UUID.randomUUID()}.jpg")

        ref.putFile(imageUri)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }
                ref.downloadUrl
            }
            .addOnSuccessListener { uri ->
                onSuccess(uri.toString())
            }
            .addOnFailureListener {
                onError()
            }
    }

    // WYSYŁKA DO BACKENDU  - Bartek
    private fun wyslijZgloszenieDoBackendu(localImageUri: String) {

        if (lat == null || lon == null) {
            Toast.makeText(this, "Brak lokalizacji", Toast.LENGTH_SHORT).show()
            return
        }

        val report = LocalReport(
            id = UUID.randomUUID().toString(),
            lat = lat!!,
            lon = lon!!,
            animal = "inne",
            eventTime = System.currentTimeMillis(),
            createdAtLocal = System.currentTimeMillis(),
            localImageUri = localImageUri,
            imageUrl = null,
            status = ReportStatus.PENDING,
            retryCount = 0
        )

        lifecycleScope.launch(Dispatchers.IO) {
            dao.insert(report)
        }

        Toast.makeText(this, "Zapisano offline", Toast.LENGTH_SHORT).show()
    }
}