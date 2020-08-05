package com.revelhealth.comms

import com.revelhealth.domain.CommunicationChannel
import com.revelhealth.domain.WeatherCondition
import com.revelhealth.domain.WeatherDetail


class WeatherCommunicationChannelDeterminationService : CommunicationChannelDeterminationService<WeatherDetail> {

    override fun determineCommunicationChannel(quality: WeatherDetail): CommunicationChannel {
        if (quality.weatherCondition == WeatherCondition.Rain) {
            return CommunicationChannel.PHONE
        }
        val temp = quality.temperature
        return when {
            temp > 75 -> CommunicationChannel.TEXT
            temp < 55 -> CommunicationChannel.PHONE
            else -> CommunicationChannel.EMAIL
        }
    }

}
