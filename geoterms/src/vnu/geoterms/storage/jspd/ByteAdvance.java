/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vnu.geoterms.storage.jspd;

/**
 *
 * @author Khanh
 */
class ByteAdvance {

    public static final byte[] intToByteArray(int i) {
        byte[] dword = new byte[4];
        dword[0] = ((byte) (i >> 24 & 0xFF));
        dword[1] = ((byte) (i >> 16 & 0xFF));
        dword[2] = ((byte) (i >> 8 & 0xFF));
        dword[3] = ((byte) (i & 0xFF));
        return dword;
    }

    public static final int byteArrayToInt(byte[] b) {
        return (b[0] << 24) + ((b[1] & 0xFF) << 16) + ((b[2] & 0xFF) << 8) + (b[3] & 0xFF);
    }

    public static final int byteArrayToInt(byte[] b, int start) {
        return (b[(0 + start)] << 24) + ((b[(1 + start)] & 0xFF) << 16) + ((b[(2 + start)] & 0xFF) << 8) + (b[(3 + start)] & 0xFF);
    }

}
