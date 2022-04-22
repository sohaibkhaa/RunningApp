package com.example.runnigapp.other

import android.content.Context
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.runnigapp.R
import com.example.runnigapp.db.Run
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import java.text.SimpleDateFormat
import java.util.*

class CustomMarkerView(
    val runs: List<Run>,
    c: Context,
    layoutId: Int
) : MarkerView(c, layoutId) {

    override fun getOffset(): MPPointF {
        return MPPointF(-width / 2f, -height.toFloat())

    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        super.refreshContent(e, highlight)
        if (e == null) {
            return
        }
        val currentRunId = e.x.toInt()
        val run = runs[currentRunId]

        val calendar = Calendar.getInstance().apply {
            timeInMillis = run.timestamp
        }
        val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
        findViewById<TextView>(R.id.tvDate).text = dateFormat.format(calendar.time)
        val avgSpeed = "${run.avgSpeedInKMH}km/h"
        findViewById<TextView>(R.id.tvAvgSpeed).text = avgSpeed
        val distanceInKm = "${run.distanceInMeters / 1000f}km"
        findViewById<TextView>(R.id.tvDistance).text = distanceInKm
        findViewById<TextView>(R.id.tvDuration).text =
            TrackingUtility.getFormattedStopWatchTime(run.timeInMillis)
        val caloriesBurned = "${run.caloriesBurned}kcal"
        findViewById<TextView>(R.id.tvCaloriesBurned).text = caloriesBurned
    }
}