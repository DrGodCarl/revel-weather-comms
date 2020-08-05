package com.revelhealth.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherMapClient {

    @GET("data/2.5/forecast")
    fun forecast(
        @Query("q") query: String = "minneapolis,us",
        @Query("units") units: String = "imperial",
        // TODO - the app id is probably ~secret~ so it shouldn't be in the code
        @Query("APPID") appId: String = "09110e603c1d5c272f94f64305c09436"
    ): Call<ForecastDataWrapper>

}

data class MainDetails(
    val temp: Double
)

data class WeatherDetails(
    val main: String
)

data class WeatherData(
    // TODO - rename dt and figure out gson annotation stuff
    val dt: Long,
    val main: MainDetails,
    val weather: List<WeatherDetails>
)

data class ForecastDataWrapper(
    val list: List<WeatherData>
)
