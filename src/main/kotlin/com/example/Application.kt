package com.example

import io.micronaut.runtime.Micronaut

fun main(args: Array<String>) {
    Micronaut.build(*args)
        .eagerInitSingletons(true)
        .start()
}
