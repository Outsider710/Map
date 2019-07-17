package com.apator.map.fragments


import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.apator.map.R
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.LegendRenderer
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint


class PassportFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_passport, container, false)
        val graph = view.findViewById<GraphView>(R.id.graph)
        genereteGraph(graph, generateGraphLines(),generateGraphLines(),generateGraphLines())
        return view
    }

    //Test functions
    private fun generateGraphLines(): BarGraphSeries<DataPoint> {
        return BarGraphSeries(
            arrayOf(
                DataPoint(1.0, Math.random() * 600),
                DataPoint(2.0, Math.random() * 600),
                DataPoint(3.0, Math.random() * 600),
                DataPoint(4.0, Math.random() * 600),
                DataPoint(5.0, Math.random() * 600),
                DataPoint(6.0, Math.random() * 600),
                DataPoint(7.0, Math.random() * 600),
                DataPoint(8.0, Math.random() * 600),
                DataPoint(9.0, Math.random() * 600),
                DataPoint(10.0, Math.random() * 600),
                DataPoint(11.0, Math.random() * 600),
                DataPoint(12.0, Math.random() * 600)
            )
        )
    }

    private fun genereteGraph(
        graphView: GraphView,
        series1: BarGraphSeries<DataPoint>,
        series2: BarGraphSeries<DataPoint>,
        series3: BarGraphSeries<DataPoint>
    ) {
        val gridRen = graphView.gridLabelRenderer
        val legendRenderer = graphView.legendRenderer
        gridRen.numHorizontalLabels = 12
        gridRen.numVerticalLabels = 10
        series1.color = Color.YELLOW
        series1.title = "AC"
        series2.color = Color.RED
        series2.title = "DC"
        series3.title = "XD"
        graphView.addSeries(series1)
        graphView.addSeries(series2)
        graphView.addSeries(series3)
        legendRenderer.isVisible = true
        legendRenderer.align = LegendRenderer.LegendAlign.TOP

}
    //End of Test functions

}
