package co.anbora.labs.jmeter.ide.notifications

import co.anbora.labs.jmeter.ide.icons.JmeterIcons
import com.intellij.notification.Notification
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.project.Project

object JMeterNotifications {
    fun createNotification(
        title: String,
        content: String,
        type: NotificationType,
        vararg actions: AnAction
    ): Notification {
        val notification = NotificationGroupManager.getInstance()
            .getNotificationGroup("JMeter_Notification")
            .createNotification(content, type)
            .setTitle(title)
            .setIcon(JmeterIcons.FILE)

        for (action in actions) {
            notification.addAction(action)
        }

        return notification
    }

    fun showNotification(notification: Notification, project: Project?) {
        try {
            notification.notify(project)
        } catch (e: Exception) {
            notification.notify(project)
        }
    }
}
