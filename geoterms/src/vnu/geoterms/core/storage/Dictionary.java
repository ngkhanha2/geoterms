/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vnu.geoterms.core.storage;

import vnu.geoterms.core.Interface.IDictionary;

/**
 *
 * @author Khanh
 */
public abstract class Dictionary implements IDictionary {

    private String fileName;

    public Dictionary(String fileName) {
        this.fileName = fileName;
    }

    public String getFileDirectory() {
        return getDirectory() + this.fileName + ".jspd";
    }

    public String getDirectory() {
        return "dict/" + this.fileName + "/";
    }

    @Override
    public String getFileName() {
        return fileName;
    }
}
