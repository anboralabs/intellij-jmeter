package co.anbora.labs.jmeter.ide.actions

import co.anbora.labs.jmeter.ide.license.CheckLicense
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction

class BuyLicense: DumbAwareAction("Buy") {
    override fun actionPerformed(e: AnActionEvent) {
        CheckLicense.requestLicense("Support plugin development.")
    }
}