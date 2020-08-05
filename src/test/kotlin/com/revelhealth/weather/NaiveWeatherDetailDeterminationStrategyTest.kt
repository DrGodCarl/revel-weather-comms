package com.revelhealth.weather

import com.revelhealth.domain.WeatherCondition
import com.revelhealth.retrofit.MainDetails
import com.revelhealth.retrofit.WeatherData
import com.revelhealth.retrofit.WeatherDetails
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

internal class NaiveWeatherDetailDeterminationStrategyTest : StringSpec({
    val classUnderTest = NaiveWeatherDetailDeterminationStrategy()

    "determineWeatherDetails should use the average temperature for all data points" {
        val date = LocalDate.now()
        val dataPoints = listOf(
            weatherDataForTemp(9.0),
            weatherDataForTemp(11.0)
        )

        val result = classUnderTest.determineWeatherDetails(date, dataPoints)
        result shouldBe WeatherDetailByDay(
            date = date,
            temperature = 10.0,
            weatherCondition = WeatherCondition.Other
        )
    }

    "determineWeatherDetails should use the most common weather type" {
        val date = LocalDate.now()
        val dataPoints = listOf(
            weatherDataForCondition("Rain"),
            weatherDataForCondition("Rain"),
            weatherDataForCondition("Clouds")
        )

        val result = classUnderTest.determineWeatherDetails(date, dataPoints)
        result shouldBe WeatherDetailByDay(
            date = date,
            temperature = 10.0,
            weatherCondition = WeatherCondition.Rain
        )
    }
}) {
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf
}

private fun weatherDataForTemp(temp: Double) = WeatherData(
    dt = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
    main = MainDetails(
        temp = temp
    ),
    weather = listOf()
)


private fun weatherDataForCondition(weather: String) = WeatherData(
    dt = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
    main = MainDetails(
        temp = 10.0
    ),
    weather = listOf(WeatherDetails(
        main = weather
    ))
)
