package co.anbora.labs.jmeter.loader

import co.anbora.labs.jmeter.ide.toolchain.JMeterToolchain
import kg.apc.jmeter.JMeterPluginsUtils
import org.apache.jmeter.DynamicClassLoader
import org.apache.jmeter.util.JMeterUtils
import java.io.File
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import java.util.logging.Logger
import kotlin.io.path.pathString

object JMeterLoader {

    private val OS_NAME: String = System.getProperty("os.name") // $NON-NLS-1$
    private val OS_NAME_LC = OS_NAME.lowercase()

    private val logger = Logger.getLogger(JMeterLoader::class.simpleName)

    fun initLoader(toolchain: JMeterToolchain, pluginClassLoader: ClassLoader?) {
        if (JMeterUtils.getDynamicLoader() == null) {
            JMeterUtils.setDynamicLoader(loadJMeterLibsToPlugin(toolchain, pluginClassLoader))
            JMeterUtils.initializeJMeter(toolchain.homePath())
            JMeterPluginsUtils.prepareJMeterEnv(toolchain.homePath())
        }
    }

    private fun loadJMeterLibsToPlugin(toolchain: JMeterToolchain, pluginClassLoader: ClassLoader?): DynamicClassLoader {
        val jars: MutableList<URL> = ArrayList()
        val usesUNC = OS_NAME_LC.startsWith("windows")

        val libTempDirs = if (toolchain.isValid()) arrayOf(
            toolchain.stdlibDir()?.toFile(),  // $NON-NLS-1$ $NON-NLS-2$
            toolchain.stdlibExtDir()?.toFile(),  // $NON-NLS-1$ $NON-NLS-2$
            toolchain.stdlibExtDir()?.toFile()
        ) else arrayOf()// $NON-NLS-1$ $NON-NLS-2$

        val libDirs = libTempDirs.filterNotNull()

        for (libDir in libDirs) {
            val libJars = libDir.listFiles { _: File?, name: String ->
                name.endsWith(
                    ".jar"
                )
            }
            if (libJars == null) {
                Throwable("Could not access $libDir").printStackTrace() // NOSONAR No logging here
                continue
            }
            Arrays.sort(libJars) // Bug 50708 Ensure predictable order of jars
            for (libJar in libJars) {
                try {
                    var s = libJar.path
                    // Fix path to allow the use of UNC URLs
                    if (usesUNC) {
                        if (s.startsWith("\\\\") && !s.startsWith("\\\\\\")) { // $NON-NLS-1$ $NON-NLS-2$
                            s = "\\\\" + s // $NON-NLS-1$
                        } else if (s.startsWith("//") && !s.startsWith("///")) { // $NON-NLS-1$ $NON-NLS-2$
                            s = "//$s" // $NON-NLS-1$
                        }
                    } // usesUNC
                    jars.add(File(s).toURI().toURL()) // See Java bug 4496398
                } catch (e: MalformedURLException) { // NOSONAR
                    logger.info {
                        "Error adding jar:" + libJar.absolutePath + e
                    }
                }
            }
        }
        return DynamicClassLoader(jars.toTypedArray<URL>(), pluginClassLoader)
    }

}
