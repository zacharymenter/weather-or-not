package com.team3.weatherornot.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationBarView
import com.team3.weatherornot.myapplication.R

class WeeklyWeatherActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.weekly_weather)

        findViewById<NavigationBarView>(R.id.weekly_nav_view).setOnItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_today -> {
                changeActivity(TodayWeatherActivity())
                return true
            }
            R.id.navigation_hourly -> {
                changeActivity(HourlyWeatherActivity())
                return true
            }
        }
        return false
    }

    private fun changeActivity(activity: Activity) {
        val intent = Intent(this, activity::class.java)
        startActivity(intent)
    }

}