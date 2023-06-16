package org.apache.jmeter;

import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.MainFrame;
import org.apache.jmeter.gui.tree.JMeterTreeListener;
import org.apache.jmeter.gui.tree.JMeterTreeModel;

import javax.swing.*;

public class JMeterFrame extends JFrame {

    public JMeterFrame() {
        JMeterTreeModel treeModel = new JMeterTreeModel();
        JMeterTreeListener treeLis = new JMeterTreeListener(treeModel);
        GuiPackage.initInstance(treeLis, treeModel);
        add(new MainFrame(treeModel, treeLis));
    }

}
