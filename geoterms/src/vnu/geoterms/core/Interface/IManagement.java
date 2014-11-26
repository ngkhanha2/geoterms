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

    String getEntryHTMLDefinition(String entry);

    void addDictionary(IDictionary dictionary);

    String getLocale();

    void setLocale(String locale);

    ListModel getListModelEntriesWithKey(String key);
}
