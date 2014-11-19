/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vnu.geoterms.core.storage;

import static java.lang.Integer.min;
import java.text.Collator;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import javax.swing.*;
import vnu.geoterms.core.Interface.IComparator;
import vnu.geoterms.core.Interface.IEntries;

/**
 *
 * @author Khanh
 */
public class Entries implements IEntries {

    private DefaultListModel<String> listModel;

    private IComparator comparator;

    public Entries() {
        this.listModel = new DefaultListModel<String>();
        this.comparator = new Comparator("en");
    }

    private int compare(String s1, String s2) {
        int length = min(s1.length(), s2.length());
        for (int i = 0; i < length; ++i) {
            if (s1.charAt(i) < s2.charAt(i)) {
                return -1;
            }
            if (s1.charAt(i) > s2.charAt(i)) {
                return 1;
            }
        }
        return 0;
    }

    private int binarySearchPosition(String entry) {
        int left = 0;
        int right = this.listModel.size() - 1;
        int mid;
        while (left <= right) {
            mid = (left + right) >> 1;
            if (this.comparator.compare(entry, this.listModel.getElementAt(mid)) <= 0) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    @Override
    public int insert(String entry) {
        int index = binarySearchPosition(entry);
        if (index >= this.listModel.size()) {
            this.listModel.addElement(entry);
        } else {
            if (this.comparator.compare(entry, this.listModel.elementAt(index)) != 0) {
                this.listModel.add(index, entry);
                return 1;
            }
        }
        return 0;
    }

    @Override
    public int remove(int index) {

        return 0;
    }

    @Override
    public int remove(String entry
    ) {

        return 0;
    }

    @Override
    public ListModel getListModel() {
        return listModel;
    }

    @Override
    public String getDefinition(int index
    ) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int find(String entry) {
        return binarySearchPosition(entry);
    }

    @Override
    public void refresh() {

    }

    public void changeLanguage(String newLanguage) {
        this.comparator = new Comparator(newLanguage);
        Collator esCollator = Collator.getInstance(new Locale(newLanguage));
        Collections.sort(Arrays.asList(listModel.toArray()), esCollator);
    }
}
