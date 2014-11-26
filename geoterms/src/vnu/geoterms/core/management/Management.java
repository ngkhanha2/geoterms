/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vnu.geoterms.core.management;

import java.util.ArrayList;
import javax.swing.DefaultListModel;
import vnu.geoterms.core.Interface.*;
import javax.swing.ListModel;

/**
 *
 * @author Khanh
 */
public class Management implements IManagement {

    //private IDictionaries dictionaries;
    private DefaultListModel<String> listModelLanguages = null;
    private String language;
    private ArrayList<IDictionary> dictionaries = null;

    public Management() {
        this.dictionaries = new ArrayList<IDictionary>();

        this.listModelLanguages = new DefaultListModel<String>();
        this.listModelLanguages.addElement("en");
        this.listModelLanguages.addElement("vi");

        this.language = "en";
        //this.entries.changeLanguage(language);
    }

    @Override
    public ListModel getListModelDictionaries() {
        return null;
    }

    public int insert(String entry, int dictionaryIndex) {
        return 0;
    }

    @Override
    public String getEntryHTMLDefinition(String entry) {
        String value = "<html><body><div>";
        int realIndex;
        for (int i = 0; i < this.dictionaries.size(); ++i) {
            realIndex = this.dictionaries.get(i).find(entry);
            if (realIndex >= 0) {
                value += "<div><b>" + this.dictionaries.get(i).getName() + "</b><hr></div>";
                value += this.dictionaries.get(i).getHTMLDivDefinition(realIndex);
                value += "<div></div>";
            }
        }
        value += "</div></body></html>";
        return value;
    }

    @Override
    public ListModel getListModelEntriesWithKey(String key) {
        DefaultListModel listModel = new DefaultListModel();
        listModel.clear();
        if (!key.trim().isEmpty()) {
            key = key.trim().toLowerCase();
            for (int i = 0; i < this.dictionaries.size(); ++i) {
                int index = this.dictionaries.get(i).find(key);
                if (index == -1) {
                    continue;
                }
                while (true) {
                    String s = this.dictionaries.get(i).getEntry(index).toLowerCase().trim();
                    if (s.startsWith(key)) {
                        listModel.addElement(s);
                    } else {
                        break;
                    }
                    ++index;
                }
            }
        }
        return listModel;
    }

    @Override
    public void addDictionary(IDictionary dictionary) {
        this.dictionaries.add(dictionary);
        //dictionary.syncronize(this.entries);
    }

    @Override
    public String getLocale() {
        return this.language;
    }

    @Override
    public void setLocale(String locale) {
        this.language = locale;
        //this.entries.changeLanguage(locale);
    }
}
