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

    override fun getName(): String = FILETYPE_NAME

    override fun getDescription(): String = "JMeter viewer"

    override fun getDisplayName(): String = "JMeter viewer"

    override fun getIcon(): Icon = JmeterIcons.FILE
}
