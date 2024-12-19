package co.anbora.labs.jmeter.ide.toolchain

import co.anbora.labs.jmeter.ide.toolchain.flow.JMeterToolchainPublisher
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.*
import com.intellij.util.xmlb.XmlSerializerUtil
import java.util.concurrent.Flow

@State(
    name = "JMeter Home",
    storages = [Storage("NewJMeterToolchains.xml")]
)
class JMeterKnownToolchainsState: PersistentStateComponent<JMeterKnownToolchainsState?> {
    companion object {
        fun getInstance() = service<JMeterKnownToolchainsState>()
    }

    var knownToolchains: Set<String> = emptySet()

    @Volatile
    private var jMeterToolchains: MutableSet<JMeterToolchain> = mutableSetOf()

    fun knownToolchains(): Set<JMeterToolchain> = jMeterToolchains

    @Volatile
    private var toolchainPublisher = JMeterToolchainPublisher()

    fun isKnown(homePath: String): Boolean {
        return knownToolchains.contains(homePath)
    }

    fun add(toolchain: JMeterToolchain) {
        knownToolchains = knownToolchains + toolchain.homePath()
        jMeterToolchains.add(toolchain)
        toolchainPublisher.publish(jMeterToolchains)
    }

    fun remove(toolchain: JMeterToolchain) {
        knownToolchains = knownToolchains - toolchain.homePath()
        jMeterToolchains.remove(toolchain)
        toolchainPublisher.publish(jMeterToolchains)
    }

    fun subscribe(subscriber: Flow.Subscriber<Set<JMeterToolchain>>) {
        toolchainPublisher.subscribe(subscriber)
    }

    override fun getState() = this

    override fun loadState(state: JMeterKnownToolchainsState) {
        XmlSerializerUtil.copyBean(state, this)
    }

    override fun initializeComponent() {
        val app = ApplicationManager.getApplication()
        app.executeOnPooledThread {
            jMeterToolchains = knownToolchains.map { JMeterToolchain.fromPath(it) }.toMutableSet()
            toolchainPublisher.publish(jMeterToolchains)
        }.get()
    }
}
