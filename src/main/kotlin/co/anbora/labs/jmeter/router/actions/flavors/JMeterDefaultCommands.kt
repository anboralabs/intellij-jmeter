package co.anbora.labs.jmeter.router.actions.flavors

import co.anbora.labs.jmeter.router.actions.RouterActionsFlavor
import org.apache.jmeter.gui.action.CheckDirty
import org.apache.jmeter.gui.action.Command
import org.apache.jmeter.gui.action.EditCommand
import org.apache.jmeter.gui.action.Load

class JMeterDefaultCommands: RouterActionsFlavor() {
    override fun getDefaultCommands(): Collection<Command> {
        return listOf<Command>(Load(), CheckDirty(), EditCommand())
    }
}