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
public interface IDictionary {

    int insert(String entry, String definition);

    void edit(String entry, String newEntry, String newDefinition);

    void edit(int index, String newEntry, String newDefinition);

    void remove(String entry);

    int find(String entry);

    void close();
}
