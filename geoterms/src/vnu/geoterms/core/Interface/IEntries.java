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
public interface IEntries {

    int insert(String entry);

    int remove(int index);

    int remove(String entry);

    ListModel getListModel();

    void refresh();

    String getDefinition(int index);

    int find(String entry);

    void changeLanguage(String newLanguage);
}
