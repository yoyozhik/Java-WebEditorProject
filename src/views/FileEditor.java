/* GUI for editing specified file */
/* Author: Wei Zhang
   Latest Version: 2016 Jan 17
*/
/*API
public class FileEditor {
    public FileEditor()
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

public class FileEditor extends WebEditorGUIGeneric {
    //GUI items
    private JFrame editorGUI;
    private JLabel pathLb;
    private RegJTextArea contentTA;
    private RegJButton save;
    private RegJButton close;
    
    public FileEditor() {
        setupGUI();
        HashMap<String, RegJButton> feButtonGroup = new HashMap<String, RegJButton>();
        HashMap<String, RegJTextArea> feTextAreaGroup = new HashMap<String, RegJTextArea>();
        HashMap<String, JLabel> feLabelGroup = new HashMap<String, JLabel>();
        HashMap<String, JFrame> feFrameGroup = new HashMap<String, JFrame>();
        
        feButtonGroup.put("save".toLowerCase(), save);
        feButtonGroup.put("close".toLowerCase(), close);
        
        feTextAreaGroup.put("contentTA".toLowerCase(), contentTA);
        
        feLabelGroup.put("pathLb".toLowerCase(), pathLb);
        
        feFrameGroup.put("editorGUI".toLowerCase(), editorGUI);
        
        initGUIObjsButton(feButtonGroup);
        initGUIObjsLabel(feLabelGroup);
        initGUIObjsTextArea(feTextAreaGroup);
        initGUIObjsFrame(feFrameGroup);

    }
    
    private void setupGUI() {
        JFrame editorGUI = new JFrame();
        this.editorGUI = editorGUI;
        //add component panels
        RegJPanel banner = new RegJPanel();
        RegJPanel center = new RegJPanel();
        RegJPanel bot = new RegJPanel();
        editorGUI.getContentPane().add(BorderLayout.NORTH, banner);
        editorGUI.getContentPane().add(BorderLayout.CENTER, center);
        editorGUI.getContentPane().add(BorderLayout.SOUTH, bot);
        
        //Add banner item
        JLabel leadingLb = new JLabel("File Path: ");
        JLabel pathLb = new JLabel();
        banner.add(leadingLb);
        banner.add(pathLb);
        this.pathLb = pathLb;
        
        //Add center item
        center.setLayout(new BorderLayout());
        RegJTextArea contentTA = new RegJTextArea();
        contentTA.setMargin(new Insets(10,10,10,10));
        //contentTA.getDocument().addDocumentListener(new EditDocumentListener());
        this.contentTA = contentTA;
        JScrollPane scroll = new JScrollPane(contentTA);
        scroll.setVerticalScrollBarPolicy(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        center.add(BorderLayout.CENTER, scroll);
        
        //Add bot item
        RegJButton save = new RegJButton(" Save ");
        RegJButton close = new RegJButton("Close");
        bot.add(save);
        bot.add(close);
        //save.addActionListener(new SaveActionListener());
        //close.addActionListener(new ExitActionListener());
        //save.setEnabled(false);
        this.save = save;
        this.close = close;
        //show GUI
        editorGUI.setSize(700, 500);
        //editorGUI.addWindowListener(new FileEditorWindowListener());
    }    
}