/* Site Info Setup */
/* API
public class SiteInfoCfgController {
    public SiteInfoCfgController(String urlPath, String namePath)
    public void start()
}
*/
package controllers;
import views.*;
import utilities.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class SiteInfoCfgController {
    private SiteInfoCfg siteInfoCfg;
    private final String urlPath;
    private final String namePath;
    private Command reloadMain;
    
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

    private class SiteInfoWindowListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent we) {
            //if (webEditorGUI != null) {
            //    webEditorGUI.setEnabled(true);
            //}
        }
    }
    
    private class SaveActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            save();
            reloadMain.execute(null);
        }
    }
    
    private class ExitActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            exit();
        }
    }
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

    private void save() {
        save(urlPath, "urlTF");
        save(namePath, "nameTF");
        siteInfoCfg.buttonSetEnabled("save", false);
    }
    
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
    
    private void exit() {
        siteInfoCfg.frameDispatchEvent("editorGUI", WindowEvent.WINDOW_CLOSING);
    }
    
}