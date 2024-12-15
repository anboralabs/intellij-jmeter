package co.anbora.labs.jmeter.ide.toolchain.flow

import co.anbora.labs.jmeter.ide.toolchain.JMeterToolchain
import java.util.concurrent.Executors
import java.util.concurrent.Flow
import java.util.concurrent.SubmissionPublisher

class JMeterToolchainPublisher: SubmissionPublisher<Set<JMeterToolchain>>(
    Executors.newSingleThreadExecutor(), 5
) {
    private var value: Set<JMeterToolchain>? = null

    fun publish(value: Set<JMeterToolchain>) {
        this.value = value
        super.submit(this.value)
    }

    override fun subscribe(subscriber: Flow.Subscriber<in Set<JMeterToolchain>>) {
        super.subscribe(subscriber)

        value?.let { subscriber.onNext(it) }
    }
}