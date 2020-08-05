package com.revelhealth

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


fun main(args: Array<String>) {
    val weatherClient = weatherClient()
    println(weatherClient.forecast().execute().body())
}

private fun weatherClient(): OpenWeatherMapClient {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return retrofit.create(OpenWeatherMapClient::class.java)
}
