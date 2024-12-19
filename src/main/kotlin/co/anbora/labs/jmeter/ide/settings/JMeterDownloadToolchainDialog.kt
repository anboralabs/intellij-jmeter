package co.anbora.labs.jmeter.ide.settings

import co.anbora.labs.jmeter.ide.settings.ui.ReleaseListCellRenderer
import co.anbora.labs.jmeter.ide.toolchain.JMeterKnownToolchainsState
import co.anbora.labs.jmeter.ide.toolchain.JMeterToolchain
import co.anbora.labs.jmeter.ide.utils.DownloadHelper
import co.anbora.labs.jmeter.ide.utils.toPath
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.*
import com.intellij.openapi.ui.BrowseFolderDescriptor.Companion.withPathToTextConvertor
import com.intellij.openapi.ui.BrowseFolderDescriptor.Companion.withTextToPathConvertor
import com.intellij.openapi.ui.validation.CHECK_DIRECTORY
import com.intellij.openapi.ui.validation.CHECK_NON_EMPTY
import com.intellij.ui.dsl.builder.*
import com.intellij.ui.layout.ValidationInfoBuilder
import javax.swing.JComponent

class JMeterDownloadToolchainDialog(
    val project: Project,
    private val onDownloadJMeter: (path: String) -> Unit
): DialogWrapper(project) {

    data class LocationModel (
        var location: String,
    )

    private val mainPanel: DialogPanel
    private val model: LocationModel = LocationModel("${PathManager.getConfigDir()}")
    private lateinit var releaseField: Cell<ComboBox<JMeterRelease>>

    init {
        title = "Download JMeter"

        mainPanel = panel {
            row("Version:") {
                releaseField = comboBox(Releases.Versions, ReleaseListCellRenderer())
                    .columns(COLUMNS_LARGE)
                    .align(AlignX.FILL)
            }
            row("Location:") {
                val fileChooserDescriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor()
                    .withTitle("Select Directory")
                    .withPathToTextConvertor(::getPresentablePath)
                    .withTextToPathConvertor(::getCanonicalPath)
                textFieldWithBrowseButton("JMeter Directory", project, fileChooserDescriptor)
                    .align(AlignX.FILL)
                    .bindText(model::location)
                    .trimmedTextValidation(CHECK_NON_EMPTY, CHECK_DIRECTORY)
                    .columns(COLUMNS_LARGE)
                    .validationOnApply { validateJMeterLocation() }
            }
        }

        init()
    }

    override fun createCenterPanel(): JComponent = mainPanel

    private fun ValidationInfoBuilder.validateJMeterLocation(): ValidationInfo? {
        if (model.location.isEmpty()) {
            return error("JMeter location is required")
        }

        return null
    }

    override fun doOKAction() {
        val release = releaseField.component.item
        val location = model.location.toPath()

        val installed = location.resolve(release.destinationFolder)

        if (JMeterKnownToolchainsState.getInstance().isKnown(installed.toString())) {
            setErrorText("This location is already added")
            return
        }

        DownloadHelper.downloadAsync(project, release, location, {
            val newPath = installed.toString()
            JMeterKnownToolchainsState.getInstance().add(JMeterToolchain.fromPath(newPath))
            onDownloadJMeter(newPath)

            super.doOKAction()
        }, { exception ->
            setErrorText(exception.message)
        })
    }
}