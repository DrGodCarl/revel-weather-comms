package com.revelhealth.comms

import com.natpryce.Failure
import com.natpryce.Success
import com.natpryce.valueOrNull
import com.revelhealth.domain.CommunicationChannel
import com.revelhealth.domain.WeatherCondition
import com.revelhealth.domain.WeatherDetail
import com.revelhealth.weather.DailyWeatherFetcher
import com.revelhealth.weather.WeatherDetailByDay
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.StringSpec
import io.kotest.inspectors.forAll
import io.kotest.inspectors.forOne
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDate

internal class ForecastingCommunicationChannelDeterminationServiceTest : StringSpec({
    val dailyWeatherFetcher = mockk<DailyWeatherFetcher>()
    val weatherCommsDeterminationService = mockk<CommunicationChannelDeterminationService<WeatherDetail>>()

    val classUnderTest =
        ForecastingCommunicationChannelDeterminationService(dailyWeatherFetcher, weatherCommsDeterminationService)

    "determineNextFiveDays should coordinate between forecast and channels if everything works well" {
        val weatherDetails = fiveDaysOfWeather()
        every { dailyWeatherFetcher.getFiveDayForecast() } returns Success(weatherDetails)
        every { weatherCommsDeterminationService.determineCommunicationChannel(any()) } returns CommunicationChannel.TEXT

        val result = classUnderTest.determineNextFiveDays()
        val results = result.valueOrNull()
        results.shouldNotBeNull()

        // each date is represented
        weatherDetails.map { it.date }.forEach { date ->
            results.forOne {
                it.date shouldBe date
            }
        }

        results.forAll {
            it.communicationChannel shouldBe CommunicationChannel.TEXT
        }

        weatherDetails.forEach {
            verify { weatherCommsDeterminationService.determineCommunicationChannel(it) }
        }
    }

    "determineNextFiveDays should bubble up errors" {
        val runtimeError = RuntimeException("Something went wrong")
        every { dailyWeatherFetcher.getFiveDayForecast() } returns Failure(runtimeError)

        val result = classUnderTest.determineNextFiveDays()
        result shouldBe Failure(runtimeError)

        verify(exactly = 0) { weatherCommsDeterminationService.determineCommunicationChannel(any()) }
    }

}) {
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf
}

fun fiveDaysOfWeather(): List<WeatherDetailByDay> {
    var numOfDays: Long = 0
    return generateSequence { LocalDate.now().plusDays(numOfDays++) }
        .map {
            WeatherDetailByDay(
                it,
                77.0,
                WeatherCondition.Rain
            )
        }.take(5).toList()
}
