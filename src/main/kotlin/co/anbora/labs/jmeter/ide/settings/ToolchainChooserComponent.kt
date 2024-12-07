package co.anbora.labs.jmeter.ide.settings

import co.anbora.labs.jmeter.ide.icons.JmeterIcons
import co.anbora.labs.jmeter.ide.toolchain.JMeterKnownToolchainsState
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.ComponentWithBrowseButton
import com.intellij.ui.ColoredListCellRenderer
import com.intellij.ui.PopupMenuListenerAdapter
import com.intellij.ui.SimpleTextAttributes
import com.intellij.util.ui.SwingHelper
import com.jgoodies.common.base.Objects
import java.awt.event.ActionEvent
import java.awt.event.ItemEvent
import java.awt.event.KeyEvent
import javax.swing.DefaultComboBoxModel
import javax.swing.JList
import javax.swing.event.PopupMenuEvent

class ToolchainChooserComponent(
    private val project: Project,
    private val browseActionListener: Runnable,
    private val onSelectAction: (ToolchainInfo) -> Unit
): ComponentWithBrowseButton<ComboBox<ToolchainInfo>>(KeyEventAwareComboBox(), null) {

    private val comboBox = childComponent as KeyEventAwareComboBox
    private val knownToolchains get() = JMeterKnownToolchainsState.getInstance().knownToolchains
    private var knownToolchainInfos = knownToolchains
        .map { ToolchainInfo(it, JMeterConfigurationUtil.guessToolchainVersion(it)) }
        .filter { it.version != JMeterConfigurationUtil.UNDEFINED_VERSION }

    class NoToolchain : ToolchainInfo("", "") {
        companion object {
            val instance = NoToolchain()
        }
    }

    class AddToolchain : ToolchainInfo("", "") {
        companion object {
            val instance = AddToolchain()
        }
    }

    class DownloadToolchain : ToolchainInfo("", "") {
        companion object {
            val instance = DownloadToolchain()
        }
    }

    private var myLastSelectedItem: ToolchainInfo = NoToolchain.instance
    private val myModel: ToolchainComboBoxModel = ToolchainComboBoxModel()
    private var myDropDownListUpdateRequested = false


    init {
        /*knownToolchainInfos.forEach { info ->
            comboBox.addItem(info)
        }

        comboBox.addItem(NoToolchain.instance)

        comboBox.renderer = object : ColoredListCellRenderer<ToolchainInfo>() {
            override fun customizeCellRenderer(
                list: JList<out ToolchainInfo>,
                value: ToolchainInfo?,
                index: Int,
                selected: Boolean,
                hasFocus: Boolean,
            ) {
                if (value == null || value is NoToolchain) {
                    append("<No JMeter Home>")
                    return
                }

                icon = JmeterIcons.FILE
                append(value.version)
                append("  ")
                append(value.location, SimpleTextAttributes.GRAYED_ATTRIBUTES)
            }
        }

        comboBox.addItemListener {
            val item = comboBox.selectedItem as? ToolchainInfo ?: return@addItemListener
            onSelectAction(item)
        }

        setButtonIcon(AllIcons.General.Add)*/

        this.addActionListener { e: ActionEvent? -> this.showInterpretersDialog(e) }
        this.comboBox.setModel(this.myModel)
        this.comboBox.renderer = DelegatingListCellRenderer()
        // this.comboBox.setMinimumAndPreferredWidth(0)
        this.myModel.addElement(NoToolchain.instance)
        this.myModel.selectedItem = NoToolchain.instance
        this.requestDropDownListUpdate()
        this.comboBox.addItemListener { e: ItemEvent ->
            if (e.stateChange == 1) {
                this.handleSelectedItemChange()
            }
        }
    }

    private fun showInterpretersDialog(e: ActionEvent?) {

    }

    private fun requestDropDownListUpdate() {
        if (!this.myDropDownListUpdateRequested) {
            this.myDropDownListUpdateRequested = true
            this.comboBox.addPopupMenuListener(object: PopupMenuListenerAdapter() {
                override fun popupMenuWillBecomeVisible(e: PopupMenuEvent) {
                    this@ToolchainChooserComponent.comboBox.removePopupMenuListener(this)
                    this@ToolchainChooserComponent.updateDropDownList()
                    this@ToolchainChooserComponent.myDropDownListUpdateRequested = false
                }
            })
        }
    }

    private fun updateDropDownList() {
        val selected = this.getToolchainRef()
        val toolchains = this.getAllAvailableToolchains()
        this.comboBox.doWithItemStateChangedEventsDisabled {
            SwingHelper.updateItems(this.comboBox, toolchains, null)
            this.comboBox.addItem(AddToolchain.instance)
            this.comboBox.addItem(DownloadToolchain.instance)
        }
        if (!Objects.equals(this.comboBox.selectedItem, selected)) {
            this.comboBox.selectedItem = selected
            this.handleSelectedItemChange()
        }
    }

    private fun handleSelectedItemChange() {
        val selected = this.getToolchainRef()
        when (selected) {
            is NoToolchain -> {}
            is AddToolchain -> {
                this.comboBox.setSelectedItem(this.myLastSelectedItem)
                if (!this.comboBox.myKeyEventProcessing) {
                    ApplicationManager.getApplication().invokeLater(browseActionListener , ModalityState.current())
                }
            }
            is DownloadToolchain -> {}
            else -> {
                if (this.myLastSelectedItem != selected) {
                    this.myLastSelectedItem = selected
                    this@ToolchainChooserComponent.onSelectAction(selected)
                }
            }
        }
    }

    private fun getToolchainRef(): ToolchainInfo {
        var ref = this.comboBox.selectedItem as? ToolchainInfo
        if (ref == null) {
            ref = NoToolchain.instance
        }
        return ref
    }

    private fun getAllAvailableToolchains(): List<ToolchainInfo> {
        val result = mutableSetOf<ToolchainInfo>()
        result.addAll(knownToolchainInfos)

        if (result.isEmpty()) {
            result.add(this.getToolchainRef())
        }
        return result.toList()
    }

    fun selectedToolchain(): ToolchainInfo? {
        return comboBox.selectedItem as? ToolchainInfo
    }

    fun refresh() {
        comboBox.removeAllItems()
        knownToolchainInfos = knownToolchains
            .map { ToolchainInfo(it, JMeterConfigurationUtil.guessToolchainVersion(it)) }
            .filter { it.version != JMeterConfigurationUtil.UNDEFINED_VERSION }

        knownToolchainInfos.forEach { info ->
            comboBox.addItem(info)
        }
        comboBox.addItem(NoToolchain())
    }

    fun select(location: String) {
        if (location.isEmpty()) {
            comboBox.selectedItem = NoToolchain.instance
            return
        }

        val infoToSelect = knownToolchainInfos.find { it.location == location } ?: return
        comboBox.selectedItem = infoToSelect
    }

    private open class KeyEventAwareComboBox: ComboBox<ToolchainInfo>() {
        var myKeyEventProcessing = false
        private var myItemStateChangedEventsAllowed = true

        override fun processKeyEvent(e: KeyEvent?) {
            this.myKeyEventProcessing = true
            super.processKeyEvent(e)
            this.myKeyEventProcessing = false
        }

        override fun selectedItemChanged() {
            if (this.myItemStateChangedEventsAllowed) {
                super.selectedItemChanged()
            }
        }

        fun doWithItemStateChangedEventsDisabled(runnable: Runnable) {
            this.myItemStateChangedEventsAllowed = false

            try {
                runnable.run()
            } finally {
                this.myItemStateChangedEventsAllowed = true
            }
        }
    }

    private open class DelegatingListCellRenderer: ColoredListCellRenderer<ToolchainInfo>() {
        override fun customizeCellRenderer(
            list: JList<out ToolchainInfo>,
            value: ToolchainInfo?,
            index: Int,
            selected: Boolean,
            hasFocus: Boolean,
        ) {
            if (value == null || value is NoToolchain) {
                append("<No JMeter Home>")
                return
            }

            if (value is AddToolchain) {
                append("Add...")
                return
            }

            if (value is DownloadToolchain) {
                append("Download...")
                return
            }

            icon = JmeterIcons.FILE
            append(value.version)
            append("  ")
            append(value.location, SimpleTextAttributes.GRAYED_ATTRIBUTES)
        }
    }

    private inner class ToolchainComboBoxModel: DefaultComboBoxModel<ToolchainInfo>()
}
