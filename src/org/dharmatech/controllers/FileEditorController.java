/* Controller for editing specified file */
/* Author: Wei Zhang
   Latest Version: 2016 Jan 17
*/
/* The controller of GUI FileEditor */
/* Handles all plain text handling, including:
   Title, Paragraph, Code,
   Framework, Stylesheet */
/*API
public class FileEditorController {
    public FileEditorController(String filePath) {}
    public void start() {}
    public static void main(String[] args) {}
}
*/
package org.dharmatech.controllers;
import org.dharmatech.views.*;
import org.dharmatech.models.*;
import org.dharmatech.utilities.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class FileEditorController {
    private FileEditor fileEditor; //GUI
    
    //Optional 
    private WebModuleEnum typeEnum = null;
    private DesignInfoSet designInfoSet = null;
    private String pageName = null;
    private int id = 0;
    //parameters
    private final String filePath;
    //Constructor with the target file path
    //To be used by framework/stylesheet editor
    public FileEditorController(String filePath) throws IOException {
        if (filePath == null) {
            throw new NullPointerException("Null filePath");
        }
        File f = new File(filePath);
        if (f.exists() && !f.isFile()) {
            throw new IOException("Already a dir: " + filePath);
        }
        this.filePath = filePath;
        fileEditor = new FileEditor();
        
    }
    
    //Constructor with the target file path, a designInfoSet object,
    //page name, module id, and module type
    //To be used by module editor
    public FileEditorController(String filePath, 
        DesignInfoSet designInfoSet, 
        String pageName, int id, WebModuleEnum typeEnum) 
        throws IOException {
        this(filePath);
        if (designInfoSet == null) {
            throw new NullPointerException("Null designInfoSet");
        }
        if (typeEnum == null) {
            throw new NullPointerException("Null typeEnum");
        }
        if (pageName == null) {
            throw new NullPointerException("Null pageName");
        }
        this.typeEnum = typeEnum;
        checkTypeEnum(this.typeEnum);
        this.designInfoSet = new DesignInfoSet(designInfoSet);
        this.pageName = pageName;
        this.id = id;
    }
    
    private void checkTypeEnum(WebModuleEnum typeEnum) {
        switch (typeEnum) {
            case TITLE:
            case PARAGRAPH:
            case CODE:
                break;
            default:
                throw new IllegalArgumentException("Type not Title/Paragraph/Code: "
                    + typeEnum.getValue());
        }
    }
    //Start file editor
    public void start() {
        if (fileEditor == null) {
            throw new NullPointerException("Null fileEditor");
        }
        basicSetup();
        init();
        fileEditor.frameSetVisible("editorGUI", true);
        fileEditor.textAreaEnableUndo("contentTA");
        fileEditor.textAreaSetLineWrap("contentTA", true);

        fileEditor.textAreaSetCaretPosition("contentTA", 0);
        fileEditor.textAreaRequestFocus("contentTA");
    }
    
    //Basic setup
    private void basicSetup() {
        fileEditor.buttonAddActionListener("save", new SaveActionListener());
        fileEditor.buttonAddActionListener("close", new ExitActionListener());
        fileEditor.textAreaAddDocumentListener("contentTA", new EditDocumentListener());
        fileEditor.frameAddWindowListener("editorGUI", new FileEditorWindowListener());
    }
    //Initialize
    private void init() {
        fileEditor.labelSetText("pathLb", filePath);
        String line = FileUtilities.read(filePath, "UTF-8");
        if (line != null) {
            line = UnicodeConvert.toChars(line);
            fileEditor.textAreaSetText("contentTA", line);
        }
        fileEditor.buttonSetEnabled("save", false);
    }
   
    //Listeners
    //Save Listener
    private class SaveActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            save();
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
    private class FileEditorWindowListener extends WindowAdapter {
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
            fileEditor.buttonSetEnabled("save", true);
        }
        public void insertUpdate(DocumentEvent event) {
            fileEditor.buttonSetEnabled("save", true);
        }
        public void removeUpdate(DocumentEvent event) {
            fileEditor.buttonSetEnabled("save", true);
        }
    }
    
    //Save function
    private void save() {
        if (typeEnum == null) {  //Regular plain text
            FileUtilities.write(filePath, 
                fileEditor.textAreaGetText("contentTA"), "UTF-8");
        } else {
            WebModuleDefault module = null;
            module = WebModule.createWebModule(new DesignInfoSet(designInfoSet), 
                pageName, id, typeEnum);
            /*switch (typeEnum) {
                case TITLE:
                    module = new WebModuleTitle(new DesignInfoSet(designInfoSet), 
                        pageName, id);
                    break;
                case PARAGRAPH:
                    module = new WebModuleParagraph(new DesignInfoSet(designInfoSet), 
                        pageName, id);
                    break;
                case CODE:
                    module = new WebModuleCode(new DesignInfoSet(designInfoSet), 
                        pageName, id);
                    break;
                default:
                    throw new IllegalArgumentException("Type not Title/Paragraph/Code: "
                        + typeEnum.getValue());
            }
            */
            FileUtilities.write(filePath, 
                module.genRecord(fileEditor.textAreaGetText("contentTA")),
                "UTF-8");
        }
        fileEditor.buttonSetEnabled("save", false);
    }
    //Exit function
    private void close() {
        fileEditor.frameDispatchEvent("editorGUI", WindowEvent.WINDOW_CLOSING);        
    }
    
    public static void main(String[] args) {
        File f = new File("./cfg/hello.java");
        String s = f.getParent();
        System.out.println(s);
    }
}