package co.anbora.labs.jmeter.ide.settings

import co.anbora.labs.jmeter.ide.utils.toPath
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.io.path.exists

object JMeterConfigurationUtil {

    const val TOOLCHAIN_NOT_SETUP = "JMeter path not found, not setup correctly?"
    const val UNDEFINED_VERSION = "N/A"
    const val UNKNOWN_VERSION = "unknown"

    const val STANDARD_LIB_PATH = "lib"
    const val STANDARD_BIN_PATH = "bin"

    const val LIB_EXT_PATH = "ext"
    const val LIB_JUNIT_PATH = "junit"

    const val STANDARD_JMETER_CONFIG = "jmeter.properties"

    fun guessToolchainVersion(path: String): String {
        if (path.isBlank()) {
            return UNDEFINED_VERSION
        }

        val configPath = "$path/$STANDARD_BIN_PATH/$STANDARD_JMETER_CONFIG"
        if (!configPath.toPath().exists()) {
            return UNDEFINED_VERSION
        }

        val pattern = Pattern.compile("(\\d+.\\d+(.\\d+)?)")
        val matcher: Matcher = pattern.matcher(path)

        var result = ""

        if (matcher.find()) {
            result = matcher.group()
        }

        return result.trim().ifEmpty { UNKNOWN_VERSION }
    }
}
