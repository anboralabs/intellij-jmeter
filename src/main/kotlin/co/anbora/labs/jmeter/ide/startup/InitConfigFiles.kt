package co.anbora.labs.jmeter.ide.startup

import co.anbora.labs.jmeter.ide.settings.Settings
import co.anbora.labs.jmeter.util.UnzipUtility
import com.intellij.openapi.application.PreloadingActivity
import com.intellij.openapi.progress.ProgressIndicator
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.absolutePathString

class InitConfigFiles: PreloadingActivity() {
    private val unzipUtility = UnzipUtility()
    override fun preload(indicator: ProgressIndicator) {
        val binPath = Paths.get(Settings.PLUGIN_PATH).resolve("bin")
        if (Files.notExists(binPath)) {
            unzipUtility.unzip("bin.zip", Settings.PLUGIN_PATH)
        }
    }
}