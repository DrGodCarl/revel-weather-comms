package com.revelhealth.weather

import com.natpryce.Failure
import com.natpryce.valueOrNull
import com.revelhealth.domain.WeatherCondition
import com.revelhealth.retrofit.*
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.every
import io.mockk.mockk
import retrofit2.mock.Calls
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

internal class DailyWeatherFetcherImplTest : StringSpec({
    val weatherClient = mockk<OpenWeatherMapClient>()
    val strategy = DummyWeatherDetailDeterminationStrategy()

    val classUnderTest = DailyWeatherFetcherImpl(
        weatherClient, strategy
    )

    "getFiveDayForecast should coordinate with the api and the strategy" {
        val forecast = ForecastDataWrapper(forecast())

        every { weatherClient.forecast() } returns Calls.response(forecast)

        val result = classUnderTest.getFiveDayForecast()

        val results = result.valueOrNull()
        results.shouldNotBeNull()

        results.size shouldBe 5
        // actual internal data is really decided by the strategy - skipping testing here
        results.map { it.date }.distinct().size shouldBe 5
    }

    "getFiveDayForecast should only take the last five days" {
        val forecast = ForecastDataWrapper(forecast(days = 100))

        every { weatherClient.forecast() } returns Calls.response(forecast)

        val result = classUnderTest.getFiveDayForecast()

        val results = result.valueOrNull()
        results.shouldNotBeNull()

        results.size shouldBe 5
        // actual internal data is really decided by the strategy - skipping testing here
        results.map { it.date }.distinct().size shouldBe 5
    }

    "getFiveDayForecast should bubble up errors" {
        val runtimeError = RuntimeException("Something went wrong")
        every { weatherClient.forecast() } returns Calls.failure(runtimeError)

        val result = classUnderTest.getFiveDayForecast()

        result.shouldBeTypeOf<Failure<*>>()
    }
}) {
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf
}

private fun forecast(days: Long = 5L): List<WeatherData> {
    return (1L..days).map { LocalDateTime.now().plusDays(it).toEpochSecond(ZoneOffset.UTC) }.map {
        WeatherData(
            dt = it,
            main = MainDetails(
                temp = 10.0
            ),
            weather = listOf(
                WeatherDetails(
                    main = "Rain"
                )
            )
        )
    }
}

private class DummyWeatherDetailDeterminationStrategy : WeatherDetailDeterminationStrategy {
    override fun determineWeatherDetails(date: LocalDate, dataPoints: List<WeatherData>): WeatherDetailByDay {
        return WeatherDetailByDay(
            date = date,
            temperature = 10.0,
            weatherCondition = WeatherCondition.Rain
        )
    }
}
