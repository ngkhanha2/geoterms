/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vnu.geoterms.core.component;

import javax.swing.JCheckBox;

/**
 *
 * @author Khanh
 */
public class JCheckBoxWithObject extends JCheckBox {

    private Object object;

    public JCheckBoxWithObject(Object object) {
        this.object = object;
        this.setText(object.toString());
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
        this.setText(object.toString());
    }
}
