/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vnu.geoterms.core.storage.jspd;

import vnu.geoterms.core.storage.Comparator;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import vnu.geoterms.core.storage.Dictionary;

/**
 *
 * @author Khanh
 */
public class DictionarySPDict extends Dictionary {

    private long listFileOffset;
    private Comparator comparator;

    private RandomAccessFile raf;
    private int dataRemain;
    private byte[] otherInformation;

    private int entryQuantity;

    private String name;

    private boolean selected;

    private byte[] buffer;
    private int bufferLength;

    public DictionarySPDict(String fileName) {
        super(fileName);
        try {
            File f = new File(this.getFileDirectory());
            if (!f.exists()) {
                return;
            }
            this.buffer = new byte[20];
            this.bufferLength = 20;

            this.raf = new RandomAccessFile(f, "rw");
            readBytesAtFileOffset(this.raf.getFilePointer(), 7);
            String title = new String(buffer, 0, 7, "UTF-8");
            if (!title.equals("2SPDict")) {
                title = null;
                return;
            }

            this.listFileOffset = this.raf.readInt();
            this.dataRemain = this.raf.readInt();
            this.raf.seek(this.listFileOffset);
            //Read other information of jspd file
            readShortBytesAtFilePointer();
            this.otherInformation = buffer.clone();

            String[] information = (new String(this.otherInformation, "UTF-8")).split("\000");
            this.name = information[0];
            //System.err.println(information[1]);
            this.listFileOffset = this.raf.getFilePointer();
            this.comparator = new Comparator(information[1]);
            this.entryQuantity = (int) (this.raf.length() - this.listFileOffset >> 2);

            information = null;
            title = null;
        } catch (Exception ex) {

        }
    }

    private void changeFilePointerByListIndex(int index) throws IOException {
        this.raf.seek(this.listFileOffset + (index << 2));
        this.raf.seek(this.raf.readInt());
    }

    private void changeBufferLength() {
        if (this.bufferLength > this.buffer.length) {
            this.buffer = new byte[this.bufferLength];
        }
    }

    private void readShortBytesAtFilePointer() throws IOException {
        // Get length of buffer
        this.bufferLength = this.raf.readShort();
        changeBufferLength();
        this.raf.read(buffer, 0, this.bufferLength);
    }

    private void readLongBytesAtFilePointer() throws IOException {
        // Get length of buffer
        this.bufferLength = this.raf.readInt();
        changeBufferLength();
        this.raf.read(buffer, 0, this.bufferLength);
    }

//    private byte[] readShortBytesAtFileOffset(long offset) throws IOException {
//        this.raf.seek(offset);
//        int length = this.raf.readShort();
//        byte[] buffer = new byte[length];
//        this.raf.read(buffer, 0, length);
//        return buffer;
//    }
//
//    private byte[] readLongBytesAtFileOffset(long offset) throws IOException {
//        this.raf.seek(offset);
//        int length = this.raf.readInt();
//        byte[] buffer = new byte[length];
//        this.raf.read(buffer, 0, length);
//        return buffer;
//    }
    private void readBytesAtFileOffset(long offset, int length) throws IOException {
        this.raf.seek(offset);
        this.bufferLength = length;
        changeBufferLength();
        this.raf.read(buffer, 0, length);
    }

    private String readShortStringAtFilePointer() throws IOException {
        readShortBytesAtFilePointer();
        return new String(this.buffer, 0, this.bufferLength, "UTF-8");
    }

