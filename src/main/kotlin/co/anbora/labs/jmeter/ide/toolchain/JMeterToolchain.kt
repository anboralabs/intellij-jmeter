package co.anbora.labs.jmeter.ide.toolchain

import co.anbora.labs.jmeter.ide.settings.JMeterConfigurationUtil
import co.anbora.labs.jmeter.ide.settings.ui.NullToolchain
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import java.nio.file.Path

interface JMeterToolchain {
    fun name(): String
    fun version(): String
    fun stdlibExtDir(): VirtualFile?
    fun stdlibJunitDir(): VirtualFile?
    fun stdlibDir(): VirtualFile?
    fun stdBinDir(): VirtualFile?
    fun stdConfig(): VirtualFile?
    fun rootDir(): VirtualFile?
    fun homePath(): String
    fun isValid(): Boolean

    companion object {
        fun fromPath(homePath: String): JMeterToolchain {
            if (homePath == "") {
                return NullToolchain
            }

            val virtualFileManager = VirtualFileManager.getInstance()
            val rootDir = virtualFileManager.findFileByNioPath(Path.of(homePath)) ?: return NullToolchain
            return fromDirectory(rootDir)
        }

        private fun fromDirectory(rootDir: VirtualFile): JMeterToolchain {
            val version = JMeterConfigurationUtil.guessToolchainVersion(rootDir.path)
            return JMeterLocalToolchain(version, rootDir)
        }
    }
}
