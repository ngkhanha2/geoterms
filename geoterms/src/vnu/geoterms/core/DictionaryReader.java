/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vnu.geoterms.core;

import java.util.ArrayList;
import vnu.geoterms.core.Interface.IDictionary;
import vnu.geoterms.core.Interface.IDictionaryBuilder;
import vnu.geoterms.core.Interface.IDictionaryReader;

/**
 *
 * @author Khanh
 */
public class DictionaryReader implements IDictionaryReader {

    private ArrayList<IDictionaryBuilder> dictionaryBuilders;

    public DictionaryReader() {
        this.dictionaryBuilders = new ArrayList<IDictionaryBuilder>();
    }

    @Override
    public void addBuilder(IDictionaryBuilder builder) {
        this.dictionaryBuilders.add(builder);
    }

    @Override
    public IDictionary read(String fileName) {
        IDictionary dictionary = null;
        for (int i = 0; i < dictionaryBuilders.size(); ++i) {
            dictionary = dictionaryBuilders.get(i).build(fileName);
            if (dictionary != null) {
                return dictionary;
            }
        }
        return null;
    }

}
