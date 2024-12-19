package co.anbora.labs.jmeter.ide.settings.ui

import co.anbora.labs.jmeter.ide.toolchain.JMeterToolchain
import com.intellij.openapi.vfs.VirtualFile
import java.nio.file.Path

object NullToolchain: JMeterToolchain {
    override fun name(): String = "<No JMeter Home>"

    override fun version(): String = ""

    override fun stdlibExtDir(): Path? = null

    override fun stdlibJunitDir(): Path? = null

    override fun stdlibDir(): Path? = null

    override fun stdBinDir(): Path? = null

    override fun stdConfig(): Path? = null

    override fun rootDir(): Path? = null

    override fun homePath(): String = ""

    override fun isValid(): Boolean = false
}