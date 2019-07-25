package com.apator.map.fragments


import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.apator.map.R
import com.apator.map.helpers.ValuesGenerator
import com.apator.map.tools.DrawableToBitmap
import com.apator.map.viewmodel.SolarViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.expressions.Expression
import com.mapbox.mapboxsdk.style.expressions.Expression.get
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.fragment_map.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.random.Random

class MapFragment : Fragment(), OnMapReadyCallback, PermissionsListener {

    companion object {
        const val ICON_ID = "ICON_ID"
        const val LAYER_ID = "LAYER_ID"
        const val SOURCE_ID = "SOURCE_ID"
    }
    private val solarViewModel: SolarViewModel by viewModel()
    private lateinit var mapView: MapView
    private var isFabOpen = false
    private lateinit var geoJson: GeoJsonSource
    val bundle = Bundle()
    private lateinit var mapboxMap: MapboxMap
    private var permissionsManager: PermissionsManager = PermissionsManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        solarViewModel.solarLiveData.observe(this, Observer { solarList ->
            if (solarList == null) {
                Toast.makeText(context, getString(R.string.api_key_error), Toast.LENGTH_SHORT).show()
                return@Observer
            }
            solarList.forEach {
                it.lat += (Random.nextDouble(0.0001, 0.0009))
                it.lon += (Random.nextDouble(0.0001, 0.0009))
            }
            solarViewModel.insertAllStations(solarList)
            Toast.makeText(context, getString(R.string.synchronization_successful), Toast.LENGTH_SHORT).show()
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val getPreferences = PreferenceManager.getDefaultSharedPreferences(context!!)

        val view = inflater.inflate(R.layout.fragment_map, container, false)

        view.map_sync_date.text =
            getPreferences.getString(getString(R.string.sync_key), getString(R.string.sync_summary))

        geoJson = GeoJsonSource(
            SOURCE_ID, GeoJsonOptions()
                .withCluster(true)
                .withClusterRadius(20)
        )
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
            if (ValuesGenerator.isOnline(context!!)) {
//                Toast.makeText(context, "Synchronized", Toast.LENGTH_SHORT).show()
                val getPreference = PreferenceManager.getDefaultSharedPreferences(context)
                val summary = "${getString(R.string.last_sync)} ${ValuesGenerator.getActualDate()}"
                getPreference.edit().putString(getString(R.string.sync_key), summary).apply()
                val syncData = getPreference.getString(getString(R.string.sync_key), getString(R.string.sync_summary))
                map_sync_date.text = syncData
                solarSync()

            } else {
                Toast.makeText(context, getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
            }
        }
        fabreset.setOnClickListener {
            if (!ValuesGenerator.isLocalizationEnabled(context!!)) {
                Toast.makeText(context, getString(R.string.localization_disabled), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            isFabOpen = false
            if (ValuesGenerator.checkLocalizationPermision(context!!)) {
                if (mapboxMap.locationComponent.lastKnownLocation != null) targetCameraOnLocation()
            } else {
                Toast.makeText(context, getString(R.string.app_require_localization_permission), Toast.LENGTH_SHORT)
                    .show()
                requestPermissions(arrayOf(ACCESS_FINE_LOCATION), 99)
            }

        }
        fabsettings.setOnClickListener {
            isFabOpen = false
            view.findNavController().navigate(R.id.action_mapFragment_to_settingsFragment2)
        }
        return view
    }

    private fun targetCameraOnLocation() {
        mapboxMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    mapboxMap.locationComponent.lastKnownLocation!!.latitude,
                    mapboxMap.locationComponent.lastKnownLocation!!.longitude
                ), 5.0
            )
        )
    }

    fun solarSync() {
        val getPreferences = PreferenceManager.getDefaultSharedPreferences(context!!)
        solarViewModel.fetchAllSolars(
            getPreferences.getString(
                getString(R.string.api_key),
                getString(R.string.DATA_API_KEY)
            )!!
        )
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
        solarViewModel.getAllSolars().observe(this, Observer {

            it.forEach { solarEntity ->
                val feature = Feature.fromGeometry(Point.fromLngLat(solarEntity.lon, solarEntity.lat))
                feature.addStringProperty("id", solarEntity.id)

                if (markers.filter { it.getProperty("id").asString == feature.getProperty("id").asString }.isEmpty())
                    markers.add(feature)
            }
            geoJson.setGeoJson(FeatureCollection.fromFeatures(markers))
        })
    }


    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        val bitMapIcon = DrawableToBitmap.drawableToBitmap(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.baseline_location_on_24,
                null
            )!!
        )!!
        mapboxMap.setStyle(
            Style.Builder().fromUrl(getString(R.string.map_url))
                .withSource(geoJson)
                .withImage(ICON_ID, bitMapIcon)
                .withLayers(
                    SymbolLayer(LAYER_ID, SOURCE_ID)
                        .withProperties(
                            iconImage(ICON_ID),
                            iconAllowOverlap(true),
                            iconOffset(arrayOf(0f, -9f)),
                            textField(Expression.toString(get("point_count"))),
                            textOffset(arrayOf(0f, 0.5f))

                        )
                )
        ) { enableLocationComponent(it) }


        syncMarkers()

        mapboxMap.addOnMapClickListener {
            val screenPoint = mapboxMap.projection.toScreenLocation(it)
            val features = mapboxMap.queryRenderedFeatures(screenPoint, LAYER_ID)
            if (features.isNotEmpty()) {
                val selectedFeature = features[0]
                when (selectedFeature.getStringProperty("id")) {
                    null -> {
                        val newZoom = mapboxMap.cameraPosition.zoom + 1
                        val newCameraPosition = CameraPosition.Builder().target(it).zoom(newZoom).build()
                        val cameraUpdate = CameraUpdateFactory.newCameraPosition(newCameraPosition)

                        mapboxMap.animateCamera(cameraUpdate)


                    }
                    else -> {
                        val id = selectedFeature.getStringProperty("id")
                        bundle.putString("id", id)
                        findNavController().navigate(R.id.action_mapFragment_to_passportFragment, bundle)
                    }

                }

            }
            true
        }
    }


    //Location component
    @SuppressLint("MissingPermission")
    private fun enableLocationComponent(loadedMapStyle: Style) {
        if (PermissionsManager.areLocationPermissionsGranted(this.context!!)) {

            val customLocationComponentOptions = LocationComponentOptions.builder(this.context!!)
                .trackingGesturesManagement(true)
                .build()

            val locationComponentActivationOptions =
                LocationComponentActivationOptions.builder(this.context!!, loadedMapStyle)
                    .locationComponentOptions(customLocationComponentOptions)
                    .build()
//
            mapboxMap.locationComponent.apply {

                activateLocationComponent(locationComponentActivationOptions)

                isLocationComponentEnabled = true

            }

        } else {
            permissionsManager = PermissionsManager(this)
            requestPermissions(arrayOf(ACCESS_FINE_LOCATION), 99)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (grantResults[0] == 0)
            onPermissionResult(true)
        else
            onPermissionResult(false)
    }

    override fun onExplanationNeeded(permissionsToExplain: List<String>) {
        Toast.makeText(this.context, getString(R.string.permission_required), Toast.LENGTH_LONG).show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            enableLocationComponent(mapboxMap.style!!)
        } else {
            Toast.makeText(this.context, getString(R.string.permission_denied), Toast.LENGTH_LONG).show()

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

