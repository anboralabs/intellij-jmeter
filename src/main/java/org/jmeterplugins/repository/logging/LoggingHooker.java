package org.jmeterplugins.repository.logging;

import java.lang.reflect.Constructor;
import org.jmeterplugins.repository.PluginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingHooker {
  private static final Logger log =
      LoggerFactory.getLogger(LoggingHooker.class);
  private final PluginManager mgr;

  public LoggingHooker(PluginManager mgr) { this.mgr = mgr; }

  public void hook() {
    try {
      if (!isJMeter32orLater()) {
        Logger logger = LoggerFactory.getLogger("jmeter.save.SaveService");
        // FIXME: what to do? logger.setLogTargets(new LogTarget[]{new
        // LoggerPanelWrapping(mgr)});
      } else {
        Class<?> cls = Class.forName(
            "org.jmeterplugins.repository.logging.LoggerAppender");
        Constructor<?> constructor =
            cls.getConstructor(String.class, PluginManager.class);
        constructor.newInstance("pmgr-logging-appender", mgr);
      }
    } catch (Throwable ex) {
      log.error("Cannot hook into logging", ex);
    }
  }

  public static boolean isJMeter32orLater() {
    try {
      Class<?> cls = LoggingHooker.class.getClassLoader().loadClass(
          "org.apache.jmeter.gui.logging.GuiLogEventBus");
      if (cls != null) {
        return true;
      }
    } catch (ClassNotFoundException ex) {
      log.debug(
          "Class 'org.apache.jmeter.gui.logging.GuiLogEventBus' not found", ex);
    } catch (Throwable ex) {
      log.warn("Fail to detect JMeter version", ex);
    }
    return false;
  }
}
