package co.anbora.labs.jmeter.router.actions

import com.intellij.openapi.extensions.ExtensionPointName
import org.apache.jmeter.gui.action.Command

abstract class RouterActionsFlavor {

    abstract fun getDefaultCommands(): Collection<Command>

    /**
     * Flavor is added to result in [getApplicableFlavors] if this method returns true.
     * @return whether this flavor is applicable.
     */
    protected open fun isApplicable(): Boolean = true

    companion object {
        private val EP_NAME: ExtensionPointName<RouterActionsFlavor> =
            ExtensionPointName.create("co.anbora.labs.jmeter.jmeter-intellij.routerActions")

        fun getApplicableFlavors(): List<RouterActionsFlavor> =
            EP_NAME.extensionList.filter { it.isApplicable() }
    }

}