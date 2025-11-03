package co.anbora.labs.jmeter.ide.editor

import co.anbora.labs.jmeter.fileTypes.JMeterFileType
import co.anbora.labs.jmeter.ide.editor.gui.JMeterFileEditor
import co.anbora.labs.jmeter.ide.editor.gui.NotConfiguredFileEditor
import co.anbora.labs.jmeter.ide.toolchain.JMeterToolchainService.Companion.toolchainSettings
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileEditor.FileEditorProvider
import com.intellij.openapi.fileTypes.FileTypeRegistry
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.SingleRootFileViewProvider

private const val EDITOR_TYPE_ID = "co.anbora.labs.jmeter.editor"

class JMeterEditorProvider: FileEditorProvider, DumbAware {

    override fun accept(project: Project, file: VirtualFile): Boolean {
        return isJmeterFile(file)
                && !SingleRootFileViewProvider.isTooLargeForContentLoading(file)
                && !SingleRootFileViewProvider.isTooLargeForIntelligence(file)
    }

    private fun isJmeterFile(file: VirtualFile): Boolean {
        val fileType = FileTypeRegistry.getInstance().getFileTypeByExtension(file.extension.orEmpty())
        return fileType == JMeterFileType
    }

    override fun createEditor(project: Project, file: VirtualFile): FileEditor = createJMeterEditor(project, file)

    override fun getEditorTypeId(): String = EDITOR_TYPE_ID

    override fun getPolicy(): FileEditorPolicy = FileEditorPolicy.HIDE_DEFAULT_EDITOR

    private fun createJMeterEditor(project: Project, file: VirtualFile): FileEditor {
        val toolchain = toolchainSettings.toolchain()

        if (toolchain.isValid()) {
            return JMeterFileEditor(project, file)
        }

        return NotConfiguredFileEditor(project, file)
    }
}
