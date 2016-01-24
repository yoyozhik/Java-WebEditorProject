/* GUI for editing specified file */
/* Author: Wei Zhang
   Latest Version: 2016 Jan 17
*/
/*API
public class fileUploaderController {
    public fileUploaderController(String cfgPath, String uploadDir)
    public void start()
    public static void main(String[] args)
}
*/
package controllers;
import views.*;
import models.*;
import utilities.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class FileUploaderController {
    private FileUploader fileUploader;
    private DesignInfoSet designInfoSet;
    private WebModuleEnum typeEnum;
    private int id;
    //parameters
    private final String cfgPath;
    private final String uploadDir;
    private String sourcePath;
    private String pageName;
    private JFileChooser fileOpen;

    public FileUploaderController(DesignInfoSet designInfoSet, 
        String cfgPath, String uploadDir, String pageName, int id, 
        WebModuleEnum typeEnum) throws IOException {
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
        if (typeEnum == null) {
            throw new NullPointerException("Null typeEnum");
        }
        this.designInfoSet = new DesignInfoSet(designInfoSet);
        File f = new File(cfgPath);
        if (f.exists() && !f.isFile()) {
            throw new IOException("Already a dir: " + cfgPath);
        }
        this.cfgPath = cfgPath;
        this.uploadDir = uploadDir;
        this.pageName = pageName;
        this.typeEnum = typeEnum;
        switch (typeEnum) {
            case FILE: 
                this.fileUploader = new FileUploader(); 
                break;
            case IMAGE: 
                this.fileUploader = new ImageUploader(); 
                break;
            default: 
                throw new IllegalArgumentException("Illegal type in FileUploaderController: "
                    + typeEnum.getValue());
        }        
        this.id = id;
        this.fileOpen = new JFileChooser();
    }
    
    //Start file uploader
    public void start() {
        if (fileUploader == null) {
            throw new NullPointerException("Null fileUploader");
        }
        basicSetup();
        init();
        fileUploader.frameSetVisible("uploaderGUI", true);
        fileUploader.framePack("uploaderGUI");
    }
    
    //Basic setup
    private void basicSetup() {
        fileUploader.buttonAddActionListener("select", new SelFileActionListener());
        fileUploader.buttonAddActionListener("upload", new UploadActionListener());
        fileUploader.buttonAddActionListener("close", new ExitActionListener());
        fileUploader.textFieldAddDocumentListener("fileNameTF", new EditDocumentListener());
        fileUploader.frameAddWindowListener("uploaderGUI", new fileUploaderWindowListener());
        if (fileUploader.objExists("widthTF", GUIObjTypeEnum.TEXTFIELD)) {
            fileUploader.textFieldAddDocumentListener("widthTF", new EditDocumentListener());
        }
        if (fileUploader.objExists("heightTF", GUIObjTypeEnum.TEXTFIELD)) {
            fileUploader.textFieldAddDocumentListener("heightTF", new EditDocumentListener());
        }
    }
    //Initialize
    private void init() {
        //fileUploader.labelSetText("cfgPath", cfgPath);
        WebModuleDefault module = null;
        fileUploader.buttonSetEnabled("select", true);
        switch (typeEnum) {
            case FILE:
                module = new WebModuleFile(new DesignInfoSet(designInfoSet), pageName, id);
                sourcePath = ((WebModuleFile) module).getSourcePath();
                if (sourcePath != null) {
                    fileUploader.labelSetText("sourceLb", FileUtilities.autoEllipsis(sourcePath));
                    fileUploader.textFieldSetText("fileNameTF", 
                        ((WebModuleFile) module).getFileName());
                }
                break;
            case IMAGE:
                module = new WebModuleImage(new DesignInfoSet(designInfoSet), pageName, id);
                sourcePath = ((WebModuleImage) module).getSourcePath();
                if (sourcePath != null) {
                    fileUploader.labelSetText("sourceLb", FileUtilities.autoEllipsis(sourcePath));
                    fileUploader.textFieldSetText("fileNameTF", 
                        ((WebModuleImage) module).getFileName());
                    fileUploader.textFieldSetText("widthTF", 
                        ((WebModuleImage) module).getWidth());
                    fileUploader.textFieldSetText("heightTF", 
                        ((WebModuleImage) module).getHeight());
                }
                break;
            default: 
                throw new IllegalArgumentException("Not File or Image: " + typeEnum.getValue());
        }
        //Disable further selection
        if (sourcePath != null) {
            fileUploader.buttonSetEnabled("select", false);
        }
        fileUploader.buttonSetEnabled("upload", false);
        fileUploader.labelSetText("uploadDestLb", FileUtilities.autoEllipsis(uploadDir));
        fileUploader.labelSetToolTipText("uploadDestLb", uploadDir);

    }
   
    //Listeners
    //Select file Listener
    private class SelFileActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            fileOpen.resetChoosableFileFilters();
            fileOpen.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileUploader.frameShowOpenDialog("uploaderGUI", fileOpen);
            if (fileOpen.getSelectedFile() == null) { //Cancelled
                return;
            }
            sourcePath = fileOpen.getSelectedFile().toString();
            fileUploader.labelSetText("sourceLb", FileUtilities.autoEllipsis(sourcePath));
            fileUploader.labelSetToolTipText("sourceLb", sourcePath);
            String fileName = (new File(sourcePath)).getName();
            fileUploader.textFieldSetText("fileNameTF", fileName);
            fileUploader.textFieldSetToolTipText("fileNameTF", fileName);
            fileUploader.buttonSetEnabled("upload", true);
        }        
    }
    //Save Listener
    private class UploadActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            upload();
        }
    }
    //close Listener
    private class ExitActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            close();
        }
    }
    //Window Close Listener
    private class fileUploaderWindowListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent we) {
            //if (webEditorGUI != null) {
            //    webEditorGUI.setEnabled(true);
            //}
        }
    }
    //Document Edit Listener
    private class EditDocumentListener implements DocumentListener {
        @Override
        public void changedUpdate(DocumentEvent event) {
            fileUploader.buttonSetEnabled("upload", true);
        }
        public void insertUpdate(DocumentEvent event) {
            fileUploader.buttonSetEnabled("upload", true);
        }
        public void removeUpdate(DocumentEvent event) {
            fileUploader.buttonSetEnabled("upload", true);
        }
    }
    
    //Save function
    private void upload() {
        String destPath = uploadDir + File.separator 
            + fileUploader.textFieldGetText("fileNameTF"); //Target path
        WebModuleDefault module = null;
        String status = "";
        switch (typeEnum) {
            case FILE:
                module = new WebModuleFile(new DesignInfoSet(designInfoSet), pageName, id);
                status = ((WebModuleFile) module).upload(sourcePath, destPath);
                FileUtilities.write(module.getCfgPath(), 
                    ((WebModuleFile) module).genRecord(destPath), "UTF-8");
                break;
            case IMAGE:
                module = new WebModuleImage(new DesignInfoSet(designInfoSet), pageName, id);
                status = ((WebModuleImage) module).upload(sourcePath, destPath);
                FileUtilities.write(module.getCfgPath(), 
                    ((WebModuleImage) module).genRecord(destPath,
                    fileUploader.textFieldGetText("widthTF"), 
                    fileUploader.textFieldGetText("heightTF")), 
                    "UTF-8");
                break;
            default: 
                throw new IllegalArgumentException("Not File or Image: " + typeEnum.getValue());
        }            
        fileUploader.labelSetText("statusLb", status);
        fileUploader.buttonSetEnabled("upload", false);
        fileUploader.labelSetText("statusLb", status);
    }
    //Exit function
    private void close() {
        fileUploader.frameDispatchEvent("uploaderGUI", WindowEvent.WINDOW_CLOSING);        
    }
    
    public static void main(String[] args) {
        FileUploaderController fUp = null;
        try {
            fUp = new FileUploaderController(new DesignInfoSet(new HashMap<String, String>()), "a", "b", "", 1, WebModuleEnum.FILE);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        fUp.start();
    }
}