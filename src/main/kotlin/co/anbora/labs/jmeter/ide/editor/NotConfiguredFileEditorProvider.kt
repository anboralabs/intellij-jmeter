package co.anbora.labs.jmeter.ide.editor

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.UserDataHolder
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.vfs.VirtualFile
import java.beans.PropertyChangeListener
import javax.swing.JComponent


class NotConfiguredFileEditorProvider(
    private val projectArg: Project,
    private val fileArg: VirtualFile
): FileEditor, FileEditorLocation {

    private val userDataHolder: UserDataHolder = UserDataHolderBase()
    private var viewerState: JmeterViewerState = JmeterViewerState()

    override fun <T : Any?> getUserData(key: Key<T>): T? {
        TODO("Not yet implemented")
    }

    override fun <T : Any?> putUserData(key: Key<T>, value: T?) {
        TODO("Not yet implemented")
    }

    override fun dispose() {
        TODO("Not yet implemented")
    }

    override fun getComponent(): JComponent {
        TODO("Not yet implemented")
    }

    override fun getPreferredFocusedComponent(): JComponent? {
        TODO("Not yet implemented")
    }

    override fun getName(): String {
        TODO("Not yet implemented")
    }

    override fun setState(state: FileEditorState) {
        TODO("Not yet implemented")
    }

    override fun isModified(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isValid(): Boolean {
        TODO("Not yet implemented")
    }

    override fun addPropertyChangeListener(listener: PropertyChangeListener) {
        TODO("Not yet implemented")
    }

    override fun removePropertyChangeListener(listener: PropertyChangeListener) {
        TODO("Not yet implemented")
    }

    override fun compareTo(other: FileEditorLocation?): Int {
        TODO("Not yet implemented")
    }

    override fun getEditor(): FileEditor {
        TODO("Not yet implemented")
    }
}