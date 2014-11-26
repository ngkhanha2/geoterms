/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vnu.geoterms;

import javax.swing.UIManager;
import vnu.geoterms.core.management.Management;
import vnu.geoterms.core.storage.Dictionary;
import vnu.geoterms.core.storage.jspd.DictionarySPDict;
import vnu.geoterms.core.ui.JFrameMain;

/**
 *
 * @author Khanh
 */
public class Geoterms {

    public static String const_dictionary_anhviet_file = "AnhViet.jspd";
    public static String const_dictionary_tdbkqs_file = "Tdbkqs.jspd";
    public static String const_dictionary_vietanh_file = "VietAnh.jspd";

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JFrameMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        final Management management = new Management();
        management.addDictionary(new DictionarySPDict(const_dictionary_anhviet_file));
//        management.addDictionary(new DictionarySPDict(const_dictionary_tdbkqs_file));
        management.addDictionary(new DictionarySPDict(const_dictionary_vietanh_file));
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFrameMain(management).setVisible(true);
            }
        });

    }
}
