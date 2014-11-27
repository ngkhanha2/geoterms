/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vnu.geoterms.core.component;

import java.awt.Component;
import javax.swing.*;

/**
 *
 * @author Khanh
 */
class CellRenderer implements ListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        JCheckBox checkbox = (JCheckBox) value;

        if (isSelected) {
            // checkbox.setBorderPainted(true); 
            // checkbox.setForeground(UIManager.getColor("List.selectionForeground")); 
            // checkbox.setBackground(UIManager.getColor("List.selectionBackground")); 
        } else {
            // checkbox.setBorderPainted(false); 
            // checkbox.setForeground(UIManager.getColor("List.foreground")); 
            checkbox.setBackground(UIManager.getColor("List.background"));
        }
        return checkbox;
    }

}
