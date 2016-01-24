/* Controller for gallery uploader */
/* Author: Wei Zhang
   Latest Version: 2016 Jan 20
*/
/*API
public class GalleryUploaderController {
    public GalleryUploaderController(String filePath)
    public void start()
    public static void main(String[] args)
}
*/
package controllers;
import views.*;
import models.*;
import utilities.*;

import javax.swing.*;
import javax.swing.filechooser .*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.table.*;

public class GalleryUploaderController {
    private GalleryUploader galleryUploader;
    private int id;
    private int npr;
    private int descriptMode;
    private String pageName;
    
    //parameters
    private final String cfgPath;
    private final String uploadDir;
    //private String websiteDir;
    private DesignInfoSet designInfoSet;
       
    private final String[] colNames = {"SourceFile", "File Name", "Description"};

    public GalleryUploaderController(DesignInfoSet designInfoSet, 
        String cfgPath, String uploadDir, String pageName, int id)
        throws IOException {
        if (designInfoSet == null) {
            throw new NullPointerException("Null designInfoSet");
        }
        if (cfgPath == null) {
            throw new NullPointerException("Null cfgPath");
        }
        if (uploadDir == null) {
            throw new NullPointerException("Null uploadDir");
        }
        if (pageName == null) {
            throw new NullPointerException("Null pageName");
        }
        this.designInfoSet = new DesignInfoSet(designInfoSet);
        File f = new File(cfgPath);
        if (f.exists() && !f.isFile()) {
            throw new IOException("Already a dir: " + cfgPath);
        }
        this.cfgPath = cfgPath;
        this.uploadDir = uploadDir;
        this.id = id;
        this.pageName = pageName;
        galleryUploader = new GalleryUploader();
        //this.websiteDir = uploadDir;
        //int i = uploadDir.indexOf(File.separator + "uploads" + File.separator);
        //if (i > 0) {
        //    this.websiteDir = uploadDir.substring(0, i);
        //}
    }

    //Start page cfg
    public void start() {
        if (galleryUploader == null) {
            throw new NullPointerException("Null galleryUploader");
        }
        basicSetup();
        init();
        galleryUploader.frameSetVisible("uploaderGUI", true);       
        galleryUploader.framePack("uploaderGUI");
    }

    //Basic setup
    private void basicSetup() {
        galleryUploader.buttonAddActionListener("upload", new UploadActionListener());
        galleryUploader.buttonAddActionListener("close", new ExitActionListener());
        galleryUploader.buttonAddActionListener("select", new SelectActionListener());
        //galleryUploader.buttonAddActionListener("insert", new InsertActionListener());
        galleryUploader.buttonAddActionListener("delete", new DeleteActionListener());
        galleryUploader.buttonAddActionListener("moveUp", new MoveActionListener(-1));
        galleryUploader.buttonAddActionListener("moveDown", new MoveActionListener(1));
        //GalleryUploader.textAreaAddDocumentListener("table", new EditDocumentListener());
        galleryUploader.frameAddWindowListener("uploaderGUI", new GalleryWindowListener());
        galleryUploader.comboBoxIntAddActionListener("nprCB", new NPRBoxListener());
        galleryUploader.comboBoxStrAddActionListener("descriptModeCB", new ModeBoxListener());
    }
    //Initialize
    private void init() {
        //(galleryUploader.tableGetColumn("imagsTb", colNames[0])).setPreferredWidth(
        //    Math.round(galleryUploader.tableGetPreferredSize("imagesTb").width * 0.1f));
        WebModuleGallery module = new WebModuleGallery(new DesignInfoSet(designInfoSet), pageName, id);
        galleryUploader.labelSetText("uploadDestLb", FileUtilities.autoEllipsis(uploadDir));
        galleryUploader.labelSetToolTipText("uploadDestLb", uploadDir);
        galleryUploader.comboBoxIntRemoveAllItems("nprCB");
        galleryUploader.comboBoxStrRemoveAllItems("descriptModeCB");
        for (int i = 1; i <= module.getMaxNpr(); i++) {
            galleryUploader.comboBoxIntAddItem("nprCB", i);
        }
        npr = module.getDefNpr();
        galleryUploader.comboBoxIntSetSelectedIndex("nprCB", npr - 1);
        for (int i = 0; i < module.getDescriptModeStrs().length; i++) {
            galleryUploader.comboBoxStrAddItem("descriptModeCB", module.getDescriptModeStrs()[i]); 
        }
        descriptMode = module.getDefDescriptMode();
        galleryUploader.comboBoxStrSetSelectedIndex("descriptModeCB", descriptMode - 1);
        //Initialize format even if no data yet
        Object[][] data = {{"", "", ""}};
        RegTableModel model = new RegTableModel(data, colNames);
        galleryUploader.tableSetModel("imagesTb", model);
        
        //Load images
        
        File f = new File(cfgPath);
        if (f.exists() && f.isFile()) {
            data = module.getData();
            npr = module.getNpr();
            descriptMode = module.getDescriptMode();
            if (npr < 1) {
                npr = 1;
            }
            if (npr > galleryUploader.comboBoxIntGetItemCount("nprCB")) {
                npr = galleryUploader.comboBoxIntGetItemCount("nprCB");
            }
            if (descriptMode < 1) {
                descriptMode = 1;
            }
            if (descriptMode > galleryUploader.comboBoxStrGetItemCount("descriptModeCB")) {
                descriptMode = galleryUploader.comboBoxStrGetItemCount("descriptModeCB");
            }
            galleryUploader.comboBoxIntSetSelectedIndex("nprCB", npr - 1);
            galleryUploader.comboBoxStrSetSelectedIndex("descriptModeCB", descriptMode - 1);
            model = new RegTableModel(data, colNames);
            galleryUploader.tableSetModel("imagesTb", model);
            //(galleryUploader.tableGetColumn("imagesTb", colNames[0])).setPreferredWidth(
            //    Math.round(galleryUploader.tableGetPreferredSize("imagesTb").width * 0.05f));
            //table.getColumn(colNames[0]).setMaxWidth(20);
        }
        galleryUploader.tableSetRowSelectionInterval("imagesTb", 0, 0); //Need to select something! (it has at least 1 empty row)
    }
    
