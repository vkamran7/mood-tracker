package com.example.moodtracker.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
private val ISO = DateTimeFormatter.ISO_LOCAL_DATE

@RequiresApi(Build.VERSION_CODES.O)
fun todayIso(): String = LocalDate.now().format(ISO)
@RequiresApi(Build.VERSION_CODES.O)
fun isoToPretty(iso: String): String {
    val d = LocalDate.parse(iso, ISO)
    return d.toString() // keep simple for MVP; later: "Mon, Jan 5"
}