package co.anbora.labs.jmeter.ide.listeners

import co.anbora.labs.jmeter.ide.editor.gui.JmxFileEditor
import co.anbora.labs.jmeter.ide.utils.toPath
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener

class JMeterSelectionListener: FileEditorManagerListener {

    override fun selectionChanged(event: FileEditorManagerEvent) {
        val editor = event.newEditor
        if (editor is JmxFileEditor) {
            val fileName = event.newFile?.path?.toPath()?.toFile()?.name.orEmpty()
            editor.reInitInstance(fileName)
        }
        super.selectionChanged(event)
    }
}
