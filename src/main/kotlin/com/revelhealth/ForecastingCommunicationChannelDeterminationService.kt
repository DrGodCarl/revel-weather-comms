package com.revelhealth

import com.natpryce.onFailure
import java.time.LocalDate

class ForecastingCommunicationChannelDeterminationService(
    private val dailyWeatherFetcher: DailyWeatherFetcher,
    private val weatherCommsDeterminationService: WeatherCommunicationChannelDeterminationService
) {

    // TODO - resultify
    fun determineNextFiveDays(): List<CommunicationChannelByDate> {
        val forecast = dailyWeatherFetcher.getWeatherForecast().onFailure { throw it.reason }
        return forecast
            .map {
                CommunicationChannelByDate(
                    it.date,
                    weatherCommsDeterminationService.communicationChannelForWeather(it)
                )
            }
    }

}

data class CommunicationChannelByDate(
    val date: LocalDate,
    val communicationChannel: CommunicationChannel
)
