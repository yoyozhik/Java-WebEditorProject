/* WebEditor Controller */
/* Author: Wei Zhang
   Latest Version: 2016 Jan 26
*/
/* The main controller of the entire WebEditor Project*/
/* The controller of GUI WebEditor */
/* API
class WebEditorController {
    public WebEditorController() {}
    public void start() {}
    public static void main(String[] args) {}
}
*/
/* Model-View-Controller Pattern */
/*
Windows Example
Compile: javac org\\dharmatech\\views\\*.java org\\dharmatech\\controllers\\*.java org\\dharmatech\\models\\*.java org\\dharmatech\\utilities\\*.java -d ..\\class
Execute: java org.dharmatech.controllers.WebEditorController
*/

package org.dharmatech.controllers;
import org.dharmatech.views.*;
import org.dharmatech.models.*;
import org.dharmatech.utilities.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class WebEditorController {
    private WebEditor webEditor;
    private String rootDir;
    private ArrayList<Page> pages = new ArrayList<Page>();
    private int currentPage = -1;
    private HashMap<String, String> designSet = new HashMap<String, String>();
    private JFileChooser fileOpen;
    
    //private FileEditorController fileEditorController;
    
    //Configurations
    private static final String resourceDirRel = "Resources";
    private static final String websiteDirRel = "Website";
    private static final String rootCfgRel = "Design-Resources" + File.separator 
        + "cfg" + File.separator + "Root.cfg";
    private static final String imagesResourcesRel = "images-resource";
    private static final String imagesResourcesDesignResourceRel = "Design-Resources" 
        + File.separator + imagesResourcesRel;
    private static final String uploadsResourcesRel = "uploads";
    private static final String lastOpenCfgRel = "cfg" + File.separator + "Last_OpenDir.txt";
    private String pagesCfgTxt;
    private String frameworkCfg;
    private String mobileFrameworkCfg;
    private String websiteNameCfg;
    private String websiteURLCfg;
    private String stylesheet;
    private String mobileStylesheet;
    private String mobileOptCfg;
    
    //constructor, setting constant string values
    public WebEditorController() {
        webEditor = new WebEditor();
        designSet.put("resourceDirRel", resourceDirRel);
        designSet.put("websiteDirRel", websiteDirRel);
        designSet.put("imagesResourcesRel", imagesResourcesRel);
        designSet.put("imagesResourcesDesignResourceRel", imagesResourcesDesignResourceRel);
        designSet.put("uploadsResourcesRel", uploadsResourcesRel);
        designSet.put("lastOpenCfgRel", lastOpenCfgRel);
        fileOpen = new JFileChooser();
    }
    
    //Initialize WebEditor GUI
    private void basicSetup() {
        webEditor.frameSetDefaultCloseOperation("webEditorGUI", 
            JFrame.EXIT_ON_CLOSE);
            
        webEditor.comboBoxStrAddActionListener("pagesBox", 
            new PagesBoxListener());
        
        webEditor.buttonAddActionListener("selRoot", 
            new SelectRootActionListener());
        webEditor.buttonAddActionListener("addTitle", 
            new InsertActionListener(WebModuleEnum.TITLE));
        webEditor.buttonAddActionListener("addParagraph", 
            new InsertActionListener(WebModuleEnum.PARAGRAPH));
        webEditor.buttonAddActionListener("addCode", 
            new InsertActionListener(WebModuleEnum.CODE));
        webEditor.buttonAddActionListener("addFile", 
            new InsertActionListener(WebModuleEnum.FILE));
        webEditor.buttonAddActionListener("addImage", 
            new InsertActionListener(WebModuleEnum.IMAGE));
        webEditor.buttonAddActionListener("addGallery", 
            new InsertActionListener(WebModuleEnum.GALLERY));
        webEditor.buttonAddActionListener("addDivider", 
            new InsertActionListener(WebModuleEnum.DIVIDER));
        webEditor.buttonAddActionListener("edit", 
            new EditActionListener());
        webEditor.buttonAddActionListener("delete", 
            new DeleteActionListener());
        webEditor.buttonAddActionListener("rename", 
            new RenameActionListener());
        webEditor.buttonAddActionListener("save", 
            new SaveActionListener());
        webEditor.buttonAddActionListener("compile", 
            new CompileActionListener());
        webEditor.textAreaAddDocumentListener("contentTA", new EditDocumentListener());

    }
    
    //Encapsulate reload operations to Command interface
    //so that PagesCfgController & SiteInfoCfgController can trigger reloading
    //without exposing the controller itself to those classes
    private class ReloadCfg implements Command {
        public void execute(Object data) 
        {
            webEditor.buttonClick("save");
            //init();
            loadPages();
            webEditor.labelSetText("statusLb", "Configuration reloaded.");
        }
    }
    
    //Initialize
    private void init() {
        //Initialize status
        menuItemsSetEnabledAll(false);
        
        webEditor.labelSetText("statusLb", "Initializing...");
        webEditor.labelSetText("rootDirLb", "<html><font color=\"red\">Root Dir Unspecified</font></html>");
        webEditor.textAreaSetText("contentTA", "");
        webEditor.textAreaEnableUndo("contentTA");
        webEditor.textAreaSetLineWrap("contentTA", true);
        webEditor.buttonSetEnabled("rename", false);
        
        //Load root
        boolean rootLoaded = initRoot();
        if (!rootLoaded) {  //Root configuration not successful; nothing to load further
            return;
        }        
        //Load pages
        loadPages();
        //Initialize menuItems
        initMenuItems();
        
        //Finish setup
        webEditor.labelSetText("statusLb", "Ready");
    }
    
    //Setup root; true means successful root configuration
    private boolean initRoot() {
        if (rootCfgRel == null) {
            throw new NullPointerException("Null rootCfgRel");
        }
        //Root
        String line = FileUtilities.read(rootCfgRel, "UTF-8");
        if (line != null) {
            line = line.trim();
            webEditor.labelSetText("rootDirLb", line);
            rootDir = line;
        } else {
            webEditor.labelSetText("statusLb", "<html><font color=\"red\">Please specify Design Root.</font></html>");
            return false;  //If no root, nothing to load
        }
        File f = new File(rootDir);
        if (f.exists() && !f.isFile()) {
            setupCfg();
        } else {
            webEditor.labelSetText("rootDirLb", "Unspecified");
            webEditor.labelSetText("statusLb", "<html><font color=\"red\">Please specify Design Root.</font></html>");
            rootDir = null;  //Do not accept illeagal root dir
            return false;
        }
        return true;
    }
    //Setup menuItems
    private void initMenuItems() {
        //Remove action listeners if they exist; 
        //otherwise they may end up with multiple action listener copies when we switch root
        webEditor.menuItemRemoveAllActionListeners("frameworkItem");
        webEditor.menuItemRemoveAllActionListeners("styleItem");
        webEditor.menuItemRemoveAllActionListeners("mobileFrameworkItem");
        webEditor.menuItemRemoveAllActionListeners("mobileStyleItem");
        webEditor.menuItemRemoveAllActionListeners("mobileOptItem");
        webEditor.menuItemRemoveAllActionListeners("pageItem");
        webEditor.menuItemRemoveAllActionListeners("infoItem");
        //Add action listeners
        webEditor.menuItemAddActionListener("frameworkItem", new MenuTemplateActionListener(frameworkCfg));
        webEditor.menuItemAddActionListener("styleItem", new MenuTemplateActionListener(stylesheet));
        webEditor.menuItemAddActionListener("mobileFrameworkItem", new MenuTemplateActionListener(mobileFrameworkCfg));
        webEditor.menuItemAddActionListener("mobileStyleItem", new MenuTemplateActionListener(mobileStylesheet));
        webEditor.menuItemAddActionListener("mobileOptItem", new MenuTemplateActionListener(mobileOptCfg));
        webEditor.menuItemAddActionListener("pageItem", new PageItemActionListener());
        webEditor.menuItemAddActionListener("infoItem", new InfoItemActionListener());
        menuItemsSetEnabledAll(true);
    }
    //Load pages
    private void loadPages() {
        if (rootDir == null) {
            throw new NullPointerException("Null rootDir");
        }
        if (pagesCfgTxt == null) {
            throw new NullPointerException("Null pagesCfgTxt");
        }
        String line = FileUtilities.read(pagesCfgTxt, "UTF-8");
        pages.clear();
        pages = new ArrayList<Page>();
        webEditor.comboBoxStrRemoveAllItems("pagesBox");
        StringBuilder allPageNames = new StringBuilder("");
        StringBuilder allPageTitles = new StringBuilder("");
        StringBuilder allDisplayInNav = new StringBuilder("");
        if (line != null) {
            //Create a defensive copy of designSet
            //HashMap<String, String> designSetCopy = new HashMap<String, String>(designSet);
            DesignInfoSet designInfoSet = new DesignInfoSet(designSet);
            String[] lines = line.split("\n");
            for(String sline: lines) {
                //Use advanced splitting to handle comma(,) in "", like "hello, world"
                String[] pars = FileUtilities.recordSplit(sline);
                if (pars.length >= 4) { //Parse each line to get each component
                    int level = Integer.parseInt(pars[0]);
                    //String pageName = pars[1].replace("\"", "");
                    //String pageTitle = pars[2].replace("\"", "");
                    String pageName = pars[1];
                    String pageTitle = pars[2];
                    //boolean displayInNav = Boolean.parseBoolean(pars[3].replace("\"", ""));
                    boolean displayInNav = Boolean.parseBoolean(pars[3]);
                    StringBuilder uns = new StringBuilder();
                    for (int l = 1; l < level; l++) {
                        uns.append("--");
                    }
                    String item = uns.toString() + pageName
                        + " (" + pageTitle + ")";
                    webEditor.comboBoxStrAddItem("pagesBox", item); 
                    allPageNames.append(pageName + "\n");
                    allPageTitles.append(pageTitle + "\n");
                    allDisplayInNav.append(Boolean.toString(displayInNav) + "\n");
                    pages.add(new Page(level, pageName, pageTitle, displayInNav, designInfoSet));
                }
            }
            webEditor.comboBoxStrSetSelectedIndex("pagesBox", 0);
        }
        designSet.put("allPageNames", new String(allPageNames));
        designSet.put("allPageTitles", new String(allPageTitles));
        designSet.put("allDisplayInNav", new String(allDisplayInNav));
        //Need to reset designSet copy
        //HashMap<String, String> designSetCopy = new HashMap<String, String>(designSet);
        DesignInfoSet designInfoSet = new DesignInfoSet(designSet);
        for (Page p : pages) {
            p.setDesignInfoSet(designInfoSet);
        }
    }
    //Setup config
    private void setupCfg() {
        menuItemsSetEnabledAll(false);
        if (rootDir == null) {
            throw new NullPointerException("Null rootDir");
        }
        File r = new File(rootDir);
        if (r.exists() && !r.isFile()) {
            pagesCfgTxt = rootDir + File.separator + resourceDirRel 
                + File.separator + "Pages.cfg.txt";
            frameworkCfg = rootDir + File.separator + resourceDirRel 
                + File.separator + "Framework.txt";
            mobileFrameworkCfg = rootDir + File.separator+ resourceDirRel
                + File.separator + "Framework-mobile.txt";
            websiteNameCfg = rootDir + File.separator + resourceDirRel 
                + File.separator + "Website_Name.txt";
            websiteURLCfg = rootDir + File.separator + resourceDirRel 
                + File.separator + "Website_URL.txt";
            stylesheet = rootDir + File.separator + websiteDirRel 
                + File.separator + "styles"  + File.separator + "stylesheet.css";
            mobileStylesheet = rootDir + File.separator + websiteDirRel
                + File.separator + "styles"  + File.separator + "stylesheet-mobile.css";
            mobileOptCfg = rootDir + File.separator + resourceDirRel
                + File.separator + "mobile_optExtra.txt";

            designSet.put("rootDir", rootDir);               
            designSet.put("pagesCfgTxt", pagesCfgTxt);
            designSet.put("frameworkCfg", frameworkCfg);
            designSet.put("mobileFrameworkCfg", mobileFrameworkCfg);
            designSet.put("websiteNameCfg", websiteNameCfg);
            designSet.put("websiteURLCfg", websiteURLCfg);
            designSet.put("stylesheet", stylesheet);
            designSet.put("mobileStylesheet", mobileStylesheet); 
            designSet.put("mobileOptCfg", mobileOptCfg); 
            //Need to reset designSet copy
            //HashMap<String, String> designSetCopy = new HashMap<String, String>(designSet);
            DesignInfoSet designInfoSet = new DesignInfoSet(designSet);
            for (Page p : pages) {
                p.setDesignInfoSet(designInfoSet);
            }
        } else {
            webEditor.labelSetText("rootDirLb", "Root Dir Unspecified");
        }
    }
    //Launch 
    public void start() {
        if (webEditor == null) {
            throw new NullPointerException("Null webEditor");
        }
        basicSetup();
        init();
        webEditor.frameSetVisible("webEditorGUI", true);
        webEditor.textAreaRequestFocus("contentTA");
    }
    
    /*
    Action Listeners
    */
    //JCombobox Listener
    private class PagesBoxListener implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent event) {
            if (pages.isEmpty()) {
                return;
            }
            int sel = webEditor.comboBoxStrGetSelectedIndex("pagesBox");
            currentPage = sel;
            String line = pages.get(sel).getPageContent();
            //String pagePath = rootDir + File.separator + resourceDirRel 
            //    + File.separator + (pages.get(sel)).getCfgPath();
            //String line = FileUtilities.read(pagePath, "UTF-8");
            webEditor.textAreaSetText("contentTA", line);
            webEditor.textAreaSetCaretPosition("contentTA", 0);
            webEditor.textAreaSetInfo("contentTA", new DesignInfoSet(designSet), pages.get(sel).getPageName());
            webEditor.buttonSetEnabled("save", false);
            webEditor.textAreaRequestFocus("contentTA");
        }
    }
    
    //Menu Listener Group
    //Framework/Stylesheet Listener
    private class MenuTemplateActionListener implements ActionListener {
        private String filePath;
        public MenuTemplateActionListener() {
        }
        public MenuTemplateActionListener(String filePath) {
            this.filePath = filePath;
        }
        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }
        @Override
        public void actionPerformed (ActionEvent event) {
            if (filePath == null) {
                throw new NullPointerException("Null filePath to menu Item");
            }
            FileEditorController fileEditorController = null;
            try {
                fileEditorController = new FileEditorController(filePath);
            } catch (IOException ex) {
                System.out.println("IOException when launching menu editor");
                ex.printStackTrace();
            } catch(Exception ex) {
                System.out.println("Exception when launching menu editor");
                ex.printStackTrace();
            }
            fileEditorController.start();
        }
    }
    //PageItem Listener
    private class PageItemActionListener implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent event) {            
            PagesCfgController pagesCfgController = null;
            try {
                pagesCfgController = new PagesCfgController(pagesCfgTxt, new ReloadCfg());
            } catch (IOException ex) {
                System.out.println("IOException when launching page config");
                ex.printStackTrace();
            } catch(Exception ex) {
                System.out.println("Exception when launching page config");
                ex.printStackTrace();
            }
            pagesCfgController.start();
        }
    }
    //InfoItem Listener
    private class InfoItemActionListener implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent event) {
            SiteInfoCfgController siteInfoCfgController = null;
            try {
                siteInfoCfgController = new SiteInfoCfgController(websiteURLCfg, websiteNameCfg, new ReloadCfg());
            } catch (IOException ex) {
                System.out.println("IOException when launching site info config");
                ex.printStackTrace();
            } catch(Exception ex) {
                System.out.println("Exception when launching site info config");
                ex.printStackTrace();
            }
            siteInfoCfgController.start();            
        }
    }
    
    //Button Listener Group
    //Selection Listener
    private class SelectRootActionListener implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent event) {
            fileOpen.resetChoosableFileFilters();
            fileOpen.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            webEditor.frameShowOpenDialog("webEditorGUI", fileOpen);
            //JFileChooser fileOpen = webEditor.frameFileChooser("webEditorGUI", 
            //    JFileChooser.DIRECTORIES_ONLY, null);
            if (fileOpen.getSelectedFile() == null) { //Cancelled
                return;
            }
            rootDir = fileOpen.getSelectedFile().toString();
            FileUtilities.write(rootCfgRel, rootDir, "UTF-8");
            init();
        }
    }    
    
    //Edit
    //Edit function
    private void editAction() {
        WebModuleDefault module = getSelectedModule();
        if (module != null) {
            module.startEditor();
        }        
    }
    //Delete action
    private void deleteAction() {
        WebModuleDefault module = getSelectedModule();
        if (module != null) {
            boolean success = module.delete();
            if (!success) {
                //
            }
            DesignInfoSet designInfoSet = new DesignInfoSet(designSet);
            //ContentParser cP = new ContentParser(designInfoSet);
            int location = webEditor.textAreaGetSelectionStart("contentTA");
            String s = webEditor.textAreaGetText("contentTA");
            String chosen = webEditor.textAreaGetSelectedText("contentTA");
            s = s.substring(0, location)
                + ContentParser.removeFirstPattern(chosen)
                + s.substring(location + chosen.length(), s.length());
            webEditor.textAreaSetText("contentTA", s);
            webEditor.labelSetText("statusLb", "Status: Deleted " 
                + module.getType().getValue() + "_" + module.getID() + ".");
        }
    }
    
    //Rename action - TO DO
    private void renameAction() {
    }
    //helper: parse the selection and get the target module
    private WebModuleDefault getSelectedModule() {
        String chosen = webEditor.textAreaGetSelectedText("contentTA");
        if (chosen == null) {
            return null;
        }
        //HashMap<String, String> designSetCopy = new HashMap<String, String>(designSet);
        DesignInfoSet designInfoSet = new DesignInfoSet(designSet);
        //ContentParser cP = new ContentParser(designInfoSet);
        Matcher m = ContentParser.patternTypeIdFind(chosen);
        WebModuleDefault module = null;
        if (m.find()) {
            String type = m.group(1).toUpperCase();
            int id = Integer.parseInt(m.group(2));
            String pageName = pages.get(currentPage).getPageName();
            switch(type) {
                case "TITLE": 
                    module = new WebModuleTitle(designInfoSet, pageName, id);
                    break;
                case "PARAGRAPH": 
                    module = new WebModuleParagraph(designInfoSet, pageName, id);
                    break;
                case "CODE": 
                    module = new WebModuleCode(designInfoSet, pageName, id);
                    break;
                case "FILE": 
                    module = new WebModuleFile(designInfoSet, pageName, id);
                    break;
                case "IMAGE": 
                    module = new WebModuleImage(designInfoSet, pageName, id);
                    break;
                case "GALLERY": 
                    module = new WebModuleGallery(designInfoSet, pageName, id);
                    break;
                case "DIVIDER": 
                    module = new WebModuleDivider(designInfoSet, pageName, id);
                    break;
                default:                    
            }
        }
        return module;
    }
    
    //Edit button Listener
    private class EditActionListener implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent event) {
            editAction();
        }
    }
    //Delete button Listener
    private class DeleteActionListener implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent event) {
            deleteAction();
        }
    }
    //Rename button Listener
    private class RenameActionListener implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent event) {
            renameAction();
        }
    }    
    //Insert ActionListener Group
    //Insert: Get next available ID to use
    private int getNextID(WebModuleEnum typeEnum) {
        String type = typeEnum.getValue(); 
        String text = webEditor.textAreaGetText("contentTA");
        int id = 1;
        //HashMap<String, String> designSetCopy = new HashMap<String, String>(designSet);
        DesignInfoSet designInfoSet = new DesignInfoSet(designSet);
        //ContentParser cP = new ContentParser(designInfoSet);
        while (ContentParser.patternExists(text, type, id)) {
            id++;            
        }
        //cP = null;
        return id;
    }
    //Insert Action Listeners
    //Base inner InsertActionListener class
    private class InsertActionListener implements ActionListener {
        private WebModuleEnum typeEnum = WebModuleEnum.CODE;
        public InsertActionListener(WebModuleEnum typeEnum) {
            super();
            if (typeEnum == null) {
                throw new NullPointerException("Null type for insert");
            }
            this.typeEnum = typeEnum;
        }
        @Override
        public void actionPerformed(ActionEvent event) {
            String type = typeEnum.getValue();
            int id = getNextID(typeEnum);
            String s = webEditor.textAreaGetText("contentTA");
            int location = webEditor.textAreaGetCaretPosition("contentTA");
            String newStr = " <<<###_" + type + "_" + id + "_###>>> \n";
            s = s.substring(0, location) 
                + newStr
                + s.substring(location, s.length());
            webEditor.textAreaSetText("contentTA", s);
            webEditor.textAreaSetSelection("contentTA", location, location + newStr.length());
            webEditor.buttonClick("edit");
        }
    }
    
    //Save action
    private void saveAction() {
        if (pages == null) {
            throw new NullPointerException("Null pages");
        }
        if (pages.size() > 0) {
            pages.get(currentPage).save(webEditor.textAreaGetText("contentTA"));
            webEditor.buttonSetEnabled("save", false);
            webEditor.labelSetText("statusLb", "Saved Page " + pages.get(currentPage).getPageName() + ".");
        } 
    }
    //Compile action
    private boolean validateCfgFile(String filePath, String info) {
        if (filePath == null) {
            throw new NullPointerException("Null filePath");
        }
        if (info == null) {
            throw new NullPointerException("Null info");
        }
        if (!(new File(filePath)).exists() 
            || FileUtilities.read(filePath) == null
            || FileUtilities.read(filePath).equals("")) {
            webEditor.labelSetText("statusLb", 
                "<html><font color=\"red\">" + info 
                + " file missing or is blank. </font></html>");
            return false;
        }
        return true;
    }
    private void compileAction() {
        if (pages == null) {
            throw new NullPointerException("Null pages");
        }
        if (currentPage >= 0 && pages.size() > 0) {
            webEditor.buttonClick("save");
        }
        if (!validateCfgFile(frameworkCfg, "Framework")
            || !validateCfgFile(mobileFrameworkCfg, "Mobile Framework")
            || !validateCfgFile(websiteNameCfg, "WebSite Name")
            || !validateCfgFile(websiteURLCfg, "Website URL")) {
            return;
        }
        if (pages.size() > 0) {
            webEditor.labelSetText("statusLb", "Compiling...");
            //String navTextMain = compileBuildNav(0);
            //String navTextMobile = compileBuildNav(1);           
            
            for (int i = 0; i < pages.size(); i++) {
                webEditor.labelSetText("statusLb", "Compiling Page " 
                    + pages.get(i).getPageName() + " ... " 
                    + "(" + (i+1) + "/" + pages.size() + ")");
                pages.get(i).compileMain(frameworkCfg);
                pages.get(i).compileMobile(mobileFrameworkCfg);
            }
            webEditor.labelSetText("statusLb", "Compiled " + pages.size() + " Pages.");
        } else {
            webEditor.labelSetText("statusLb", 
                "<html><font color=\"red\">There are 0 pages. Nothing compiled.</font></html>");
            return;
        }
        //Copy images resource to website folder
        String sourceImageDirStr = imagesResourcesDesignResourceRel;
        String destImageDirStr = rootDir
            + File.separator + websiteDirRel
            + File.separator + imagesResourcesRel;
        File sourceImageDir = new File(sourceImageDirStr);
        if (!sourceImageDir.exists() || sourceImageDir.isFile()) {
            //throw new NullPointerException("Images resource folder does not exist: " 
            //    + sourceImageDirStr);
            webEditor.labelSetText("statusLb", 
                "<html><font color=\"red\">Error: folder " 
                + imagesResourcesRel 
                + " missing or is invalid. </font></html>");
            return;
        }
        File[] allImages = sourceImageDir.listFiles();
        File destImageDir = new File(destImageDirStr);
        for (File f : allImages) {
            File d = new File(destImageDir + File.separator + f.getName());
            if (!d.exists()) {
                try {
                    FileUtilities.copyFile(f.getAbsolutePath(), d.getAbsolutePath(), false);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    //Save button Listener
    private class SaveActionListener implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent event) {
            saveAction();
        }
    }
    //Compile button Listener
    private class CompileActionListener implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent event) {
            compileAction();
        }
    }

    //textArea listener
    //Document Edit Listener
    private class EditDocumentListener implements DocumentListener {
        @Override
        public void changedUpdate(DocumentEvent event) {
            webEditor.buttonSetEnabled("save", true);
        }
        public void insertUpdate(DocumentEvent event) {
            webEditor.buttonSetEnabled("save", true);
        }
        public void removeUpdate(DocumentEvent event) {
            webEditor.buttonSetEnabled("save", true);
        }
    }
    
    
  
    //enable/disable all menuItems
    private void menuItemsSetEnabledAll(boolean enable) {
        webEditor.menuItemSetEnabled("frameworkItem", enable);
        webEditor.menuItemSetEnabled("styleItem", enable);
        webEditor.menuItemSetEnabled("mobileFrameworkItem", enable);
        webEditor.menuItemSetEnabled("mobileStyleItem", enable);
        webEditor.menuItemSetEnabled("mobileOptItem", enable);
        webEditor.menuItemSetEnabled("pageItem", enable);
        webEditor.menuItemSetEnabled("infoItem", enable);
    }
 
    public static void main(String[] args) {
        WebEditorController wEController = new WebEditorController();
        wEController.start();
    }
}