package co.anbora.labs.jmeter.ide.toolchain

import co.anbora.labs.jmeter.ide.settings.JMeterConfigurationUtil
import co.anbora.labs.jmeter.ide.settings.ui.NullToolchain
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.exists
import kotlin.io.path.pathString

interface JMeterToolchain {
    fun name(): String
    fun version(): String
    fun stdlibExtDir(): Path?
    fun stdlibJunitDir(): Path?
    fun stdlibDir(): Path?
    fun stdBinDir(): Path?
    fun stdConfig(): Path?
    fun rootDir(): Path?
    fun homePath(): String
    fun isValid(): Boolean

    companion object {
        fun fromPath(homePath: String): JMeterToolchain {
            if (homePath == "") {
                return NullToolchain
            }

            val path = Path.of(homePath)
            if (!Files.isDirectory(path)) {
                return NullToolchain
            }
            return fromDirectory(path)
        }

        private fun fromDirectory(rootDir: Path): JMeterToolchain {
            val version = JMeterConfigurationUtil.guessToolchainVersion(rootDir.pathString)
            return JMeterLocalToolchain(version, rootDir)
        }
    }
}
