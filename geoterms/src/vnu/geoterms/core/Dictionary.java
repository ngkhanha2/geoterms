/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vnu.geoterms.core;

import java.io.File;
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
        return null;
    }

    public String getDirectory() {
        return System.getProperty("user.dir") + File.separator + "dict" + File.separator + this.fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getHTMLDivDefinition(int index) {
        return "<div></div>";
    }

    public String getHTMLDefinition(int index) {
        return "<html></html>";
    }
}
