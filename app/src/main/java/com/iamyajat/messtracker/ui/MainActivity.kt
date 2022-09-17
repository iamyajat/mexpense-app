package com.iamyajat.messtracker.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.iamyajat.messtracker.R
import com.iamyajat.messtracker.databinding.ActivityMainBinding
import com.iamyajat.messtracker.util.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setupWithNavController(navController)

        sharedPref = getSharedPreferences("tracker", Context.MODE_PRIVATE)!!

        val firstTime = sharedPref.getBoolean("firstTime", true)
        if (firstTime) {
            NotificationHelper.createNotificationChannel(
                this,
                getString(R.string.notif_group),
                "Get reminded about logging your meals",
                "tracker-01"
            )
            sharedPref.edit().putBoolean("firstTime", false).apply()
        }
    }
}