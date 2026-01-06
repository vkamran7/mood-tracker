package com.example.moodtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.room.Room
import com.example.moodtracker.data.DatabaseProvider
import com.example.moodtracker.data.MoodRepository
import com.example.moodtracker.ui.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val db = remember { DatabaseProvider.get(this) }
            val repo = remember { MoodRepository(db.moodDao()) }

            MaterialTheme {
                Surface {
                    App(repo = repo)
                }
            }
        }
    }
}
