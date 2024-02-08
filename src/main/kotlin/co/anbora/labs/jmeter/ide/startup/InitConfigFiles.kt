package co.anbora.labs.jmeter.ide.startup

import co.anbora.labs.jmeter.ide.actions.SetupFilesAction
import co.anbora.labs.jmeter.ide.notifications.JMeterNotifications
import co.anbora.labs.jmeter.ide.toolchain.JMeterToolchainService.Companion.toolchainSettings
import co.anbora.labs.jmeter.loader.JMeterLoader
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import org.apache.jmeter.JMeter
import org.apache.jmeter.plugin.PluginManager
import org.apache.jmeter.util.JMeterUtils

class InitConfigFiles: ProjectActivity {

    override suspend fun execute(project: Project) {
        PluginManager.install(JMeter(), true)

        val toolchain = toolchainSettings.toolchain()

        if (toolchain.isValid()) {
            JMeterLoader.initLoader(toolchain, this.javaClass.classLoader)
            return
        }

        val notification = JMeterNotifications.createNotification(
            "JMeter Plugin",
            "Please setup JMeter Home",
            NotificationType.INFORMATION,
            SetupFilesAction()
        )

        JMeterNotifications.showNotification(notification, project)
    }
}
