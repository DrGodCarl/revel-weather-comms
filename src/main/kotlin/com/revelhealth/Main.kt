package com.revelhealth

import com.natpryce.onFailure
import com.revelhealth.weather.DailyWeatherFetcherImpl
import com.revelhealth.comms.ForecastingCommunicationChannelDeterminationService
import com.revelhealth.comms.WeatherCommunicationChannelDeterminationService
import com.revelhealth.retrofit.OpenWeatherMapClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun main(args: Array<String>) {
    val results = determinationService().determineNextFiveDays()
    results.onFailure {
        println("Something went wrong: ${it.reason.message}")
        return
    }.forEach {
        println("${it.date} - ${it.communicationChannel}")
    }
}

// This essentially builds the dependency graph of the project by hand.
// If the project were to grow, I'd look into a DI framework for it.
private fun determinationService(): ForecastingCommunicationChannelDeterminationService {
    return ForecastingCommunicationChannelDeterminationService(
        dailyWeatherFetcher(),
        weatherCommunicationChannelDeterminationService()
    )
}

private fun dailyWeatherFetcher(): DailyWeatherFetcherImpl {
    return DailyWeatherFetcherImpl(weatherClient())
}

private fun weatherClient(): OpenWeatherMapClient {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return retrofit.create(OpenWeatherMapClient::class.java)
}

private fun weatherCommunicationChannelDeterminationService(): WeatherCommunicationChannelDeterminationService {
    return WeatherCommunicationChannelDeterminationService()
}
