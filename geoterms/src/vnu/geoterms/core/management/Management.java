/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vnu.geoterms.core.management;

import javax.swing.DefaultListModel;
import vnu.geoterms.core.storage.*;
import vnu.geoterms.core.Interface.*;
import javax.swing.ListModel;

/**
 *
 * @author Khanh
 */
public class Management implements IManagement {

    private IEntries entries;
    private IDictionaries dictionaries;
    private DefaultListModel<String> listModelLanguages;
    private String language;

    public Management() {
        this.entries = new Entries();
        this.dictionaries = new Dictionaries();

        this.listModelLanguages = new DefaultListModel<String>();
        this.listModelLanguages.addElement("en");
        this.listModelLanguages.addElement("vi");

        this.language = "en";
        //this.entries.changeLanguage(language);
    }

    @Override
    public ListModel getListModelDictionaries() {
        return this.dictionaries.getListModel();
    }

    @Override
    public ListModel getListModelEntries() {
        return this.entries.getListModel();
    }

    @Override
    public int insert(String entry, int dictionaryIndex) {
        return 0;
    }

    @Override
    public int findEntry(String entry) {
        return this.entries.find(entry);
    }

    @Override
    public String getEntryHTMLDefinition(int index) {
        String value = "<html><body><div>";
        int realIndex;
        for (int i = 0; i < this.dictionaries.getDictionaries().size(); ++i) {
            realIndex = this.dictionaries.getDictionaries().get(i).find((String) this.getListModelEntries().getElementAt(index));
            if (realIndex >= 0) {
                value += "<div><b>" + this.dictionaries.getDictionaries().get(i).getName() + "</b><hr></div>";
                value += this.dictionaries.getDictionaries().get(i).getHTMLDivDefinition(realIndex);
                value += "<div></div>";
            }
        }
        value += "</div></body></html>";
        return value;
    }

    @Override
    public ListModel getListModelLanguages() {
        return listModelLanguages;
    }

    @Override
    public void addDictionary(IDictionary dictionary) {
        this.dictionaries.add(dictionary);
        dictionary.syncronize(this.entries);
    }

    @Override
    public String getLanguage() {
        return this.language;
    }

    @Override
    public void setLanguage(String language) {
        this.language = language;
        this.entries.changeLanguage(language);

    }
}
