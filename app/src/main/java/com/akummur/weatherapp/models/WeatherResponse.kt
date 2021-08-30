package com.akummur.weatherapp.models

import android.icu.number.IntegerWidth
import java.io.Serializable

data class WeatherResponse(
        val coord: Coord,
        val weather: List<Weather>,
        val base: String,
        val main: Main,
        val visibility: String,
        val wind: Wind,
        val clouds: Cloud,
        val dt: Int,
        val sys: Sys,
        val timezone: Int,
        val id: Int,
        val name: String,
        val cod: Int
) : Serializable