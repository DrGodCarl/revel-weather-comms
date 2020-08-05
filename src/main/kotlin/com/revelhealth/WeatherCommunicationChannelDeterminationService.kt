package com.revelhealth

class WeatherCommunicationChannelDeterminationService {

    fun communicationChannelForWeather(weather: WeatherDetail): CommunicationChannel {
        if (weather.weatherCondition == WeatherCondition.Rain) {
            return CommunicationChannel.PHONE
        }
        val temp = weather.temperature
        return when {
            temp > 75 -> CommunicationChannel.TEXT
            temp in 55.0..75.0 -> CommunicationChannel.EMAIL
            else -> CommunicationChannel.PHONE
        }
    }

}

enum class CommunicationChannel {
    TEXT, EMAIL, PHONE
}
