package com.revelhealth.domain

import com.revelhealth.domain.WeatherCondition

interface WeatherDetail {
    val temperature: Double
    val weatherCondition: WeatherCondition
}