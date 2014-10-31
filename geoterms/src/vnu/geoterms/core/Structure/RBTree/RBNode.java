/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vnu.geoterms.core.Structure.RBTree;

/**
 *
 * @author Khanh
 */
class RBNode {

    private Object object;
    private RBColor color;

    public RBNode(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public RBColor getColor() {
        return color;
    }

    public void setColor(RBColor color) {
        this.color = color;
    }
}
