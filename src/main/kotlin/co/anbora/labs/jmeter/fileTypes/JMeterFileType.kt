package co.anbora.labs.jmeter.fileTypes

import co.anbora.labs.jmeter.ide.icons.JmeterIcons
import com.intellij.openapi.fileTypes.UserBinaryFileType
import javax.swing.Icon

object JMeterFileType: UserBinaryFileType() {

    private const val FILETYPE_NAME = "JMeter"

    const val EDITOR_NAME = "JMeter Details"

    override fun getName(): String = FILETYPE_NAME

    override fun getDescription(): String = "JMeter viewer"

    override fun getDisplayName(): String = "JMeter viewer"

    override fun getIcon(): Icon = JmeterIcons.FILE
}
