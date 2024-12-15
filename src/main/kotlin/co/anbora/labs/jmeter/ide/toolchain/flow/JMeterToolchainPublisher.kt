package co.anbora.labs.jmeter.ide.toolchain.flow

import java.util.concurrent.Executors
import java.util.concurrent.Flow
import java.util.concurrent.SubmissionPublisher

class JMeterToolchainPublisher: SubmissionPublisher<Set<String>>(
    Executors.newSingleThreadExecutor(), 5
) {
    private var value: Set<String>? = null

    fun publish(value: Set<String>) {
        this.value = value
        super.submit(this.value)
    }

    override fun subscribe(subscriber: Flow.Subscriber<in Set<String>>) {
        super.subscribe(subscriber)

        value?.let { subscriber.onNext(it) }
    }
}