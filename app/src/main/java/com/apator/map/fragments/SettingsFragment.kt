package com.apator.map.fragments


import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.apator.map.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

    }


}
