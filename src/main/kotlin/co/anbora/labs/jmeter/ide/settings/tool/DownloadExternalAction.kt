package co.anbora.labs.jmeter.ide.settings.tool

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState

class DownloadExternalAction(
    private val downloadAction: Runnable
): AnAction("Download", "Download JMeter", AllIcons.Actions.Download) {
    override fun actionPerformed(actionEvent: AnActionEvent) {
        ApplicationManager.getApplication().invokeLater(downloadAction , ModalityState.current())
    }
}