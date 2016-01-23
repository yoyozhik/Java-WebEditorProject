/* GUI for uploading a file */
/* Author: Wei Zhang
   Latest Version: 2016 Jan 17
*/
/*API
public class FileUploader {
    public FileUploader()
    public static void main(String[] args)
}
*/
package views;
import controllers.*;
import models.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class FileUploader extends WebEditorGUIGeneric {
    //GUI items
    protected JFrame uploaderGUI;
    protected RegJButton select;
    protected RegJButton upload;
    protected RegJButton close;
    protected JLabel uploadDestLb;
    protected JLabel sourceLb;
    protected JTextField fileNameTF;
    protected JLabel statusLb;

    public FileUploader() {
        setupGUI();
        HashMap<String, RegJButton> upButtonGroup = new HashMap<String, RegJButton>();
        HashMap<String, JTextField> upTextFieldGroup = new HashMap<String, JTextField>();
        HashMap<String, JLabel> upLabelGroup = new HashMap<String, JLabel>();
        HashMap<String, JFrame> upFrameGroup = new HashMap<String, JFrame>();
        
        upButtonGroup.put("select".toLowerCase(), select);
        upButtonGroup.put("upload".toLowerCase(), upload);
        upButtonGroup.put("close".toLowerCase(), close);
        
        upTextFieldGroup.put("fileNameTF".toLowerCase(), fileNameTF);
        
        upLabelGroup.put("uploadDestLb".toLowerCase(), uploadDestLb);
        upLabelGroup.put("sourceLb".toLowerCase(), sourceLb);
        upLabelGroup.put("statusLb".toLowerCase(), statusLb);
        
        upFrameGroup.put("uploaderGUI".toLowerCase(), uploaderGUI);
        
        initGUIObjsButton(upButtonGroup);
        initGUIObjsLabel(upLabelGroup);
        initGUIObjsTextField(upTextFieldGroup);
        initGUIObjsFrame(upFrameGroup);

    }
    
    protected void setupGUI() {
        JFrame uploaderGUI = new JFrame();
        this.uploaderGUI = uploaderGUI;
        
        RegJPanel uploaderPanel = new RegJPanel();
        uploaderGUI.add(BorderLayout.CENTER, uploaderPanel);
        uploaderPanel.setLayout(new BoxLayout(uploaderPanel, BoxLayout.Y_AXIS));
        
        RegJPanel infoPanel = new RegJPanel();
        RegJPanel selectPanel = new RegJPanel();
        RegJPanel fileNamePanel = new RegJPanel();
        //RegJPanel widthPanel = new RegJPanel();
        //RegJPanel heightPanel = new RegJPanel();
        RegJPanel opsPanel = new RegJPanel();
        RegJPanel statusPanel = new RegJPanel();
        infoPanel.setLayout(new BorderLayout());
        selectPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        fileNamePanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        statusPanel.setLayout(new BorderLayout());
        //widthPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        //heightPanel.setLayout(new FlowLayout(FlowLayout.LEADING));        
        //opsPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        
        uploaderPanel.add(infoPanel);
        uploaderPanel.add(selectPanel);
        uploaderPanel.add(fileNamePanel);
        //uploaderPanel.add(widthPanel);
        //uploaderPanel.add(heightPanel);
        uploaderPanel.add(opsPanel);
        uploaderPanel.add(statusPanel);
        
        //Info
        RegJPanel lbInfoPanel = new RegJPanel();
        JLabel uploadDestLbInfo = new JLabel("Upload Destination: ");
        RegJPanel lbPanel = new RegJPanel();
        JLabel uploadDestLb = new JLabel("Unspecified");
        lbInfoPanel.add(uploadDestLbInfo);
        lbPanel.add(uploadDestLb);
        infoPanel.add(BorderLayout.WEST, lbInfoPanel);
        infoPanel.add(BorderLayout.CENTER, lbPanel);
        this.uploadDestLb = uploadDestLb;
        
        //Select
        RegJButton select = new RegJButton("Select");
        JLabel sourceLb = new JLabel("Unspecified");
        selectPanel.add(select);
        selectPanel.add(sourceLb);
        this.select = select;
        this.sourceLb = sourceLb;
        
        //FileName
        JLabel fileNameLbInfo = new JLabel("File Name: ");
        JTextField fileNameTF = new JTextField(30);
        fileNamePanel.add(fileNameLbInfo);
        fileNamePanel.add(fileNameTF);
        this.fileNameTF = fileNameTF;
                
        //Ops
        RegJButton upload = new RegJButton("Upload");
        RegJButton close = new RegJButton("Close");
        opsPanel.add(upload);
        opsPanel.add(close);
        this.upload = upload;
        this.close = close;
        
        //Status
        JLabel statusLbInfo = new JLabel("Status: ");
        JLabel statusLb = new JLabel("Ready");
        statusPanel.add(BorderLayout.WEST, statusLbInfo);
        statusPanel.add(BorderLayout.CENTER, statusLb);
        this.statusLb = statusLb;
        
        //GUI
        uploaderGUI.setSize(500, 400);
       
    }    
}