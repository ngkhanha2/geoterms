/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vnu.geoterms.core.interfaces;

/**
 *
 * @author Khanh
 */
public interface IStructure {

    void insert(Object item);

    void remove(Object item);

    void search(Object item);

    IIterator getIterator();
}
