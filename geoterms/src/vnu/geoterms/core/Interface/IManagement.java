/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vnu.geoterms.core.Interface;

import javax.swing.*;

/**
 *
 * @author Khanh
 */
public interface IManagement {

    ListModel getListModelDictionaries();

    ListModel getListModelEntries();

    int insert(String entry, int dictionaryIndex);

    int findEntry(String entry);

    String getEntryHTMLDefinition(int index);

    ListModel getListModelLanguages();

    void addDictionary(IDictionary dictionary);

    String getLanguage();

    void setLanguage(String currentLanguage);
}
