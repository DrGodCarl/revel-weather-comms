package com.revelhealth.comms

import com.revelhealth.domain.CommunicationChannel
import com.revelhealth.domain.WeatherCondition
import com.revelhealth.domain.WeatherDetail
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.StringSpec
import io.kotest.property.forAll
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.numericDoubles

internal class WeatherCommunicationChannelDeterminationServiceTest : StringSpec({
    val classUnderTest = WeatherCommunicationChannelDeterminationService()

    "determineCommunicationChannel should return PHONE if it's raining" {
        forAll<Double> {
            val weather = TestWeatherDetail(temperature = it, weatherCondition = WeatherCondition.Rain)
            val result = classUnderTest.determineCommunicationChannel(weather)
            result == CommunicationChannel.PHONE
        }
    }

    "determineCommunicationChannel should return TEXT if it's over 75 out and not raining" {
        forAll(Arb.numericDoubles(from = 75.1)) {
            val weather = TestWeatherDetail(temperature = it, weatherCondition = WeatherCondition.Other)
            val result = classUnderTest.determineCommunicationChannel(weather)
            result == CommunicationChannel.TEXT
        }
    }

    "determineCommunicationChannel should return PHONE if it's under 55 out and not raining" {
        forAll(Arb.numericDoubles(to = 54.9)) {
            val weather = TestWeatherDetail(temperature = it, weatherCondition = WeatherCondition.Other)
            val result = classUnderTest.determineCommunicationChannel(weather)
            result == CommunicationChannel.PHONE
        }
    }

    "determineCommunicationChannel should return EMAIL if it's between 55 and 75 out and not raining" {
        forAll(Arb.numericDoubles(from = 55.0, to = 75.0)) {
            val weather = TestWeatherDetail(temperature = it, weatherCondition = WeatherCondition.Other)
            val result = classUnderTest.determineCommunicationChannel(weather)
            result == CommunicationChannel.EMAIL
        }
    }

}) {
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf
}

data class TestWeatherDetail(
    override val temperature: Double,
    override val weatherCondition: WeatherCondition
): WeatherDetail
