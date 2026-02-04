package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val back: ImageView = findViewById(R.id.iwBack)
        back.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                finish()
            }
        })
        findViewById<ConstraintLayout>(R.id.rowShare).setOnClickListener {
            shareApp()
        }
        findViewById<ConstraintLayout>(R.id.rowSupport).setOnClickListener {
            writeToSupport()
        }
        findViewById<ConstraintLayout>(R.id.rowOffer).setOnClickListener {
            openUserAgreement()
        }
    }

    private fun shareApp() {

        val shareMassage =
            getString(R.string.share_message, getString(R.string.practicum_android_course_url))
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareMassage)
            type = "text/plain"
        }
        startActivity(intent)
    }
    private fun writeToSupport() {
        val email = getString(R.string.support_email)
        val subject = getString(R.string.email_subject)
        val body = getString(R.string.email_body)
        val mailtoUri = Uri.parse("mailto:$email").buildUpon()
            .appendQueryParameter("subject", subject)
            .appendQueryParameter("body", body)
            .build()
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = mailtoUri
        }
        startActivity(intent)
    }

    private fun openUserAgreement() {
        val url = getString(R.string.offer_url)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }


}