package com.team3.weatherornot.api

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.team3.weatherornot.weather.Weather

/**
 * Code found at https://stackoverflow.com/questions/28172496/android-volley-how-to-isolate-requests-in-another-class
 *
 * Singleton class for making API calls using application context
 */
class APIManager private constructor(context: Context) {
    private val apiKey = "345319f45656517a0f88de5d5cdf0a7d"
    var requestQueue: RequestQueue = Volley.newRequestQueue(context.applicationContext)

    //saved weather data so we don't make too many api calls
    var weather: Weather? = null


    /**
     * Gets the weather information for the passed in location and calls the listener function to
     * get the result back to the caller
     */
    fun getWeatherForLocation(lat: Double, lon: Double, listener: WeatherAPIListener<Weather>) {
        //if there's already weather data for this location, return that
        if (weather != null && (lat == weather!!.lat && lon == weather!!.lon)) {
            println("SAVED DATA")
            listener.getResult(weather!!)
        } else {
            val apiURL: String = "https://api.openweathermap.org/data/2.5/onecall?appid=$apiKey" +
                    "&lat=$lat&lon=$lon&units=imperial"

            println("API CALL")
            // make api call.
            val request = JsonObjectRequest(Request.Method.GET, apiURL, null,
                {
                    weather = Weather(lat, lon, it)
                    listener.getResult(weather!!)
                },
                {
                    println("Error! $it")
                }
            )

            requestQueue.add(request)
        }
    }

    companion object {
        private var instance: APIManager? = null
        @Synchronized
        fun getInstance(context: Context): APIManager? {
            if (null == instance) instance = APIManager(context)
            return instance
        }

        //this is so you don't need to pass context each time
        @JvmStatic
        @Synchronized
        fun getInstance(): APIManager? {
            checkNotNull(instance) {
                APIManager::class.java.simpleName +
                        " is not initialized, call getInstance(...) first"
            }
            return instance
        }
    }
}