package com.revelhealth.weather

import com.revelhealth.retrofit.WeatherData
import java.time.LocalDate

interface WeatherDetailDeterminationStrategy {

    fun determineWeatherDetails(date: LocalDate, dataPoints: List<WeatherData>): WeatherDetailByDay

}