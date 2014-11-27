/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vnu.geoterms.core.Interface;

import java.util.ArrayList;

/**
 *
 * @author Khanh
 */
public interface IManagement {

    ArrayList<IDictionary> getDictionaries();

    String getEntryHTMLDefinition(String entry);

    void addDictionary(IDictionary dictionary);

    ArrayList<String> getEntriesWithKey(String key);

    void update();
}
