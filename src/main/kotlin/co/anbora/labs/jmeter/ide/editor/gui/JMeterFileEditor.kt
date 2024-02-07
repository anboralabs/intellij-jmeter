package co.anbora.labs.jmeter.ide.editor.gui

import co.anbora.labs.jmeter.fileTypes.JmxFileType.EDITOR_NAME
import co.anbora.labs.jmeter.ide.editor.JmeterViewerState
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.fileEditor.FileEditorStateLevel
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.UserDataHolder
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.vfs.VirtualFile
import com.thoughtworks.xstream.converters.ConversionException
import org.apache.jmeter.NewDriver
import org.apache.jmeter.gui.GuiPackage
import org.apache.jmeter.gui.MainFrame
import org.apache.jmeter.gui.action.ActionRouter
import org.apache.jmeter.gui.action.Load
import org.apache.jmeter.gui.tree.JMeterTreeListener
import org.apache.jmeter.gui.tree.JMeterTreeModel
import org.apache.jmeter.save.SaveService
import org.apache.jmeter.util.JMeterUtils
import org.apache.jorphan.collections.HashTree
import java.beans.PropertyChangeListener
import java.io.File
import javax.swing.JComponent
import javax.swing.JPanel

abstract class JMeterFileEditor(
    private val projectArg: Project,
    private val fileArg: VirtualFile,
): FileEditor, FileEditorLocation {

    protected lateinit var mainPanel: JPanel

    private val userDataHolder: UserDataHolder = UserDataHolderBase()
    private var viewerState: JmeterViewerState = JmeterViewerState()


    abstract fun initComponents()

    override fun getName(): String = EDITOR_NAME

    override fun addPropertyChangeListener(listener: PropertyChangeListener) = Unit

    override fun removePropertyChangeListener(listener: PropertyChangeListener) = Unit

    override fun dispose() = Unit

    override fun getComponent(): JComponent = mainPanel

    override fun getPreferredFocusedComponent(): JComponent = mainPanel

    override fun isModified(): Boolean = false

    override fun getCurrentLocation(): FileEditorLocation = this

    override fun getEditor(): FileEditor = this

    override fun <T : Any?> getUserData(key: Key<T>): T? = userDataHolder.getUserData(key)

    override fun <T : Any?> putUserData(key: Key<T>, value: T?) = userDataHolder.putUserData(key, value)

    override fun compareTo(other: FileEditorLocation?): Int = 1

    override fun isValid(): Boolean = this.fileArg.isValid

    override fun getFile(): VirtualFile = this.fileArg

    override fun getState(level: FileEditorStateLevel): FileEditorState = viewerState

    override fun setState(state: FileEditorState) {
        val newState = state as? JmeterViewerState
        this.viewerState = newState ?: JmeterViewerState()
    }

}
