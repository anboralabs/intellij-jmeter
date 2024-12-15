package co.anbora.labs.jmeter.ide.toolchain.flow

import co.anbora.labs.jmeter.ide.toolchain.JMeterToolchain
import java.util.concurrent.Flow
import java.util.logging.Logger

class JMeterToolchainSubscriber(
    private val callback: (List<JMeterToolchain>) -> Unit,
): Flow.Subscriber<Set<String>> {

    private val logger = Logger.getLogger(JMeterToolchainSubscriber::class.simpleName)

    private lateinit var subscription: Flow.Subscription

    override fun onSubscribe(subscription: Flow.Subscription) {
        this.subscription = subscription
        this.subscription.request(1)
    }

    override fun onError(throwable: Throwable) {
        this.logger.severe { throwable.message }
    }

    override fun onComplete() {
        this.logger.info { "Completed." }
    }

    override fun onNext(item: Set<String>) {
        val installed = item
            .map { JMeterToolchain.fromPath(it) }
            .filter { it.isValid() }
            .toList()
        this.callback(installed)
        this.subscription.request(1)
    }

    fun unsubscribe() {
        this.subscription.cancel()
    }
}