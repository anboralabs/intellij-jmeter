package co.anbora.labs.jmeter.ide.toolchain

import co.anbora.labs.jmeter.ide.settings.ui.NullToolchain
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil
import com.intellij.util.xmlb.annotations.Attribute

@State(
    name = "JMeter Toolchain",
    storages = [Storage("NewJMeterHome.xml")]
)
class JMeterToolchainService: PersistentStateComponent<JMeterToolchainService.ToolchainState?> {
    private var state = ToolchainState()
    val toolchainLocation: String
        get() = state.toolchainLocation

    @Volatile
    private var toolchain: JMeterToolchain = NullToolchain

    fun setToolchain(newToolchain: JMeterToolchain) {
        toolchain = newToolchain
        state.toolchainLocation = newToolchain.homePath()
    }

    fun toolchain(): JMeterToolchain = toolchain

    override fun getState() = state

    override fun loadState(state: ToolchainState) {
        XmlSerializerUtil.copyBean(state, this.state)
    }

    override fun initializeComponent() {
        val app = ApplicationManager.getApplication()
        app.executeOnPooledThread {
            val currentLocation = state.toolchainLocation
            if (toolchain == NullToolchain && currentLocation.isNotEmpty()) {
                setToolchain(JMeterToolchain.fromPath(currentLocation))
            }
        }.get()
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
