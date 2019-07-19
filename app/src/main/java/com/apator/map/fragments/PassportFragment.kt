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
        genereteGraph(graphac, generateGraphLines(600), "AC", "KW/h AC", 2137.69)

        val graphdc = view.findViewById<GraphView>(R.id.graphDC)
        genereteGraph(graphdc, generateGraphLines(600), "DC", "KW/h DC", 2137.69)

        val graphpoa = view.findViewById<GraphView>(R.id.graphPOA)
        genereteGraph(graphpoa, generateGraphLines(600), "POA", "kWh/m2", 2137.69)

        val graphsolrad = view.findViewById<GraphView>(R.id.graphSolRad)
        genereteGraph(graphsolrad, generateGraphLines(10), "Solar Radiation", "kWh/m2/day", 21.37)

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
        return BarGraphSeries(
            arrayOf(
                DataPoint(1.0, Math.random() * maxval),
                DataPoint(2.0, Math.random() * maxval),
                DataPoint(3.0, Math.random() * maxval),
                DataPoint(4.0, Math.random() * maxval),
                DataPoint(5.0, Math.random() * maxval),
                DataPoint(6.0, Math.random() * maxval),
                DataPoint(7.0, Math.random() * maxval),
                DataPoint(8.0, Math.random() * maxval),
                DataPoint(9.0, Math.random() * maxval),
                DataPoint(10.0, Math.random() * maxval),
                DataPoint(11.0, Math.random() * maxval),
                DataPoint(12.0, Math.random() * maxval)
            )
        )
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
