/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vnu.geoterms.core.ui;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.File;

import javax.swing.*;
import vnu.geoterms.core.Interface.*;
import vnu.geoterms.core.storage.Dictionary;

/**
 *
 * @author Khanh
 */
public class JFrameMain extends javax.swing.JFrame {

    private String HOME_PAGE_STRING;
    private IManagement management;
    private Dictionary currentDictionary;

    private void loadDictionary() {
        int i = jComboBoxDictionaries.getSelectedIndex() - 1;
        if (i < 0) {
            ((DefaultListModel) this.jListEntry.getModel()).clear();
            this.currentDictionary = null;
        } else {
            if (!(this.currentDictionary == this.management.getDictionaries().get(i))) {
                this.currentDictionary = (Dictionary) this.management.getDictionaries().get(i);
                jScrollPaneEntry.getVerticalScrollBar().setValue(0);
                this.jScrollPaneEntry.getVerticalScrollBar().setMaximum(this.currentDictionary.getSize() * this.jListEntry.getFixedCellHeight());
                DefaultListModel model = (DefaultListModel) this.jListEntry.getModel();
                model.clear();
                for (i = 0; i < this.currentDictionary.getSize(); ++i) {
                    model.addElement("");
                }
                reloadJListEntry();
            }
        }
        System.gc();
    }

    private void reloadJListEntry() {
        if (this.currentDictionary == null) {
            return;
        }
        int segment = this.jSplitPane1.getHeight() / this.jListEntry.getFixedCellHeight();
        int value = this.jScrollPaneEntry.getVerticalScrollBar().getValue() / this.jListEntry.getFixedCellHeight();
        int jListEntryMinIndex = Integer.max(value - segment, 0);
        DefaultListModel model = (DefaultListModel) this.jListEntry.getModel();

        int jListEntryMaxIndex = Integer.min(value + segment, model.size());

        for (int i = jListEntryMinIndex; i < jListEntryMaxIndex; ++i) {
            if (((String) model.get(i)).isEmpty()) {
                model.set(i, this.currentDictionary.getEntry(i));
            }
        }
        System.gc();
    }

    private void insertEntry() {
        if (this.currentDictionary == null) {
            return;
        }
        JDialogInsertEntry jDialog = new JDialogInsertEntry(this, rootPaneCheckingEnabled, currentDictionary);
        jDialog.setLocationRelativeTo(this);
        jDialog.setVisible(true);
        int index = jDialog.getIndex();
        jDialog = null;
        System.gc();
        if (index >= 0) {
            this.jScrollPaneEntry.getVerticalScrollBar().setMaximum(this.currentDictionary.getSize() * this.jListEntry.getFixedCellHeight());
            ((DefaultListModel) this.jListEntry.getModel()).insertElementAt(this.currentDictionary.getEntry(index), index);
        }
        System.gc();
    }

    private void editEntry() {
        if (this.currentDictionary == null) {
            return;
        }
        int index = this.jListEntry.getSelectedIndex();
        if (index == -1) {
            return;
        }
        JDialog jDialog = new JDialogEditEntry(this, true, this.currentDictionary, index);
        jDialog.setLocationRelativeTo(this);
        jDialog.setVisible(true);
        jDialog = null;
        System.gc();
    }

    private void removeEntry() {
        if (this.currentDictionary == null) {
            return;
        }
        int index = this.jListEntry.getSelectedIndex();
        if (index == -1) {
            return;
        }
        int result = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa từ khóa này?", "VNU - Geoterms", JOptionPane.YES_NO_OPTION);
        if (result == 1) {
            return;
        }
        if (this.currentDictionary.remove(index) > -1) {
            ((DefaultListModel) this.jListEntry.getModel()).remove(index);
        }
        System.gc();
    }

