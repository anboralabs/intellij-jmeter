package co.anbora.labs.jmeter.ide.settings

import co.anbora.labs.jmeter.ide.toolchain.JMeterToolchain
import co.anbora.labs.jmeter.ide.toolchain.JMeterToolchainService.Companion.toolchainSettings
import com.intellij.openapi.Disposable
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.util.Disposer
import javax.swing.JComponent

class JMeterProjectSettingsConfigurable(private val project: Project) : Configurable, Disposable {

    private val mainPanel: DialogPanel
    private val model = JMeterProjectSettingsForm.Model(
        homeLocation = "",
    )
    private val settingsForm = JMeterProjectSettingsForm(project, model)

    init {
        mainPanel = settingsForm.createComponent()

        mainPanel.registerValidators(this)
    }

    override fun createComponent(): JComponent = mainPanel

    override fun getPreferredFocusedComponent(): JComponent = mainPanel

    override fun isModified(): Boolean {
        mainPanel.apply()

        val settings = project.toolchainSettings
        return model.homeLocation != settings.toolchainLocation
    }

    override fun apply() {
        mainPanel.apply()

        validateSettings()

        val settings = project.toolchainSettings
        settings.setToolchain(JMeterToolchain.fromPath(model.homeLocation))
    }

    private fun validateSettings() {
        val issues = mainPanel.validateAll()
        if (issues.isNotEmpty()) {
            throw ConfigurationException(issues.first().message)
        }
    }

    override fun reset() {
        val settings = project.toolchainSettings

        with(model) {
            homeLocation = settings.toolchainLocation
        }

        settingsForm.reset()
        mainPanel.reset()
    }

    override fun getDisplayName(): String = "JMeter"

    companion object {
        @JvmStatic
        fun show(project: Project) {
            ShowSettingsUtil.getInstance().showSettingsDialog(project, JMeterProjectSettingsConfigurable::class.java)
        }
    }

    override fun dispose() {
    }
}
