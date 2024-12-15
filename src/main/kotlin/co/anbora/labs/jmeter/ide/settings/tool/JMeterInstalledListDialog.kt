package co.anbora.labs.jmeter.ide.settings.tool

import co.anbora.labs.jmeter.ide.settings.ui.JMeterListCellRenderer
import co.anbora.labs.jmeter.ide.settings.ui.NullToolchain
import co.anbora.labs.jmeter.ide.toolchain.JMeterKnownToolchainsState
import co.anbora.labs.jmeter.ide.toolchain.JMeterToolchain
import co.anbora.labs.jmeter.ide.toolchain.flow.JMeterToolchainSubscriber
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.ComponentUtil
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.JBUI.Borders
import javax.swing.DefaultListModel
import javax.swing.JComponent

class JMeterInstalledListDialog(
    val project: Project,
    private val addAction: Runnable,
    private val downloadAction: Runnable
): DialogWrapper(project, false) {

    private val myListModel = DefaultListModel<JMeterToolchain>()
    private val myList = JBList(myListModel)

    private lateinit var myCenterPanelComponent: JComponent
    private lateinit var subscriber: JMeterToolchainSubscriber

    init {

        this.myList.selectionMode = 2
        this.myList.emptyText.setText(NullToolchain.name())
        this.myList.cellRenderer = JMeterListCellRenderer()

        val decorator = ToolbarDecorator.createDecorator(this.myList)
            .setPanelBorder(Borders.empty())
            .setAddAction {
                ApplicationManager.getApplication().invokeLater(addAction , ModalityState.current())
            }
            .setRemoveAction {  }
            .addExtraActions(DownloadExternalAction(downloadAction))
            .disableUpDownActions()

        val decoratorPanel = decorator.createPanel()
        decoratorPanel.preferredSize = JBUI.size(550, 300)
        val pane = ComponentUtil.getScrollPane(this.myList)
        if (pane != null) {
            pane.horizontalScrollBarPolicy = 31
        }

        this.myCenterPanelComponent = decoratorPanel

        this.title = "JMeter Manager"
        init()
        this.attachSubscribers()
    }

    private fun attachSubscribers() {
        this.subscriber = JMeterToolchainSubscriber {
            this.myListModel.clear()
            this.fillList(it)
        }

        JMeterKnownToolchainsState.getInstance().subscribe(subscriber)
    }

    private fun fillList(installed: List<JMeterToolchain>) {
        this.myListModel.addAll(installed)
    }

    override fun createCenterPanel(): JComponent = this.myCenterPanelComponent

    override fun dispose() {
        this.subscriber.unsubscribe()
        super.dispose()
    }
}