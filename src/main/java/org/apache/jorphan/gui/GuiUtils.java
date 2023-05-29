/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jorphan.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public final class GuiUtils {

    /**
     * Create a scroll panel that sets its preferred size to its minimum size.
     * Explicitly for scroll panes that live inside other scroll panes, or
     * within containers that stretch components to fill the area they exist in.
     * Use this for any component you would put in a scroll pane (such as
     * TextAreas, tables, JLists, etc). It is here for convenience and to avoid
     * duplicate code. JMeter displays best if you follow this custom.
     *
     * @param comp
     *            the component which should be placed inside the scroll pane
     * @return a JScrollPane containing the specified component
     */
    public static JScrollPane makeScrollPane(Component comp) {
        JScrollPane pane = new JScrollPane(comp);
        pane.setPreferredSize(pane.getMinimumSize());
        return pane;
    }

    /**
     * Clears border of the given component.
     *
     * @param comp input component
     * @param <C> component type
     * @return the given component with border set to empty
     */
    public static<C extends JComponent> C emptyBorder(C comp) {
        comp.setBorder(BorderFactory.createEmptyBorder());
        return comp;
    }

    /**
     * Fix the size of a column according to the header text.
     *
     * @param column to be resized
     * @param table containing the column
     */
    public static void fixSize(TableColumn column, JTable table) {
        TableCellRenderer rndr;
        rndr = column.getHeaderRenderer();
        if (rndr == null){
            rndr = table.getTableHeader().getDefaultRenderer();
        }
        Component c = rndr.getTableCellRendererComponent(
                table, column.getHeaderValue(), false, false, -1, column.getModelIndex());
        int width = c.getPreferredSize().width+10;
        column.setMaxWidth(width);
        column.setPreferredWidth(width);
        column.setResizable(false);
    }

    /**
     * Create a GUI component JLabel + JComboBox with a left and right margin (5px)
     * @param label the label
     * @param comboBox the combo box
     * @return the JComponent (margin+JLabel+margin+JComboBox)
     */
    public static JComponent createLabelCombo(String label, JComboBox<?> comboBox) {
        JPanel labelCombo = new JPanel();
        labelCombo.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JLabel caption = new JLabel(label);
        caption.setLabelFor(comboBox);
        caption.setBorder(new EmptyBorder(0, 5, 0, 5));
        labelCombo.add(caption);
        labelCombo.add(comboBox);
        return labelCombo;
    }

    /**
     * Stop any editing that is currently being done on the table. This will
     * save any changes that have already been made.
     *
     * @param table the table to stop on editing
     */
    public static void stopTableEditing(JTable table) {
        if (table.isEditing()) {
            TableCellEditor cellEditor = table.getCellEditor(table.getEditingRow(), table.getEditingColumn());
            cellEditor.stopCellEditing();
        }
    }

    /**
     * cancel any editing that is currently being done on the table.
     *
     * @param table the table to cancel on editing
     * @since 3.1
     */
    public static void cancelEditing(JTable table) {
        // If a table cell is being edited, we must cancel the editing
        if (table != null && table.isEditing()) {
            TableCellEditor cellEditor = table.getCellEditor(table.getEditingRow(), table.getEditingColumn());
            cellEditor.cancelCellEditing();
        }
    }

    /**
     * Get pasted text from clipboard
     *
     * @return String Pasted text
     * @throws UnsupportedFlavorException
     *             if the clipboard data can not be get as a {@link String}
     * @throws IOException
     *             if the clipboard data is no longer available
     */
    public static String getPastedText() throws UnsupportedFlavorException, IOException {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable trans = clipboard.getContents(null);
        DataFlavor[] flavourList = trans.getTransferDataFlavors();
        Collection<DataFlavor> flavours = new ArrayList<>(flavourList.length);
        if (Collections.addAll(flavours, flavourList) && flavours.contains(DataFlavor.stringFlavor)) {
            return (String) trans.getTransferData(DataFlavor.stringFlavor);
        } else {
            return null;
        }
    }

    /**
     * Make menu scrollable
     * @param menu {@link JMenu}
     */
    public static void makeScrollableMenu(JMenu menu) {
        if (menu.getItemCount() > 0 && !GraphicsEnvironment.isHeadless()) {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            // We use 80% of height
            int maxItems = (int)Math.round(
                    screenSize.getHeight()*0.8/menu.getMenuComponent(0).getPreferredSize().getHeight());
            MenuScroller.setScrollerFor(menu, maxItems, 200);
        }
    }

    /**
     * Copy text to clipboard
     * @param text Text to copy
     */
    public static final void copyTextToClipboard(String text) {
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(text);
        clpbrd.setContents(stringSelection, null);
    }
}
