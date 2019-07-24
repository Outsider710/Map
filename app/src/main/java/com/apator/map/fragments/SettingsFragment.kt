package com.apator.map.fragments


import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.preference.*
import com.apator.map.R
import com.apator.map.database.Entity.SolarEntity
import com.apator.map.helpers.ValuesGenerator
import com.apator.map.helpers.mappers.SolarListJSONToDb
import com.apator.map.viewmodel.SolarViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class SettingsFragment : PreferenceFragmentCompat() {
    lateinit var preferences: SharedPreferences
    private val generator = ValuesGenerator()
    private val solarViewModel: SolarViewModel by viewModel()
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences = PreferenceManager.getDefaultSharedPreferences(context)

        val apiKeyPreference: EditTextPreference = findPreference(getString(R.string.api_key))!!
        apiKeyPreference.summary =
            preferences.getString(getString(R.string.api_key), getString(R.string.apiKey_summary))
        apiKeyPreference.setOnPreferenceChangeListener { preference, newValue ->
            preference.summary = newValue.toString()
            true
        }
        val lastSyncPreference: Preference = findPreference(getString(R.string.sync_key))!!
        lastSyncPreference.summary =
            preferences.getString(getString(R.string.sync_key), getString(R.string.sync_summary))
        lastSyncPreference.setOnPreferenceClickListener {
            if(generator.isOnline(context!!)) {
                fetchSolars()
                val summary = "${getString(R.string.last_sync)} ${generator.getActualDate()}"
                it.summary = summary
                preferences.edit().putString(getString(R.string.sync_key), summary).apply()
            } else {
                Toast.makeText(context,"No Internet",Toast.LENGTH_SHORT).show()
            }


            true
        }
        val timeWindowPreference: ListPreference = findPreference(getString(R.string.timeWindow_key))!!
        timeWindowPreference.apply { summary = entry }
        timeWindowPreference.setOnPreferenceChangeListener { preference, newValue ->
            preference.summary = getEntry(preference as ListPreference, (newValue as String).toInt())
            true
        }
    }

    private fun fetchSolars() {
        val getPreferences = PreferenceManager.getDefaultSharedPreferences(context!!)
        /*solarViewModel.fetchSolarsAmerica(
            getPreferences.getString(
                getString(R.string.api_key),
                getString(R.string.DATA_API_KEY)
            )!!
        )
        solarViewModel.fetchSolarsAsia(
            getPreferences.getString(
                getString(R.string.api_key),
                getString(R.string.DATA_API_KEY)
            )!!
        )*/
        solarViewModel.fetchAllSolars(
            getPreferences.getString(
                getString(R.string.api_key),
                getString(R.string.DATA_API_KEY)
            )!!
        )
        solarViewModel.solarLiveData.observe(this, Observer { solarList ->
            if(solarList == null) {
                Toast.makeText(context, "Błąd klucza API", Toast.LENGTH_SHORT).show()
                return@Observer
            }
            solarViewModel.insertAllStations(solarList)
        })
    }

    private fun getEntry(preference: ListPreference, newValue: Int): CharSequence {
        return preference.entries[newValue - 1]

    }
}