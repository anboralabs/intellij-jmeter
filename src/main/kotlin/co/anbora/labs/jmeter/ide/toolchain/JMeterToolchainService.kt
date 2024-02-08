package co.anbora.labs.jmeter.ide.toolchain

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil
import com.intellij.util.xmlb.annotations.Attribute

@State(
    name = "JMeter Toolchain",
    storages = [Storage("JMeterHome.xml")]
)
class JMeterToolchainService: PersistentStateComponent<JMeterToolchainService.ToolchainState?> {
    private var state = ToolchainState()
    val toolchainLocation: String
        get() = state.toolchainLocation

    @Volatile
    private var toolchain: JMeterToolchain = JMeterToolchain.NULL

    fun setToolchain(newToolchain: JMeterToolchain) {
        toolchain = newToolchain
        state.toolchainLocation = newToolchain.homePath()
    }

    fun toolchain(): JMeterToolchain {
        val currentLocation = state.toolchainLocation
        if (toolchain == JMeterToolchain.NULL && currentLocation.isNotEmpty()) {
            setToolchain(JMeterToolchain.fromPath(currentLocation))
        }
        return toolchain
    }

    fun isNotSet(): Boolean = toolchain == JMeterToolchain.NULL

    override fun getState() = state

    override fun loadState(state: ToolchainState) {
        XmlSerializerUtil.copyBean(state, this.state)
    }

    companion object {
        val toolchainSettings
            get() = service<JMeterToolchainService>()
    }

    class ToolchainState {
        @Attribute("url")
        var toolchainLocation: String = ""
    }
}
