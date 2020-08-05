package com.revelhealth

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// TODO - reorganize the project now that I have a better understanding of the problem
fun main(args: Array<String>) {
    val results = determinationService().determineNextFiveDays()
    results.forEach {
        println("${it.date} - ${it.communicationChannel}")
    }
}

private fun weatherClient(): OpenWeatherMapClient {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return retrofit.create(OpenWeatherMapClient::class.java)
}

private fun determinationService(): ForecastingCommunicationChannelDeterminationService {
    return ForecastingCommunicationChannelDeterminationService(
        dailyWeatherFetcher(),
        weatherCommunicationChannelDeterminationService()
    )
}

private fun dailyWeatherFetcher(): DailyWeatherFetcher {
    return DailyWeatherFetcher(weatherClient())
}

private fun weatherCommunicationChannelDeterminationService(): WeatherCommunicationChannelDeterminationService {
    return WeatherCommunicationChannelDeterminationService()
}
