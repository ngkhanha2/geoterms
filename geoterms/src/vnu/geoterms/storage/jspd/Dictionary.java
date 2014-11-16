/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vnu.geoterms.storage.jspd;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import vnu.geoterms.core.Interface.IDictionary;

/**
 *
 * @author Khanh
 */
public class Dictionary implements IDictionary {

    private long listFileOffset;
    private DefaultListModel<String> model;
    private Comparator comparator;

    private RandomAccessFile raf;
    private int dataRemain;
    private byte[] otherInformation;

    public Dictionary(String fileName) {
        try {
            File f = new File(fileName);
            if (!f.exists()) {
                return;
            }
            this.raf = new RandomAccessFile(f, "rw");
            String title = new String(readBytesAtFileOffset(this.raf.getFilePointer(), 7), 0, 7, "UTF-8");
            if (!title.equals("2SPDict")) {
                return;
            }

            this.listFileOffset = this.raf.readInt();
            this.dataRemain = this.raf.readInt();
            this.raf.seek(this.listFileOffset);
            //Read other information of jspd file
            this.otherInformation = readShortBytesAtFilePointer();
            this.listFileOffset = this.raf.getFilePointer();

            this.comparator = new Comparator();

            //Read the entries of the dictionary
            this.model = new DefaultListModel<>();
            int entries = (int) (this.raf.length() - this.listFileOffset >> 2);
            for (int i = 0; i < entries; ++i) {
                changeFilePointerByListIndex(i);
                this.model.addElement(readShortStringAtFilePointer());
            }
        } catch (Exception ex) {

        }
    }

    private void changeFilePointerByListIndex(int index) throws IOException {
        this.raf.seek(this.listFileOffset + index * 4);
        this.raf.seek(this.raf.readInt());
    }

    private byte[] readShortBytesAtFilePointer() throws IOException {
        // Get length of buffer
        int length = this.raf.readShort();
        //Declare buffer
        byte[] buffer = new byte[length];
        //Read buffer
        this.raf.read(buffer, 0, length);
        return buffer;
    }

    private byte[] readLongBytesAtFilePointer() throws IOException {
        // Get length of buffer
        int length = this.raf.readInt();
        //Declare buffer
        byte[] buffer = new byte[length];
        //Read buffer
        this.raf.read(buffer, 0, length);
        return buffer;
    }

    private byte[] readShortBytesAtFileOffset(long offset) throws IOException {
        this.raf.seek(offset);
        int length = this.raf.readShort();
        byte[] buffer = new byte[length];
        this.raf.read(buffer, 0, length);
        return buffer;
    }

    private byte[] readLongBytesAtFileOffset(long offset) throws IOException {
        this.raf.seek(offset);
        int length = this.raf.readInt();
        byte[] buffer = new byte[length];
        this.raf.read(buffer, 0, length);
        return buffer;
    }

    private byte[] readBytesAtFileOffset(long offset, int length) throws IOException {
        this.raf.seek(offset);
        byte[] buffer = new byte[length];
        this.raf.read(buffer, 0, length);
        return buffer;
    }

    private String readShortStringAtFilePointer() throws IOException {
        //Get buffer
        byte[] buffer = readShortBytesAtFilePointer();
        //Convert buffer to string
        return new String(buffer, 0, buffer.length, "UTF-8");
    }

    private String readLongStringAtFilePointer() throws IOException {
        //Get buffer
        byte[] buffer = readLongBytesAtFilePointer();
        //Convert buffer to string
        return new String(buffer, 0, buffer.length, "UTF-8");
    }

    private void writeShortBytesAtFilePointer(byte[] bytes, int length) throws IOException {
        this.raf.writeShort(length);
        this.raf.write(bytes, 0, length);
    }

    private void writeLongBytesAtFilePointer(byte[] bytes, int length) throws IOException {
        this.raf.writeInt(length);
        this.raf.write(bytes, 0, length);
    }

    private void writeShortStringAtFilePointer(String s) throws IOException {
        byte[] bytes = s.getBytes("UTF-8");
        writeShortBytesAtFilePointer(bytes, bytes.length);
    }

    private void writeLongStringAtFilePointer(String s) throws IOException {
        byte[] bytes = s.getBytes("UTF-8");
        writeLongBytesAtFilePointer(bytes, bytes.length);
    }

    private byte[] intToByteArray(int i) {
        byte[] dword = new byte[4];
        dword[0] = ((byte) (i >> 24 & 0xFF));
        dword[1] = ((byte) (i >> 16 & 0xFF));
        dword[2] = ((byte) (i >> 8 & 0xFF));
        dword[3] = ((byte) (i & 0xFF));
        return dword;
    }

