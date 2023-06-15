package co.anbora.labs.jmeter.ide.icons

import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

object JmeterIcons {

    val FILE = getIcon("jmeter.svg")

    private fun getIcon(path: String): Icon {
        return IconLoader.findIcon("/icons/$path", JmeterIcons.javaClass.classLoader) as Icon
    }
}