package co.anbora.labs.jmeter.ide.settings.color

import java.awt.Color
import javax.swing.UIManager


fun findColorByKey(vararg colorKeys: String): Color? {
    var c: Color? = null
    for (key in colorKeys) {
        c = UIManager.getColor(key)
        if (c != null) {
            break
        }
    }
    assert(c != null) { "Can't find color for keys " + colorKeys.contentToString() }
    return c
}