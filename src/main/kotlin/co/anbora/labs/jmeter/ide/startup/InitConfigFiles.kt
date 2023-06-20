package co.anbora.labs.jmeter.ide.startup

import co.anbora.labs.jmeter.ide.settings.Settings
import co.anbora.labs.jmeter.util.UnzipUtility
import com.intellij.openapi.application.ApplicationActivationListener
import com.intellij.openapi.wm.IdeFrame
import java.nio.file.Files
import java.nio.file.Paths

class InitConfigFiles: ApplicationActivationListener {
    private val unzipUtility = UnzipUtility()

    override fun applicationActivated(ideFrame: IdeFrame) {
        val binPath = Paths.get(Settings.PLUGIN_PATH).resolve("bin")
        if (Files.notExists(binPath)) {
            unzipUtility.unzip("bin.zip", Settings.PLUGIN_PATH)
        }
    }
}