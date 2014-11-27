/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vnu.geoterms.core.component;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

/**
 *
 * @author Khanh
 */
public class JCheckBoxList extends JList {

    public JCheckBoxList() {
        setCellRenderer(new CellRenderer());
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int index = locationToIndex(e.getPoint());
                if (index != -1) {
                    JCheckBox checkbox = (JCheckBox) getModel().getElementAt(
                            index);
                    checkbox.setSelected(!checkbox.isSelected());
                    repaint();
                }
            }
        });
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public void selectAll() {
        int size = this.getModel().getSize();
        for (int i = 0; i < size; i++) {
            JCheckBox checkbox = (JCheckBoxWithObject) this.getModel()
                    .getElementAt(i);
            checkbox.setSelected(true);
        }
        this.repaint();
    }

    public void deselectAll() {
        int size = this.getModel().getSize();
        for (int i = 0; i < size; i++) {
            JCheckBox checkbox = (JCheckBoxWithObject) this.getModel()
                    .getElementAt(i);
            checkbox.setSelected(false);
        }
        this.repaint();
    }
}
