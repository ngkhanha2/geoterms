/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vnu.geoterms.core.Interface;

import java.util.ArrayList;
import javax.swing.*;

/**
 *
 * @author Khanh
 */
public interface IDictionaries {

    ArrayList<IDictionary> getDictionaries();

    ListModel getListModel();

    void add(IDictionary dictionary);
}