    private String readLongStringAtFilePointer() throws IOException {
        readLongBytesAtFilePointer();
        return new String(this.buffer, 0, this.bufferLength, "UTF-8");
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
        int right = this.entryQuantity - 1;
        int mid;
        while (left <= right) {
            mid = (left + right) >> 1;
            if (this.comparator.compare(entry, getEntry(mid).toLowerCase()) <= 0) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    private int binarySearchExisting(String entry) {
        int left = 0;
        int right = this.entryQuantity;
        int mid;
        int equal;

        while (left <= right) {
            mid = (left + right) >> 1;
            try {
                equal = this.comparator.compare(entry, getEntry(mid).toLowerCase());
            } catch (Exception ex) {
                return -1;
            }
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
    public int insert(String entry, String definition) {
        int index = binarySearchExisting(entry);
        if (index != -1) {
            return -2;
        }
        index = binarySearchPosition(entry);
        try {
            readBytesAtFileOffset(this.listFileOffset, (int) (this.raf.length() - this.listFileOffset));
            byte[] byteEntryList = this.buffer;
            this.raf.seek(this.listFileOffset - this.otherInformation.length - 2);

            writeShortStringAtFilePointer(entry);

            writeLongStringAtFilePointer(definition);

            writeShortBytesAtFilePointer(this.otherInformation, this.otherInformation.length);

            int filePointer = (int) this.raf.getFilePointer();
            this.raf.write(byteEntryList, 0, index * 4);
            this.raf.writeInt((int) this.listFileOffset - this.otherInformation.length - 2);
            this.raf.write(byteEntryList, index * 4, this.bufferLength - index * 4);

            this.raf.seek(7L);

            this.raf.writeInt(filePointer - this.otherInformation.length - 2);
            this.listFileOffset = filePointer;
            ++this.entryQuantity;
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
        if (index < 0 || index >= this.entryQuantity) {
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

            readBytesAtFileOffset(listFileOffset, (int) (this.raf.length() - this.listFileOffset));
            byte[] byteEntryList = this.buffer;
            this.raf.seek(this.listFileOffset - this.otherInformation.length - 2);

            writeShortStringAtFilePointer(entry);
            writeLongStringAtFilePointer(newDefinition);

            byte[] b = intToByteArray((int) (this.listFileOffset - this.otherInformation.length) - 2);
            for (int i = 0; i < 4; ++i) {
                byteEntryList[i + index * 4] = b[i];
            }

            writeShortBytesAtFilePointer(this.otherInformation, this.otherInformation.length);

            long filePointer = this.raf.getFilePointer();
            this.raf.write(byteEntryList, 0, this.bufferLength);

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
        if (index < 0 || index >= this.entryQuantity) {
            return -1;
        }
        try {
            changeFilePointerByListIndex(index);
            int length = this.raf.readShort();
            this.raf.write(new byte[length], 0, length);

            length = this.raf.readInt();
            this.raf.write(new byte[length], 0, length);

            readBytesAtFileOffset(this.listFileOffset + index * 4 + 4, (this.entryQuantity - index - 1) * 4);
            byte[] byteEntryList = this.buffer;

            this.raf.seek(this.listFileOffset + index * 4);
            this.raf.write(byteEntryList, 0, this.bufferLength);
            this.raf.setLength(this.raf.getFilePointer());
            this.raf.seek(11L);
            --this.entryQuantity;
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
        try {
            this.raf.close();
        } catch (IOException ex) {
            Logger.getLogger(DictionarySPDict.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getEntry(int index) {
        if (index < 0 || index >= this.entryQuantity) {
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
        if (index < 0 || index >= this.entryQuantity) {
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

    @Override
    public String getHTMLDivDefinition(int index) {
        String definition = getDefinition(index);
        if (definition == null) {
            return "<div></div>";
        }
        StringBuilder value = new StringBuilder(definition.length() * 2);

        value.append("<div>");
        char kytu = definition.charAt(0);
        for (int i = 0; i < definition.length(); i++) {
            if ((i == 0) || (definition.charAt(i - 1) == '\n')) {
                if (i != 0) {
                    switch (kytu) {
                        case '@':
                            value.append("</font></b><br></div>");
                            break;
                        case '*':
                            value.append("</font></div></b>");
                            break;
                        case '-':
                            value.append("</font></div>");
                            break;
                        case '=':
                            value.append("</font></div>");
                            break;
                        case '+':
                            value.append("</font></div>");
                            break;
                        case '!':
                            value.append("</b></font></div>");
                            break;
                        case '~':
                            value.append("\"></center>");
                            break;
                        default:
                            value.append("</font><br>");
                    }
                }
                switch (definition.charAt(i)) {
                    case '@':
                        value.append("<div style=\"margin-left: 0px;\"><font color=red><b>");
                        break;
                    case '*':
                        value.append("<div style=\"margin-left: 20px;\"><font color=blue><b>");
                        break;
                    case '-':
                        value.append("<div style=\"margin-left: 40px;\"><font>");
                        break;
                    case '=':
                        value.append("<div style=\"margin-left: 60px;\"><font color=green>");
                        break;
                    case '+':
                        value.append("<div style=\"margin-left: 60px;\"><font color=gray>");
                        break;
                    case '!':
                        value.append("<div style=\"margin-left: 20px;\"><font color=brown><b>");
                        break;
                    case '~':
                        value.append("<center><img src = \"");
                        File f = new File(getDirectory() + File.separator + "images" + File.separator);
                        value.append(f.toURI().toString());
                        break;
                    default:
                        value.append(definition.charAt(i));
                }
                kytu = definition.charAt(i);
            } else {
                value.append(definition.charAt(i));
            }
        }
        switch (kytu) {
            case '@':
                value.append("</font></b><br>");
                break;
            case '*':
                value.append("</div></font></b>");
                break;
            case '-':
                value.append("</font></div>");
                break;
            case '=':
                value.append("</div></font>");
                break;
            case '+':
                value.append("</div></font>");
                break;
            case '!':
                value.append("</b></div></font>");
                break;
            case '~':
                value.append("\"></center>");
                break;
            default:
                value.append("<br>");
        }
        value.append("</div>");

        return value.toString();
    }

    @Override
    public String getHTMLDefinition(int index) {
        return "<html><body>" + getHTMLDivDefinition(index) + "</body></html>";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int indexOf(String entry) {
        return binarySearchExisting(entry);
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int getSize() {
        return this.entryQuantity;
    }

//    @Override
//    public String[] getEntries() {
//        String entries[] = new String[this.entryQuantity];
//        for (int i = 0; i < this.entryQuantity; ++i) {
//            try {
//                this.raf.seek(this.listFileOffset + (i << 2));
//                this.raf.seek(this.raf.readInt());
//                this.bufferLength = this.raf.readShort();
//                if (this.bufferLength > this.buffer.length) {
//                    this.buffer = new byte[this.bufferLength];
//                }
//                this.raf.read(buffer, 0, this.bufferLength);
//                entries[i] = new String(this.buffer, 0, this.bufferLength, "UTF-8");
//            } catch (IOException ex) {
//                Logger.getLogger(DictionarySPDict.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//        }
//        return entries;
//    }
}
