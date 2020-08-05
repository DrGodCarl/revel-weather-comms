package com.revelhealth.weather

import com.natpryce.Result
import com.revelhealth.domain.WeatherCondition
import com.revelhealth.domain.WeatherDetail
import java.time.LocalDate

interface DailyWeatherFetcher {
    fun getWeatherForecast(): Result<List<WeatherDetailByDay>, RuntimeException>
}

data class WeatherDetailByDay(
    val date: LocalDate,
    override val temperature: Double,
    override val weatherCondition: WeatherCondition
) : WeatherDetail
