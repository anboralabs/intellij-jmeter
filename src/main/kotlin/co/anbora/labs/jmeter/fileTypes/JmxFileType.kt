package co.anbora.labs.jmeter.fileTypes

import co.anbora.labs.jmeter.ide.icons.JmeterIcons
import co.anbora.labs.jmeter.ide.settings.Settings
import co.anbora.labs.jmeter.ide.settings.Settings.OPTION_KEY_JMETER_PATH
import com.intellij.openapi.fileTypes.UserBinaryFileType
import org.apache.jmeter.JMeter
import org.apache.jmeter.plugin.PluginManager
import org.apache.jmeter.util.JMeterUtils
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.*
import java.util.logging.Logger
import javax.swing.Icon

object JmxFileType: UserBinaryFileType() {

    private const val FILETYPE_NAME = "Jmeter"

    private val log = Logger.getLogger(JmxFileType::class.java.name)

    const val EDITOR_NAME = "JMeter Details"

    init {
        PluginManager.install(JMeter(), true)
        JMeterUtils.setLocale(Locale.US)
        JMeterUtils.setJMeterHome(Settings[OPTION_KEY_JMETER_PATH])
        initializeProperties()
    }

    override fun getName(): String = FILETYPE_NAME

    override fun getDescription(): String = "JMeter viewer"

    override fun getDisplayName(): String = "JMeter viewer"

    override fun getIcon(): Icon = JmeterIcons.FILE

    private fun initializeProperties() {
        JMeterUtils.loadJMeterProperties(
            Settings[OPTION_KEY_JMETER_PATH] + File.separator
                    + "bin" + File.separator // $NON-NLS-1$
                    + "jmeter.properties"
        )

        val jmeterProps = JMeterUtils.getJMeterProperties()

        // Add local JMeter properties, if the file is found
        val userProp = JMeterUtils.getPropDefault("user.properties", "") //$NON-NLS-1$
        if (userProp.isNotEmpty()) { //$NON-NLS-1$
            val file = JMeterUtils.findFile(userProp)
            if (file.canRead()) {
                try {
                    FileInputStream(file).use { fis ->
                        log.info {
                            "Loading user properties from: $file"
                        }
                        val tmp = Properties()
                        tmp.load(fis)
                        jmeterProps.putAll(tmp)
                    }
                } catch (e: IOException) {
                    log.warning {
                        "Error loading user property file: $userProp $e"
                    }
                }
            }
        }


        // Add local system properties, if the file is found
        val sysProp = JMeterUtils.getPropDefault("system.properties", "") //$NON-NLS-1$
        if (sysProp.isNotEmpty()) {
            val file = JMeterUtils.findFile(sysProp)
            if (file.canRead()) {
                try {
                    FileInputStream(file).use { fis ->
                        log.info {
                            "Loading system properties from: $file"
                        }
                        System.getProperties().load(fis)
                    }
                } catch (e: IOException) {
                    log.warning {
                        "Error loading system property file: $sysProp $e"
                    }
                }
            }
        }
    }
}
