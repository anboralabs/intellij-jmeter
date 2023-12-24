package org.jmeterplugins.repository;

import javax.swing.*;
import org.apache.jmeter.gui.plugin.MenuCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PluginManagerMenuCreator implements MenuCreator {
  private static final Logger log =
      LoggerFactory.getLogger(PluginManagerMenuCreator.class);

  @Override
  public JMenuItem[] getMenuItemsAtLocation(MENU_LOCATION location) {
    if (location == MENU_LOCATION.OPTIONS) {
      try {
        PluginManagerCMDInstaller.main(new String[0]);
      } catch (Throwable e) {
        log.warn("Was unable to install pmgr cmdline tool", e);
      }

      try {
        return new JMenuItem[] {new PluginManagerMenuItem()};
      } catch (Throwable e) {
        log.error("Failed to load Plugins Manager", e);
        return new JMenuItem[0];
      }
    } else {
      return new JMenuItem[0];
    }
  }

  @Override
  public JMenu[] getTopLevelMenus() {
    return new JMenu[0];
  }

  @Override
  public boolean localeChanged(MenuElement menu) {
    return false;
  }

  @Override
  public void localeChanged() {}
}
