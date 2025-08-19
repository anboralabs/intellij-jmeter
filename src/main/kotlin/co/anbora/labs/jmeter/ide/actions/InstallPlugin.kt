package co.anbora.labs.jmeter.ide.actions

import com.intellij.ide.BrowserUtil
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.openapi.actionSystem.AnActionEvent

class InstallPlugin(val pluginUrl: String): NotificationAction("Install") {
    override fun actionPerformed(
        event: AnActionEvent,
        notification: Notification
    ) {
        BrowserUtil.browse(pluginUrl)
        notification.expire()
    }
}