    /**
     * Creates new form JFrameMain
     *
     * @param management
     */
    public JFrameMain(IManagement management) {
        this.management = management;

        HOME_PAGE_STRING = "<html><head></head><body><h1>Welcome to VNU-Geoterms</h1><img src=\"file:\\" + System.getProperty("user.dir") + File.separator + "util" + File.separator + "vnu_hus_logo.png\"><br/>This is the 3nd demo version of VNU-Geotems.<h2>Author</h2>CVPR group, Faculty of Information Technology, Ho Chi Minh City University of Science.</body></html>";

        initComponents();

        this.jListEntry.setModel(new DefaultListModel());
        this.jComboBoxDictionaries.addItem("...");
        for (int i = 0; i < this.management.getDictionaries().size(); ++i) {
            this.jComboBoxDictionaries.addItem(this.management.getDictionaries().get(i).getName());
        }

        this.currentDictionary = null;

        this.jScrollPaneEntry.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {

            @Override
            public void adjustmentValueChanged(AdjustmentEvent ae) {
                //jListEntryVerticalScrollBarValue = jScrollPaneEntry.getVerticalScrollBar().getValue() / jListEntry.getFixedCellHeight();
                reloadJListEntry();
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPaneEntry = new javax.swing.JScrollPane();
        jListEntry = new javax.swing.JList();
        jTextFieldEntry = new javax.swing.JTextField();
        jComboBoxDictionaries = new javax.swing.JComboBox();
        jToolBar1 = new javax.swing.JToolBar();
        jButtonInsertEntry = new javax.swing.JButton();
        jButtonEdit = new javax.swing.JButton();
        jButtonRemove = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jToolBar3 = new javax.swing.JToolBar();
        jButtonCopyDefinition = new javax.swing.JButton();
        jButtonSaveDefinition = new javax.swing.JButton();
        jButtonPrintDefinition = new javax.swing.JButton();
        jButtonFindInDefinition = new javax.swing.JButton();
        jButtonHomePage = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPaneDefinition = new javax.swing.JTextPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuTools = new javax.swing.JMenu();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItemExit = new javax.swing.JMenuItem();
        jMenuHelp = new javax.swing.JMenu();
        jMenuItemHelpContents = new javax.swing.JMenuItem();
        jMenuItemAbout = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("VNU - Geoterms - 3nd Demo");

        jSplitPane1.setDividerLocation(200);

        jListEntry.setFixedCellHeight(20);
        jListEntry.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListEntryMouseClicked(evt);
            }
        });
        jScrollPaneEntry.setViewportView(jListEntry);

        jTextFieldEntry.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldEntryKeyReleased(evt);
            }
        });

        jComboBoxDictionaries.setToolTipText("Chọn từ điển");
        jComboBoxDictionaries.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxDictionariesActionPerformed(evt);
            }
        });

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jButtonInsertEntry.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Add16.gif"))); // NOI18N
        jButtonInsertEntry.setFocusable(false);
        jButtonInsertEntry.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonInsertEntry.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonInsertEntry.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonInsertEntryMouseClicked(evt);
            }
        });
        jToolBar1.add(jButtonInsertEntry);

        jButtonEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Edit16.gif"))); // NOI18N
        jButtonEdit.setFocusable(false);
        jButtonEdit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonEdit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonEdit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonEditMouseClicked(evt);
            }
        });
        jToolBar1.add(jButtonEdit);

        jButtonRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Delete16.gif"))); // NOI18N
        jButtonRemove.setFocusable(false);
        jButtonRemove.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonRemove.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonRemove.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonRemoveMouseClicked(evt);
            }
        });
        jToolBar1.add(jButtonRemove);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPaneEntry, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jTextFieldEntry, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jComboBoxDictionaries, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jComboBoxDictionaries, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldEntry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneEntry, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jSplitPane1.setLeftComponent(jPanel3);

        jToolBar3.setFloatable(false);
        jToolBar3.setRollover(true);

        jButtonCopyDefinition.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Copy16.gif"))); // NOI18N
        jButtonCopyDefinition.setToolTipText("Copy");
        jButtonCopyDefinition.setFocusable(false);
        jButtonCopyDefinition.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonCopyDefinition.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar3.add(jButtonCopyDefinition);

        jButtonSaveDefinition.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Save16.gif"))); // NOI18N
        jButtonSaveDefinition.setToolTipText("Save");
        jButtonSaveDefinition.setFocusable(false);
        jButtonSaveDefinition.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonSaveDefinition.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar3.add(jButtonSaveDefinition);

        jButtonPrintDefinition.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Print16.gif"))); // NOI18N
        jButtonPrintDefinition.setToolTipText("Print");
        jButtonPrintDefinition.setFocusable(false);
        jButtonPrintDefinition.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonPrintDefinition.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar3.add(jButtonPrintDefinition);

        jButtonFindInDefinition.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Find16.gif"))); // NOI18N
        jButtonFindInDefinition.setToolTipText("Find ");
        jButtonFindInDefinition.setFocusable(false);
        jButtonFindInDefinition.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonFindInDefinition.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar3.add(jButtonFindInDefinition);

        jButtonHomePage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/navigation/Home16.gif"))); // NOI18N
        jButtonHomePage.setToolTipText("Go to home page");
        jButtonHomePage.setFocusable(false);
        jButtonHomePage.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonHomePage.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonHomePage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonHomePageMouseClicked(evt);
            }
        });
        jToolBar3.add(jButtonHomePage);

        jTextPaneDefinition.setEditable(false);
        jTextPaneDefinition.setContentType("text/html"); // NOI18N
        jTextPaneDefinition.setText(HOME_PAGE_STRING);
        jScrollPane2.setViewportView(jTextPaneDefinition);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar3, javax.swing.GroupLayout.DEFAULT_SIZE, 457, Short.MAX_VALUE)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jToolBar3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE))
        );

        jSplitPane1.setRightComponent(jPanel1);

        jMenuTools.setText("Công cụ");
        jMenuTools.setToolTipText("");
        jMenuTools.add(jSeparator1);

        jMenuItemExit.setText("Thoát");
        jMenuItemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemExitActionPerformed(evt);
            }
        });
        jMenuTools.add(jMenuItemExit);

        jMenuBar1.add(jMenuTools);

        jMenuHelp.setText("Trợ giúp");

        jMenuItemHelpContents.setText("Trợ giúp");
        jMenuHelp.add(jMenuItemHelpContents);

        jMenuItemAbout.setText("Giới thiệu");
        jMenuItemAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAboutActionPerformed(evt);
            }
        });
        jMenuHelp.add(jMenuItemAbout);

        jMenuBar1.add(jMenuHelp);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jSplitPane1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonHomePageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonHomePageMouseClicked
        // TODO add your handling code here:
        this.jTextPaneDefinition.setText(HOME_PAGE_STRING);
    }//GEN-LAST:event_jButtonHomePageMouseClicked

    private void jListEntryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListEntryMouseClicked
        // TODO add your handling code here:
