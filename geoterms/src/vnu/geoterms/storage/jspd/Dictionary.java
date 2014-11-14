/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vnu.geoterms.storage.jspd;

import java.io.*;
import static java.lang.Integer.min;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import vnu.geoterms.core.Interface.*;

/**
 *
 * @author Khanh
 */
public class Dictionary implements IDictionary {

    private int startListFilePointer;
    private DefaultListModel model;

    public DefaultListModel getModel() {
        return model;
    }

    public thongtin Info = new thongtin();
    byte[] bs = new byte['È'];
    static StringBuilder value = new StringBuilder(10000);

    int viTriTu;
    int tg;
    int tgi;
    int doDaiString;
    int mid;
    int left;
    int right;
    LangComparator langComparator;
    public String tu;
    public String nghia;
    public String nghiaTraVe;
    public int thutu;
    RandomAccessFile raf;

    public Dictionary(String s) {
        try {
            File f = new File(s);
            this.Info.tenFile = f.getName().substring(0, f.getName().length() - 5);
            if (!f.exists()) {
                this.Info = null;
                return;
            }
            this.raf = new RandomAccessFile(s, "rw");
            this.raf.read(this.bs, 0, 7);
            this.tu = new String(this.bs, 0, 7, "UTF-8");
            if (!this.tu.equals("2SPDict")) {
                this.Info = null;
                return;
            }
            this.startListFilePointer = this.raf.readInt();
            this.Info.duLieuThua = this.raf.readInt();
            this.raf.seek(this.startListFilePointer);
            this.doDaiString = this.raf.readShort();
            this.Info.info = new byte[this.doDaiString];
            this.raf.read(this.Info.info, 0, this.doDaiString);
            this.startListFilePointer = ((int) this.raf.getFilePointer());
            this.tu = new String(this.Info.info, "UTF-8");

            String[] dln = this.tu.split("");
            this.Info.tenTuDien = dln[0];
            this.Info.maSapXep = dln[1];
            this.Info.phatAm = dln[2];
            this.Info.font1 = dln[3];
            this.Info.size1 = dln[4];
            this.Info.font2 = dln[5];
            this.Info.size2 = dln[6];
            this.Info.tacGia = dln[7];
            this.Info.thongTinThem = dln[8];
            this.Info.tongSoTu = (this.Info.tongSoTu = (int) (this.raf.length() - this.startListFilePointer) / 4);
            this.langComparator = new LangComparator(this.Info.maSapXep);

            this.model = new DefaultListModel();
            for (int i = 0; i < this.Info.tongSoTu; ++i) {
                this.model.addElement(getEntry(i));
            }
        } catch (IOException ex) {
            this.Info = null;
        }
    }

    public String ChuThuong(String s) {
        return s.toLowerCase(this.langComparator.locale);
    }

