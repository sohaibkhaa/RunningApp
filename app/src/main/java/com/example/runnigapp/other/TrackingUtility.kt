package com.example.runnigapp.other

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.core.content.ContextCompat
import com.example.runnigapp.services.PolyLine
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.concurrent.TimeUnit

object TrackingUtility {
    val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    val locationPermissionsQ = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
    )

    fun hasLocationPermissions(context: Context) =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
        } else {

            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
        }

    fun calculatePolyLineLength(polyLine: PolyLine): Float {
        var distance = 0f
        for (i in 0..polyLine.size - 2) {
            val pos1 = polyLine[i]
            val pos2 = polyLine[i + 1]
            val result  = FloatArray(1)
            Location.distanceBetween(
                pos1.latitude,
                pos1.longitude,
                pos2.latitude,
                pos1.longitude,
                result
            )
            distance += result[0]
        }
        return distance
    }

    fun getFormattedStopWatchTime(ms: Long, includeMillis: Boolean = false): String {
        var milliSeconds = ms
        val hours = TimeUnit.MILLISECONDS.toHours(milliSeconds)
        milliSeconds -= TimeUnit.HOURS.toMillis(hours)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliSeconds)
        milliSeconds -= TimeUnit.MINUTES.toMillis(minutes)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliSeconds)
        val f: NumberFormat = DecimalFormat("00")
        if (!includeMillis) {
//            return "${if (hours < 10) "0" else ""}$hours:" + //adding 0 before value less tan 10
//                    "${if (minutes < 10) "0" else ""}$minutes:" +
//                    "${if (seconds < 10) "0" else ""}$seconds"
            return "${f.format(hours)}:${f.format(minutes)}:${f.format(seconds)}"

        }
        // adding millis at the end
        milliSeconds -= TimeUnit.SECONDS.toMillis(seconds)
        milliSeconds /= 10
//        return "${if (hours < 10) "0" else ""}$hours:" + //adding 0 before value less tan 10
//                "${if (minutes < 10) "0" else ""}$minutes:" +
//                "${if (seconds < 10) "0" else ""}$seconds:" +
//                "${if (milliSeconds < 10) "0" else ""}$milliSeconds"
        return "${f.format(hours)}:${f.format(minutes)}:${f.format(seconds)}:${f.format(milliSeconds)}"

    }

}