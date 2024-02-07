package co.anbora.labs.jmeter.ide.toolchain.flavors

import co.anbora.labs.jmeter.ide.toolchain.JMeterToolchainFlavor
import co.anbora.labs.jmeter.ide.utils.toPathOrNull
import java.io.File
import java.nio.file.Path
import kotlin.io.path.isDirectory
import kotlin.io.path.isExecutable

class JMeterSysPathToolchainFlavor : JMeterToolchainFlavor() {
    override fun getHomePathCandidates(): Sequence<Path> {
        return System.getenv("JMETER_HOME")
            .orEmpty()
            .split(File.pathSeparator)
            .asSequence()
            .filter { it.isNotEmpty() }
            .mapNotNull { it.toPathOrNull() }
            .filter { it.isExecutable() }
            .map { it.parent }
            .filter { it.isDirectory() }
    }
}
