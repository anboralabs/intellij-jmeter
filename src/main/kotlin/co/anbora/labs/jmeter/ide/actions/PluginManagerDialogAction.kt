package co.anbora.labs.jmeter.ide.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import org.jmeterplugins.repository.PluginManager
import org.jmeterplugins.repository.PluginManagerDialog

class PluginManagerDialogAction: AnAction() {
    override fun actionPerformed(p0: AnActionEvent) {
        val dialog = PluginManagerDialog(PluginManager())

        dialog.pack()
        dialog.isVisible = true
    }
}