//        this.jTextPaneDefinition.setText(this.management.getEntryHTMLDefinition((String) this.jListEntry.getSelectedValue()));
        this.jTextPaneDefinition.setText(this.currentDictionary.getHTMLDefinition(this.jListEntry.getSelectedIndex()));
    }//GEN-LAST:event_jListEntryMouseClicked

    private void jMenuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemExitActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jMenuItemExitActionPerformed

    private void jMenuItemAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAboutActionPerformed
        // TODO add your handling code here:
        JDialog jDialog = new JDialogAbout(this, true);
        jDialog.setLocationRelativeTo(this);
        jDialog.setVisible(true);
        jDialog = null;
        System.gc();
    }//GEN-LAST:event_jMenuItemAboutActionPerformed

    private void jComboBoxDictionariesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxDictionariesActionPerformed
        // TODO add your handling code here:
        loadDictionary();
    }//GEN-LAST:event_jComboBoxDictionariesActionPerformed

    private void jTextFieldEntryKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldEntryKeyReleased
        // TODO add your handling code here:
        int index = this.currentDictionary.find(this.jTextFieldEntry.getText());
        if (index >= 0) {
            this.jListEntry.setSelectedIndex(index);
            this.jListEntry.ensureIndexIsVisible(index);
            this.jScrollPaneEntry.getVerticalScrollBar().setValue(index * this.jListEntry.getFixedCellHeight());
            reloadJListEntry();
        }
    }//GEN-LAST:event_jTextFieldEntryKeyReleased

    private void jButtonInsertEntryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonInsertEntryMouseClicked
        // TODO add your handling code here:
        insertEntry();
    }//GEN-LAST:event_jButtonInsertEntryMouseClicked

    private void jButtonEditMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonEditMouseClicked
        // TODO add your handling code here:
        editEntry();
    }//GEN-LAST:event_jButtonEditMouseClicked

    private void jButtonRemoveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonRemoveMouseClicked
        // TODO add your handling code here:
        removeEntry();
    }//GEN-LAST:event_jButtonRemoveMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCopyDefinition;
    private javax.swing.JButton jButtonEdit;
    private javax.swing.JButton jButtonFindInDefinition;
    private javax.swing.JButton jButtonHomePage;
    private javax.swing.JButton jButtonInsertEntry;
    private javax.swing.JButton jButtonPrintDefinition;
    private javax.swing.JButton jButtonRemove;
    private javax.swing.JButton jButtonSaveDefinition;
    private javax.swing.JComboBox jComboBoxDictionaries;
    private javax.swing.JList jListEntry;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu jMenuHelp;
    private javax.swing.JMenuItem jMenuItemAbout;
    private javax.swing.JMenuItem jMenuItemExit;
    private javax.swing.JMenuItem jMenuItemHelpContents;
    private javax.swing.JMenu jMenuTools;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPaneEntry;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextField jTextFieldEntry;
    private javax.swing.JTextPane jTextPaneDefinition;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar3;
    // End of variables declaration//GEN-END:variables
}
