package com.team3.weatherornot

import com.team3.weatherornot.api.Weather
import org.junit.Test
// import androidx.test.core.app.ApplicationProvider
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


/**
 * Tests for the API wrapper class
 */
@RunWith(RobolectricTestRunner::class)
class APITests {
    private val api = Weather(44.811348, -91.498497) //EC

    @Test
    fun test_timezone() {
        val timezone = api.getTimeZone()
        assert(timezone == "America/Chicago")
    }

    @Test
    fun test_getCurrentWeather() {
        val current = api.currentWeather
        assert(current.time > 0)

        val temp = current.temp
        assert(temp > -200)
        assert(temp < 200)

        val precip = current.precip
        assert(precip >= 0)
        assert(precip <= 1)

        assert(current.condition.isNotBlank())
    }

    @Test
    fun test_getHourlyWeather() {
        val hourly = api.hourlyWeather
        assert(hourly.size == 48)

        for (hour in hourly) {
            assert(hour.time > 0)

            val temp = hour.temp
            assert(temp > -200)
            assert(temp < 200)

            val precip = hour.precip
            assert(precip >= 0)
            assert(precip <= 1)

            assert(hour.condition.isNotBlank())
        }
    }

    @Test
    fun test_getHourlyWeatherForHours() {
        val hourly = api.getHourlyWeatherForHours(10)
        assert(hourly.size == 10)

        for (hour in hourly) {
            assert(hour.time > 0)

            val temp = hour.temp
            assert(temp > -200)
            assert(temp < 200)

            val precip = hour.precip
            assert(precip >= 0)
            assert(precip <= 1)

            assert(hour.condition.isNotBlank())
        }
    }

    @Test
    fun test_getWeeklyWeather() {
        val daily = api.weeklyWeather
        assert(daily.size == 7)

        for (day in daily) {
            assert(day.time > 0)

            val minTemp = day.minTemp
            assert(minTemp > -200)
            assert(minTemp < 200)

            val maxTemp = day.maxTemp
            assert(maxTemp > -200)
            assert(maxTemp < 200)

            val precip = day.precip
            assert(precip >= 0)
            assert(precip <= 1)

            assert(day.condition.isNotBlank())
        }
    }

    @Test
    fun test_getSpecificDayWeather() {
        val day = api.getSpecificDayWeather(2)

        assert(day.time > 0)

        val minTemp = day.minTemp
        assert(minTemp > -200)
        assert(minTemp < 200)

        val maxTemp = day.maxTemp
        assert(maxTemp > -200)
        assert(maxTemp < 200)

        val precip = day.precip
        assert(precip >= 0)
        assert(precip <= 1)

        assert(day.condition.isNotBlank())
    }
}