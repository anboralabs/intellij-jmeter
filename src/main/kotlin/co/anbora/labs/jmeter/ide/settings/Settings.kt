package co.anbora.labs.jmeter.ide.settings

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.extensions.PluginId
import java.nio.file.Paths
import kotlin.io.path.absolutePathString


object Settings {

    val OPTION_KEY_JMETER_PATH = "pathJmeterExecution"
    val SELECTED_JMETER_PATH = "selectedJmeterPath"

    val PLUGIN_ID = "co.anbora.labs.jmeter.jmeter-intellij"

    val PLUGIN_PATH: String = PluginManagerCore.getPlugin(PluginId.findId(PLUGIN_ID))
        ?.pluginPath
        ?.resolve("lib")
        ?.absolutePathString()
        .orEmpty()

    private val jaasConf = "jaas.conf"
    private val jmeterProperties = "jmeter.properties"
    private val krb5 = "krb5.conf"
    private val log4j2 = "log4j2.xml"
    private val reportGeneratorProperties = "reportgenerator.properties"
    private val saveServiceProperties = "saveservice.properties"
    private val systemProperties = "system.properties"
    private val upgradeProperties = "upgrade.properties"
    private val userProperties = "user.properties"

    val binPath = Paths.get(PLUGIN_PATH).resolve("bin")

    val allConfigFiles = listOf(
        jaasConf, jmeterProperties, krb5, log4j2,
        reportGeneratorProperties, saveServiceProperties, systemProperties,
        upgradeProperties, userProperties
    )

    private val PROPERTIES = PropertiesComponent.getInstance()

    init {

        if (PROPERTIES.getValue(OPTION_KEY_JMETER_PATH).isNullOrBlank()) {
            PROPERTIES.setValue(OPTION_KEY_JMETER_PATH, PLUGIN_PATH)
        }
        if (PROPERTIES.getValue(SELECTED_JMETER_PATH).isNullOrBlank()) {
            PROPERTIES.setValue(SELECTED_JMETER_PATH, PathHomeConfig.INTERNAL.name)
        }
    }

    operator fun set(key: String, value: String?) {
        PROPERTIES.setValue(key, value)
    }

    operator fun get(key: String): String {
        return PROPERTIES.getValue(key).orEmpty()
    }

    fun getSelectedJmeterPath(): PathHomeConfig {
        return PathHomeConfig.valueOf(PROPERTIES.getValue(SELECTED_JMETER_PATH) ?: PathHomeConfig.INTERNAL.name)
    }
}
