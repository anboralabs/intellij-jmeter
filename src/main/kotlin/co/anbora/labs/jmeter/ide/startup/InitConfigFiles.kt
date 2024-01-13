package co.anbora.labs.jmeter.ide.startup

import co.anbora.labs.jmeter.ide.actions.SetupFilesAction
import co.anbora.labs.jmeter.ide.icons.JmeterIcons
import co.anbora.labs.jmeter.ide.notifications.JMeterNotifications
import co.anbora.labs.jmeter.ide.settings.Settings
import co.anbora.labs.jmeter.util.UnzipUtility
import com.intellij.notification.Notification
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.application.ApplicationActivationListener
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.openapi.wm.IdeFrame
import org.apache.commons.jexl2.internal.AbstractExecutor
import java.nio.file.Files
import java.nio.file.Paths
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
        return Settings.allConfigFiles.any {
            !Settings.binPath.resolve(it).exists()
        }
    }
}
