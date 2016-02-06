/* Main central class of WebEditor */
/* Author: Wei Zhang
   Latest Version: 2016 Jan 07
*/
/* API
class WebEditor {
    public WebEditor()
    public static void main(String[] args)
}
*/

package org.dharmatech.views;
import org.dharmatech.controllers.*;
import org.dharmatech.models.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.util.HashMap.*;

public class WebEditor extends WebEditorGUIGeneric{
    //GUI items
    private JFrame webEditorGUI;
    
    private RegJTextArea contentTA;
    
    private JLabel statusLb;
    private JLabel rootDirLb;
    
    private JComboBox<String> pagesBox;
    
    private RegJButton selRoot;
    private RegJButton edit;
    private RegJButton delete;
    private RegJButton rename;
    private RegJButton addTitle;
    private RegJButton addParagraph;
    private RegJButton addCode;
    private RegJButton addFile;
    private RegJButton addImage;
    private RegJButton addGallery;
    private RegJButton addDivider;
    private RegJButton save;
    private RegJButton compile;
    
    private JMenuItem frameworkItem;
    private JMenuItem styleItem;
    private JMenuItem mobileFrameworkItem;
    private JMenuItem mobileStyleItem;
    private JMenuItem mobileOptItem;
    private JMenuItem pageItem;
    private JMenuItem infoItem;
    
    
    
    
    //Configurations
    private static final String resourceDirRel = "Resources";
    private static final String websiteDirRel = "Website";
    private static final String rootCfgRel = "cfg" + File.separator + "Root.cfg";
    private static final String lastOpenCfgRel = "cfg" + File.separator + "Last_OpenDir.txt";
    private String pagesCfg;
    private String frameworkCfg;
    private String mobileFrameworkCfg;
    private String websiteNameCfg;
    private String websiteURLCfg;
    private String stylesheet;
    private String mobileStylesheet;
    
    
    //Data
    private ArrayList<String> pages = new ArrayList<String>();
    private ArrayList<Boolean> pagesNav = new ArrayList<Boolean>();
    private String lastOpen;
    
