package com.revelhealth.comms

import com.natpryce.Result
import com.natpryce.map
import com.revelhealth.domain.CommunicationChannel
import com.revelhealth.domain.WeatherDetail
import com.revelhealth.weather.DailyWeatherFetcher
import java.time.LocalDate

class ForecastingCommunicationChannelDeterminationService(
    private val dailyWeatherFetcher: DailyWeatherFetcher,
    private val weatherCommsDeterminationService: CommunicationChannelDeterminationService<WeatherDetail>
) {

    fun determineNextFiveDays(): Result<List<CommunicationChannelByDate>, RuntimeException> {
        return dailyWeatherFetcher.getFiveDayForecast().map { forecast ->
            forecast
                .map {
                    CommunicationChannelByDate(
                        it.date,
                        weatherCommsDeterminationService.determineCommunicationChannel(it)
                    )
                }
        }
    }

}

data class CommunicationChannelByDate(
    val date: LocalDate,
    val communicationChannel: CommunicationChannel
)
