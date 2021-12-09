package com.team3.weatherornot.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputLayout
import com.team3.weatherornot.R
import com.team3.weatherornot.api.APIManager
import com.team3.weatherornot.database.Dao
import com.team3.weatherornot.weather.DailyWeather

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SuggestFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SuggestFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_suggest, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        APIManager.getInstance()?.getWeatherForLocation(44.8113, -91.4985) { weather ->
            val items: List<String> = weather.weeklyWeather.map { it.getDayAbbreviation() } //might want to format the day differently
            val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
            val textField = view.findViewById<TextInputLayout>(R.id.suggest_drop_down)
            (textField.editText as? AutoCompleteTextView)?.setAdapter(adapter)

            // this will execute after something is selected in the dropdown menu
            textField.editText?.doAfterTextChanged {
                val selectedDayTV = view.findViewById<TextView>(R.id.suggest_date)
                selectedDayTV.text = it.toString()

                for (day in weather.weeklyWeather) {
                    if (day.getDayAbbreviation() == it.toString()) {
                        findActivitiesForDay(day, view)
                    }
                }
            }
        }
    }

    private fun findActivitiesForDay(day: DailyWeather, view: View) {
        val weatherActivities = Dao.getJson(view.context.applicationContext)
        println(weatherActivities.toString())

        for (activity in weatherActivities) {
            if (activity.Min_Temperature < day.minTemp || activity.Max_Temperature > day.maxTemp) {
                println(activity.Activity_Name)
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SuggestFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SuggestFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}