    private int binarySearchPosition(String entry) {
        int left = 0;
        int right = this.model.getSize() - 1;
        int mid;
        while (left <= right) {
            mid = (left + right) >> 1;
            if (this.comparator.compare(entry, this.model.elementAt(mid)) <= 0) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    private int binarySearchExisting(String entry) {
        int left = 0;
        int right = this.model.getSize() - 1;
        int mid;
        int equal;

        while (left <= right) {
            mid = (left + right) >> 1;
            equal = this.comparator.compare(entry, this.model.elementAt(mid));
            if (equal == 0) {
                return mid;
            } else {
                if (equal < 0) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
        }
        return -1;
    }

    @Override
    public ListModel getModel() {
        return this.model;
    }

    @Override
    public int insert(String entry, String definition) {
        int index = binarySearchExisting(entry);
        if (index != -1) {
            return -2;
        }
        index = binarySearchPosition(entry);
        try {
            byte[] byteEntryList = readBytesAtFileOffset(this.listFileOffset, (int) (this.raf.length() - this.listFileOffset));
            this.raf.seek(this.listFileOffset - this.otherInformation.length - 2);

            writeShortStringAtFilePointer(entry);

            writeLongStringAtFilePointer(definition);

            writeShortBytesAtFilePointer(this.otherInformation, this.otherInformation.length);

            int filePointer = (int) this.raf.getFilePointer();
            this.raf.write(byteEntryList, 0, index * 4);
            this.raf.writeInt((int) this.listFileOffset - this.otherInformation.length - 2);
            this.raf.write(byteEntryList, index * 4, byteEntryList.length - index * 4);

            this.raf.seek(7L);

            this.raf.writeInt(filePointer - this.otherInformation.length - 2);
            this.listFileOffset = filePointer;
            this.model.insertElementAt(entry, index);
        } catch (Exception ex) {
            index = -1;
        }
        return index;
    }

    @Override
    public int edit(String entry, String newDefinition) {
        int index = binarySearchExisting(entry);
        if (index == -1) {
            return -1;
        }
        return edit(index, newDefinition);
    }

    @Override
    public int edit(int index, String newDefinition) {
        if (index < 0 || index >= this.model.getSize()) {
            return -1;
        }
        try {
            changeFilePointerByListIndex(index);

            String entry = readShortStringAtFilePointer();

            this.raf.seek(this.raf.getFilePointer() - entry.getBytes().length - 2L);
            int length = this.raf.readShort();
            this.raf.write(new byte[length], 0, length);

            length = this.raf.readInt();
            this.raf.write(new byte[length], 0, length);

            byte[] byteEntryList = readBytesAtFileOffset(listFileOffset, (int) (this.raf.length() - this.listFileOffset));
            this.raf.seek(this.listFileOffset - this.otherInformation.length - 2);

            writeShortStringAtFilePointer(entry);
            writeLongStringAtFilePointer(newDefinition);

            byte[] b = intToByteArray((int) (this.listFileOffset - this.otherInformation.length) - 2);
            for (int i = 0; i < 4; ++i) {
                byteEntryList[i + index * 4] = b[i];
            }

            writeShortBytesAtFilePointer(this.otherInformation, this.otherInformation.length);

            long filePointer = this.raf.getFilePointer();
            this.raf.write(byteEntryList, 0, byteEntryList.length);

            this.raf.seek(7L);
            this.raf.writeInt((int) (filePointer - this.otherInformation.length - 2));
            ++this.dataRemain;
            this.raf.writeInt(this.dataRemain);
            this.listFileOffset = filePointer;
        } catch (Exception ex) {
            index = -1;
        }
        return index;
    }

    @Override
    public int remove(String entry) {
        int index = binarySearchExisting(entry);
        if (index == -1) {
            return -1;
        }
        return remove(index);
    }

    @Override
    public int remove(int index) {
        if (index < 0 || index >= this.model.getSize()) {
            return -1;
        }
        try {
            changeFilePointerByListIndex(index);
            int length = this.raf.readShort();
            this.raf.write(new byte[length], 0, length);

            length = this.raf.readInt();
            this.raf.write(new byte[length], 0, length);

            byte[] byteEntryList = readBytesAtFileOffset(this.listFileOffset + index * 4 + 4, (this.model.getSize() - index - 1) * 4);

            this.raf.seek(this.listFileOffset + index * 4);
            this.raf.write(byteEntryList, 0, byteEntryList.length);
            this.raf.setLength(this.raf.getFilePointer());
            this.raf.seek(11L);
            this.model.removeElementAt(index);
            ++this.dataRemain;
            this.raf.writeInt(this.dataRemain);
        } catch (Exception ex) {
            index = -1;
        }
        return index;
    }

    @Override
    public int find(String entry) {
        return binarySearchPosition(entry);
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getEntry(int index) {
        if (index < 0 || index >= this.model.getSize()) {
            return null;
        }
        try {
            changeFilePointerByListIndex(index);
            return readShortStringAtFilePointer();
        } catch (Exception ex) {

        }
        return null;
    }

    @Override
    public String getDefinition(int index) {
        if (index < 0 || index >= this.model.getSize()) {
            return null;
        }
        String entry = getEntry(index);
        if (entry == null) {
            return null;
        }
        try {
            return readLongStringAtFilePointer();
        } catch (Exception ex) {

        }
        return null;
    }
}
