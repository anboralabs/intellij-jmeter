package co.anbora.labs.jmeter.ide.startup

import co.anbora.labs.jmeter.ide.actions.SetupFilesAction
import co.anbora.labs.jmeter.ide.notifications.JMeterNotifications
import co.anbora.labs.jmeter.ide.settings.Settings
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import kotlin.io.path.exists

class InitConfigFiles: ProjectActivity {

    override suspend fun execute(project: Project) {
        if (checkConfigFilesNotExist()) {
            val notification = JMeterNotifications.createNotification(
                "JMeter Plugin Setup",
                "Some configurations files needed to start use it.",
                NotificationType.INFORMATION,
                SetupFilesAction()
            )

            JMeterNotifications.showNotification(notification, project)
        }
    }

    private fun checkConfigFilesNotExist(): Boolean {
        return false;
    }
}
