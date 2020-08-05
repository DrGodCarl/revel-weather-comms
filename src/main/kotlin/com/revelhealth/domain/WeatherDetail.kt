package com.revelhealth.domain

interface WeatherDetail {
    val temperature: Double
    val weatherCondition: WeatherCondition
}