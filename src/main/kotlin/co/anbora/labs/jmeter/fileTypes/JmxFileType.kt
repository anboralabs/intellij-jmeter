package co.anbora.labs.jmeter.fileTypes

import co.anbora.labs.jmeter.ide.icons.JmeterIcons
import com.intellij.openapi.fileTypes.UserBinaryFileType
import org.apache.jmeter.JMeterGuiLauncher
import org.apache.jmeter.gui.action.LookAndFeelCommand
import org.apache.jmeter.util.JMeterUtils
import org.apache.jorphan.gui.JMeterUIDefaults
import java.util.*
import javax.swing.Icon

object JmxFileType: UserBinaryFileType() {

    private const val FILETYPE_NAME = "Jmeter"

    const val EDITOR_NAME = "Jmeter Details"

    private val res = ResourceBundle.getBundle("org/apache/jmeter/jmeter")

    init {
        JMeterUtils.setLocale(Locale.US)
        JMeterUtils.setJMeterHome(System.getenv("JMETER_HOME"))
        JMeterUtils.loadJMeterProperties(res)
        //JMeterUtils.applyHiDPIOnFonts()
        //JMeterUIDefaults.INSTANCE.install()

        val jMeterLaf = LookAndFeelCommand.getPreferredLafCommand()
        try {
            //LookAndFeelCommand.activateLookAndFeel("laf:com.sun.java.swing.plaf.motif.MotifLookAndFeel")
        } catch (_: IllegalArgumentException) {
        }
    }

    override fun getName(): String = FILETYPE_NAME

    override fun getDescription(): String = "Jmeter viewer"

    override fun getDisplayName(): String = "Jmeter viewer"

    override fun getIcon(): Icon = JmeterIcons.FILE
}