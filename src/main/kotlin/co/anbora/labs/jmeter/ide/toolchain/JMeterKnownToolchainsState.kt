package co.anbora.labs.jmeter.ide.toolchain

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "JMeter Home",
    storages = [Storage("NewJMeterToolchains.xml")]
)
class JMeterKnownToolchainsState : PersistentStateComponent<JMeterKnownToolchainsState?> {
    companion object {
        fun getInstance() = service<JMeterKnownToolchainsState>()
    }

    var knownToolchains: Set<String> = emptySet()

    fun isKnown(homePath: String): Boolean {
        return knownToolchains.contains(homePath)
    }

    fun add(toolchain: JMeterToolchain) {
        knownToolchains = knownToolchains + toolchain.homePath()
    }

    override fun getState() = this

    override fun loadState(state: JMeterKnownToolchainsState) {
        XmlSerializerUtil.copyBean(state, this)
    }
}
