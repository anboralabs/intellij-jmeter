package co.anbora.labs.jmeter.ide.actions

import co.anbora.labs.jmeter.ide.notifications.JMeterNotifications
import co.anbora.labs.jmeter.ide.settings.Settings
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.DumbAwareAction
import java.nio.file.Files
import java.util.concurrent.CompletableFuture
import kotlin.io.path.exists

class SetupFilesAction: DumbAwareAction("Setup") {
    override fun actionPerformed(p0: AnActionEvent) {
        ProgressManager.getInstance().executeNonCancelableSection {
            createConfigFiles().whenComplete { _, error ->
                if (error != null) {
                    val notification = JMeterNotifications.createNotification(
                        "JMeter Plugin Setup",
                        "Error creating configurations",
                        NotificationType.ERROR
                    )

                    JMeterNotifications.showNotification(notification, p0.project)
                } else {
                    val notification = JMeterNotifications.createNotification(
                        "JMeter Plugin Setup",
                        "Please restart the IDE",
                        NotificationType.INFORMATION,
                        RestartIDEAction()
                    )

                    JMeterNotifications.showNotification(notification, p0.project)
                }
            }
        }
    }

    private fun createConfigFiles(): CompletableFuture<Unit> {
        return CompletableFuture()
    }
}
