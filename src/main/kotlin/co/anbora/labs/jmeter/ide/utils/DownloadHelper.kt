package co.anbora.labs.jmeter.ide.utils

import co.anbora.labs.jmeter.ide.settings.JMeterRelease
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.util.io.HttpRequests
import org.apache.commons.io.IOUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import java.util.function.Consumer
import java.util.zip.ZipEntry
import java.util.zip.ZipFile


object DownloadHelper {

    fun downloadAsync(
        project: Project,
        release: JMeterRelease,
        downloadPath: Path,
        onSuccess: Consumer<Unit>,
        onError: Consumer<Throwable>
    ) {
        val dlFilePath = downloadPath.resolve(release.getZipName())
        ProgressManager.getInstance().run(object : Task.Backgroundable(project, "Downloading JMeter ${release.name}", false) {
            override fun run(progressIndicator: ProgressIndicator) {
                try {
                    HttpRequests.request(release.url).connect { request ->
                        downloadFile(
                            request.inputStream,
                            dlFilePath,
                            progressIndicator,
                            request.connection.getContentLength().toLong()
                        )
                        this@DownloadHelper.uncompress(dlFilePath, downloadPath)
                    }
                } catch (ex: IOException) {
                    onError.accept(ex)
                }
            }

            override fun shouldStartInBackground(): Boolean = false

            override fun isConditionalModal(): Boolean = true

            override fun onFinished() {
                onSuccess.accept(Unit)
            }
        })
    }

    @Throws(IOException::class)
    private fun downloadFile(input: InputStream, dlFileName: Path, progressIndicator: ProgressIndicator, size: Long) {
        val buffer = ByteArray(4096)
        Files.createDirectories(dlFileName.parent)

        Files.newOutputStream(dlFileName).use { output ->
            var accumulated = 0L
            var lg: Int
            while ((input.read(buffer).also { lg = it }) > 0 && !progressIndicator.isCanceled) {
                output.write(buffer, 0, lg)
                accumulated += lg.toLong()
                progressIndicator.fraction = accumulated.toDouble() / size.toDouble()
            }
        }
    }

    @Throws(IOException::class)
    private fun uncompress(dlFilePath: Path, cmd: Path) {
        if (!Files.exists(cmd)) {
            Files.createDirectories(cmd)
        }
        ZipFile(dlFilePath.toFile()).use { zipFile ->
            val entries: Enumeration<out ZipEntry> = zipFile.entries()
            while (entries.hasMoreElements()) {
                val entry = entries.nextElement()
                val entryDestination: File = cmd.resolve(entry.name).toFile()
                if (entry.isDirectory) {
                    entryDestination.mkdirs()
                } else {
                    entryDestination.getParentFile().mkdirs()
                    zipFile.getInputStream(entry).use { input ->
                        FileOutputStream(entryDestination).use { output ->
                            IOUtils.copy(input, output)
                        }
                    }
                }
            }
        }
    }
}