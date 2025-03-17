package com.almax.simpleforegroundservice

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.almax.simpleforegroundservice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUi()
    }

    private fun setupUi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS), 200
            )
        }

        binding.apply {
            btnStart.setOnClickListener {
                startForegroundService(Intent(this@MainActivity, ForegroundService::class.java))
            }
            btnStop.setOnClickListener {
                stopService(Intent(this@MainActivity, ForegroundService::class.java))
            }
        }
    }
}