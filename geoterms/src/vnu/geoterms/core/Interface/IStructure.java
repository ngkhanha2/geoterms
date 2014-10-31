/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vnu.geoterms.core.Interface;

/**
 *
 * @author Khanh
 */
public interface IStructure {

    void insert(Object o);

    void remove(Object o);

    Object find(Object o);

    Object take(Object o);

    void close();
}
