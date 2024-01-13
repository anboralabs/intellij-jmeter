package co.anbora.labs.jmeter.ide.settings.ui

import co.anbora.labs.jmeter.ide.settings.PathHomeConfig
import co.anbora.labs.jmeter.ide.settings.Settings
import co.anbora.labs.jmeter.ide.settings.Settings.OPTION_KEY_JMETER_PATH
import co.anbora.labs.jmeter.ide.settings.color.findColorByKey
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.ui.TextComponentAccessors
import com.intellij.ui.DocumentAdapter
import com.intellij.ui.TextFieldWithHistoryWithBrowseButton
import com.intellij.ui.components.JBTextField
import com.intellij.util.NotNullProducer
import com.intellij.util.ui.FormBuilder
import com.intellij.util.ui.SwingHelper
import com.intellij.util.ui.UIUtil
import java.awt.Color
import java.util.*
import java.util.function.Consumer
import java.util.function.Supplier
import javax.swing.BorderFactory
import javax.swing.JComponent
import javax.swing.JTextField
import javax.swing.event.DocumentEvent
import javax.swing.plaf.basic.BasicComboBoxEditor

class JmeterHomePathSettings: Configurable {

    private lateinit var homePathField: TextFieldWithHistoryWithBrowseButton

    private val globalConfigView = GlobalConfigView(disabledBehavior())

    init {
        createUI()
    }

    private fun createUI() {
        homePathField = createTextFieldWithHistory(detectJmeterPaths())
        homePathField.addBrowseFolderListener(
            "",
            "Path",
            null,
            FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor(),
            TextComponentAccessors.TEXT_FIELD_WITH_HISTORY_WHOLE_TEXT
        );
        setupTextFieldDefaultValue(homePathField.childComponent.textEditor) {
            Settings[OPTION_KEY_JMETER_PATH]
        }
    }

    private fun createTextFieldWithHistory(defaultValues: NotNullProducer<List<String>>): TextFieldWithHistoryWithBrowseButton {
        val textFieldWithHistoryWithBrowseButton = TextFieldWithHistoryWithBrowseButton()
        val textFieldWithHistory = textFieldWithHistoryWithBrowseButton.childComponent
        textFieldWithHistory.editor = object : BasicComboBoxEditor() {
            override fun createEditorComponent(): JTextField {
                return JBTextField()
            }
        }
        textFieldWithHistory.setHistorySize(-1)
        textFieldWithHistory.setMinimumAndPreferredWidth(0)
        SwingHelper.addHistoryOnExpansion(textFieldWithHistory, defaultValues)
        return textFieldWithHistoryWithBrowseButton
    }

    private fun setupTextFieldDefaultValue(
        textField: JTextField,
        defaultValueSupplier: Supplier<String?>
    ) {
        val defaultShellPath: String? = defaultValueSupplier.get()
        if (defaultShellPath.isNullOrBlank()) return
        textField.document.addDocumentListener(object : DocumentAdapter() {
            override fun textChanged(e: DocumentEvent) {
                textField.foreground = if (defaultShellPath == textField.text) getDefaultValueColor() else getChangedValueColor()
            }
        })
        if (textField is JBTextField) {
            textField.emptyText.text = defaultShellPath
        }
    }

    fun getDefaultValueColor(): Color? = findColorByKey("TextField.inactiveForeground", "nimbusDisabledText")

    fun getChangedValueColor(): Color? = findColorByKey("TextField.foreground")

    private fun detectJmeterPaths(): NotNullProducer<List<String>> = NotNullProducer { arrayListOf() }

    private fun disabledBehavior(): Consumer<PathHomeConfig> = Consumer {
        homePathField.isEnabled = PathHomeConfig.EXTERNAL == it
    }

    override fun createComponent(): JComponent {

        val lintFieldsWrapperBuilder = FormBuilder.createFormBuilder()
            .setHorizontalGap(UIUtil.DEFAULT_HGAP)
            .setVerticalGap(UIUtil.DEFAULT_VGAP)

        lintFieldsWrapperBuilder.addLabeledComponent("JMeter home (JMETER_HOME):", homePathField)

        val builder = FormBuilder.createFormBuilder()
            .setHorizontalGap(UIUtil.DEFAULT_HGAP)
            .setVerticalGap(UIUtil.DEFAULT_VGAP)

        val panel = builder
            .addComponent(globalConfigView.getComponent())
            .addComponent(lintFieldsWrapperBuilder.panel)
            .addSeparator(4)
            .addVerticalGap(4)
            .panel

        val centerPanel = SwingHelper.wrapWithHorizontalStretch(panel)
        centerPanel.border = BorderFactory.createEmptyBorder(5, 0, 0, 0)

        return centerPanel
    }

    override fun isModified(): Boolean {
        return !Objects.equals(homePathField.text, Settings[OPTION_KEY_JMETER_PATH])
                || globalConfigView.isModified()
    }

    override fun reset() {
        homePathField.text = Settings[OPTION_KEY_JMETER_PATH]
        globalConfigView.reset()
    }

    override fun apply() {
        Settings[OPTION_KEY_JMETER_PATH] = homePathField.text
        globalConfigView.apply()
    }

    override fun getDisplayName(): String = "JMeter"
}
