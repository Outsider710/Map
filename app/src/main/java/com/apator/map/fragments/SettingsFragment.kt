package com.apator.map.fragments


import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.apator.map.R

class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var sharedPreference: SharedPreferences
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apiKeyPreference:EditTextPreference = findPreference(getString(R.string.API_KEY_MAPBOX))!!
        apiKeyPreference.summary = apiKeyPreference.summary
        apiKeyPreference.setOnPreferenceChangeListener { preference, value ->
            apiKeyPreference.summary = value as String
            true
        }
        val lastSyncPreference: Preference = findPreference(getString(R.string.sync_key))!!
        lastSyncPreference.summary = lastSyncPreference.summary
        lastSyncPreference.setOnPreferenceClickListener{
            it.summary = "Last Sync: Now"
            true
        }
//        val timeWindowPreference:EditTextPreference = findPreference(getString(R.string.timeWindow_key))!!
//        timeWindowPreference.summary = apiKeyPreference.text
//        timeWindowPreference.setOnPreferenceChangeListener { preference, value ->
//            timeWindowPreference.summary = value as String
//            true
//        }
    }

}
