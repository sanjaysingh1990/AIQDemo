package com.wh.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.wh.aqldemo.R
import com.wh.model.AQIStatus

object Utils {
    fun getStatus(value: Int, context: Context): AQIStatus {
        return when (value) {
            in 0..50 -> {
                AQIStatus(ContextCompat.getColor(context, R.color.color_status_good), "Good","0-50")
            }
            in 51..100 -> {
                AQIStatus(
                    ContextCompat.getColor(context, R.color.color_status_satisfactory),
                    "Satisfactory","51-100"
                )
            }
            in 101..200 -> {
                AQIStatus(
                    ContextCompat.getColor(context, R.color.color_status_moderate),
                    "Moderate","101-200"
                )
            }
            in 201..300 -> {
                AQIStatus(ContextCompat.getColor(context, R.color.color_status_poor), "Poor","201-300")
            }
            in 301..400 -> {
                AQIStatus(
                    ContextCompat.getColor(context, R.color.color_status_verypoor),
                    "Very Poor","301-400"
                )
            }
            in 401..500 -> {
                AQIStatus(ContextCompat.getColor(context, R.color.color_status_severe), "Severe","401-500")
            }
            else -> AQIStatus(ContextCompat.getColor(context, R.color.gray_light), "Unknown")
        }
    }
}