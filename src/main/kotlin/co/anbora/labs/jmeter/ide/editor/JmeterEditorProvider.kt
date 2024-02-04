package co.anbora.labs.jmeter.ide.editor

import co.anbora.labs.jmeter.fileTypes.JmxFileType
import co.anbora.labs.jmeter.ide.gui.JmeterEditor
import com.intellij.diff.editor.DiffVirtualFile
import com.intellij.openapi.fileEditor.AsyncFileEditorProvider
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileTypes.FileTypeRegistry
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.SingleRootFileViewProvider
import com.thoughtworks.xstream.converters.ConversionException
import org.apache.jmeter.NewDriver.loadJMeterLibsToPlugin
import org.apache.jmeter.gui.GuiPackage
import org.apache.jmeter.gui.MainFrame
import org.apache.jmeter.gui.action.ActionRouter
import org.apache.jmeter.gui.action.Load
import org.apache.jmeter.gui.tree.JMeterTreeListener
import org.apache.jmeter.gui.tree.JMeterTreeModel
import org.apache.jmeter.save.SaveService
import org.apache.jmeter.util.JMeterUtils
import org.apache.jorphan.collections.HashTree
import java.io.File

private const val EDITOR_TYPE_ID = "co.anbora.labs.jmeter.editor"

class JmeterEditorProvider: AsyncFileEditorProvider, DumbAware {

    override fun accept(project: Project, file: VirtualFile): Boolean {
        return isJmeterFile(file)
                && !SingleRootFileViewProvider.isTooLargeForContentLoading(file)
                && !SingleRootFileViewProvider.isTooLargeForIntelligence(file)
                && file !is DiffVirtualFile
    }

    private fun isJmeterFile(file: VirtualFile): Boolean {
        val fileType = FileTypeRegistry.getInstance().getFileTypeByExtension(file.extension.orEmpty())
        return fileType == JmxFileType
    }

    override fun createEditor(project: Project, file: VirtualFile): FileEditor = createJmeterEditor(file, project)

    override fun getEditorTypeId(): String = EDITOR_TYPE_ID

    override fun getPolicy(): FileEditorPolicy = FileEditorPolicy.HIDE_DEFAULT_EDITOR

    override fun createEditorAsync(project: Project, file: VirtualFile): AsyncFileEditorProvider.Builder {
        return object : AsyncFileEditorProvider.Builder() {
            override fun build(): FileEditor = createJmeterEditor(file, project)
        }
    }

    private fun createJmeterEditor(
        file: VirtualFile,
        project: Project
    ): JmeterEditor {
        val treeModel = JMeterTreeModel()
        val treeLis = JMeterTreeListener(treeModel)
        GuiPackage.initInstance(treeLis, treeModel)

        val instance = ActionRouter.getInstance()

        val pluginClassLoader = this.javaClass.classLoader
        JMeterUtils.setDynamicLoader(loadJMeterLibsToPlugin(JMeterUtils.getJMeterHome(), pluginClassLoader))

        instance.populateCommandMap()
        treeLis.setActionHandler(instance)
        val main = MainFrame(treeModel, treeLis)
        loadFile(file.toNioPath().toFile())
        return JmeterEditor(project, file, main)
    }

    private fun loadFile(testFile: File) {
        //val currentThread = Thread.currentThread()
        //val originalClassLoader = currentThread.contextClassLoader
        try {
            //currentThread.setContextClassLoader(JMeterUtils.getDynamicLoader());

            val tree: HashTree = SaveService.loadTree(testFile)
            GuiPackage.getInstance().testPlanFile = testFile.absolutePath
            Load.insertLoadedTree(1, tree)
        } catch (e: ConversionException) {
            JMeterUtils.reportErrorToUser(SaveService.CEtoString(e))
        } catch (e: Exception) {
            JMeterUtils.reportErrorToUser(e.toString())
        } finally {
            //currentThread.setContextClassLoader(originalClassLoader);
        }
    }
}