package com.mbimpson.myweather.ui.home

import android.content.Context
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mbimpson.myweather.GlobalVariables
import com.mbimpson.myweather.R
import org.json.JSONObject
import java.net.URL


class HomeFragment : Fragment() {

    private var listener: OnSharedPreferenceChangeListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val titleTextView: TextView = root.findViewById(R.id.text_home)
        val sharedPrefs = activity?.getPreferences(Context.MODE_PRIVATE)
        val city = sharedPrefs?.getString(getString(R.string.prefs_city_key), null)
        titleTextView.text = city

        sharedPrefs?.registerOnSharedPreferenceChangeListener(listener)

        WeatherTask().execute()

        return root
    }

    private fun getLabelText(label: String, id: String, json: JSONObject): String {
        return label + ": " + json.getString(id)
    }

    private fun parseResults(result: String?) {
        try {
            val jsonObj = JSONObject(result)
            val main = jsonObj.getJSONObject("main")
            val temp = getLabelText("temp", "temp", main)+"°C"
            val tempMin = getLabelText("min", "temp_min", main)
            val tempMax = getLabelText("max", "temp_max", main)
            val feelsLike = getLabelText("feels like", "feels_like", main)

            val view = view
            if (view != null) {
                view.findViewById<TextView>(R.id.text_temp).text = temp
                view.findViewById<TextView>(R.id.text_temp_max).text = tempMax
                view.findViewById<TextView>(R.id.text_temp_min).text = tempMin
                view.findViewById<TextView>(R.id.text_feels_like).text = feelsLike
            }
        }catch (e: Exception) {
            this.showToast("An error occurred retrieving weather")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    inner class WeatherTask: AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String? {
            val apiKey = GlobalVariables().ApiKey
            try{
                val sharedPrefs = activity?.getPreferences(Context.MODE_PRIVATE)
                val city = sharedPrefs?.getString(getString(R.string.prefs_city_key), null)
                return URL("https://api.openweathermap.org/data/2.5/weather?q=$city&units=metric&appid=$apiKey").readText(
                    Charsets.UTF_8
                )
            }catch (e: Exception){
                Log.d("WeatherTask", "Error getting weather")
                return null
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            parseResults(result)
        }
    }
}