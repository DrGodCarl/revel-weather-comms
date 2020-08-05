package com.revelhealth

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.math.BigDecimal

interface OpenWeatherMapClient {

    @GET("data/2.5/forecast")
    fun forecast(
        @Query("q") query: String = "minneapolis,us",
        @Query("units") units: String = "imperial",
        @Query("APPID") appId: String = "09110e603c1d5c272f94f64305c09436"
    ): Call<ForecastDataWrapper>

}

data class MainDetails(
    val temp: BigDecimal
)

data class WeatherDetails(
    val id: Int
)

data class WeatherData(
    val dt: Long,
    val main: MainDetails,
    val weather: List<WeatherDetails>
)

data class ForecastDataWrapper(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<WeatherData>
)
