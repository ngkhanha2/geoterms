/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vnu.geoterms.storage.jspd;

import java.text.Collator;
import java.util.Locale;

/**
 *
 * @author Khanh
 */
class TComparator {

    Collator collator;
    Locale locale;

    public TComparator(String lang) {
        this.locale = new Locale(lang);
        this.collator = Collator.getInstance(this.locale);
    }

    public int compare(Object emp1, Object emp2) {
        return this.collator.compare((String) emp1, (String) emp2);
    }

    public int SoSanh(String emp1, String emp2) {
        if (emp1 == null) {
            emp1 = "";
        }
        if (emp2 == null) {
            emp2 = "";
        }
        return this.collator.compare(emp1, emp2);
    }

    public int compareThuong(String emp1, String emp2) {
        return this.collator.compare(emp1.toLowerCase(this.locale), emp2.toLowerCase(this.locale));
    }
}
