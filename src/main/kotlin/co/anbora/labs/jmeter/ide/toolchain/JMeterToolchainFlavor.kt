package co.anbora.labs.jmeter.ide.toolchain

import co.anbora.labs.jmeter.ide.settings.JMeterConfigurationUtil
import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.util.io.isDirectory
import java.nio.file.Path

abstract class JMeterToolchainFlavor {

    fun suggestHomePaths(): Sequence<Path> = getHomePathCandidates().filter { isValidToolchainPath(it) }

    protected abstract fun getHomePathCandidates(): Sequence<Path>

    /**
     * Flavor is added to result in [getApplicableFlavors] if this method returns true.
     * @return whether this flavor is applicable.
     */
    protected open fun isApplicable(): Boolean = true

    /**
     * Checks if the path is the name of a V toolchain of this flavor.
     *
     * @param path path to check.
     * @return true if paths points to a valid home.
     */
    protected open fun isValidToolchainPath(path: Path): Boolean {
        return path.isDirectory()
                && path.resolve(JMeterConfigurationUtil.STANDARD_BIN_PATH).isDirectory()
                && path.resolve(JMeterConfigurationUtil.STANDARD_LIB_PATH).isDirectory()
    }

    companion object {
        private val EP_NAME: ExtensionPointName<JMeterToolchainFlavor> =
            ExtensionPointName.create("co.anbora.labs.jmeter.jmeter-intellij.toolchainFlavor")

        fun getApplicableFlavors(): List<JMeterToolchainFlavor> =
            EP_NAME.extensionList.filter { it.isApplicable() }

        fun getFlavor(path: Path): JMeterToolchainFlavor? =
            getApplicableFlavors().find { flavor -> flavor.isValidToolchainPath(path) }
    }
}
