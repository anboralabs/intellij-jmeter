package co.anbora.labs.jmeter

import org.apache.jmeter.JMeterGuiLauncher
import org.apache.jmeter.NewDriver
import org.apache.jmeter.util.JMeterUtils
import java.util.*

fun main(args: Array<String>) {
    JMeterUtils.setLocale(Locale.US)
    JMeterUtils.setJMeterHome(System.getenv("JMETER_HOME"))
    JMeterUtils.loadJMeterProperties("jmeter.properties")
    JMeterGuiLauncher.startGui(null)
}