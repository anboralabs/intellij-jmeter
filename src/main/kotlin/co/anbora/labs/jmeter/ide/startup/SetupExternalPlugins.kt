package co.anbora.labs.jmeter.ide.startup

import co.anbora.labs.jmeter.ide.notifications.JMeterNotifications
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.ide.plugins.PluginManagerCore
import net.sf.json.JSONArray

class SetupExternalPlugins: ProjectActivity {
    override suspend fun execute(project: Project) {
        try {
            val stream = javaClass.classLoader.getResourceAsStream("plugins/external.json") ?: return
            val jsonText = stream.bufferedReader(Charsets.UTF_8).use { it.readText() }
            val array = JSONArray.fromObject(jsonText)

            for (i in 0 until array.size) {
                val obj = array.getJSONObject(i)
                val id = obj.optString("id")
                if (id.isNullOrBlank()) continue
                val pluginId = PluginId.getId(id)
                val installed = PluginManagerCore.isPluginInstalled(pluginId)
                if (!installed) {
                    JMeterNotifications.externalPluginsNotification(
                        obj.optString("description"),
                        obj.optString("url"),
                        project
                    )
                }
            }
        } catch (_: Throwable) {
            // Be resilient on startup; avoid crashing the activity
        }
    }
}

