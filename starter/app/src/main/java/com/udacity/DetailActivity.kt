package com.udacity

import android.app.NotificationManager
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    lateinit var nameText: TextView
    lateinit var statusText: TextView
    lateinit var okButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.cancel(MainActivity.NOTIFICATION_ID)

        nameText = findViewById(R.id.name)
        statusText = findViewById(R.id.status)
        okButton = findViewById(R.id.button)

        val prefs = getSharedPreferences("download", 0)
        val name = prefs.getString("download name", "").toString()
        val status = prefs.getString("download status", "Fail").toString()

        nameText.text = name
        statusText.text = status
        if (status == "Success") {
            statusText.setTextColor(Color.GREEN)
        } else {
            statusText.setTextColor(Color.RED)
        }

        okButton.setOnClickListener { finish() }
    }


}
