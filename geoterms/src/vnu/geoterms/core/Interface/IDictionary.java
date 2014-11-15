/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vnu.geoterms.core.Interface;

import javax.swing.ListModel;

/**
 *
 * @author Khanh
 */
public interface IDictionary {

    ListModel getModel();

    int insert(String entry, String definition);

    int edit(String entry, String newEntry, String newDefinition);

    int edit(int index, String newEntry, String newDefinition);

    int remove(int index);

    int remove(String entry);

    int find(String entry);

    String getEntry(int index);
    
    String getDefinition(int index);
    
    void close();
}
