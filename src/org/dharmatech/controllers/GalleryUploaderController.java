/* Controller for gallery uploader */
/* Author: Wei Zhang
   Latest Version: 2016 Jan 20
*/
/* The controller of GUI GalleryUploader */
/* Handles gallery uploading */
/*API
public class GalleryUploaderController {
    public GalleryUploaderController(String filePath) {}
    public void start() {}
    public static void main(String[] args) {}
}
*/
package org.dharmatech.controllers;
import org.dharmatech.views.*;
import org.dharmatech.models.*;
import org.dharmatech.utilities.*;

import javax.swing.*;
import javax.swing.filechooser .*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.table.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class GalleryUploaderController {
    private GalleryUploader galleryUploader; //GUI
    private int id;
    private int npr;
    private int descriptMode;
    private String pageName;
    //Use one JFileChooser object so that it remembers last location
    private JFileChooser fileOpen; 
    
    //parameters
    private final String cfgPath;
    private final String uploadDir;
    private final int previewWidth = 100;
    private final int previewHeight = 100;
    //private String websiteDir;
    private DesignInfoSet designInfoSet;
       
    private final String[] colNames = {"SourceFile", "File Name", "Description"};

    //Constructor with a designInfoSet object,
    //module configuration path, file uploading destination dir,
    //page name, and module id
    //No module type needed because it is only for Gallery module
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
        this.fileOpen = new JFileChooser();
        galleryUploader = new GalleryUploader();
    }

    //Start page cfg
    public void start() {
        if (galleryUploader == null) {
            throw new NullPointerException("Null galleryUploader");
        }
        basicSetup();
        //galleryUploader.framePack("uploaderGUI");
        init();
        galleryUploader.frameSetVisible("uploaderGUI", true);       
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
        
        //origFileNames = new HashMap<String, Integer>();
        //Initialize preview
        npr = module.getMaxNpr();
        initPreview(npr);
        //We need to pack the GUI here instead of after init()
        //Otherwise the preview panel can be shrunk to the default 4-image view
        galleryUploader.framePack("uploaderGUI");

        //npr & descriptMode
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
            initPreview(model.getRowCount());
            //Init fileNames
            for (int i = 0; i < data.length; i++) {
                if (data[i][1].equals("")) {
                    continue;
                }
                //origFileNames.put(data[i][1].trim().toLowerCase(), new Integer(i));
            }
        }
        //Better to select something! (it has at least 1 empty row as guaranteed when initializing)
        //Not really needed anymore because the non-selection is now checked by actions
        //But selecting the first row is preferred in visual
        galleryUploader.tableSetRowSelectionInterval("imagesTb", 0, 0); 
    }
    
    //helper: reload the file paths and preview panel
    private void reload() {
        //Load images        
        WebModuleGallery module = new WebModuleGallery(new DesignInfoSet(designInfoSet), pageName, id);
        File f = new File(cfgPath);
        if (f.exists() && f.isFile()) {
            Object[][] data = module.getData();
            RegTableModel model = new RegTableModel(data, colNames);
            galleryUploader.tableSetModel("imagesTb", model);
            //(galleryUploader.tableGetColumn("imagesTb", colNames[0])).setPreferredWidth(
            //    Math.round(galleryUploader.tableGetPreferredSize("imagesTb").width * 0.05f));
            //table.getColumn(colNames[0]).setMaxWidth(20);
            initPreview(model.getRowCount());
            //Init fileNames
            for (int i = 0; i < data.length; i++) {
                if (data[i][1].equals("")) {
                    continue;
                }
                //origFileNames.put(data[i][1].trim().toLowerCase(), new Integer(i));
            }
        }
        galleryUploader.tableSetRowSelectionInterval("imagesTb", 0, 0); //Need to select something! (it has at least 1 empty row)
    }
    
    //Listeners
    //Window listener
    private class GalleryWindowListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent we) {
            //if (webEditorGUI != null) {
            //    webEditorGUI.setEnabled(true);
            //}
        }
    }
    //Exit Listener
    private class ExitActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            close();
        }
    }
    
    //Upload Listener
    private class UploadActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            upload();
        }
    }
    
   //Select file Listener
    private class SelectActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            fileOpen.resetChoosableFileFilters();
            FileNameExtensionFilter extFilter = new FileNameExtensionFilter(
                "image files (*.bmp, *.wbmp, *.jpg, *.jpeg, *.png, *.gif )",
                "bmp", "wbmp", "jpg", "jpeg", "png", "gif");
            fileOpen.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileOpen.setFileFilter(extFilter); 
            fileOpen.setMultiSelectionEnabled(true);
            galleryUploader.frameShowOpenDialog("uploaderGUI", fileOpen);
            if (fileOpen.getSelectedFiles() == null) { //Cancelled
                return;
            }
            for (File f : fileOpen.getSelectedFiles()) {
                if (f == null) {
                    continue;
                }
                String sourcePath = f.toString(); //Definitely full path; no need to add websiteDir
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
                galleryUploader.tableSetRowSelectionInterval("imagesTb", 
                    currentLoc + 1, currentLoc + 1);
            }
            initPreview((galleryUploader.tableGetModel("imagesTb"))
                .getRowCount());
            galleryUploader.buttonSetEnabled("upload", true);
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
    //Delete Listener
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
                galleryUploader.tableSetRowSelectionInterval("imagesTb",
                    currentLoc, currentLoc);
            } else if (tbModel.getRowCount() > 0) {
                galleryUploader.tableSetRowSelectionInterval("imagesTb",
                    currentLoc - 1, currentLoc - 1);
            }
            initPreview((galleryUploader.tableGetModel("imagesTb"))
                .getRowCount());            
        }
    }
    //Move Listener: up or down
    private class MoveActionListener implements ActionListener {
        private int direction = -1; //-1: up; +1: down
        public MoveActionListener(int direction) {
            if (direction <= 0) { //Handle no-selection case
                this.direction = -1;  //up
            } else {
                this.direction = 1; //down
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
                //Update selected row to allow continuous moving 
                //without reselecting the target
                galleryUploader.tableSetRowSelectionInterval("imagesTb"
                    , currentLoc + direction, currentLoc + direction);
                initPreview((galleryUploader.tableGetModel("imagesTb"))
                .getRowCount());
            }
        }
    }
    //Insert a row: once you add images, you need to insert rows
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
        galleryUploader.labelSetText("statusLb", 
            "Status: Inserted row at location " + i + ".");
    }
    //Delete a row
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
    //Swap a row: moving is essentially swapping
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
        galleryUploader.labelSetText("statusLb", 
            "Status: Swapped rows " + i + " and " + j + ".");
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
        //Collect data
        WebModuleGallery module = new WebModuleGallery(new DesignInfoSet(designInfoSet),
            pageName, id);
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
        //Check name collision
        boolean nameCollisionRow = getNameCollision(data, module.getUploadDir());
        if (nameCollisionRow) {
            return;
        }
        //Upload
        module.upload(data);
        //Write cfg
        FileUtilities.write(module.getCfgPath(), 
            FileUtilities.writeProcSeparator(
                module.genRecord(data, npr, descriptMode)
            ),
            "UTF-8");
        galleryUploader.labelSetText("statusLb", 
            "Status: uploading/saving done." + " Total " + iR + " items.");
        //File names may have changed: need to reload
        reload();
    }
    //Check name collision
    private boolean getNameCollision(Object[][] data, String uploadDir) {
        //Check whether new names collide with each other
        HashMap<File, Integer> newFileMap = new HashMap<File, Integer>();
        for (int i = 0; i < data.length; i++) {
            if (data[i][0].equals("")) {
                continue;
            }
            String name = ((String) data[i][1]).trim();
            File newF = new File(uploadDir + File.separator + name);
            if (newFileMap.get(newF) != null) {
                galleryUploader.labelSetText("statusLb", 
                    "<html><font color=\"red\">Status: Name collision detected for " + name 
                    + "!</font></html>");
                galleryUploader.tableSetRowSelectionInterval("imagesTb", 
                    i, i);
                galleryUploader.tableAddRowSelectionInterval("imagesTb", 
                    newFileMap.get(newF), newFileMap.get(newF));
                return true;
            }
            newFileMap.put(newF, new Integer(i));
        }
        //Check whether old files collide with each other (conservative)
        //Only happens if one wants to upload one file to two locations
        //Usually won't happen; disallowing this makes the 3rd check easier
        HashMap<File, Integer> oldFileMap = new HashMap<File, Integer>();
        for (int i = 0; i < data.length; i++) {
            if (data[i][0].equals("")) {
                continue;
            }
            File oldF = new File(((String) data[i][0]).trim());
            String name = oldF.getName();
            if (oldFileMap.get(oldF) != null) {
                galleryUploader.labelSetText("statusLb", 
                    "<html><font color=\"red\">Status: Name collision detected for " + name 
                    + "!</font></html>");
                galleryUploader.tableSetRowSelectionInterval("imagesTb", 
                    i, i);
                galleryUploader.tableAddRowSelectionInterval("imagesTb", 
                    oldFileMap.get(oldF), oldFileMap.get(oldF));
                return true;
            }
            oldFileMap.put(oldF, i);
        }
        //Check whether new names collide with others' old names 
        for (File fileKey : newFileMap.keySet()) {
            //Collision mainly happens to the cases where we have source files 
            //that are previously uploaded (no issue if the files come from outside)
            //It is safe if the new path does not equal to the original path of another uploaded file
            //It is also safe if the original uploaded file is renamed before getting 
            //collided with a new path (which means no collision actually)
            if (oldFileMap.get(fileKey) == null 
                || oldFileMap.get(fileKey) <= newFileMap.get(fileKey)) {
                //a "<=" is sufficient because its renaming part is guaranteed by the 2nd check
                continue;
            }
            galleryUploader.labelSetText("statusLb", 
                "<html><font color=\"red\">Status: Name collision detected for " + fileKey.getName() 
                + "!</font></html>");
            galleryUploader.tableSetRowSelectionInterval("imagesTb", 
                oldFileMap.get(fileKey), oldFileMap.get(fileKey));
            galleryUploader.tableAddRowSelectionInterval("imagesTb", 
                newFileMap.get(fileKey), newFileMap.get(fileKey));
            return true;
        }
        return false;
    }
    //Exit
    private void close() {
        galleryUploader.frameDispatchEvent("uploaderGUI", WindowEvent.WINDOW_CLOSING);
    }
    
    //JCombobox Listener for npr
    private class NPRBoxListener implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent event) {
            int sel = galleryUploader.comboBoxIntGetSelectedIndex("nprCB");
            setNpr(sel + 1);
            initPreview((galleryUploader.tableGetModel("imagesTb"))
                .getRowCount());
        }
    }
    //JCombobox Listener for descriptMode
    private class ModeBoxListener implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent event) {
            int sel = galleryUploader.comboBoxStrGetSelectedIndex("descriptModeCB");
            setDescriptMode(sel + 1);
        }
    }
    //Set npr
    private void setNpr(int npr) {
        this.npr = npr;
    }
    //Set descriptMode
    private void setDescriptMode(int descriptMode) {
        this.descriptMode = descriptMode;
    } 
    //Initial the preview panel with images or empty labels
    private void initPreview(int count) {
        galleryUploader.panelRemoveAllItems("previewPn");
        int rows = (int) Math.ceil(1.0 * count / npr);
        for (int r = 0; r < rows; r++) {
            RegJPanel rowPn = new RegJPanel();
            rowPn.setLayout(new FlowLayout(FlowLayout.LEADING));
            galleryUploader.panelAddComponent("previewPn", rowPn);
            for (int i = 0; i < npr; i++) {
                int index = r * npr + i;
                JLabel picLabel = loadPreviewImage(index);
                picLabel.setPreferredSize(new Dimension(previewWidth, previewHeight));
                rowPn.add(picLabel);
            }
        }
    }
    //load images as previews into the labels, or return an empty label
    private JLabel loadPreviewImage(int index) {
        TableModel tbModel = galleryUploader.tableGetModel("imagesTb");
        try {
            JLabel previewLb = new JLabel(
                new ImageIcon(
                    FileUtilities.resizeImage(
                        ImageIO.read(
                            new File(
                                tbModel.getValueAt(index, 0).toString())),
                        previewWidth, previewHeight)
                )
            );
            previewLb.setToolTipText(tbModel.getValueAt(index, 1).toString());
            return previewLb;
        } catch (Exception ex) {
            return new JLabel("");
        }        
    }  
    
    public static void main(String[] args) {
        GalleryUploaderController gUp = null;
        try {
            gUp = new GalleryUploaderController(new DesignInfoSet(new HashMap<String, String>()), "cfg.path", "uploads", "index", 1);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        gUp.start();
    }
}