package com.revelhealth.weather

import com.natpryce.*
import com.revelhealth.domain.WeatherDetail
import com.revelhealth.domain.WeatherCondition
import com.revelhealth.retrofit.OpenWeatherMapClient
import com.revelhealth.retrofit.WeatherData
import com.revelhealth.retrofit.network
import com.revelhealth.util.maybeGet
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

class DailyWeatherFetcherImpl(private val weatherClient: OpenWeatherMapClient) : DailyWeatherFetcher {

    override fun getWeatherForecast(): Result<List<WeatherDetailByDay>, RuntimeException> {
        // get the forecast, but bail early if there are network problems.
        val forecast = network { weatherClient.forecast() }.onFailure { return it }
        // group the data by the dates
        val dateToWeatherData =
            forecast.list.groupBy {
                // TODO - double check that this is giving me the right dates for the timestamps
                ZonedDateTime.ofInstant(Instant.ofEpochSecond(it.dt), ZoneId.of("America/Chicago")).toLocalDate()
            }
        // we only want forecasted dates - some data points are for "today"
        val forecastDates = dateToWeatherData.keys.sorted().takeLast(5)
        return forecastDates.map { date ->
            // the `maybeGet` should _never_ fail, but if it does we'll have something to go off of
            dateToWeatherData.maybeGet(date).map {
                determineWeatherDetails(
                    date, it
                )
            }
        }.allValues()
    }

    private fun determineWeatherDetails(date: LocalDate, dataPoints: List<WeatherData>): WeatherDetailByDay {
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
