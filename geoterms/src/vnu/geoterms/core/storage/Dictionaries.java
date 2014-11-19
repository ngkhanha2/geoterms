/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vnu.geoterms.core.storage;

import java.util.*;
import javax.swing.*;
import vnu.geoterms.core.Interface.*;

/**
 *
 * @author Khanh
 */
public class Dictionaries implements IDictionaries {

    private ArrayList<IDictionary> dictionaries;

    private DefaultListModel<IDictionary> listModelDictionaries;

    public Dictionaries() {
        this.listModelDictionaries = new DefaultListModel<IDictionary>();
        this.dictionaries = new ArrayList<IDictionary>();
    }

    public ListModel<IDictionary> getListModel() {
        return listModelDictionaries;
    }

    public void add(IDictionary dictionary) {
        this.dictionaries.add(dictionary);
    }

    public ArrayList<IDictionary> getDictionaries() {
        return dictionaries;
    }
}
