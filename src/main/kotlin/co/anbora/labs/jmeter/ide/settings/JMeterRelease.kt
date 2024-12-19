package co.anbora.labs.jmeter.ide.settings

data class JMeterRelease(val url: String, val name: String, val destinationFolder: String) {
    fun getZipName() = "$name.zip"
}
