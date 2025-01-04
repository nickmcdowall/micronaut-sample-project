package com.example

import io.kotest.assertions.nondeterministic.eventually
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.micronaut.configuration.kafka.annotation.KafkaClient
import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.Topic
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest
import kotlin.time.Duration.Companion.seconds

private val eventsSent: MutableList<String> = mutableListOf()

@MicronautTest
class TopicListenerTests(private val inboundClient: InboundClient) : BehaviorSpec({

    beforeTest {
        eventsSent.clear()
    }

    Given("an event") {
        val event = "part A"

        When("the event is submitted") {
            inboundClient.send(event)

            Then("it is published successfully") {
                eventually(2.seconds) { eventsSent shouldHaveSize 1 }
                eventsSent.single() shouldBe "part A, part B"
            }
        }
    }
}) {

    @KafkaClient
    interface InboundClient {
        @Topic("inbound")
        fun send(event: String)
    }

    @KafkaListener
    class OutboundListener {
        @Topic("outbound")
        fun captureFromOutbound(event: String) {
            eventsSent.add(event)
        }
    }
}