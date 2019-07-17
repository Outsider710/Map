package com.apator.map.fragments


import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation

import com.apator.map.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint


class PassportFragment : Fragment() {

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
