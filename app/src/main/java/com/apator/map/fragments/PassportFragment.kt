package com.apator.map.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.anychart.APIlib
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.apator.map.R
import com.apator.map.helpers.mappers.SolarDetailsJSONToDb
import com.apator.map.helpers.mappers.SolarDetailsToModel
import com.apator.map.model.Details
import com.apator.map.viewmodel.SolarViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_passport.*
import kotlinx.android.synthetic.main.fragment_passport.view.*
import org.koin.android.viewmodel.ext.android.viewModel


class PassportFragment : Fragment() {

    private val solarViewModel: SolarViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_passport, container, false)


        val back = view.passport_back
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

                panel_name.text = "${details!!.city}:$detailsId"
                elevation_value.text = details!!.elevation.toString()
                time_zone_value.text = details!!.tz.toString()
                city_value.text = details!!.city
                state_value.text = details!!.state
                solarRes_file_value.text = detailsId

                APIlib.getInstance().setActiveAnyChartView(view.ac_any_chart_view)
                genereteGraph(view.ac_any_chart_view,generateGraphLines(details!!.acMontly),getString(R.string.ac),getString(R.string.ac_unit))
                APIlib.getInstance().setActiveAnyChartView(view.dc_any_chart_view)
                genereteGraph(view.dc_any_chart_view,generateGraphLines(details!!.dcMontly),getString(R.string.dc),getString(R.string.dc_unit))
                APIlib.getInstance().setActiveAnyChartView(view.poa_any_chart_view)
                genereteGraph(view.poa_any_chart_view,generateGraphLines(details!!.poaMontly),getString(R.string.poa),getString(R.string.poa_unit))
                APIlib.getInstance().setActiveAnyChartView(view.solrad_any_chart_view)
                genereteGraph(view.solrad_any_chart_view,generateGraphLines(details!!.solradMontly),getString(R.string.solRad),getString(R.string.solRad_unit))
            }

        })



        if (details != null) {
        }
        return view
    }


    //Test functions
    private fun generateGraphLines(valuesArray: ArrayList<Double>): ArrayList<DataEntry> {
        val dataArray = ArrayList<DataEntry>()
        val months = resources.getStringArray(R.array.Months)
        for (i in 0..11)
            dataArray.add(ValueDataEntry(months[i], valuesArray[i]))

        return dataArray
    }

    private fun genereteGraph(
        anyChartView: AnyChartView,
        data: ArrayList<DataEntry>,
        title: String,
        yAxisTitle: String
    ) {
        val chart = AnyChart.column()
        chart.data(data)
        chart.getSeriesAt(0).name(title)
        chart.title(title)
            .yAxis(0).title(yAxisTitle)
        anyChartView.setChart(chart)

    }
    //End of Test functions

}
