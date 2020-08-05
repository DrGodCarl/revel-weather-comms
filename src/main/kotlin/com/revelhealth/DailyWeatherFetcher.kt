package com.revelhealth

import com.natpryce.*
import com.revelhealth.retrofit.network
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

class DailyWeatherFetcher(private val weatherClient: OpenWeatherMapClient) {

    fun getWeatherForecast(): Result<List<WeatherDetailByDay>, RuntimeException> {
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

/**
 * Result-safe way to unwrap a value from a map
 */
private fun <K, V, E> Map<K, V>.maybeGet(key: K, error: () -> E): Result<V, E> {
    return this[key]?.let { Success(it) } ?: Failure(error())
}

/**
 * A special case of the above.
 */
private fun <K, V> Map<K, V>.maybeGet(key: K): Result<V, RuntimeException> {
    return this.maybeGet(key) { RuntimeException("They key $key was missing from the map.") }
}

data class WeatherDetailByDay(
    val date: LocalDate,
    override val temperature: Double,
    override val weatherCondition: WeatherCondition
) : WeatherDetail

enum class WeatherCondition {
    Rain,

    // consider adding more as they become relevant
    Other
}
