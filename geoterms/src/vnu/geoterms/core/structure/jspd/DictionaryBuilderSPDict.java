/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vnu.geoterms.core.structure.jspd;

import java.io.File;
import java.io.RandomAccessFile;
import vnu.geoterms.core.DictionaryBuilder;
import vnu.geoterms.core.Comparator;
import vnu.geoterms.core.Interface.IDictionaryBuilder;
import vnu.geoterms.core.Interface.IDictionary;

/**
 *
 * @author Khanh
 */
public class DictionaryBuilderSPDict extends DictionaryBuilder {

    @Override
    public IDictionary build(String fileName) {
        File f = new File(this.getDictionariesDirectory() + File.separator + fileName + File.separator + fileName + ".jspd");
        if (!f.exists()) {
            return null;
        }
        try {
            RandomAccessFile raf = new RandomAccessFile(f, "rw");
            byte[] buffer = new byte[7];
            raf.read(buffer, 0, 7);
            String title = new String(buffer, 0, 7, "UTF-8");
            if (title.equals("2SPDict")) {
                raf.close();
                return new DictionarySPDict(fileName);
            }
            raf.close();
        } catch (Exception ex) {

        }
        return null;
    }

}
