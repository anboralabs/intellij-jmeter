package co.anbora.labs.jmeter.ide.toolchain

import co.anbora.labs.jmeter.ide.toolchain.flow.JMeterToolchainPublisher
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.util.xmlb.XmlSerializerUtil
import java.util.concurrent.Flow

@State(
    name = "JMeter Home",
    storages = [Storage("NewJMeterToolchains.xml")]
)
class JMeterKnownToolchainsState : PersistentStateComponent<JMeterKnownToolchainsState?> {
    companion object {
        fun getInstance() = service<JMeterKnownToolchainsState>()
    }

    var knownToolchains: Set<String> = emptySet()

    @Volatile
    private var toolchainPublisher = JMeterToolchainPublisher()

    fun isKnown(homePath: String): Boolean {
        return knownToolchains.contains(homePath)
    }

    fun add(toolchain: JMeterToolchain) {
        knownToolchains = knownToolchains + toolchain.homePath()
        toolchainPublisher.publish(knownToolchains)
    }

    fun remove(toolchain: JMeterToolchain) {
        knownToolchains = knownToolchains - toolchain.homePath()
        toolchainPublisher.publish(knownToolchains)
    }

    fun subscribe(subscriber: Flow.Subscriber<Set<String>>) {
        toolchainPublisher.subscribe(subscriber)
    }

    override fun getState() = this

    override fun loadState(state: JMeterKnownToolchainsState) {
        XmlSerializerUtil.copyBean(state, this)
        toolchainPublisher.publish(knownToolchains)
    }
}
