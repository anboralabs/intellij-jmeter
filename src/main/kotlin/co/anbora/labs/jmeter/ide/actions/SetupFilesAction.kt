package co.anbora.labs.jmeter.ide.actions

import co.anbora.labs.jmeter.ide.settings.JMeterProjectSettingsConfigurable
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.ProjectManager

class SetupFilesAction: DumbAwareAction("Setup") {
    override fun actionPerformed(p0: AnActionEvent) {
        val project = p0.project ?: ProjectManager.getInstance().defaultProject
        JMeterProjectSettingsConfigurable.show(project)
    }
}
