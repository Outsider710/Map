package com.apator.map.fragments


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.apator.map.R
import com.apator.map.helpers.mappers.SolarDetailsJSONToDb
import com.apator.map.helpers.mappers.SolarDetailsToModel
import com.apator.map.model.Details
import com.apator.map.viewmodel.SolarViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import com.mapbox.mapboxsdk.style.expressions.Expression.array
import kotlinx.android.synthetic.main.fragment_passport.*
import org.koin.android.viewmodel.ext.android.viewModel


class PassportFragment : Fragment() {


    val solarViewModel: SolarViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_passport, container, false)
        val graphac = view.findViewById<GraphView>(R.id.graphAC)
        genereteGraph(graphac, generateGraphLines(600), getString(R.string.ac), getString(R.string.ac_unit), 2137.69)

        val graphdc = view.findViewById<GraphView>(R.id.graphDC)
        genereteGraph(graphdc, generateGraphLines(600), getString(R.string.dc), getString(R.string.dc_unit), 2137.69)

        val graphpoa = view.findViewById<GraphView>(R.id.graphPOA)
        genereteGraph(graphpoa, generateGraphLines(600), getString(R.string.poa), getString(R.string.poa_unit), 2137.69)

        val graphsolrad = view.findViewById<GraphView>(R.id.graphSolRad)
        genereteGraph(graphsolrad, generateGraphLines(10), getString(R.string.solRad), getString(R.string.solRad_unit), 21.37)

        val back = view.findViewById<FloatingActionButton>(R.id.passport_back)
        back.setOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

        val detailsId = arguments!!.getString("id")!!
        var details: Details? = null

        solarViewModel.getDetailsById(detailsId).observe(this, Observer { detailsEntity ->

            if (detailsEntity == null) {
                solarViewModel.fetchSolar(file_id = detailsId)
                solarViewModel.solarDetailsLiveData.observe(this, Observer { solar ->
                    val detailsDb = SolarDetailsJSONToDb.map(solar)
                    solarViewModel.insertDetails(detailsDb)
                    details = SolarDetailsToModel.map(detailsDb)
                })
            } else {
                details = SolarDetailsToModel.map(detailsEntity)

                elevation_value.text = details!!.elevation.toString()
                time_zone_value.text = details!!.tz.toString()
                city_value.text = details!!.city
                state_value.text = details!!.state
            }

        })
        Toast.makeText(context, details.toString(), Toast.LENGTH_SHORT).show()
        if (details != null) {
        }
        return view
    }


    //Test functions
    private fun generateGraphLines(maxval: Int): BarGraphSeries<DataPoint> {
        val dataArray = ArrayList<DataPoint>()
        for (i in 1..12)
            dataArray.add(DataPoint(i*1.0, Math.random() * maxval))
        return BarGraphSeries(dataArray.toTypedArray())
    }

    private fun genereteGraph(
        graphView: GraphView,
        series1: BarGraphSeries<DataPoint>,
        title: String,
        leftvalname: String,
        annualValue: Double
    ) {
        val gridRen = graphView.gridLabelRenderer
        gridRen.numHorizontalLabels = 12
        gridRen.numVerticalLabels = 10
        gridRen.verticalAxisTitle = leftvalname
        gridRen.horizontalAxisTitle = "Annual: " + annualValue + " " + leftvalname
        graphView.title = title
        series1.color = Color.BLUE
        graphView.addSeries(series1)


    }
    //End of Test functions

}
