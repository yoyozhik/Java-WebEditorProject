/* Site Info Setup GUI */
/* API
public class SiteInfoCfg {
    public SiteInfoCfg()
    public static void main(String[] args)
}
*/
package org.dharmatech.views;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class SiteInfoCfg extends WebEditorGUIGeneric {
    private JFrame editorGUI;
    private RegJButton save;
    private RegJButton close;
    private JTextField urlTF;
    private JTextField nameTF;
    
    public SiteInfoCfg() {
        setupGUI();
        HashMap<String, RegJButton> siButtonGroup = new HashMap<String, RegJButton>();
        HashMap<String, JTextField> siTextFieldGroup = new HashMap<String, JTextField>();
        //HashMap<String, JLabel> siLabelGroup = new HashMap<String, JLabel>();
        HashMap<String, JFrame> siFrameGroup = new HashMap<String, JFrame>();
        //HashMap<String, PagesJTable> siTableGroup = new HashMap<String, PagesJTable>();
        
        siButtonGroup.put("save".toLowerCase(), save);
        siButtonGroup.put("close".toLowerCase(), close);
        
        siFrameGroup.put("editorGUI".toLowerCase(), editorGUI);
        
        siTextFieldGroup.put("urlTF".toLowerCase(), urlTF);
        siTextFieldGroup.put("nameTF".toLowerCase(), nameTF);
    
        initGUIObjsButton(siButtonGroup);
        //initGUIObjsLabel(siLabelGroup);
        initGUIObjsTextField(siTextFieldGroup);
        initGUIObjsFrame(siFrameGroup);
        //initGUIObjsTable(siTableGroup);
    }
    
    private void setupGUI() {
        JFrame editorGUI = new JFrame();
        this.editorGUI = editorGUI;
        RegJPanel top = new RegJPanel();
        RegJPanel bot = new RegJPanel();
        editorGUI.getContentPane().add(BorderLayout.NORTH, top);
        editorGUI.getContentPane().add(BorderLayout.CENTER, bot);
        
        //Content
        RegJPanel urlPanel = new RegJPanel();
        RegJPanel namePanel = new RegJPanel();
        top.setLayout(new BorderLayout());
        top.add(BorderLayout.NORTH, urlPanel);
        top.add(BorderLayout.SOUTH, namePanel);
        //URL
        urlPanel.setLayout(new BorderLayout());
        JLabel urlLb = new JLabel("Website URL  ");
        JTextField urlTF = new JTextField(30);
        urlPanel.add(BorderLayout.WEST, urlLb);
        urlPanel.add(BorderLayout.CENTER, urlTF);
        //Name
        namePanel.setLayout(new BorderLayout());
        JLabel nameLb = new JLabel("Website NAME ");
        JTextField nameTF = new JTextField(30);
        namePanel.add(BorderLayout.WEST, nameLb);
        namePanel.add(BorderLayout.CENTER, nameTF);
        this.urlTF = urlTF;
        this.nameTF = nameTF;
        
        //Buttons
        bot.setLayout(new FlowLayout(FlowLayout.LEADING, 20, 0));
        RegJButton save = new RegJButton("Save");
        RegJButton close = new RegJButton("Close");
        bot.add(save);
        bot.add(close);
        this.save = save;
        this.close = close;
        
        //GUI
        editorGUI.setSize(700, 300);
        
    }
    
}