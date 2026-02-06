package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val settingsCard: CardView = findViewById(R.id.cvSettings)
        val mediaCard: CardView = findViewById(R.id.cvMedia)
        val searchCard: CardView = findViewById(R.id.cvSearch)
        settingsCard.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)

        }
        mediaCard.setOnClickListener {
            val intent = Intent(this, MediaLibraryActivity::class.java)
            startActivity(intent)
        }
        searchCard.setOnClickListener {

            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

    }
}