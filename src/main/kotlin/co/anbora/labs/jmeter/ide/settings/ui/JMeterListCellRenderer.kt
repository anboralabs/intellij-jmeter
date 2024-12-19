package co.anbora.labs.jmeter.ide.settings.ui

import co.anbora.labs.jmeter.ide.icons.JmeterIcons
import co.anbora.labs.jmeter.ide.toolchain.JMeterToolchain
import com.intellij.openapi.ui.getPresentablePath
import com.intellij.ui.ColoredListCellRenderer
import com.intellij.ui.SimpleTextAttributes
import javax.swing.JList

class JMeterListCellRenderer: ColoredListCellRenderer<JMeterToolchain>() {
    override fun customizeCellRenderer(
        list: JList<out JMeterToolchain>,
        value: JMeterToolchain?,
        index: Int,
        selected: Boolean,
        hasFocus: Boolean,
    ) {
        when {
            value == null -> {
                append(NullToolchain.name())
                return
            }
            value is AddToolchain || value is DownloadToolchain || value is NullToolchain -> {
                append(value.name())
                return
            }
            !value.isValid() -> {
                icon = JmeterIcons.FILE
                append(value.version())
                append("  ")
                append(getPresentablePath(value.homePath()), SimpleTextAttributes.ERROR_ATTRIBUTES)
            }
            else -> {
                icon = JmeterIcons.FILE
                append(value.version())
                append("  ")
                append(getPresentablePath(value.homePath()), SimpleTextAttributes.GRAYED_ATTRIBUTES)
            }
        }
    }
}