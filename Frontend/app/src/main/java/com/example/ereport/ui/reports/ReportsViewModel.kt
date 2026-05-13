package com.example.ereport.ui.reports

import androidx.lifecycle.ViewModel
import com.example.ereport.model.LocalReport
import com.example.ereport.model.ReportDao
import kotlinx.coroutines.flow.Flow

class ReportsViewModel(
    private val dao: ReportDao
) : ViewModel() {

    //obserwacja wszystkich zgłoszeń - UI użytkownika
    val reports: Flow<List<LocalReport>> = dao.observeReports()
}