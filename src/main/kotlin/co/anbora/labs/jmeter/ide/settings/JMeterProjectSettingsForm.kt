package co.anbora.labs.jmeter.ide.settings

import co.anbora.labs.jmeter.ide.toolchain.JMeterKnownToolchainsState
import co.anbora.labs.jmeter.ide.toolchain.JMeterToolchain
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.util.Condition
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.panel
import org.apache.jmeter.util.JMeterUtils
import java.nio.file.Path

class JMeterProjectSettingsForm(private val project: Project, private val model: Model) {

    data class Model(
        var homeLocation: String,
    )

    private val mainPanel: DialogPanel
    private val toolchainChooser = ToolchainChooserComponent(
        project,
        { showNewToolchainDialog() },
        { downloadNewToolchain() }
    ) { onSelect(it) }

    private fun showNewToolchainDialog() {
        val dialog = JMeterNewToolchainDialog(createFilterKnownToolchains(), project)
        if (!dialog.showAndGet()) {
            return
        }

        toolchainChooser.refresh()

        val addedToolchain = dialog.addedToolchain()
        if (addedToolchain != null) {
            toolchainChooser.select(addedToolchain)
        }
    }

    private fun createFilterKnownToolchains(): Condition<Path> {
        val knownToolchains = JMeterKnownToolchainsState.getInstance().knownToolchains()
        return Condition { path ->
            knownToolchains.none { it.homePath() == path.toAbsolutePath().toString() }
        }
    }

    private fun downloadNewToolchain() {
        val dialog = JMeterDownloadToolchainDialog(project) { path ->
            toolchainChooser.refresh()
            toolchainChooser.select(path)
        }
        if (!dialog.showAndGet()) {
            return
        }
    }

    private fun onSelect(toolchainInfo: JMeterToolchain) {
        model.homeLocation = toolchainInfo.homePath()
        JMeterUtils.initializeJMeter(toolchainInfo.homePath())
    }

    init {
        mainPanel = panel {
            row {
                cell(toolchainChooser)
                    .align(AlignX.FILL)
            }
        }

        // setup initial location
        model.homeLocation = toolchainChooser.selectedToolchain()?.homePath() ?: ""
    }

    fun createComponent() = mainPanel

    fun reset() {
        toolchainChooser.select(model.homeLocation)
    }
}
