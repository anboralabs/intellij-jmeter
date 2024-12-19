package co.anbora.labs.jmeter.ide.toolchain

import co.anbora.labs.jmeter.ide.settings.JMeterConfigurationUtil
import co.anbora.labs.jmeter.ide.settings.JMeterConfigurationUtil.UNDEFINED_VERSION
import com.intellij.openapi.util.io.FileUtil
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.pathString

class JMeterLocalToolchain(
    private val version: String,
    private val rootDir: Path,
) : JMeterToolchain {
    private val homePath = rootDir.pathString

    private val binDir = rootDir.resolve(JMeterConfigurationUtil.STANDARD_BIN_PATH)
    private val stdConfig = binDir.resolve(JMeterConfigurationUtil.STANDARD_JMETER_CONFIG)

    private val libDir = rootDir.resolve(JMeterConfigurationUtil.STANDARD_LIB_PATH)
    private val libExtDir = libDir.resolve(JMeterConfigurationUtil.LIB_EXT_PATH)
    private val libJunitDir = libDir.resolve(JMeterConfigurationUtil.LIB_JUNIT_PATH)

    override fun name(): String = version

    override fun version(): String = version

    override fun stdlibExtDir(): Path = libExtDir

    override fun stdlibJunitDir(): Path = libJunitDir

    override fun stdlibDir(): Path = libDir

    override fun stdBinDir(): Path = binDir

    override fun stdConfig(): Path = stdConfig

    override fun rootDir(): Path = rootDir

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

    private fun isValidDir(dir: Path?): Boolean {
        return dir != null && Files.isDirectory(dir)
    }

    private fun isValidConfig(config: Path?): Boolean {
        return config != null && Files.isRegularFile(config)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JMeterLocalToolchain

        return FileUtil.comparePaths(other.homePath(), homePath()) == 0
    }

    override fun hashCode(): Int = homePath.hashCode()
}
