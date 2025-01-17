package co.anbora.labs.jmeter.ide.listeners

import co.anbora.labs.jmeter.ide.checker.CheckerFlavor
import co.anbora.labs.jmeter.ide.editor.gui.JMeterFileEditor
import co.anbora.labs.jmeter.ide.license.CheckLicense
import co.anbora.labs.jmeter.ide.notifications.JMeterNotifications
import co.anbora.labs.jmeter.ide.utils.toPath
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener

class JMeterSelectionListener: FileEditorManagerListener {

    override fun selectionChanged(event: FileEditorManagerEvent) {
        val editor = event.newEditor
        if (editor is JMeterFileEditor) {
            if (!CheckerFlavor.isSupported()) {
                CheckLicense.requestLicense("Support plugin")
                JMeterNotifications.supportNotification(editor.getProject())
            }
            val fileName = event.newFile?.path?.toPath()?.toFile()?.name.orEmpty()
            editor.reInitInstance(fileName)
        }
        super.selectionChanged(event)
    }
}
