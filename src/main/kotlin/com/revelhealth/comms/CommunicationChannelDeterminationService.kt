package com.revelhealth.comms

import com.revelhealth.domain.CommunicationChannel

interface CommunicationChannelDeterminationService<T> {
    fun determineCommunicationChannel(quality: T): CommunicationChannel
}
