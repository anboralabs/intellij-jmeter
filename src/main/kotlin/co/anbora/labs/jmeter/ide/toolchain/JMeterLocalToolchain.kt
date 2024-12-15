package co.anbora.labs.jmeter.ide.toolchain

import co.anbora.labs.jmeter.ide.settings.JMeterConfigurationUtil
import co.anbora.labs.jmeter.ide.settings.JMeterConfigurationUtil.UNDEFINED_VERSION
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.isFile

class JMeterLocalToolchain(
    private val version: String,
    private val rootDir: VirtualFile,
) : JMeterToolchain {
    private val homePath = rootDir.path

    private val binDir = rootDir.findChild(JMeterConfigurationUtil.STANDARD_BIN_PATH)
    private val stdConfig = binDir?.findChild(JMeterConfigurationUtil.STANDARD_JMETER_CONFIG)

    private val libDir = rootDir.findChild(JMeterConfigurationUtil.STANDARD_LIB_PATH)
    private val libExtDir = libDir?.findChild(JMeterConfigurationUtil.LIB_EXT_PATH)
    private val libJunitDir = libDir?.findChild(JMeterConfigurationUtil.LIB_JUNIT_PATH)

    override fun name(): String = version

    override fun version(): String = version

    override fun stdlibExtDir(): VirtualFile? = libExtDir

    override fun stdlibJunitDir(): VirtualFile? = libJunitDir

    override fun stdlibDir(): VirtualFile? = libDir

    override fun stdBinDir(): VirtualFile? = binDir

    override fun stdConfig(): VirtualFile? = stdConfig

    override fun rootDir(): VirtualFile = rootDir

    override fun homePath(): String = homePath

    override fun isValid(): Boolean {
        return isValidDir(rootDir())
                && isValidDir(stdBinDir())
                && isValidConfig(stdConfig())
                && isValidDir(stdlibDir())
                && isValidDir(stdlibExtDir())
                && isValidDir(stdlibJunitDir())
                && version() != UNDEFINED_VERSION
    }

    private fun isValidDir(dir: VirtualFile?): Boolean {
        return dir != null && dir.isValid
                && dir.isInLocalFileSystem && dir.isDirectory
    }

    private fun isValidConfig(config: VirtualFile?): Boolean {
        return config != null && config.isValid
                && config.isInLocalFileSystem && config.isFile
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JMeterLocalToolchain

        return FileUtil.comparePaths(other.homePath(), homePath()) == 0
    }

    override fun hashCode(): Int = homePath.hashCode()
}
