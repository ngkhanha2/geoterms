/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vnu.geoterms.core.ui;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.File;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import vnu.geoterms.core.Dictionary;
import vnu.geoterms.core.Interface.*;

/**
 *
 * @author Khanh
 */
public class JFrameMain extends javax.swing.JFrame {

    private String HOME_PAGE_STRING= "<html><head></head><body><h1>Chào mừng bạn đến với VNU - Geoterms</h1>Đây là phiên bản thử nghiệm thứ 4.</body></html>";
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

    private void selectEntry() {
        if (this.currentDictionary == null) {
            return;
        }
        int index = this.jListEntry.getSelectedIndex();
        if (index == -1) {
            return;
        }
        this.jTextPaneDefinition.setText(this.currentDictionary.getHTMLDefinition(index));
    }

    private void findInDefinition() {
        String pattern = this.jTextFieldFindInDefinition.getText().trim().toLowerCase(Locale.US);
        if (pattern.isEmpty()) {
            return;
        }
        try {
            String document = this.jTextPaneDefinition.getDocument().getText(0, this.jTextPaneDefinition.getDocument().getLength()).toLowerCase(Locale.US);
            int index = document.indexOf(pattern);
            if (index >= 0) {
                this.jTextPaneDefinition.requestFocus();
                this.jTextPaneDefinition.select(index, index + pattern.length());
            }
        } catch (BadLocationException ex) {
            Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void printDefinition() {
        try {
            boolean done = jTextPaneDefinition.print();
        } catch (Exception ex) {

        }
    }

    /**
     * Creates new form JFrameMain
     *
     * @param management
     */
    public JFrameMain(IManagement management) {
        this.management = management;

//        HOME_PAGE_STRING = "<html><head></head><body></body></html>"; // <h1>VNU - Geoterms</h1><img src=\"file:\\" + System.getProperty("user.dir") + File.separator + "util" + File.separator + "vnu_hus_logo.png\" width=\"80\" height=\"101\">

        initComponents();

        this.jListEntry.setModel(new DefaultListModel());
        this.jComboBoxDictionaries.addItem("...");
        for (int i = 0; i < this.management.getDictionaries().size(); ++i) {
            this.jComboBoxDictionaries.addItem(this.management.getDictionaries().get(i).getName());
        }

        this.jToolBarSearchInDefinition.setVisible(this.jToggleButtonFindInDefinition.isSelected());

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
        jPanelEntries = new javax.swing.JPanel();
        jScrollPaneEntry = new javax.swing.JScrollPane();
        jListEntry = new javax.swing.JList();
        jTextFieldEntry = new javax.swing.JTextField();
        jComboBoxDictionaries = new javax.swing.JComboBox();
        jToolBar1 = new javax.swing.JToolBar();
        jButtonInsertEntry = new javax.swing.JButton();
        jButtonEdit = new javax.swing.JButton();
        jButtonRemove = new javax.swing.JButton();
        jPanelDefinition = new javax.swing.JPanel();
        jToolBar3 = new javax.swing.JToolBar();
        jButtonCopyDefinitionHTMLCode = new javax.swing.JButton();
        jButtonPrintDefinition = new javax.swing.JButton();
        jToggleButtonFindInDefinition = new javax.swing.JToggleButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPaneDefinition = new javax.swing.JTextPane();
        jToolBarSearchInDefinition = new javax.swing.JToolBar();
        jTextFieldFindInDefinition = new javax.swing.JTextField();
        jButtonFindInDefinition = new javax.swing.JButton();
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

        javax.swing.GroupLayout jPanelEntriesLayout = new javax.swing.GroupLayout(jPanelEntries);
        jPanelEntries.setLayout(jPanelEntriesLayout);
        jPanelEntriesLayout.setHorizontalGroup(
            jPanelEntriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPaneEntry, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jTextFieldEntry, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jComboBoxDictionaries, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
        );
        jPanelEntriesLayout.setVerticalGroup(
            jPanelEntriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEntriesLayout.createSequentialGroup()
                .addComponent(jComboBoxDictionaries, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldEntry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneEntry, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jSplitPane1.setLeftComponent(jPanelEntries);

        jToolBar3.setFloatable(false);
        jToolBar3.setRollover(true);

        jButtonCopyDefinitionHTMLCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Copy16.gif"))); // NOI18N
        jButtonCopyDefinitionHTMLCode.setToolTipText("Sao chép mã HTML");
        jButtonCopyDefinitionHTMLCode.setFocusable(false);
        jButtonCopyDefinitionHTMLCode.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonCopyDefinitionHTMLCode.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonCopyDefinitionHTMLCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonCopyDefinitionHTMLCodeMouseClicked(evt);
            }
        });
        jToolBar3.add(jButtonCopyDefinitionHTMLCode);

        jButtonPrintDefinition.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Print16.gif"))); // NOI18N
        jButtonPrintDefinition.setToolTipText("In văn bản HTML");
        jButtonPrintDefinition.setFocusable(false);
        jButtonPrintDefinition.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonPrintDefinition.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonPrintDefinition.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonPrintDefinitionMouseClicked(evt);
            }
        });
        jToolBar3.add(jButtonPrintDefinition);

        jToggleButtonFindInDefinition.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Find16.gif"))); // NOI18N
        jToggleButtonFindInDefinition.setFocusable(false);
        jToggleButtonFindInDefinition.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButtonFindInDefinition.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButtonFindInDefinition.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jToggleButtonFindInDefinitionMouseClicked(evt);
            }
        });
        jToolBar3.add(jToggleButtonFindInDefinition);

        jTextPaneDefinition.setEditable(false);
        jTextPaneDefinition.setContentType("text/html"); // NOI18N
        jTextPaneDefinition.setText(HOME_PAGE_STRING);
        jScrollPane2.setViewportView(jTextPaneDefinition);

        jToolBarSearchInDefinition.setFloatable(false);
        jToolBarSearchInDefinition.setRollover(true);
        jToolBarSearchInDefinition.add(jTextFieldFindInDefinition);

        jButtonFindInDefinition.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Find16.gif"))); // NOI18N
        jButtonFindInDefinition.setFocusable(false);
        jButtonFindInDefinition.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonFindInDefinition.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonFindInDefinition.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonFindInDefinitionMouseClicked(evt);
            }
        });
        jToolBarSearchInDefinition.add(jButtonFindInDefinition);

        javax.swing.GroupLayout jPanelDefinitionLayout = new javax.swing.GroupLayout(jPanelDefinition);
        jPanelDefinition.setLayout(jPanelDefinitionLayout);
        jPanelDefinitionLayout.setHorizontalGroup(
            jPanelDefinitionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar3, javax.swing.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jToolBarSearchInDefinition, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanelDefinitionLayout.setVerticalGroup(
            jPanelDefinitionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDefinitionLayout.createSequentialGroup()
                .addComponent(jToolBar3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBarSearchInDefinition, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jSplitPane1.setRightComponent(jPanelDefinition);

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
            .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 735, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jListEntryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListEntryMouseClicked
        // TODO add your handling code here:
        selectEntry();
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

    private void jToggleButtonFindInDefinitionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jToggleButtonFindInDefinitionMouseClicked
        // TODO add your handling code here:
        this.jToolBarSearchInDefinition.setVisible(this.jToggleButtonFindInDefinition.isSelected());
    }//GEN-LAST:event_jToggleButtonFindInDefinitionMouseClicked

    private void jButtonFindInDefinitionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonFindInDefinitionMouseClicked
        // TODO add your handling code here:
        findInDefinition();
    }//GEN-LAST:event_jButtonFindInDefinitionMouseClicked

    private void jButtonPrintDefinitionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonPrintDefinitionMouseClicked
        // TODO add your handling code here:
        printDefinition();
    }//GEN-LAST:event_jButtonPrintDefinitionMouseClicked

    private void jButtonCopyDefinitionHTMLCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonCopyDefinitionHTMLCodeMouseClicked
        // TODO add your handling code here:
        StringSelection stringSelection = new StringSelection(this.jTextPaneDefinition.getText());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, stringSelection);
    }//GEN-LAST:event_jButtonCopyDefinitionHTMLCodeMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCopyDefinitionHTMLCode;
    private javax.swing.JButton jButtonEdit;
    private javax.swing.JButton jButtonFindInDefinition;
    private javax.swing.JButton jButtonInsertEntry;
    private javax.swing.JButton jButtonPrintDefinition;
    private javax.swing.JButton jButtonRemove;
    private javax.swing.JComboBox jComboBoxDictionaries;
    private javax.swing.JList jListEntry;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu jMenuHelp;
    private javax.swing.JMenuItem jMenuItemAbout;
    private javax.swing.JMenuItem jMenuItemExit;
    private javax.swing.JMenuItem jMenuItemHelpContents;
    private javax.swing.JMenu jMenuTools;
    private javax.swing.JPanel jPanelDefinition;
    private javax.swing.JPanel jPanelEntries;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPaneEntry;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextField jTextFieldEntry;
    private javax.swing.JTextField jTextFieldFindInDefinition;
    private javax.swing.JTextPane jTextPaneDefinition;
    private javax.swing.JToggleButton jToggleButtonFindInDefinition;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JToolBar jToolBarSearchInDefinition;
    // End of variables declaration//GEN-END:variables
}
