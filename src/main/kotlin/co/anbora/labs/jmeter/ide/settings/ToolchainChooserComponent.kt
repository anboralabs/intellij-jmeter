package co.anbora.labs.jmeter.ide.settings

import co.anbora.labs.jmeter.ide.settings.ui.AddToolchain
import co.anbora.labs.jmeter.ide.settings.ui.DownloadToolchain
import co.anbora.labs.jmeter.ide.settings.ui.JMeterListCellRenderer
import co.anbora.labs.jmeter.ide.settings.ui.NullToolchain
import co.anbora.labs.jmeter.ide.toolchain.JMeterKnownToolchainsState
import co.anbora.labs.jmeter.ide.toolchain.JMeterToolchain
import co.anbora.labs.jmeter.ide.toolchain.JMeterToolchainService.Companion.toolchainSettings
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.ComponentWithBrowseButton
import com.intellij.util.ui.SwingHelper
import com.jgoodies.common.base.Objects
import java.awt.event.ActionEvent
import java.awt.event.ItemEvent
import javax.swing.DefaultComboBoxModel

class ToolchainChooserComponent(
    private val project: Project,
    private val addAction: Runnable,
    private val downloadAction: Runnable,
    private val onSelectAction: (JMeterToolchain) -> Unit
): ComponentWithBrowseButton<ComboBox<JMeterToolchain>>(ComboBox<JMeterToolchain>(), null) {

    private val comboBox = childComponent
    private val knownToolchains get() = JMeterKnownToolchainsState.getInstance().knownToolchains
    private var knownToolchainInfos = knownToolchains
        .map { JMeterToolchain.fromPath(it) }
        .filter { it.isValid() }

    private var myLastSelectedItem: JMeterToolchain = toolchainSettings.toolchain()
    private val myModel: ToolchainComboBoxModel = ToolchainComboBoxModel()


    init {
        this.addActionListener { e: ActionEvent? -> this.showInterpretersDialog(e) }
        this.comboBox.setModel(this.myModel)
        this.comboBox.renderer = JMeterListCellRenderer()
        // this.comboBox.setMinimumAndPreferredWidth(0)
        this.myModel.addElement(toolchainSettings.toolchain())
        this.myModel.selectedItem = toolchainSettings.toolchain()
        this.updateDropDownList()
        this.comboBox.addItemListener { e: ItemEvent ->
            if (e.stateChange == 1) {
                this.handleSelectedItemChange()
            }
        }
    }

    private fun showInterpretersDialog(e: ActionEvent?) {

    }

    private fun updateDropDownList() {
        val toolchains: LinkedHashSet<JMeterToolchain> = LinkedHashSet(knownToolchainInfos)
        toolchains.add(toolchainSettings.toolchain())
        SwingHelper.updateItems(this.comboBox, toolchains.toList(), null)
        this.comboBox.addItem(AddToolchain)
        this.comboBox.addItem(DownloadToolchain)

        val selected = toolchainSettings.toolchain()

        if (!Objects.equals(this.comboBox.selectedItem, selected)) {
            this.comboBox.selectedItem = selected
            this.handleSelectedItemChange()
        }
    }

    private fun handleSelectedItemChange() {
        when (val selected = this.getToolchainRef()) {
            is AddToolchain -> {
                this.comboBox.setSelectedItem(this.myLastSelectedItem)
                ApplicationManager.getApplication().invokeLater(addAction , ModalityState.current())
            }
            is DownloadToolchain -> {
                this.comboBox.setSelectedItem(this.myLastSelectedItem)
                ApplicationManager.getApplication().invokeLater(downloadAction , ModalityState.current())
            }
            is NullToolchain -> Unit
            else -> {
                if (this.myLastSelectedItem != selected) {
                    this.myLastSelectedItem = selected
                    this@ToolchainChooserComponent.onSelectAction(selected)
                }
            }
        }
    }

    private fun getToolchainRef(): JMeterToolchain {
        var ref = this.comboBox.selectedItem as? JMeterToolchain
        if (ref == null) {
            ref = NullToolchain
        }
        return ref
    }

    fun selectedToolchain(): JMeterToolchain? {
        return comboBox.selectedItem as? JMeterToolchain
    }

    fun refresh() {
        comboBox.removeAllItems()
        knownToolchainInfos = knownToolchains
            .map { JMeterToolchain.fromPath(it) }
            .filter { it.isValid() }

        updateDropDownList()
    }

    fun select(location: String) {
        if (location.isEmpty()) {
            comboBox.selectedItem = NullToolchain
            return
        }

        val infoToSelect = knownToolchainInfos.find { it.homePath() == location } ?: return
        comboBox.selectedItem = infoToSelect
    }

    private inner class ToolchainComboBoxModel: DefaultComboBoxModel<JMeterToolchain>()
}
