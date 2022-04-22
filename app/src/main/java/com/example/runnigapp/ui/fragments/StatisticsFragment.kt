package com.example.runnigapp.ui.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.runnigapp.R
import com.example.runnigapp.databinding.FragmentStatisticsBinding
import com.example.runnigapp.databinding.FragmentTrackingBinding
import com.example.runnigapp.other.CustomMarkerView
import com.example.runnigapp.other.TrackingUtility
import com.example.runnigapp.ui.viewmodels.MainViewModel
import com.example.runnigapp.ui.viewmodels.StatisticsViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.round

@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.fragment_statistics) {
    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StatisticsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentStatisticsBinding.bind(view)
        subscribeToObservers()
        setUpBarChart()
    }

    private fun subscribeToObservers() {
        viewModel.totalTimeRun.observe(viewLifecycleOwner) {
            it?.let {
                val totalTimeRun = TrackingUtility.getFormattedStopWatchTime(it)
                binding.tvTotalTime.text = totalTimeRun
            }
        }
        viewModel.totalDistance.observe(viewLifecycleOwner) {
            it?.let {
                val km = it / 1000f
                val totalDistance = round(km * 10f) / 10f
                val totalDistanceString = "${totalDistance}km"
                binding.tvTotalDistance.text = totalDistanceString
            }
        }
        viewModel.totalAvgSpeed.observe(viewLifecycleOwner){
            it?.let {
                val avgSpeed = round(it*10f)/10f
                val avgSpeedString = "${avgSpeed}km/h"
                binding.tvAverageSpeed.text =avgSpeedString
            }
        }
        viewModel.totCaloriesBurned.observe(viewLifecycleOwner){
            it?.let {
                val totalCaloriesBurned = "${it}kcal"
                binding.tvTotalCalories.text = totalCaloriesBurned
            }
        }
        viewModel.runsSortedByDate.observe(viewLifecycleOwner){
            it?.let {
                val allAvgSpeeds = it.indices.map { i-> BarEntry(i.toFloat(), it[i].avgSpeedInKMH) }
                val barDataSet = BarDataSet(allAvgSpeeds, "Avg Speed Over Time").apply {
                    valueTextColor = Color.WHITE
                    color = ContextCompat.getColor(requireContext(), R.color.colorAccent)
                }
                binding.barChart.data = BarData(barDataSet)
                binding.barChart.invalidate()
                binding.barChart.marker = CustomMarkerView(it.reversed(),requireContext(), R.layout.marker_view)
            }
        }
    }

    private fun setUpBarChart(){
        binding.barChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(false)
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }
        binding.barChart.axisLeft.apply {
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }
        binding.barChart.axisRight.apply {
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }
        binding.barChart.apply {
            description.text = "Avg Speed Over Time"
            legend.isEnabled = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}