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

    void addDictionary(IDictionary dictionary);

    void update();

    String getCurrentDirectory();
}
