package com.example.moodtracker.domain

enum class Mood(val emoji: String, val title: String, val score: Int) {
    GREAT("😄", "Great", 4),
    GOOD("🙂", "Good", 3),
    OKAY("😐", "Okay", 2),
    LOW("😔", "Low", 1),
    ANGRY("😡", "Angry", 0);

    companion object {
        fun fromScore(score: Int): Mood = entries.firstOrNull { it.score == score } ?: OKAY
    }
}