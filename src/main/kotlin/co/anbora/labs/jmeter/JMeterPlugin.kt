package co.anbora.labs.jmeter

import com.intellij.openapi.application.PathManager
import org.apache.jmeter.JMeterGuiLauncher
import org.apache.jmeter.util.JMeterUtils
import java.io.File
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.net.MalformedURLException
import java.net.URL
import java.net.URLClassLoader
import java.util.*
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.Path
import kotlin.io.path.walk


@OptIn(ExperimentalPathApi::class)
fun main(args: Array<String>) {
    JMeterUtils.setLocale(Locale.US)
    JMeterUtils.setJMeterHome(System.getenv("JMETER_HOME"))
    JMeterUtils.loadJMeterProperties("org/apache/jmeter/jmeter.properties")
    JMeterGuiLauncher.startGui(null)
}