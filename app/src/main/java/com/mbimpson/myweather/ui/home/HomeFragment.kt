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

    data class Temps(val temp: String, val min: String, val max: String, val feelsLike: String)

    private fun getTemps(result: String?): Temps {
        try {
            val jsonObj = JSONObject(result)
            val main = jsonObj.getJSONObject("main")
            val temp = main.getString("temp")
            val tempMin = main.getString("temp_min")
            val tempMax = main.getString("temp_max")
            val feelsLike = main.getString("feels_like")

            return Temps(temp, tempMin, tempMax, feelsLike)
        }catch(e: Exception) {
            showToast("Error displaying weather results")
            return Temps("", "", "", "")
        }
    }

    private fun parseResults(result: String?) {
        val temps = getTemps(result)
        val view = view
        if (view != null) {
            view.findViewById<TextView>(R.id.text_temp).text = "temp: " + temps.temp +"°C"
            view.findViewById<TextView>(R.id.text_temp_max).text = "max: " +temps.max
            view.findViewById<TextView>(R.id.text_temp_min).text = "min: " + temps.min
            view.findViewById<TextView>(R.id.text_feels_like).text = "feels like: " + temps.feelsLike
        }
    }

    private fun setBackgroundColour(temp: String) {
        if (temp == null || temp == "") {
            return
        }
        if (temp.toDouble() < 10) {
            view?.setBackgroundResource(R.drawable.bg_gradient_cold)
            return
        }
        if (temp.toDouble() < 22) {
            view?.setBackgroundResource(R.drawable.bg_gradient_med)
            return
        }
        if (temp.toDouble() >= 22) {
            view?.setBackgroundResource(R.drawable.bg_gradient_hot)
            return
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
            setBackgroundColour(getTemps(result).temp)
        }
    }
}