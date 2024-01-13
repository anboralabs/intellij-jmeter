package co.anbora.labs.jmeter.ide.settings.ui

import co.anbora.labs.jmeter.ide.settings.PathHomeConfig
import co.anbora.labs.jmeter.ide.settings.Settings
import co.anbora.labs.jmeter.ide.settings.Settings.SELECTED_JMETER_PATH
import com.intellij.ui.IdeBorderFactory
import com.intellij.ui.components.JBRadioButton
import com.intellij.util.ui.FormBuilder
import java.util.Objects
import java.util.function.Consumer
import javax.swing.ButtonGroup
import javax.swing.JPanel

class GlobalConfigView(val changeListener: Consumer<PathHomeConfig>) {

    private lateinit var useInternalJmeter: JBRadioButton
    private lateinit var useExternalJmeter: JBRadioButton

    init {
        createUI()
    }

    private fun createUI() {
        val buttonGroup = ButtonGroup()
        useInternalJmeter = JBRadioButton("Use internal JMeter 5.5.1 (Recommended)")
        useExternalJmeter = JBRadioButton("Set JMeter path")

        buttonGroup.add(useInternalJmeter)
        buttonGroup.add(useExternalJmeter)

        useInternalJmeter.addActionListener {
            changeListener.accept(PathHomeConfig.INTERNAL)
        }

        useExternalJmeter.addActionListener {
            changeListener.accept(PathHomeConfig.EXTERNAL)
        }
    }

    fun getComponent(): JPanel {
        val formBuilder = FormBuilder.createFormBuilder()

        formBuilder.addComponent(useInternalJmeter)
            .addComponent(useExternalJmeter)

        val panel = formBuilder.panel

        panel.border = IdeBorderFactory.createTitledBorder("Global Config")

        return panel
    }

    fun selectedLinterConfig(): PathHomeConfig {
        return when {
            useInternalJmeter.isSelected -> PathHomeConfig.INTERNAL
            useExternalJmeter.isSelected -> PathHomeConfig.EXTERNAL
            else -> PathHomeConfig.INTERNAL
        }
    }

    fun selectLinter(linter: PathHomeConfig) {
        useInternalJmeter.isSelected = PathHomeConfig.INTERNAL == linter
        useExternalJmeter.isSelected = PathHomeConfig.EXTERNAL == linter
        changeListener.accept(linter)
    }

    fun isModified(): Boolean {
        return !Objects.equals(selectedLinterConfig().name, Settings[SELECTED_JMETER_PATH])
    }

    fun reset() {
        selectLinter(PathHomeConfig.valueOf(Settings[SELECTED_JMETER_PATH]))
    }

    fun apply() {
        Settings[SELECTED_JMETER_PATH] = selectedLinterConfig().name
    }

}
