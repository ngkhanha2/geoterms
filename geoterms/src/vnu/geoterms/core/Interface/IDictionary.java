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

    int edit(String entry, String newDefinition);

    int edit(int index, String newDefinition);

    int remove(int index);

    int remove(String entry);

    int find(String entry);

    int indexOf(String entry);

    String getName();

    String getFileName();

    String getEntry(int index);

    String getDefinition(int index);

    String getHTMLDivDefinition(int index);

    boolean isSelected();

    void setSelected(boolean selected);

    void close();
}
