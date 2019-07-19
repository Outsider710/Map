package com.apator.map.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.apator.map.R
import com.apator.map.database.Entity.SolarEntity
import com.apator.map.helpers.mappers.SolarJSONToDb
import com.apator.map.tools.DrawableToBitmap
import com.apator.map.viewmodel.SolarViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import kotlinx.android.synthetic.main.fragment_map.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MapFragment : Fragment(), OnMapReadyCallback {
    private val solarViewModel: SolarViewModel by viewModel()
    private lateinit var mapView: MapView
    private var isFabOpen = false
    private val geoJson = GeoJsonSource("SOURCE_ID")
    val bundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_map, container, false)


        Mapbox.getInstance(context!!, R.string.API_KEY_MAPBOX.toString())


        mapView = view.mapView
        mapView.apply {
            getMapAsync {
                onMapReady(it)
            }
            onCreate(savedInstanceState)
        }


        val fab = view.findViewById<FloatingActionButton>(R.id.fab_more)
        val fabsync = view.findViewById<FloatingActionButton>(R.id.fab_sync)
        val fabreset = view.findViewById<FloatingActionButton>(R.id.fab_reset)
        val fabsettings = view.findViewById<FloatingActionButton>(R.id.fab_settings)
        fab.setOnClickListener {
            when (isFabOpen) {
                false -> showFabMenu(fab, fabsync, fabsettings, fabreset)
                true -> hideFabMenu(fab, fabsync, fabsettings, fabreset)
            }
        }
        fabsync.setOnClickListener {
            Toast.makeText(context, "Synchronized", Toast.LENGTH_SHORT).show()
            val current = Date()
            val formatter = SimpleDateFormat("MMM dd yyyy HH:mma")
            view.findViewById<TextView>(R.id.map_sync_date).text = formatter.format(current)

            solarSync()
        }
        fabreset.setOnClickListener {
            isFabOpen = false
            view.findNavController().navigate(R.id.action_mapFragment_to_passportFragment)
        }
        fabsettings.setOnClickListener {
            isFabOpen = false
            view.findNavController().navigate(R.id.action_mapFragment_to_settingsFragment2)
        }
        return view
    }

    private fun solarSync() {
        solarViewModel.fetchSolarsAmerica()
        solarViewModel.fetchSolarsAsia()

        solarViewModel.solarLiveData.observe(this, androidx.lifecycle.Observer { solarList ->
            val solarEntity = arrayListOf<SolarEntity>()
            solarList.outputs?.allStations?.forEach {
                solarEntity.add(SolarJSONToDb.map(it!!))
            }
            solarViewModel.insertAllStations(solarEntity)


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

    private fun syncMarkers() {
        val markers = ArrayList<Feature>()
        solarViewModel.getAllSolars().observe(this, androidx.lifecycle.Observer {

            it.forEach { solarEntity ->
                val feature = Feature.fromGeometry(Point.fromLngLat(solarEntity.lon, solarEntity.lat))
                feature.addStringProperty("id",solarEntity.id)
                markers.add(feature)
            }
            geoJson.setGeoJson(FeatureCollection.fromFeatures(markers))
        })
    }


    override fun onMapReady(mapboxMap: MapboxMap) {
        val bitMapIcon = DrawableToBitmap.drawableToBitmap(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.baseline_location_on_24,
                null
            )!!
        )!!
        mapboxMap.setStyle(
            Style.Builder().fromUrl("mapbox://styles/helpuspls/cjy8p994g08ot1cmm5t3naox5")
                .withSource(geoJson)
                .withImage("ICON_ID", bitMapIcon)
                .withLayer(
                    SymbolLayer("LAYER_ID", "SOURCE_ID")
                        .withProperties(
                            PropertyFactory.iconImage("ICON_ID"),
                            iconAllowOverlap(true),
                            iconOffset(arrayOf(0f, -9f))
                        )
                )
        )
        syncMarkers()
        mapboxMap.addOnMapClickListener {
            val screenPoint = mapboxMap.projection.toScreenLocation(it)
            val features = mapboxMap.queryRenderedFeatures(screenPoint, "LAYER_ID")
            if (features.isNotEmpty()) {
                val selectedFeature = features[0]
                val id = selectedFeature.getStringProperty("id")
                bundle.putString("id",id)
                Navigation.findNavController(view!!).navigate(R.id.action_mapFragment_to_passportFragment,bundle)
            }
            true
        }

    }
    //LifeCycle
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }


}

