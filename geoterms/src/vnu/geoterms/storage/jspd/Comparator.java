/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vnu.geoterms.storage.jspd;

import java.text.Collator;
import java.util.Locale;
import vnu.geoterms.core.Interface.IComparator;

/**
 *
 * @author Khanh
 */
public class Comparator implements IComparator {

    Collator collator;

    public Comparator() {
        this.collator = Collator.getInstance(Locale.US);
    }

    @Override
    public int compare(Object o1, Object o2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int compare(String s1, String s2) {
        return this.collator.compare(s1, s2);
    }
}
