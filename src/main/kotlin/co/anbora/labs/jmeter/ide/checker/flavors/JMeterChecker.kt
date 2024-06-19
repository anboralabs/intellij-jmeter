package co.anbora.labs.jmeter.ide.checker.flavors

import co.anbora.labs.jmeter.ide.checker.CheckerFlavor
import co.anbora.labs.jmeter.ide.license.CheckLicense

class JMeterChecker: CheckerFlavor() {
    override fun check(): Boolean = CheckLicense.isLicensed() ?: false
}