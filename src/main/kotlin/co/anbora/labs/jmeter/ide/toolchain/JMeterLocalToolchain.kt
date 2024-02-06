package co.anbora.labs.jmeter.ide.toolchain

import co.anbora.labs.jmeter.ide.settings.JMeterConfigurationUtil
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.VirtualFile

class JMeterLocalToolchain(
    private val version: String,
    private val rootDir: VirtualFile,
) : JMeterToolchain {
    private val homePath = rootDir.path
    private val libDir = rootDir.findChild(JMeterConfigurationUtil.STANDARD_LIB_PATH)
    private val binDir = rootDir.findChild(JMeterConfigurationUtil.STANDARD_BIN_PATH)
    private val libExtDir = libDir?.findChild(JMeterConfigurationUtil.LIB_EXT_PATH)
    private val libJunitDir = libDir?.findChild(JMeterConfigurationUtil.LIB_JUNIT_PATH)

    override fun name(): String = version

    override fun version(): String = version

    override fun stdlibExtDir(): VirtualFile? = libExtDir

    override fun stdlibJunitDir(): VirtualFile? = libJunitDir

    override fun stdlibDir(): VirtualFile? = libDir

    override fun stdBinDir(): VirtualFile? = binDir

    override fun rootDir(): VirtualFile = rootDir

    override fun homePath(): String = homePath

    override fun isValid(): Boolean {
        val dir = rootDir()
        return dir.isValid && dir.isInLocalFileSystem && dir.isDirectory
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JMeterLocalToolchain

        return FileUtil.comparePaths(other.homePath(), homePath()) == 0
    }

    override fun hashCode(): Int = homePath.hashCode()
}
