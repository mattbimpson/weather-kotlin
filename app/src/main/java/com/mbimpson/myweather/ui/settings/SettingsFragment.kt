package com.mbimpson.myweather.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.mbimpson.myweather.R


class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_settings, container, false)
        val button: Button = root.findViewById(R.id.btnSavePrefs)
        button.setOnClickListener {
            val editCity: EditText = root.findViewById(R.id.edit_city)
            val sharedPrefs = activity?.getPreferences(Context.MODE_PRIVATE)
            if (sharedPrefs != null) {
                with (sharedPrefs.edit()) {
                    putString(getString(R.string.prefs_city_key), editCity.text.toString())
                    commit()
                }
            }
        }

        return root
    }
}