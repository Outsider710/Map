package com.apator.map.fragments


import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.apator.map.R
import com.apator.map.database.Entity.SolarEntity
import com.apator.map.helpers.ValuesGenerator
import com.apator.map.helpers.mappers.SolarListJSONToDb
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
import com.mapbox.mapboxsdk.style.expressions.Expression.*
import com.mapbox.mapboxsdk.style.layers.HeatmapLayer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.fragment_map.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.net.MalformedURLException
import java.net.URL
import kotlin.random.Random
import com.mapbox.mapboxsdk.style.expressions.Expression.zoom
import com.mapbox.mapboxsdk.style.layers.CircleLayer


class MapFragment : Fragment(), OnMapReadyCallback, PermissionsListener {
    private val solarViewModel: SolarViewModel by viewModel()
    private lateinit var mapView: MapView
    private var isFabOpen = false
    private lateinit var geoJson: GeoJsonSource
    val bundle = Bundle()
    private lateinit var mapboxMap: MapboxMap
    private var permissionsManager: PermissionsManager = PermissionsManager(this)
    private var generator = ValuesGenerator()
    private var from = "2014-01-01"
    private var to = "2014-01-03"
    private   val EARTHQUAKE_SOURCE_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=$from&endtime=$to"
    private   val EARTHQUAKE_SOURCE_ID = "earthquakes"
    private   val HEATMAP_LAYER_ID = "earthquakes-heat"
    private   val HEATMAP_LAYER_SOURCE = "earthquakes"
    private   val CIRCLE_LAYER_ID = "earthquakes-circle"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val getPreferences = PreferenceManager.getDefaultSharedPreferences(context!!)

        val view = inflater.inflate(R.layout.fragment_map, container, false)

        view.map_sync_date.text =
            getPreferences.getString(getString(R.string.sync_key), getString(R.string.sync_summary))

