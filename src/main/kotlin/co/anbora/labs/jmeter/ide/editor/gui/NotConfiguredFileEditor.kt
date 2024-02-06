package co.anbora.labs.jmeter.ide.editor.gui

import co.anbora.labs.jmeter.ide.settings.JMeterProjectSettingsConfigurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import java.awt.FlowLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.*
import javax.swing.border.EmptyBorder


class NotConfiguredFileEditor(
    private val projectArg: Project,
    private val fileArg: VirtualFile
): JMeterFileEditor(projectArg, fileArg) {
    init {
        initComponents()
    }

    override fun initComponents() {
        mainPanel = JPanel(FlowLayout(FlowLayout.CENTER))
        mainPanel.setBorder(EmptyBorder(5, 5, 5, 5))

        val jlError = JLabel("Before using JMeter plugin please setup.")
        val icon = ImageIcon(javaClass.getResource("images/error.png"))
        jlError.setIcon(icon)
        jlError.setHorizontalTextPosition(SwingConstants.TRAILING)
        jlError.setIconTextGap(15)

        val jbActivateLicense = JButton("Setup JMeter")
        jbActivateLicense.addActionListener { JMeterProjectSettingsConfigurable.show(projectArg) }

        mainPanel.add(jlError)
        mainPanel.add(jbActivateLicense)
    }

}