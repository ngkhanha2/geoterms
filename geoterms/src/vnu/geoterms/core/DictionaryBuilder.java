/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vnu.geoterms.core;

import java.io.File;
import vnu.geoterms.core.Interface.IDictionaryBuilder;

/**
 *
 * @author Khanh
 */
public abstract class DictionaryBuilder implements IDictionaryBuilder {

    public String getDictionariesDirectory() {
        return System.getProperty("user.dir") + File.separator + "dict";
    }

}