        geoJson = GeoJsonSource(
            "SOURCE_ID", GeoJsonOptions()
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
            if (generator.isOnline(context!!)) {
                Toast.makeText(context, "Synchronized", Toast.LENGTH_SHORT).show()
                val getPreference = PreferenceManager.getDefaultSharedPreferences(context)
                val generator = ValuesGenerator()
                val summary = "${getString(R.string.last_sync)} ${generator.getActualDate()}"
                getPreference.edit().putString(getString(R.string.sync_key),summary).apply()
                val syncData  = getPreference.getString(getString(R.string.sync_key),getString(R.string.sync_summary))
                map_sync_date.text = syncData
                solarSync()

            } else {
                Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show()
            }
        }
        fabreset.setOnClickListener {
            isFabOpen = false
            if(generator.checkLocalizationPermision(context!!)){
                if (mapboxMap.locationComponent.lastKnownLocation != null) targetCameraOnLocation()
            }else{
                Toast.makeText(context, "App Require localization Permision", Toast.LENGTH_SHORT).show()
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
        solarViewModel.fetchSolarsAmerica(
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
        )

        solarViewModel.solarLiveData.observe(this, Observer { solarList ->
            val solarEntity = arrayListOf<SolarEntity>()
            solarList.outputs?.allStations?.forEach {
                solarEntity.add(SolarListJSONToDb.map(it!!))
            }
            solarEntity.forEach {

                it.lat += (Random.nextDouble(0.0001, 0.0009))
                it.lon += (Random.nextDouble(0.0001, 0.0009))
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
            Style.Builder().fromUrl("mapbox://styles/helpuspls/cjy8p994g08ot1cmm5t3naox5")
                .withSource(geoJson)
                .withImage("ICON_ID", bitMapIcon)
                .withLayers(
                    SymbolLayer("LAYER_ID", "SOURCE_ID")
                        .withProperties(
                            iconImage("ICON_ID"),
                            iconAllowOverlap(true),
                            iconOffset(arrayOf(0f, -9f)),
                            textField(Expression.toString(get("point_count"))),
                            textOffset(arrayOf(0f, 0.5f))
                        )
                )
        )
        { style ->
      /*      onStyleLoaded(style)
            val localDate = LocalDate.now().minusDays(0)
            Timber.d("${localDate.year}-${localDate.month}-${localDate.dayOfMonth} $localDate")
            addEarthquakeSource(style)
            addHeatmapLayer(style)
            addCircleLayer(style)*/

        syncMarkers()

        mapboxMap.addOnMapClickListener {
            val screenPoint = mapboxMap.projection.toScreenLocation(it)
            val features = mapboxMap.queryRenderedFeatures(screenPoint, "LAYER_ID")
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
    private fun addEarthquakeSource(@NonNull loadedMapStyle: Style) {

        try{
            loadedMapStyle.addSource(GeoJsonSource(EARTHQUAKE_SOURCE_ID, URL(EARTHQUAKE_SOURCE_URL)))
        } catch (malformedUrlException: MalformedURLException){

            Toast.makeText(context, "That's not an url... ", Toast.LENGTH_SHORT).show()
        }


    }


    private fun addCircleLayer(loadedMapStyle: Style){
        val circleLayer = CircleLayer(CIRCLE_LAYER_ID, EARTHQUAKE_SOURCE_ID)
        circleLayer.setProperties(
            // Size circle radius by earthquake magnitude and zoom level
            circleRadius(
                interpolate(
                    linear(), zoom(),
                    literal(7), interpolate(
                        linear(), get("mag"),
                        stop(1, 1),
                        stop(6, 4)
                    ),
                    literal(16), interpolate(
                        linear(), get("mag"),
                        stop(1, 5),
                        stop(6, 50)
                    )
                )
            ),

            // Color circle by earthquake magnitude
            circleColor(
                interpolate(
                    linear(), get("mag"),
                    literal(1), rgba(33, 102, 172, 0),
                    literal(2), rgb(103, 169, 207),
                    literal(3), rgb(209, 229, 240),
                    literal(4), rgb(253, 219, 199),
                    literal(5), rgb(239, 138, 98),
                    literal(6), rgb(178, 24, 43)
                )
            ),

            // Transition from heatmap to circle layer by zoom level
            circleOpacity(
                interpolate(
                    linear(), zoom(),
                    stop(7, 0),
                    stop(8, 1)
                )
            ),
            circleStrokeColor("white"),
            circleStrokeWidth(1.0f)
        )

        loadedMapStyle.addLayerBelow(circleLayer, HEATMAP_LAYER_ID)
    }

    private fun addHeatmapLayer(loadedMapStyle: Style){
        val layer = HeatmapLayer(HEATMAP_LAYER_ID, EARTHQUAKE_SOURCE_ID)
        layer.maxZoom = 2.0F
        layer.sourceLayer = HEATMAP_LAYER_SOURCE
        layer.setProperties(
            heatmapColor(
                interpolate(
                    linear(), heatmapDensity(),
                    literal(0), rgba(33, 102, 172, 0),
                    literal(0.2), rgb(103, 169, 207),
                    literal(0.4), rgb(209, 229, 240),
                    literal(0.6), rgb(253, 219, 199),
                    literal(0.8), rgb(239, 138, 98),
                    literal(1), rgb(178, 24, 43)
                )
            ),

            heatmapWeight(
                interpolate(
                    linear(), get("mag"),
                    stop(0, 0),
                    stop(6, 1)
                )
            ),

            heatmapIntensity(
                interpolate(
                    linear(), zoom(),
                    stop(0, 1),
                    stop(9, 3)
                )
            ),

            heatmapOpacity(
                interpolate(
                    linear(), zoom(),
                    stop(7, 1),
                    stop(9, 0)
                )
            )
        )

        loadedMapStyle.addLayerAbove(layer, "waterway-label")
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
        Toast.makeText(this.context, "wymagana permisja", Toast.LENGTH_LONG).show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            enableLocationComponent(mapboxMap.style!!)
        } else {
            Toast.makeText(this.context, "Permision Denied", Toast.LENGTH_LONG).show()

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