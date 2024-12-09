package co.anbora.labs.jmeter.ide.settings.ui

import co.anbora.labs.jmeter.ide.icons.JmeterIcons
import co.anbora.labs.jmeter.ide.settings.JMeterRelease
import com.intellij.ui.ColoredListCellRenderer
import javax.swing.JList

class ReleaseListCellRenderer: ColoredListCellRenderer<JMeterRelease>() {
    override fun customizeCellRenderer(
        list: JList<out JMeterRelease>,
        value: JMeterRelease,
        index: Int,
        selected: Boolean,
        hasFocus: Boolean,
    ) {
        icon = JmeterIcons.FILE
        append(value.name)
        append("  ")
    }
}