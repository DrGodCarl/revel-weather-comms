package com.revelhealth.weather

import com.natpryce.Result
import com.natpryce.allValues
import com.natpryce.map
import com.natpryce.onFailure
import com.revelhealth.domain.WeatherCondition
import com.revelhealth.retrofit.OpenWeatherMapClient
import com.revelhealth.retrofit.WeatherData
import com.revelhealth.retrofit.network
import com.revelhealth.util.maybeGet
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

class DailyWeatherFetcherImpl(
    private val weatherClient: OpenWeatherMapClient,
    private val strategy: WeatherDetailDeterminationStrategy
) : DailyWeatherFetcher {

    override fun getFiveDayForecast(): Result<List<WeatherDetailByDay>, RuntimeException> {
        // get the forecast, but bail early if there are network problems.
        val forecast = network { weatherClient.forecast() }.onFailure { return it }
        // group the data by the dates
        val dateToWeatherData =
            forecast.list.groupBy {
                ZonedDateTime.ofInstant(Instant.ofEpochSecond(it.dt), ZoneId.of("America/Chicago")).toLocalDate()
            }
        // we only want forecasted dates - some data points are for "today"
        val forecastDates = dateToWeatherData.keys.sorted().takeLast(5)
        return forecastDates.map { date ->
            // the `maybeGet` should _never_ fail, but if it does we'll have something to go off of
            dateToWeatherData.maybeGet(date).map {
                strategy.determineWeatherDetails(
                    date, it
                )
            }
        }.allValues()
    }

}
