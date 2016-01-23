/* GUI for uploading an image */
/* Author: Wei Zhang
   Latest Version: 2016 Jan 19
*/
/*API
public class ImageUploader {
    public ImageUploader()
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

public class ImageUploader extends FileUploader {
    //GUI items
    private JTextField widthTF;
    private JTextField heightTF;

    public ImageUploader() {
        super();
        
        addGUIObjsTextField("widthTF".toLowerCase(), widthTF);
        addGUIObjsTextField("heightTF".toLowerCase(), heightTF);        
    }
    
    @Override
    protected void setupGUI() {
        JFrame uploaderGUI = new JFrame();
        this.uploaderGUI = uploaderGUI;
        
        RegJPanel uploaderPanel = new RegJPanel();
        uploaderGUI.add(BorderLayout.CENTER, uploaderPanel);
        uploaderPanel.setLayout(new BoxLayout(uploaderPanel, BoxLayout.Y_AXIS));
        
        RegJPanel infoPanel = new RegJPanel();
        RegJPanel selectPanel = new RegJPanel();
        RegJPanel fileNamePanel = new RegJPanel();
        RegJPanel opsPanel = new RegJPanel();
        RegJPanel statusPanel = new RegJPanel();
        infoPanel.setLayout(new BorderLayout());
        selectPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        fileNamePanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        
        //Width & Height added
        RegJPanel widthPanel = new RegJPanel();
        RegJPanel heightPanel = new RegJPanel();
        widthPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        heightPanel.setLayout(new FlowLayout(FlowLayout.LEADING));        
        opsPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        statusPanel.setLayout(new BorderLayout());
        
        uploaderPanel.add(infoPanel);
        uploaderPanel.add(selectPanel);
        uploaderPanel.add(fileNamePanel);
        uploaderPanel.add(widthPanel);
        uploaderPanel.add(heightPanel);
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
        
        //Width
        JLabel widthLbInfo = new JLabel("Width: ");
        JTextField widthTF = new JTextField(10);
        widthPanel.add(widthLbInfo);
        widthPanel.add(widthTF);
        this.widthTF = widthTF;
        
        //Height
        JLabel heightLbInfo = new JLabel("Height: ");
        JTextField heightTF = new JTextField(10);
        heightPanel.add(heightLbInfo);
        heightPanel.add(heightTF);
        this.heightTF = heightTF;
        
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