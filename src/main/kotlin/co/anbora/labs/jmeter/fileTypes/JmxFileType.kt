package co.anbora.labs.jmeter.fileTypes

import co.anbora.labs.jmeter.ide.icons.JmeterIcons
import co.anbora.labs.jmeter.ide.settings.Settings
import co.anbora.labs.jmeter.ide.settings.Settings.OPTION_KEY_JMETER_PATH
import com.intellij.openapi.fileTypes.UserBinaryFileType
import org.apache.jmeter.JMeter
import org.apache.jmeter.plugin.PluginManager
import org.apache.jmeter.util.JMeterUtils
import org.apache.jorphan.gui.JMeterUIDefaults
import java.util.*
import javax.swing.Icon

object JmxFileType: UserBinaryFileType() {

    private const val FILETYPE_NAME = "Jmeter"

    const val EDITOR_NAME = "Jmeter Details"

    private val res = ResourceBundle.getBundle("org/apache/jmeter/jmeter")

    init {
        PluginManager.install(JMeter(), true)
        JMeterUtils.setLocale(Locale.US)
        JMeterUtils.setJMeterHome(Settings[OPTION_KEY_JMETER_PATH])
        JMeterUtils.loadJMeterProperties(res)
        JMeterUtils.applyHiDPIOnFonts()
        JMeterUIDefaults.INSTANCE.install()
    }

    override fun getName(): String = FILETYPE_NAME

    override fun getDescription(): String = "Jmeter viewer"

    override fun getDisplayName(): String = "Jmeter viewer"

    override fun getIcon(): Icon = JmeterIcons.FILE
}