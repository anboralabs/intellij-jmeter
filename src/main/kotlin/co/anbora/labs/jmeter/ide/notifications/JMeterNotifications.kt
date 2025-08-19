package co.anbora.labs.jmeter.ide.notifications

import co.anbora.labs.jmeter.ide.actions.BuyLicense
import co.anbora.labs.jmeter.ide.actions.InstallPlugin
import co.anbora.labs.jmeter.ide.icons.JmeterIcons
import com.intellij.notification.Notification
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.project.Project

object JMeterNotifications {

    @JvmStatic
    fun createInternalNotification(
        title: String,
        content: String,
        notificationGroup: String,
        type: NotificationType,
        vararg actions: AnAction
    ): Notification {
        val notification = NotificationGroupManager.getInstance()
            .getNotificationGroup(notificationGroup)
            .createNotification(content, type)
            .setTitle(title)
            .setIcon(JmeterIcons.FILE)

        for (action in actions) {
            notification.addAction(action)
        }

        return notification
    }

    @JvmStatic
    fun createNotification(
        title: String,
        content: String,
        type: NotificationType,
        vararg actions: AnAction
    ): Notification {
        val notification = this.createInternalNotification(
            title,
            content,
            "JMeter_Notification",
            type,
            *actions
        )

        return notification
    }

    @JvmStatic
    fun showNotification(notification: Notification, project: Project?) {
        try {
            notification.notify(project)
        } catch (e: Exception) {
            notification.notify(project)
        }
    }

    fun supportNotification(project: Project?) {
        val notification = createNotification(
            "Support JMeter Plugin",
            "Buy the freemium license; 1 USD per year",
            NotificationType.WARNING,
            BuyLicense()
        )

        showNotification(notification, project)
    }

    fun externalPluginsNotification(content: String, url: String, project: Project?) {
        val notification = createInternalNotification(
            "JMeter Plugin",
            content,
            "15556c7b-3ac1-42a7-a571-b6527ed3990d_External_JMeter_Notification",
            NotificationType.INFORMATION,
            InstallPlugin(url)
        )

        showNotification(notification, project)
    }

}