    public void TatKetNoi() {
        try {
            this.raf.close();
            this.raf = null;
        } catch (IOException ex) {
            Logger.getLogger(Dictionary.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String DocTu(int i) {
        try {
            if (i >= this.Info.tongSoTu) {
                return "";
            }
            this.raf.seek(this.startListFilePointer + i * 4);
            this.viTriTu = this.raf.readInt();
            this.raf.seek(this.viTriTu);
            this.doDaiString = this.raf.readShort();
            if (this.doDaiString > this.bs.length) {
                this.bs = new byte[this.doDaiString];
            }
            this.raf.read(this.bs, 0, this.doDaiString);
            return new String(this.bs, 0, this.doDaiString, "UTF-8");
        } catch (Exception ex) {
        }
        return "";
    }

    public String DocNghia(int i) {
        try {
            if (i >= this.Info.tongSoTu) {
                return "";
            }
            this.raf.seek(this.startListFilePointer + i * 4);
            this.viTriTu = this.raf.readInt();
            this.raf.seek(this.viTriTu);
            this.doDaiString = this.raf.readShort();
            this.raf.skipBytes(this.doDaiString);
            this.doDaiString = this.raf.readInt();
            if (this.doDaiString > this.bs.length) {
                this.bs = new byte[this.doDaiString];
            }
            this.raf.read(this.bs, 0, this.doDaiString);
            return new String(this.bs, 0, this.doDaiString, "UTF-8");
        } catch (Exception ex) {
        }
        return "";
    }

    public void DocTuNghia(int i) {
        try {
            if (i >= this.Info.tongSoTu) {
                this.tu = "";
                this.nghia = "";
            }
            this.raf.seek(this.startListFilePointer + i * 4);
            this.viTriTu = this.raf.readInt();
            this.raf.seek(this.viTriTu);
            this.doDaiString = this.raf.readShort();
            if (this.doDaiString > this.bs.length) {
                this.bs = new byte[this.doDaiString];
            }
            this.raf.read(this.bs, 0, this.doDaiString);
            this.tu = new String(this.bs, 0, this.doDaiString, "UTF-8");

            this.doDaiString = this.raf.readInt();
            if (this.doDaiString > this.bs.length) {
                this.bs = new byte[this.doDaiString];
            }
            this.raf.read(this.bs, 0, this.doDaiString);
            this.nghia = new String(this.bs, 0, this.doDaiString, "UTF-8");
        } catch (Exception ex) {
            this.tu = "";
            this.nghia = "";
        }
    }

    public String[] LoadChuoiTu(int batdau, int count) {
        try {
            if (batdau >= this.Info.tongSoTu) {
                return null;
            }
            int[] itam = null;
            String[] list = null;
            if (batdau + count > this.Info.tongSoTu) {
                itam = new int[this.Info.tongSoTu - batdau];
                list = new String[this.Info.tongSoTu - batdau];
            } else {
                itam = new int[count];
                list = new String[count];
            }
            this.raf.seek(this.startListFilePointer + batdau * 4);
            for (int i = 0; i < itam.length; i++) {
                itam[i] = this.raf.readInt();
            }
            for (int i = 0; i < itam.length; i++) {
                this.raf.seek(itam[i]);
                this.doDaiString = this.raf.readShort();
                if (this.doDaiString > this.bs.length) {
                    this.bs = new byte[this.doDaiString];
                }
                this.raf.read(this.bs, 0, this.doDaiString);
                list[i] = new String(this.bs, 0, this.doDaiString, "UTF-8");
            }
            return list;
        } catch (Exception ex) {
        }
        return null;
    }

    public String[] TraTu(String s, int doDaiList) {
        String[] list = new String[doDaiList];
        this.left = 0;
        this.right = (this.Info.tongSoTu - 1);
        if (this.Info.tongSoTu == 0) {
            return null;
        }
        while (this.left <= this.right) {
            this.mid = (this.left + this.right >> 1);
            if ((this.doDaiString = this.langComparator.compareThuong(s, DocTu(this.mid))) <= 0) {
                this.right = (this.mid - 1);
            } else {
                this.left = (this.mid + 1);
            }
        }
        if (this.left == this.Info.tongSoTu) {
            this.left -= 1;
        }
        DocTuNghia(this.left);
        list[0] = this.tu;
        this.tgi = this.left;
        if (this.langComparator.compareThuong(list[0], s) == 0) {
            this.nghiaTraVe = this.nghia;
            this.tgi += 1;
            DocTuNghia(this.tgi);
            while ((this.langComparator.compareThuong(this.tu, s) == 0) && (this.tgi < this.Info.tongSoTu - 1)) {
                this.nghiaTraVe = (this.nghiaTraVe + "\n<hr>\n" + this.nghia);
                this.tgi += 1;
                DocTuNghia(this.tgi);
            }
            this.thutu = this.left;
        } else {
            this.thutu = (this.left ^ 0xFFFFFFFF);
            this.nghiaTraVe = this.nghia;
        }
        for (this.tgi = 1; this.tgi < list.length; this.tgi += 1) {
            if (this.tgi + this.left < this.Info.tongSoTu) {
                list[this.tgi] = DocTu(this.tgi + this.left);
            } else {
                list[this.tgi] = "";
            }
        }
        return list;
    }

    public int TraTuHoa(String s) {
        this.left = 0;
        this.right = (this.Info.tongSoTu - 1);
        if (this.Info.tongSoTu == 0) {
            return 0;
        }
        while (this.left <= this.right) {
            this.mid = (this.left + this.right >> 1);
            if ((this.doDaiString = this.langComparator.compare(s, DocTu(this.mid))) <= 0) {
                this.right = (this.mid - 1);
            } else {
                this.left = (this.mid + 1);
            }
        }
        if (this.langComparator.compare(s, DocTu(this.left)) != 0) {
            return this.left;
        }
        return this.left ^ 0xFFFFFFFF;
    }

    public String[] TraTu2(String s, int doDaiList) {
        String[] list = new String[doDaiList];
        this.left = 0;
        this.right = (this.Info.tongSoTu - 1);
        if (this.Info.tongSoTu == 0) {
            return null;
        }
        while (this.left <= this.right) {
            this.mid = (this.left + this.right >> 1);
            if ((this.doDaiString = this.langComparator.compareThuong(s, DocTu(this.mid))) <= 0) {
                this.right = (this.mid - 1);
            } else {
                this.left = (this.mid + 1);
            }
        }
        if (this.left == this.Info.tongSoTu) {
            this.left -= 1;
        }
        DocTuNghia(this.left);
        list[0] = this.tu;
        this.tgi = this.left;
        if (this.langComparator.compareThuong(list[0], s) == 0) {
            this.nghiaTraVe = this.nghia;
            this.tgi += 1;
            DocTuNghia(this.tgi);
            while ((this.langComparator.compareThuong(this.tu, s) == 0) && (this.tgi < this.Info.tongSoTu - 1)) {
                this.nghiaTraVe = (this.nghiaTraVe + "\n<hr>\n" + this.nghia);
                this.tgi += 1;
                DocTuNghia(this.tgi);
            }
            this.thutu = this.left;
        } else {
            this.thutu = (this.left ^ 0xFFFFFFFF);
            this.nghiaTraVe = this.nghia;
        }
        for (this.tgi = 0; this.tgi < list.length; this.tgi += 1) {
            if (this.tgi - doDaiList / 2 + this.left < this.Info.tongSoTu) {
                list[this.tgi] = DocTu(this.tgi - doDaiList / 2 + this.left);
            } else {
                list[this.tgi] = "";
            }
        }
        return list;
    }

    public String[] TraTu3(String s, int doDaiList) {
        String[] list = new String[doDaiList];
        this.left = 0;
        this.right = (this.Info.tongSoTu - 1);
        if (this.Info.tongSoTu == 0) {
            return null;
        }
        while (this.left <= this.right) {
            this.mid = (this.left + this.right >> 1);
            if ((this.doDaiString = this.langComparator.compareThuong(s, DocTu(this.mid))) <= 0) {
                this.right = (this.mid - 1);
            } else {
                this.left = (this.mid + 1);
            }
        }
        if (this.left == this.Info.tongSoTu) {
            this.left -= 1;
        }
        DocTuNghia(this.left);
        list[0] = this.tu;
        this.tgi = this.left;
        if (this.langComparator.compareThuong(list[0], s) == 0) {
            this.nghiaTraVe = this.nghia;
            this.tgi += 1;
            DocTuNghia(this.tgi);
            while ((this.langComparator.compareThuong(this.tu, s) == 0) && (this.tgi < this.Info.tongSoTu - 1)) {
                this.nghiaTraVe = (this.nghiaTraVe + "\n<hr>\n" + this.nghia);
                this.tgi += 1;
                DocTuNghia(this.tgi);
            }
            this.thutu = this.left;
        } else {
            this.thutu = (this.left ^ 0xFFFFFFFF);
            this.nghiaTraVe = this.nghia;
        }
        for (this.tgi = 0; this.tgi < list.length; this.tgi += 1) {
            if (this.tgi + this.left < this.Info.tongSoTu) {
                list[this.tgi] = DocTu(this.tgi + this.left);
            } else {
                list[this.tgi] = "";
            }
        }
        return list;
    }

    String convertHtml(String s) {
        if ((s == null) || (s.length() == 0)) {
            return "<html> <body> </body> </html> ";
        }
        value = new StringBuilder(s.length() * 2);
        value.append("<font face=\"" + this.Info.font2 + "\" STYLE=\"font-size: " + this.Info.size2 + "pt\">");
        char kytu = s.charAt(0);
        for (int i = 0; i < s.length(); i++) {
            if ((i == 0) || (s.charAt(i - 1) == '\n')) {
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
                switch (s.charAt(i)) {
                    case '@':
                        value.append("<div style=\"margin-left: 0px;\"><font color = red><b>@");
                        break;
                    case '*':
                        value.append("<div style=\"margin-left: 20px;\"><font face=\"" + this.Info.font2 + "\" STYLE=\"font-size: " + this.Info.size2 + "pt\" color =blue><b>*");
                        break;
                    case '-':
                        value.append("<div style=\"margin-left: 40px;\"><font face=\"" + this.Info.font2 + "\" STYLE=\"font-size: " + this.Info.size2 + "pt\">-");
                        break;
                    case '=':
                        value.append("<div style=\"margin-left: 60px;\"><font face=\"" + this.Info.font2 + "\" STYLE=\"font-size: " + this.Info.size2 + "pt\" color = green>=");
                        break;
                    case '+':
                        value.append("<div style=\"margin-left: 60px;\"><font face=\"" + this.Info.font2 + "\" STYLE=\"font-size: " + this.Info.size2 + "pt\" color =gray>+");
                        break;
                    case '!':
                        value.append("<div style=\"margin-left: 20px;\"><font face=\"" + this.Info.font2 + "\" STYLE=\"font-size: " + this.Info.size2 + "pt\" color = brown><b>!");
                        break;
                    case '~':
                        value.append("<center><img src = \"");
                        //File f = new File(frmMain.duongDanChinh + File.separator + "Images" + File.separator);
                        //value.append(f.toURI().toString());
                        break;
                    default:
                        value.append("<font face=\"" + this.Info.font2 + "\" STYLE=\"font-size: " + this.Info.size2 + "pt\">" + s.charAt(i));
                }
                kytu = s.charAt(i);
            } else {
                value.append(s.charAt(i));
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
        value.append("</font>");
        return value.toString();
    }

    public String DoiHtmlCho1Tu(String s) {
        return "<font face=\"" + this.Info.font1 + "\" STYLE=\"font-size: " + this.Info.size1 + "pt\">" + s + "</font>";
    }

    public int ThemTu(String tu, String nghia) {
        try {
            this.mid = TraTuHoa(tu);
            if (this.mid < 0) {
                return this.mid;
            }
            byte[] bList = new byte[(int) this.raf.length() - this.startListFilePointer];
            this.raf.seek(this.startListFilePointer);
            this.raf.read(bList, 0, bList.length);
            this.raf.seek(this.startListFilePointer - this.Info.info.length - 2);

            this.bs = tu.getBytes("UTF-8");
            this.raf.writeShort(this.bs.length);
            this.raf.write(this.bs, 0, this.bs.length);

            this.bs = nghia.getBytes("UTF-8");
            this.raf.writeInt(this.bs.length);
            this.raf.write(this.bs, 0, this.bs.length);
            this.raf.writeShort(this.Info.info.length);
            this.raf.write(this.Info.info);
            this.tg = ((int) this.raf.getFilePointer());
            this.raf.write(bList, 0, this.mid * 4);
            this.raf.writeInt(this.startListFilePointer - this.Info.info.length - 2);
            this.raf.write(bList, this.mid * 4, bList.length - this.mid * 4);
            this.raf.seek(7L);
            this.raf.writeInt(this.tg - this.Info.info.length - 2);
            this.startListFilePointer = this.tg;
            this.Info.tongSoTu += 1;
        } catch (Exception ex) {
            //JOptionPane.showMessageDialog(null, ex.getMessage(), "Thông báo", 0);
        }
        return this.mid;
    }

    public int SuaTu(int viTriSua, String nghiaSua) {
        try {
            if ((viTriSua >= this.Info.tongSoTu) || (viTriSua < 0)) {
                return -1;
            }
            this.raf.seek(this.startListFilePointer + viTriSua * 4);
            this.viTriTu = this.raf.readInt();
            this.raf.seek(this.viTriTu);
            this.doDaiString = this.raf.readShort();
            if (this.doDaiString > this.bs.length) {
                this.bs = new byte[this.doDaiString];
            }
            this.raf.read(this.bs, 0, this.doDaiString);
            this.tu = new String(this.bs, 0, this.doDaiString, "UTF-8");
            this.raf.seek(this.raf.getFilePointer() - this.doDaiString - 2L);
            this.doDaiString = this.raf.readShort();
            this.bs = new byte[this.doDaiString];
            this.raf.write(this.bs, 0, this.doDaiString);
            this.doDaiString = this.raf.readInt();
            this.bs = new byte[this.doDaiString];
            this.raf.write(this.bs, 0, this.doDaiString);

            byte[] bList = new byte[(int) this.raf.length() - this.startListFilePointer];
            this.raf.seek(this.startListFilePointer);
            this.raf.read(bList, 0, bList.length);
            this.raf.seek(this.startListFilePointer - this.Info.info.length - 2);

            this.bs = this.tu.getBytes("UTF-8");
            this.raf.writeShort(this.bs.length);
            this.raf.write(this.bs, 0, this.bs.length);

            this.bs = nghiaSua.getBytes("UTF-8");
            this.raf.writeInt(this.bs.length);
            this.raf.write(this.bs, 0, this.bs.length);
            byte[] b = ByteAdvance.intToByteArray(this.startListFilePointer - this.Info.info.length - 2);
            for (this.tgi = 0; this.tgi < 4; this.tgi += 1) {
                bList[(this.tgi + viTriSua * 4)] = b[this.tgi];
            }
            this.raf.writeShort(this.Info.info.length);
            this.raf.write(this.Info.info);

            this.tg = ((int) this.raf.getFilePointer());
            this.raf.write(bList, 0, bList.length);
            this.raf.seek(7L);
            this.raf.writeInt(this.tg - this.Info.info.length - 2);
            this.Info.duLieuThua += 1;
            this.raf.writeInt(this.Info.duLieuThua);
            this.startListFilePointer = this.tg;
        } catch (Exception ex) {
            return -1;
        }
        return viTriSua;
    }

    public int XoaTu(int viTriXoa) {
        try {
            if ((viTriXoa >= this.Info.tongSoTu) || (viTriXoa < 0)) {
                return -1;
            }
            this.raf.seek(this.startListFilePointer + viTriXoa * 4);
            this.viTriTu = this.raf.readInt();
            this.raf.seek(this.viTriTu);
            this.doDaiString = this.raf.readShort();
            this.bs = new byte[this.doDaiString];
            this.raf.write(this.bs, 0, this.doDaiString);
            this.doDaiString = this.raf.readInt();
            this.bs = new byte[this.doDaiString];
            this.raf.write(this.bs, 0, this.doDaiString);

            byte[] bList = new byte[(this.Info.tongSoTu - viTriXoa - 1) * 4];
            this.raf.seek(this.startListFilePointer + viTriXoa * 4 + 4);
            this.raf.read(bList, 0, bList.length);
            this.raf.seek(this.startListFilePointer + viTriXoa * 4);
            this.raf.write(bList, 0, bList.length);
            this.raf.setLength(this.raf.getFilePointer());
            this.raf.seek(11L);
            this.Info.duLieuThua += 1;
            this.Info.tongSoTu -= 1;
            this.raf.writeInt(this.Info.duLieuThua);
        } catch (Exception ex) {
            return -1;
        }
        return viTriXoa;
    }

    public int DoiTenTu(int viTriDoi, String tu) {
        if ((TraTuHoa(tu) < 0) || (viTriDoi >= this.Info.tongSoTu) || (viTriDoi < 0)) {
            return -1;
        }
        this.nghia = DocNghia(viTriDoi);
        XoaTu(viTriDoi);
        return ThemTu(tu, this.nghia);
    }

    public byte[] DuLieuSearch()
            throws IOException {
        this.raf.seek(15L);
        this.doDaiString = this.raf.readShort();
        this.raf.skipBytes(this.doDaiString);
        byte[] data = new byte[(int) (this.startListFilePointer - this.raf.getFilePointer())];
        this.raf.read(data, 0, data.length);
        return data;
    }

    public void VeDauDanhSach()
            throws IOException {
        this.raf.seek(15L);
    }

    public String NextTu()
            throws IOException {
        this.doDaiString = this.raf.readShort();
        if (this.doDaiString > this.bs.length) {
            this.bs = new byte[this.doDaiString];
        }
        this.raf.read(this.bs, 0, this.doDaiString);
        this.tu = new String(this.bs, 0, this.doDaiString, "UTF-8");
        this.doDaiString = this.raf.readInt();
        this.raf.skipBytes(this.doDaiString);
        return this.tu;
    }

    public void NextTuNghia()
            throws IOException {
        this.doDaiString = this.raf.readShort();
        if (this.doDaiString > this.bs.length) {
            this.bs = new byte[this.doDaiString];
        }
        this.raf.read(this.bs, 0, this.doDaiString);
        this.tu = new String(this.bs, 0, this.doDaiString, "UTF-8");
        this.doDaiString = this.raf.readInt();
        if (this.doDaiString > this.bs.length) {
            this.bs = new byte[this.doDaiString];
        }
        this.raf.read(this.bs, 0, this.doDaiString);
        this.nghia = new String(this.bs, 0, this.doDaiString, "UTF-8");
    }

    public void CapNhatInfo(String st)
            throws IOException {
        String[] dln = st.split("");
        this.Info.tenTuDien = dln[0];
        this.Info.maSapXep = dln[1];
        this.Info.phatAm = dln[2];
        this.Info.font1 = dln[3];
        this.Info.size1 = dln[4];
        this.Info.font2 = dln[5];
        this.Info.size2 = dln[6];
        this.Info.tacGia = dln[7];
        this.Info.thongTinThem = dln[8];
        byte[] bList = new byte[(int) this.raf.length() - this.startListFilePointer];
        this.raf.seek(this.startListFilePointer);
        this.raf.read(bList, 0, bList.length);
        this.raf.seek(this.startListFilePointer - this.Info.info.length - 2);
        this.Info.info = st.getBytes("UTF-8");
        this.raf.writeShort(this.Info.info.length);
        this.raf.write(this.Info.info);
        this.startListFilePointer = ((int) this.raf.getFilePointer());
        this.raf.write(bList);
        this.raf.setLength(this.raf.getFilePointer());
    }

    public void CapNhatReScan(String st, byte[] b) {
        String[] dln = st.split("");
        this.Info.tenTuDien = dln[0];
        this.Info.maSapXep = dln[1];
        this.Info.phatAm = dln[2];
        this.Info.font1 = dln[3];
        this.Info.size1 = dln[4];
        this.Info.font2 = dln[5];
        this.Info.size2 = dln[6];
        this.Info.tacGia = dln[7];
        this.Info.thongTinThem = dln[8];
        try {
            this.Info.info = st.getBytes("UTF-8");
            this.raf.seek(this.startListFilePointer - this.Info.info.length - 2);
            this.raf.writeShort(this.Info.info.length);
            this.raf.write(this.Info.info);
            this.startListFilePointer = ((int) this.raf.getFilePointer());
            this.raf.write(b);
            this.raf.setLength(this.raf.getFilePointer());
            this.langComparator = new LangComparator(this.Info.maSapXep);
        } catch (Exception ex) {
            ex = null;
        }
    }

    private void changeListFilePointer(int index) throws IOException {
        this.raf.seek(this.startListFilePointer + index * 4);
        this.raf.seek(this.raf.readInt());
    }

    private String getStringValue() {
        try {
            int length = this.raf.readShort();
            byte[] buffer = new byte[length];
            this.raf.read(buffer, 0, length);
            return new String(buffer, 0, length, "UTF-8");
        } catch (Exception ex) {

        }
        return null;
    }

    public String getEntry(int index) {
        try {
            if (index >= this.Info.tongSoTu) {
                return null;
            }
            changeListFilePointer(index);
            return getStringValue();
        } catch (Exception ex) {

        }
        return null;
    }

    public String getDefinition(int index) {
        try {
            if (index >= this.Info.tongSoTu) {
                return null;
            }
            changeListFilePointer(index);

            // Read entry
            String entry = getStringValue();
            entry = null;
            // Read entry's definition
            return getStringValue();
        } catch (Exception ex) {

        }
        return null;
    }

    private int binarySearch(String entry) {
        int left = 0;
        int right = (this.Info.tongSoTu - 1);
        int mid;

        if (this.Info.tongSoTu == 0) {
            return -1;
        }
        while (left <= right) {
            mid = (left + right) >> 1;
            if (this.langComparator.compareThuong(entry, getEntry(mid)) <= 0) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        if (left == this.Info.tongSoTu) {
            left -= 1;
        }
        return left;
    }

//OVERiDe
    @Override
    public int insert(String entry, String definition) {
        try {
            this.mid = TraTuHoa(tu);
            if (this.mid < 0) {
                return this.mid;
            }
            byte[] bList = new byte[(int) this.raf.length() - this.startListFilePointer];
            this.raf.seek(this.startListFilePointer);
            this.raf.read(bList, 0, bList.length);
            this.raf.seek(this.startListFilePointer - this.Info.info.length - 2);

            this.bs = tu.getBytes("UTF-8");
            this.raf.writeShort(this.bs.length);
            this.raf.write(this.bs, 0, this.bs.length);

            this.bs = nghia.getBytes("UTF-8");
            this.raf.writeInt(this.bs.length);
            this.raf.write(this.bs, 0, this.bs.length);
            this.raf.writeShort(this.Info.info.length);
            this.raf.write(this.Info.info);
            this.tg = ((int) this.raf.getFilePointer());
            this.raf.write(bList, 0, this.mid * 4);
            this.raf.writeInt(this.startListFilePointer - this.Info.info.length - 2);
            this.raf.write(bList, this.mid * 4, bList.length - this.mid * 4);
            this.raf.seek(7L);
            this.raf.writeInt(this.tg - this.Info.info.length - 2);
            this.startListFilePointer = this.tg;
            this.Info.tongSoTu += 1;
        } catch (Exception ex) {
            //JOptionPane.showMessageDialog(null, ex.getMessage(), "Thông báo", 0);
        }
        return this.mid;
    }

    @Override
    public void edit(String entry, String newEntry, String newDefinition) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void edit(int index, String newEntry, String newDefinition) {
//        try {
//            if ((index >= this.Info.tongSoTu) || (index < 0)) {
//                return;
//            }
//            this.raf.seek(this.startListFilePointer + index * 4);
//            this.viTriTu = this.raf.readInt();
//            this.raf.seek(this.viTriTu);
//            this.doDaiString = this.raf.readShort();
//            if (this.doDaiString > this.bs.length) {
//                this.bs = new byte[this.doDaiString];
//            }
//            this.raf.read(this.bs, 0, this.doDaiString);
//            this.tu = new String(this.bs, 0, this.doDaiString, "UTF-8");
//            this.raf.seek(this.raf.getFilePointer() - this.doDaiString - 2L);
//            this.doDaiString = this.raf.readShort();
//            this.bs = new byte[this.doDaiString];
//            this.raf.write(this.bs, 0, this.doDaiString);
//            this.doDaiString = this.raf.readInt();
//            this.bs = new byte[this.doDaiString];
//            this.raf.write(this.bs, 0, this.doDaiString);
//
//            byte[] bList = new byte[(int) this.raf.length() - this.startListFilePointer];
//            this.raf.seek(this.startListFilePointer);
//            this.raf.read(bList, 0, bList.length);
//            this.raf.seek(this.startListFilePointer - this.Info.info.length - 2);
//
//            this.bs = this.tu.getBytes("UTF-8");
//            this.raf.writeShort(this.bs.length);
//            this.raf.write(this.bs, 0, this.bs.length);
//
//            this.bs = newDefinition.getBytes("UTF-8");
//            this.raf.writeInt(this.bs.length);
//            this.raf.write(this.bs, 0, this.bs.length);
//            byte[] b = ByteAdvance.intToByteArray(this.startListFilePointer - this.Info.info.length - 2);
//            for (this.tgi = 0; this.tgi < 4; this.tgi += 1) {
//                bList[(this.tgi + index * 4)] = b[this.tgi];
//            }
//            this.raf.writeShort(this.Info.info.length);
//            this.raf.write(this.Info.info);
//
//            this.tg = ((int) this.raf.getFilePointer());
//            this.raf.write(bList, 0, bList.length);
//            this.raf.seek(7L);
//            this.raf.writeInt(this.tg - this.Info.info.length - 2);
//            this.Info.duLieuThua += 1;
//            this.raf.writeInt(this.Info.duLieuThua);
//            this.startListFilePointer = this.tg;
//        } catch (Exception ex) {
//            //return;
//        }
//        //return viTriSua;
    }

    @Override
    public void remove(String entry) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int find(String entry) {
        int index = binarySearch(entry);
        return index;
    }

    public String[] findInRange(String entry, int range) {
        int index = binarySearch(entry);

        if (index == -1) {
            return null;
        }

        int listSize = min(range, this.Info.tongSoTu - range);
        String[] list = new String[listSize];

        //this.model.clear();
        for (int i = 0; i < listSize; ++i) {
            list[i] = getEntry(index + i);
            //this.model.addElement(list[i]);
        }

        return list;
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
