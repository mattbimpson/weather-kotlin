package com.mbimpson.myweather.ui.home

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mbimpson.myweather.GlobalVariables
import com.mbimpson.myweather.R
import java.net.URL
import org.json.JSONObject

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var ApiKey: String = "82f7257b8b5d34b40d1c68d340f6db8d"
    private var City: String = "Bogota"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val titleTextView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(this, Observer {
            // textView.text = it
            var globalVars = GlobalVariables()
            titleTextView.text = globalVars.city
        })

        weatherTask().execute()

        return root
    }

    inner class weatherTask(): AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String? {
            var response:String?
            try{
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$City&units=metric&appid=$ApiKey").readText(
                    Charsets.UTF_8
                )
            }catch (e: Exception){
                response = null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val temp = main.getString("temp")+"°C"
                val tempMin = "min: " + main.getString("temp_min")
                val tempMax = "max: " + main.getString("temp_max")

                val view = view
                if (view != null) {
                    view.findViewById<TextView>(R.id.text_temp).text = temp
                    view.findViewById<TextView>(R.id.text_temp_max).text = tempMax
                    view.findViewById<TextView>(R.id.text_temp_min).text = tempMin
                }
            }catch (e: Exception) {
                // Toast.makeText(this, "An error occurred retreiving weather", Toast.LENGTH_LONG).show()
            }
        }
    }
}