    private class GalleryWindowListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent we) {
            //if (webEditorGUI != null) {
            //    webEditorGUI.setEnabled(true);
            //}
        }
    }
    //Listeners
    private class UploadActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            upload();
        }
    }
    private class ExitActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            close();
        }
    }
    
   //Select file Listener
    private class SelectActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            FileNameExtensionFilter extFilter = new FileNameExtensionFilter(
                "image files (*.bmp, *.wbmp, *.jpg, *.jpeg, *.png, *.gif )",
                "bmp", "wbmp", "jpg", "jpeg", "png", "gif");
            JFileChooser fileOpen = galleryUploader.frameFileChooser("uploaderGUI", null, extFilter);
            if (fileOpen.getSelectedFile() == null) { //Cancelled
                return;
            }
            String sourcePath = fileOpen.getSelectedFile().toString(); //Definitely full path; no need to add websiteDir
            //UploadedImage upImage = new UploadedImage(sourcePath, "", "");                
            String fileName = (new File(sourcePath)).getName();
            int currentLoc = galleryUploader.tableGetSelectedRow("imagesTb");
            //RegTableModel tbModel = (RegTableModel) galleryUploader.tableGetModel("imagesTb");
            //if (tbModel.getRowCount() > 0) {
            //    currentLoc = galleryUploader.tableGetSelectedRow("imagesTb");
            //}
            Object[] newRow = {sourcePath, fileName, ""};
            if (currentLoc < 0) {  //Handle no-selection case
                insertRow(0, newRow);
            } else {
                insertRow(currentLoc, newRow);
            }
            galleryUploader.buttonSetEnabled("upload", true);
            galleryUploader.tableSetRowSelectionInterval("imagesTb", currentLoc + 1, currentLoc + 1);
        }        
    }
    /*
    private class InsertActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            int currentLoc = galleryUploader.tableGetSelectedRow("imagesTb");
            insertRow(currentLoc);
        }
    }*/
    private class DeleteActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            int currentLoc = galleryUploader.tableGetSelectedRow("imagesTb");
            if (currentLoc < 0) { //Handle no-selection case
                return;
            }
            deleteRow(currentLoc);
            RegTableModel tbModel = (RegTableModel) galleryUploader.tableGetModel("imagesTb");
            if (tbModel.getRowCount() > currentLoc) {
                galleryUploader.tableSetRowSelectionInterval("imagesTb", currentLoc, currentLoc);
            } else if (tbModel.getRowCount() > 0) {
                galleryUploader.tableSetRowSelectionInterval("imagesTb", currentLoc - 1, currentLoc - 1);
            } 
        }
    }
    private class MoveActionListener implements ActionListener {
        private int direction = -1; //-1: up; +1: down
        public MoveActionListener(int direction) {
            if (direction <= 0) { //Handle no-selection case
                this.direction = -1;  //up
            } else {
                this.direction = 1;
            }
        }
        @Override
        public void actionPerformed(ActionEvent event) {
            int currentLoc = galleryUploader.tableGetSelectedRow("imagesTb");
            if (currentLoc < 0) {
                return;
            }
            boolean swapped = swapRow(currentLoc, currentLoc + direction);
            if (swapped) {
                galleryUploader.tableSetRowSelectionInterval("imagesTb", currentLoc + direction, currentLoc + direction);
            }
        }
    }
    
    private void insertRow(int i, Object[] newRow) {
        RegTableModel tbModel = (RegTableModel) galleryUploader.tableGetModel("imagesTb");
        if (i < 0 
            || (tbModel.getRowCount() > 0 && i >= tbModel.getRowCount())
            || (tbModel.getRowCount() == 0 && i > 0)){
            throw new IndexOutOfBoundsException("i = " + i 
                + "; i must be within 0 and " 
                + tbModel.getRowCount() + "-1");
        }
        tbModel.insertRow(i, newRow);
        galleryUploader.labelSetText("statusLb", "Status: Inserted row at location " + i + ".");
    }
    
    private void deleteRow(int i) {
        RegTableModel tbModel = (RegTableModel) galleryUploader.tableGetModel("imagesTb");
        if (i < 0 || i >= tbModel.getRowCount()) {
            throw new IndexOutOfBoundsException("i = " + i 
                + "; i must be within 0 and " 
                + tbModel.getRowCount() + "-1");
        }
        tbModel.removeRow(i);
        galleryUploader.labelSetText("statusLb", "Status: Removed row " + i + ".");
    }
    
    private boolean swapRow(int i, int j) {
       RegTableModel tbModel = (RegTableModel) galleryUploader.tableGetModel("imagesTb");
       if (i < 0 || i >= tbModel.getRowCount()) {
            throw new IndexOutOfBoundsException("i = " + i 
                + "; i must be within 0 and " 
                + tbModel.getRowCount() + "-1");
        }
        if (j < 0 || j >= tbModel.getRowCount()) { //Do not swap invalid range
            galleryUploader.labelSetText("statusLb", "Status: No swapping done.");
            return false;
        }
        tbModel.moveRow(i, i, j);
        galleryUploader.labelSetText("statusLb", "Status: Swapped rows " + i + " and " + j + ".");
        return true;
    }
    //private class EditDocumentListener implements DocumentListener {
    //    @Override
    //    public void changedUpdate(DocumentEvent event) {
    //        save.setEnabled(true);
    //    }
    //    public void insertUpdate(DocumentEvent event) {
    //        save.setEnabled(true);
    //    }
    //    public void removeUpdate(DocumentEvent event) {
    //        save.setEnabled(true);
    //    }
    //}
    //upload
    private void upload() {
        galleryUploader.labelSetText("statusLb", "Status: uploading/saving...");
        WebModuleGallery module = new WebModuleGallery(new DesignInfoSet(designInfoSet), pageName, id);
        TableModel tbModel = galleryUploader.tableGetModel("imagesTb");
        int rCount = tbModel.getRowCount();
        int cCount = tbModel.getColumnCount();
        Object[][] data = new Object[rCount][cCount];
        //System.out.println(designInfoSet.getDesignInfo("rootDir"));
        int i;
        int iR = 0;
        for (i = 0; i < rCount; i++) {
            if (tbModel.getValueAt(i, 0).toString().equals("")) {
                continue;
            }
            for (int j = 0; j < cCount; j++) {
                data[iR][j] = tbModel.getValueAt(i, j);
            }
            iR++;
            //System.out.println(i + " - " + data[i][0]);
        }
        int total = iR;
        for (int k = iR; k < rCount; k++) {
            data[k][0] = "";
            //System.out.println(k + " - " + data[k][0]);
        }
        module.upload(data);
        FileUtilities.write(module.getCfgPath(), 
            module.genRecord(data, npr, descriptMode), "UTF-8");
        galleryUploader.labelSetText("statusLb", 
            "Status: uploading/saving done." + " Total " + iR + " items.");
    }
    //Exit
    private void close() {
        galleryUploader.frameDispatchEvent("uploaderGUI", WindowEvent.WINDOW_CLOSING);
    }
    
    //JCombobox Listener
    private class NPRBoxListener implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent event) {
            int sel = galleryUploader.comboBoxIntGetSelectedIndex("nprCB");
            setNpr(sel + 1);
        }
    }
    //JCombobox Listener
    private class ModeBoxListener implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent event) {
            int sel = galleryUploader.comboBoxStrGetSelectedIndex("descriptModeCB");
            setDescriptMode(sel + 1);
        }
    }
    private void setNpr(int npr) {
        this.npr = npr;
    }
    private void setDescriptMode(int descriptMode) {
        this.descriptMode = descriptMode;
    } 

}