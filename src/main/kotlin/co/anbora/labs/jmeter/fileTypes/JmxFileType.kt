package co.anbora.labs.jmeter.fileTypes

import co.anbora.labs.jmeter.ide.icons.JmeterIcons
import com.intellij.openapi.fileTypes.UserBinaryFileType
import org.apache.jmeter.util.JMeterUtils
import java.util.*
import javax.swing.Icon

object JmxFileType: UserBinaryFileType() {

    private const val FILETYPE_NAME = "Jmeter"

    const val EDITOR_NAME = "Jmeter Details"

    override fun getName(): String = FILETYPE_NAME

    override fun getDescription(): String = "Jmeter viewer"

    override fun getDisplayName(): String = "Jmeter viewer"

    override fun getIcon(): Icon = JmeterIcons.FILE
}