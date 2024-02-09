package co.anbora.labs.jmeter.ide.editor.gui

import co.anbora.labs.jmeter.ide.toolchain.JMeterToolchainService.Companion.toolchainSettings
import co.anbora.labs.jmeter.loader.JMeterLoader
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.thoughtworks.xstream.converters.ConversionException
import org.apache.jmeter.gui.GuiPackage
import org.apache.jmeter.gui.MainFrame
import org.apache.jmeter.gui.action.ActionRouter
import org.apache.jmeter.gui.action.ExitIDECommand
import org.apache.jmeter.gui.action.Load
import org.apache.jmeter.gui.tree.JMeterTreeListener
import org.apache.jmeter.gui.tree.JMeterTreeModel
import org.apache.jmeter.save.SaveService
import org.apache.jmeter.util.JMeterUtils
import org.apache.jorphan.collections.HashTree
import java.io.File

class JmxFileEditor(
    private val projectArg: Project,
    private val fileArg: VirtualFile,
): JMeterFileEditor(projectArg, fileArg) {

    init {
        initLoader()
        initComponents()
    }

    private fun initLoader() {
        val pluginClassLoader = this.javaClass.classLoader
        val toolchain = toolchainSettings.toolchain()
        JMeterLoader.initLoader(toolchain, pluginClassLoader)
    }

    private fun loadFile(testFile: File) {
        try {
            val tree: HashTree = SaveService.loadTree(testFile)
            GuiPackage.getInstance().testPlanFile = testFile.absolutePath
            Load.insertLoadedTree(1, tree)
        } catch (e: ConversionException) {
            JMeterUtils.reportErrorToUser(SaveService.CEtoString(e))
        } catch (e: Exception) {
            JMeterUtils.reportErrorToUser(e.toString())
        }
    }

    override fun initComponents() {
        val treeModel = JMeterTreeModel()
        val treeLis = JMeterTreeListener(treeModel)
        GuiPackage.initInstance(treeLis, treeModel)

        val instance = ActionRouter.getInstance()

        instance.populateCommandMapWithCustomCommands(ExitIDECommand())
        treeLis.setActionHandler(instance)
        mainPanel = MainFrame(treeModel, treeLis)
        loadFile(file.toNioPath().toFile())
    }
}
