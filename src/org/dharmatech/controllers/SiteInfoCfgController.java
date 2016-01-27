/* controller for SiteInfoCfg */
/* Author: Wei Zhang
   Latest Version: 2016 Jan 20
*/
/* The controller of GUI SiteCfg */
/* Handles site info, including website name and url */
/* API
public class SiteInfoCfgController {
    public SiteInfoCfgController(String urlPath, String namePath) {}
    public void start() {}
    public static void main(String[] args) {}
}
*/
package org.dharmatech.controllers;
import org.dharmatech.views.*;
import org.dharmatech.utilities.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class SiteInfoCfgController {
    private SiteInfoCfg siteInfoCfg; //GUI
    private final String urlPath;
    private final String namePath;
    private Command reloadMain;
    
    //constructor with the url cfg file path, name cfg file path
    //and a Command object with a WebEditor reload method packed
    public SiteInfoCfgController(String urlPath, String namePath, Command reloadMain) throws IOException {
        if (urlPath == null) {
            throw new NullPointerException("Null urlPath");
        }
        if (namePath == null) {
            throw new NullPointerException("Null namePath");
        }
        File f = new File(urlPath);
        if (f.exists() && !f.isFile()) {
            throw new IOException("Already a dir: " + urlPath);
        }
        f = new File(namePath);
        if (f.exists() && !f.isFile()) {
            throw new IOException("Already a dir: " + namePath);
        }
        this.urlPath = urlPath;
        this.namePath = namePath;
        this.reloadMain = reloadMain;
        siteInfoCfg = new SiteInfoCfg();
    }
    
    //Start site info cfg
    public void start() {
        if (siteInfoCfg == null) {
            throw new NullPointerException("Null siteInfoCfg");
        }
        basicSetup();
        init();
        siteInfoCfg.frameSetVisible("editorGUI", true);       
        siteInfoCfg.framePack("editorGUI");
    }

    //Basic setup
    private void basicSetup() {
        siteInfoCfg.buttonAddActionListener("save", new SaveActionListener());
        siteInfoCfg.buttonAddActionListener("close", new ExitActionListener());
        siteInfoCfg.textFieldAddDocumentListener("urlTF", new EditDocumentListener());
        siteInfoCfg.textFieldAddDocumentListener("nameTF", new EditDocumentListener());
        siteInfoCfg.frameAddWindowListener("editorGUI", new SiteInfoWindowListener());
    }
    //Initialize
    private void init() {
        File urlFile = new File(urlPath);
        if (urlFile != null && urlFile.isFile()) {
            try {
                String line = FileUtilities.readLine(urlPath, "UTF-8");
                siteInfoCfg.textFieldSetText("urlTF", line);
            } catch (Exception ex) {
                System.out.println("Error loading url from " + urlPath);
                ex.printStackTrace();
            }
        }
        
        File nameFile = new File(namePath);
        if (nameFile != null && nameFile.isFile()) {
            try {
                String line = FileUtilities.readLine(namePath, "UTF-8");
                siteInfoCfg.textFieldSetText("nameTF", line);
            } catch (Exception ex) {
                System.out.println("Error loading name from " + namePath);
                ex.printStackTrace();
            }
        }
    }
    //Window Listener
    private class SiteInfoWindowListener extends WindowAdapter {
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
        public void actionPerformed(ActionEvent e) {
            exit();
        }
    }
    //Save Listener
    private class SaveActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            save();
            reloadMain.execute(null);
            siteInfoCfg.frameToFront("editorGUI");
        }
    }
    //Edit Listener
    private class EditDocumentListener implements DocumentListener {
        @Override
        public void changedUpdate(DocumentEvent event) {
            siteInfoCfg.buttonSetEnabled("save", true);
        }
        public void insertUpdate(DocumentEvent event) {
            siteInfoCfg.buttonSetEnabled("save", true);
        }
        public void removeUpdate(DocumentEvent event) {
            siteInfoCfg.buttonSetEnabled("save", true);
        }
    }
    //Save the url and website name
    private void save() {
        save(urlPath, "urlTF");
        save(namePath, "nameTF");
        siteInfoCfg.buttonSetEnabled("save", false);
    }
    //save textfield content into file
    private void save(String filePath, String fileTFID) {
        File f = new File(filePath);
        if (f.exists() && !f.isFile()) {
            throw new IllegalArgumentException("Is already a folder: " + filePath);
        }
        if (!f.exists()) {
            File dir = new File(f.getParent());
            if (dir != null & !dir.exists()) {
                dir.mkdirs();
            }
        }
        String info = siteInfoCfg.textFieldGetText(fileTFID);
        FileUtilities.write(filePath, info, "UTF-8");
    }
    //Exit
    private void exit() {
        siteInfoCfg.frameDispatchEvent("editorGUI", WindowEvent.WINDOW_CLOSING);
    }
    
    public static void main(String[] args) {
        /*SiteInfoCfgController scc = null;
        try {
            scc = new SiteInfoCfgController("url.path", "name.path", null);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        scc.start();
        */
    }
}