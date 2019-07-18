package com.apator.map.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import com.apator.map.R
import com.apator.map.database.entitis.SolarEntity
import com.apator.map.database.viewmodels.SolarDbViewModel
import com.apator.map.helpers.mappers.SolarJSONToDb
import com.apator.map.viewmodel.SolarListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.Style
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class MapFragment : Fragment() {
    val solarListViewModel = SolarListViewModel()
    val solarDbViewModel:SolarDbViewModel by viewModel()
    private var mapView: MapView? = null
    private var isFabOpen = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_map, container, false)


        Mapbox.getInstance(context!!, R.string.API_KEY_MAPBOX.toString())

        mapView = view.findViewById(R.id.mapView)
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync { mapboxMap ->

            mapboxMap.setStyle(Style.MAPBOX_STREETS) {



            }


        }
        val fab = view.findViewById<FloatingActionButton>(R.id.fab_more)
        val fabsync = view.findViewById<FloatingActionButton>(R.id.fab_sync)
        val fabreset = view.findViewById<FloatingActionButton>(R.id.fab_reset)
        val fabsettings = view.findViewById<FloatingActionButton>(R.id.fab_settings)
        fab.setOnClickListener {
            when(isFabOpen){
                false -> showFabMenu(fab, fabsync, fabsettings, fabreset)
                true -> hideFabMenu(fab, fabsync, fabsettings, fabreset)
            }
        }
        fabsync.setOnClickListener {
            Toast.makeText(context, "Synchronized",Toast.LENGTH_SHORT).show()
            val current = Date()
            val formatter = SimpleDateFormat("MMM dd yyyy HH:mma")
            view.findViewById<TextView>(R.id.map_sync_date).text = formatter.format(current)

            solarSync()
        }
        fabreset.setOnClickListener {
            Toast.makeText(context, "Location Reseted",Toast.LENGTH_SHORT).show()
        }
        fabsettings.setOnClickListener {
            view.findNavController().navigate(R.id.action_mapFragment_to_settingsFragment2)
            Toast.makeText(context,"Settings",Toast.LENGTH_SHORT).show()
        }
        return view
    }

    private fun solarSync() {


        solarDbViewModel.insertSolar(SolarEntity("asd",50.0,50.0))

        solarListViewModel.fetchSolars()

        solarListViewModel.solarLiveData.observe(this,androidx.lifecycle.Observer {solarList->
            solarList.outputs?.allStations?.forEach {

                val solarEntity = SolarJSONToDb.map(it!!)


            }
        })
    }

    private fun hideFabMenu(
        fab: FloatingActionButton?,
        fabsync: FloatingActionButton?,
        fabsettings: FloatingActionButton?,
        fabreset: FloatingActionButton?
    ) {
        isFabOpen = false
        fab!!.setImageResource(R.drawable.baseline_more_vert_24)
        fabsync?.animate()?.translationY(0f)
        fabreset?.animate()?.translationY(0f)
        fabsettings?.animate()?.translationY(0f)

    }

    private fun showFabMenu(
        fab: FloatingActionButton?,
        fabsync: FloatingActionButton?,
        fabsettings: FloatingActionButton?,
        fabreset: FloatingActionButton?
    ) {
        isFabOpen = true
        fab!!.setImageResource(R.drawable.baseline_arrow_downward_24)
        fabsync?.animate()?.translationY(-200f)
        fabreset?.animate()?.translationY(-400f)
        fabsettings?.animate()?.translationY(-600f)
    }


}

