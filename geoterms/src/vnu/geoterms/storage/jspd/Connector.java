/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vnu.geoterms.storage.jspd;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import vnu.geoterms.core.Interface.*;

/**
 *
 * @author Khanh
 */
public class Connector implements IConnector {

    private int fileOffset;

    public Connector(String fileName) {

    }

    @Override
    public Object get(int offset, int bytes) {

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void close() {

    }

}
