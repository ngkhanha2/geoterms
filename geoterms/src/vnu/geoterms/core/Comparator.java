/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vnu.geoterms.core;

import java.text.Collator;
import java.util.Locale;
import vnu.geoterms.core.Interface.IComparator;

/**
 *
 * @author Khanh
 */
public class Comparator implements IComparator {

    Collator collator;

    public Comparator(String language) {
        this.collator = Collator.getInstance(new Locale(language));
    }

    @Override
    public int compare(Object o1, Object o2) {
        return this.collator.compare((String) o1, (String) o2);
    }

}
