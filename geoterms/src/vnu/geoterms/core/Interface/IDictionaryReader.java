/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vnu.geoterms.core.Interface;

/**
 *
 * @author Khanh
 */
public interface IDictionaryReader {

    void addBuilder(IDictionaryBuilder builder);

    IDictionary read(String fileName);
}
