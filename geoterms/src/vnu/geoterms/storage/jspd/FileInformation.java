/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vnu.geoterms.storage.jspd;

import vnu.geoterms.core.Interface.*;

/**
 *
 * @author Khanh
 */
public class FileInformation implements IFileInformation {

    private String sortingCode;
//    private String phatAm;
    private String dictionaryName;
    private String author;
    private String otherInformation;
    private String font1;
    private String size1;
    private String font2;
    private String size2;
    private String fileName;
    private int wordsNumber;
    private int dataRemain;
    private byte[] information;

    public String getSortingCode() {
        return sortingCode;
    }

    public void setSortingCode(String sortingCode) {
        this.sortingCode = sortingCode;
    }

    public String getDictionaryName() {
        return dictionaryName;
    }

    public void setDictionaryName(String dictionaryName) {
        this.dictionaryName = dictionaryName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getOtherInformation() {
        return otherInformation;
    }

    public void setOtherInformation(String otherInformation) {
        this.otherInformation = otherInformation;
    }

    public String getFont1() {
        return font1;
    }

    public void setFont1(String font1) {
        this.font1 = font1;
    }

    public String getSize1() {
        return size1;
    }

    public void setSize1(String size1) {
        this.size1 = size1;
    }

    public String getFont2() {
        return font2;
    }

    public void setFont2(String font2) {
        this.font2 = font2;
    }

    public String getSize2() {
        return size2;
    }

    public void setSize2(String size2) {
        this.size2 = size2;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getWordsNumber() {
        return wordsNumber;
    }

    public void setWordsNumber(int wordsNumber) {
        this.wordsNumber = wordsNumber;
    }

    public int getDataRemain() {
        return dataRemain;
    }

    public void setDataRemain(int dataRemain) {
        this.dataRemain = dataRemain;
    }

    public byte[] getInformation() {
        return information;
    }

    public void setInformation(byte[] information) {
        this.information = information;
    }

    public FileInformation() {

    }

}
