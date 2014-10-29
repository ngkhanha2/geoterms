/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vnu.geoterms.core.rbtree;

/**
 *
 * @author Khanh
 */
public class Node {

    private Object item;
    private Node left;
    private Node right;
    private Node parrent;
    private Color color;

    public Node(Object item) {
        this.item = item;
        this.left = this.right = this.parrent = null;
        this.color = Color.Red;
    }

    public Object getItem() {
        return item;
    }

    public void setItem(Object item) {
        this.item = item;
    }

    public Object takeItem() {
        Object o = this.item;
        this.item = null;
        return o;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
        if (this.left != null) {
            this.left.setParrent(this);
        }
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
        if (this.right != null) {
            this.right.setParrent(this);
        }
    }

    public Node getParrent() {
        return parrent;
    }

    public void setParrent(Node parrent) {
        this.parrent = parrent;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
