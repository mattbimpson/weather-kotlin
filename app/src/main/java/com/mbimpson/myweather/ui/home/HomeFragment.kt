package com.mbimpson.myweather.ui.home

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.mbimpson.myweather.GlobalVariables
import com.mbimpson.myweather.R
import java.net.URL
import org.json.JSONObject

class HomeFragment : Fragment() {

    private var City: String = "Bogota"

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

        weatherTask().execute()

        return root
    }

    inner class weatherTask(): AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String? {
            var response:String?
            val apiKey = GlobalVariables().ApiKey
            try{
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$City&units=metric&appid=$apiKey").readText(
                    Charsets.UTF_8
                )
            }catch (e: Exception){
                response = null
            }
            return response
        }

        private fun getLabelText(label: String, id: String, json: JSONObject): String {
            return label + ": " + json.getString(id)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
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
                // Toast.makeText(this, "An error occurred retreiving weather", Toast.LENGTH_LONG).show()
            }
        }
    }
}