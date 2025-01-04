package com.example

import io.micronaut.configuration.kafka.annotation.KafkaClient
import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.Topic

@KafkaListener
open class TopicListener(val outboundClient: OutboundClient) {

    @Topic("inbound")
    open fun process(event: String) {
        println("Event received: $event")
        outboundClient.send("$event, part B")
    }
}

@KafkaClient
interface OutboundClient {
    @Topic("outbound")
    fun send(event: String)
}
