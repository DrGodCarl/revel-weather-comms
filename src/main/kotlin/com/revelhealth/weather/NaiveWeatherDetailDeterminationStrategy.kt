package com.revelhealth.weather

import com.revelhealth.domain.WeatherCondition
import com.revelhealth.retrofit.WeatherData
import java.time.LocalDate

class NaiveWeatherDetailDeterminationStrategy : WeatherDetailDeterminationStrategy {
    override fun determineWeatherDetails(date: LocalDate, dataPoints: List<WeatherData>): WeatherDetailByDay {
        // no day is a single temperature. This is naive and best-effort.
        val averageTemp = dataPoints.map { it.main.temp }.average()
        // no day is a single weather condition. This is naive and best-effort.
        // It may be that we want to call it rainy when some threshold is met instead.
        val mostCommonWeather = dataPoints
            .flatMap { it.weather }
            .map { it.main }
            .groupingBy { it }
            .eachCount()
            .maxBy { it.value }
            ?.key
        val weather = WeatherCondition.Rain.takeIf { mostCommonWeather == "Rain" }
            ?: WeatherCondition.Other
        return WeatherDetailByDay(
            date = date,
            temperature = averageTemp,
            weatherCondition = weather
        )
    }
}
