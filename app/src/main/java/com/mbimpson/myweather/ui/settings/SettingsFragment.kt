package com.mbimpson.myweather.ui.settings

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mbimpson.myweather.R


class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsViewModel =
            ViewModelProviders.of(this).get(SettingsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_settings, container, false)
        val textView: TextView = root.findViewById(R.id.text_settings)
        settingsViewModel.text.observe(this, Observer {
            textView.text = it
        })

        val editCity: EditText = root.findViewById(R.id.edit_city)
        editCity.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val sharedPrefs = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
                with (sharedPrefs.edit()) {
                    putString(getString(R.string.prefs_city_key), p0.toString())
                    commit()
                }
            }
        })

        /*val editCity: EditText = root.findViewById(R.id.edit_city)
        editCity.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                val sharedPrefs = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
                with (sharedPrefs.edit()) {
                    putString(getString(R.string.prefs_city_key), s.toString())
                    commit()
                }

                val test = ""
            }
        })*/
        return root
    }
}