package com.team3.weatherornot.api

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.team3.weatherornot.weather.Weather
import java.util.*

/**
 * Code found at https://stackoverflow.com/questions/28172496/android-volley-how-to-isolate-requests-in-another-class
 *
 * Singleton class for making API calls using application context
 *
 * @constructor Create [APIManager]
 *
 * @param context the application context
 */
class APIManager private constructor(context: Context) {
    private val apiKey = "345319f45656517a0f88de5d5cdf0a7d"
    private var requestQueue: RequestQueue = Volley.newRequestQueue(context.applicationContext)

    //saved weather data so we don't make too many api calls
    var weather: Weather? = null

    /**
     * Get the weather object for a specific location from the saved data if it exists or by
     * calling the open weather API. Then call the listener function to get the result back to the caller
     *
     * @param lat the latitude coordinate of the location
     * @param lon the longitude coordinate of the location
     * @param callback the listener function to be called when the API returns
     */
    fun getWeatherForLocation(lat: Double, lon: Double, callback: (weather: Weather) -> Unit) {
        //if there's already weather data for this location, return that
        if (weather != null && (weather!!.lat == lat && weather!!.lon == lon) && !shouldUpdate()) {
            callback(weather!!)
        } else {
            Thread {
                val apiURL: String =
                    "https://api.openweathermap.org/data/2.5/onecall?appid=$apiKey" +
                            "&lat=$lat&lon=$lon&units=imperial"

                // make api call.
                val request = JsonObjectRequest(Request.Method.GET, apiURL, null,
                    {
                        weather = Weather(lat, lon, it)
                        callback(weather!!)
                    },
                    {
                        println("Error! $it")
                    }
                )

                requestQueue.add(request)
            }.start()
        }
    }

    /**
     * Checks if it has been more than 5 minutes since the last time the weather info has been updated
     *
     * @return true if it has been 5 minutes, false if not
     */
    private fun shouldUpdate(): Boolean {
        val current = Calendar.getInstance().timeInMillis
        val result = current - lastUpdate > 300000 //5 minutes
        lastUpdate = current
        return result
    }

    companion object {
        private var instance: APIManager? = null
        var lastUpdate: Long = 0

        /**
         * Instantiates the APIManager object for the app
         *
         * @param context the application context
         * @return the instance of the APIManager
         */
        @Synchronized
        fun instantiate(context: Context): APIManager? {
            if (instance == null) {
                instance = APIManager(context)
                lastUpdate = Calendar.getInstance().timeInMillis
            }

            return instance
        }

        //this is so you don't need to pass context each time
        /**
         * Returns the instance of the APIManager for the application
         *
         * @return the instance of the APIManager
         */
        @JvmStatic
        @Synchronized
        fun getInstance(): APIManager? {
            checkNotNull(instance) {
                APIManager::class.java.simpleName +
                        " is not initialized, call instantiate() first"
            }
            return instance
        }
    }
}