    public WebEditor() {
        setupGUI();
        HashMap<String, RegJButton> weButtonGroup = new HashMap<String, RegJButton>();
        HashMap<String, RegJTextArea> weTextAreaGroup = new HashMap<String, RegJTextArea>();
        HashMap<String, JLabel> weLabelGroup = new HashMap<String, JLabel>();
        HashMap<String, JComboBox<String>> weComboBoxStrGroup = new HashMap<String, JComboBox<String>>();
        HashMap<String, JFrame> weFrameGroup = new HashMap<String, JFrame>();
        HashMap<String, JMenuItem> weMenuItemGroup = new HashMap<String, JMenuItem>();
        
        weButtonGroup.put("selRoot".toLowerCase(), selRoot);
        weButtonGroup.put("edit".toLowerCase(), edit);
        weButtonGroup.put("delete".toLowerCase(), delete);
        weButtonGroup.put("rename".toLowerCase(), rename);
        weButtonGroup.put("addTitle".toLowerCase(), addTitle);
        weButtonGroup.put("addParagraph".toLowerCase(), addParagraph);
        weButtonGroup.put("addCode".toLowerCase(), addCode);
        weButtonGroup.put("addFile".toLowerCase(), addFile);
        weButtonGroup.put("addImage".toLowerCase(), addImage);
        weButtonGroup.put("addGallery".toLowerCase(), addGallery);
        weButtonGroup.put("addDivider".toLowerCase(), addDivider);
        weButtonGroup.put("save".toLowerCase(), save);
        weButtonGroup.put("compile".toLowerCase(), compile);
        
        weTextAreaGroup.put("contentTA".toLowerCase(), contentTA);
        
        weLabelGroup.put("statusLb".toLowerCase(), statusLb);
        weLabelGroup.put("rootDirLb".toLowerCase(), rootDirLb);
        
        weComboBoxStrGroup.put("pagesBox".toLowerCase(), pagesBox);
        
        weFrameGroup.put("webEditorGUI".toLowerCase(), webEditorGUI);
        
        weMenuItemGroup.put("frameworkItem".toLowerCase(), frameworkItem);
        weMenuItemGroup.put("styleItem".toLowerCase(), styleItem);
        weMenuItemGroup.put("mobileFrameworkItem".toLowerCase(), mobileFrameworkItem);
        weMenuItemGroup.put("mobileStyleItem".toLowerCase(), mobileStyleItem);
        weMenuItemGroup.put("mobileOptItem".toLowerCase(), mobileOptItem);
        weMenuItemGroup.put("pageItem".toLowerCase(), pageItem);
        weMenuItemGroup.put("infoItem".toLowerCase(), infoItem);
        
        initGUIObjsButton(weButtonGroup);
        initGUIObjsLabel(weLabelGroup);
        initGUIObjsTextArea(weTextAreaGroup);
        initGUIObjsComboBoxStr(weComboBoxStrGroup);
        initGUIObjsFrame(weFrameGroup);
        initGUIObjsMenuItem(weMenuItemGroup);
    }
    
   
    private void setupGUI() {
        //Initialize main frame
        JFrame webEditorGUI = new JFrame();
        this.webEditorGUI = webEditorGUI;
        //Replace frame content pane with a custom one
        //RegJPanel mainPanel = new RegJPanel();
        //webEditorGUI.setContentPane(mainPanel);
        //Add component panels
        RegJPanel banner = new RegJPanel(); //panel for banner items
        RegJPanel east = new RegJPanel(); //panel for operation buttons 
        RegJPanel west = new RegJPanel(); //panel for content textarea
        webEditorGUI.getContentPane().add(BorderLayout.NORTH, banner);
        webEditorGUI.getContentPane().add(BorderLayout.EAST, east);
        webEditorGUI.getContentPane().add(BorderLayout.CENTER, west);
        
        //Add banner buttons
        banner.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 5));
        RegJButton selRoot = new RegJButton("Design Root");
        banner.add(selRoot);
        JLabel rootDir = new JLabel("Root Dir unspecified");
        rootDirLb = rootDir;
        banner.add(rootDir);
        this.selRoot = selRoot;
        //selRoot.addActionListener(new SelRootDirActionListener());
        
        //Add op buttons
        RegJPanel opsEdit = new RegJPanel();
        RegJPanel opsInsert = new RegJPanel();
        east.add(opsEdit);
        east.add(opsInsert);
        east.setLayout(new BoxLayout(east, BoxLayout.Y_AXIS));
        //Add button groups
        //opsEdit.setLayout(new GridLayout(3, 1, 10, 5));
        opsEdit.setLayout(new BoxLayout(opsEdit, BoxLayout.Y_AXIS));
        opsEdit.setBorder(new EmptyBorder(10,5,10,5));
        //opsInsert.setLayout(new GridLayout(7, 1, 10, 5));
        opsInsert.setLayout(new BoxLayout(opsInsert, BoxLayout.Y_AXIS));
        opsInsert.setBorder(new EmptyBorder(10,5,10,5));
        RegJButton edit = new RegJButton("Edit");
        RegJButton rename = new RegJButton("Rename");
        RegJButton delete = new RegJButton("Delete");
        RegJButton addTitle = new RegJButton("Add Title");
        RegJButton addParagraph = new RegJButton("Add Paragraph");
        RegJButton addCode = new RegJButton("Add Code");
        RegJButton addFile = new RegJButton("Add File");
        RegJButton addImage = new RegJButton("Add Image");
        RegJButton addGallery = new RegJButton("Add Gallery");
        RegJButton addDivider = new RegJButton("Add Divider");
        Dimension opsButMax = addParagraph.getMaximumSize();
        edit.setMaximumSize(opsButMax);
        rename.setMaximumSize(opsButMax);
        delete.setMaximumSize(opsButMax);
        addTitle.setMaximumSize(opsButMax);
        addParagraph.setMaximumSize(opsButMax);
        addCode.setMaximumSize(opsButMax);
        addFile.setMaximumSize(opsButMax);
        addImage.setMaximumSize(opsButMax);
        addGallery.setMaximumSize(opsButMax);
        addDivider.setMaximumSize(opsButMax);
        
        this.edit = edit;
        this.delete = delete;
        this.rename = rename;
        this.addTitle = addTitle;
        this.addParagraph = addParagraph;
        this.addCode = addCode;
        this.addFile = addFile;
        this.addImage = addImage;
        this.addGallery = addGallery;
        this.addDivider = addDivider;
        
        
        Dimension gap = new Dimension(0,5);
        Box.createRigidArea(gap);
        //Edit group
        opsEdit.add(edit);
        opsEdit.add(Box.createRigidArea(gap));
        opsEdit.add(rename);
        opsEdit.add(Box.createRigidArea(gap));
        opsEdit.add(delete);
        //Insert group
        opsInsert.add(addTitle);
        opsInsert.add(Box.createRigidArea(gap));
        opsInsert.add(addParagraph);
        opsInsert.add(Box.createRigidArea(gap));
        opsInsert.add(addCode);
        opsInsert.add(Box.createRigidArea(gap));
        opsInsert.add(addFile);
        opsInsert.add(Box.createRigidArea(gap));
        opsInsert.add(addImage);
        opsInsert.add(Box.createRigidArea(gap));
        opsInsert.add(addGallery);
        opsInsert.add(Box.createRigidArea(gap));
        opsInsert.add(addDivider);        
        
        //Add center content
        //RegJTextArea content = new RegJTextArea(20, 40);
        west.setLayout(new BorderLayout());
        west.setBorder(new EmptyBorder(5,10,5,10));
        //Page selection
        RegJPanel westTop = new RegJPanel();
        westTop.setLayout(new BorderLayout());
        west.add(BorderLayout.NORTH, westTop);
        JComboBox<String> pagesBox = new JComboBox<String>();
        westTop.add(pagesBox);
        this.pagesBox = pagesBox;
        //pagesBox.addActionListener(new PagesBoxListener());
        //Textarea
        RegJPanel westCenter = new RegJPanel();
        westCenter.setLayout(new BorderLayout());
        westCenter.setBorder(new EmptyBorder(10, 0, 10, 0));
        west.add(BorderLayout.CENTER, westCenter);
        RegJTextArea contentTA = new PageContentJTextArea(); //Extends RegJTextArea
        this.contentTA = contentTA;
        contentTA.setLineWrap(true);
        contentTA.setMargin(new Insets(10,10,10,10));
        //Only to trigger the tooltip behavior!
        //Otherwise, it seems the tooltip does not show up
        contentTA.setToolTipText(""); 
        //content.getDocument().addDocumentListener(new EditDocumentListener());
        JScrollPane contentScroll = new JScrollPane(contentTA);
        contentScroll.setVerticalScrollBarPolicy(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        contentScroll.setHorizontalScrollBarPolicy(
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        westCenter.add(contentScroll);
        //Add bot content
        //bot.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 5));
        RegJPanel westBot = new RegJPanel(); //panel for bottom items
        west.add(BorderLayout.SOUTH, westBot);        
        //westBot.setLayout(new BorderLayout());
        westBot.setLayout(new FlowLayout(FlowLayout.LEADING));
        westBot.setBorder(new EmptyBorder(10,10,10,10));
        //Save & Compile
        RegJButton save = new RegJButton("Save");
        RegJButton compile = new RegJButton("Compile");
        //westBot.add(BorderLayout.WEST, save);
        //westBot.add(BorderLayout.EAST, compile);
        westBot.add(save);
        westBot.add(compile);
        this.save = save;
        this.compile = compile;

        save.setEnabled(false);
        //Status
        RegJPanel statusPanel = new RegJPanel();
        statusPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        westBot.add(BorderLayout.SOUTH, statusPanel);
        JLabel status = new JLabel("Status: ");
        statusPanel.add(status);
        statusLb = status;

        //Add menu items
        JMenuBar menuBar = new JMenuBar();
        //Main
        JMenu mainMenu = new JMenu("Main Setup");
        JMenuItem frameworkItem = new JMenuItem("Framework");
        JMenuItem styleItem = new JMenuItem("Stylesheet");
        mainMenu.add(frameworkItem);
        mainMenu.add(styleItem);
        //frameworkItem.addActionListener(new FrameworkActionListener());
        //styleItem.addActionListener(new StylesheetActionListener());
        //Mobile
        JMenu mobileMenu = new JMenu("Mobile Setup");
        JMenuItem mobileFrameworkItem = new JMenuItem("Framework");
        JMenuItem mobileStyleItem = new JMenuItem("Stylesheet");
        JMenuItem mobileOptItem = new JMenuItem("Extra Optimization");
        mobileMenu.add(mobileFrameworkItem);
        mobileMenu.add(mobileStyleItem);
        mobileMenu.add(mobileOptItem);
        //mobileFrameworkItem.addActionListener(new MobileFrameworkActionListener());
        //mobileStyleItem.addActionListener(new MobileStylesheetActionListener());
        //Pages
        JMenu pageMenu = new JMenu("Site Config");
        JMenuItem pageItem = new JMenuItem("Pages");
        JMenuItem infoItem = new JMenuItem("Site Info");
        pageMenu.add(pageItem);
        pageMenu.add(infoItem);
        //pageItem.addActionListener(new PagesActionListener());
        //infoItem.addActionListener(new SiteInfoActionListener());
        //Add to menu
        menuBar.add(mainMenu);
        menuBar.add(mobileMenu);
        menuBar.add(pageMenu);
        webEditorGUI.setJMenuBar(menuBar);
        this.frameworkItem = frameworkItem;
        this.styleItem = styleItem;
        this.mobileFrameworkItem = mobileFrameworkItem;
        this.mobileStyleItem = mobileStyleItem;
        this.mobileOptItem = mobileOptItem;
        this.pageItem = pageItem;
        this.infoItem = infoItem;
        
        //initialize and show GUI
        //init();
        webEditorGUI.setSize(700, 500);
        //
        //webEditorGUI.setVisible(true);
        //content.requestFocus();
    }
    
    //Initiliaze GUI defaults
    private void init() {
        statusLb.setText("Status: Ready");
    }    
        
    //General information feedback
           
    
}