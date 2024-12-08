package co.anbora.labs.jmeter.ide.settings.ui

import co.anbora.labs.jmeter.ide.toolchain.JMeterToolchain
import com.intellij.openapi.vfs.VirtualFile

object AddToolchain: JMeterToolchain {
    override fun name(): String = "Add..."

    override fun version(): String = ""

    override fun stdlibExtDir(): VirtualFile? = null

    override fun stdlibJunitDir(): VirtualFile? = null

    override fun stdlibDir(): VirtualFile? = null

    override fun stdBinDir(): VirtualFile? = null

    override fun stdConfig(): VirtualFile? = null

    override fun rootDir(): VirtualFile? = null

    override fun homePath(): String = ""

    override fun isValid(): Boolean = false
}