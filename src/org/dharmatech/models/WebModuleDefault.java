/* Default module*/
/* Author: Wei Zhang
   Latest Version: 2016 Jan 18
*/
/*API
class WebModuleDefault {
    public WebModuleDefault(DesignInfoSet designInfoSet, String pageName, int id) {}
    public void setDesignInfoSet(DesignInfoSet designInfoSet) {}
    public DesignInfoSet getDesignInfoSet() {}
    public String getDesignSetItem(String key) {}
    public void setPageName(String pageName) {}
    public void setID(int id) {}
    public WebModuleEnum getType() {}
    public String getPageName() {}
    public int getID() {}
    public String getCfgPath() {}
    public String getFileExtension(String fileStr) {}
    public String getTarget() {}
    public String getTargetWithMarker() {}
    public void save(String cfgText) {}
    public String retrieveContent() {}
    public String getResourceData() {}
    public String genRecord(String text) {}
    public boolean delete() {}
    public void startEditor() {}
    public String getMarkedContent() {}    
    public static void main(String[] args) {}
}
*/

package org.dharmatech.models;
import org.dharmatech.utilities.*;
import org.dharmatech.controllers.*;

import java.util.HashMap;
import java.util.regex.*;
import java.io.*;

public abstract class WebModuleDefault {
    protected WebModuleEnum typeEnum = WebModuleEnum.CODE;
    private DesignInfoSet designInfoSet;
    private String pageName;
    private int id;
    //Constructor
    public WebModuleDefault(DesignInfoSet designInfoSet, String pageName, int id) {
        if (designInfoSet == null) {
            throw new NullPointerException("Null designInfoSet");
        }
        if (pageName == null) {
            throw new NullPointerException("Null pageName");
        }
        this.designInfoSet = new DesignInfoSet(designInfoSet);
        this.pageName = pageName;
        this.id = id;
    }
    //Set DesignInfoSet without reinstantialting
    public void setDesignInfoSet(DesignInfoSet designInfoSet) {
        this.designInfoSet = new DesignInfoSet(designInfoSet);
    }
    //Get a copy of DesignInfoSet to pass to another constructor
    public DesignInfoSet getDesignInfoSet() {
        return new DesignInfoSet(designInfoSet);
    }
    //Get design info
    public String getDesignSetItem(String key) {
        return designInfoSet.getDesignInfo(key);
    }
    //Set page name
    public void setPageName(String pageName) {
        this.pageName = pageName;
    }
    //set id
    public void setID(int id) {
        this.id = id;
    }
    //get type
    public WebModuleEnum getType() {
        return typeEnum;
    }
    //get page name
    public String getPageName() {
        return pageName;
    }
    //get id
    public int getID() {
        return id;
    }
    //get cfg path of this module
    public String getCfgPath() {
        String rootDir = getDesignSetItem("rootDir");
        String resourceDirRel = getDesignSetItem("resourceDirRel");
        if (rootDir == null) {
            throw new NullPointerException("Null rootDir");
        }
        if (resourceDirRel == null) {
            throw new NullPointerException("Null resourceDirRel");
        }
        String cfgPath = rootDir + File.separator 
            + resourceDirRel + File.separator
            + pageName + File.separator
            + typeEnum.getValue() + "_" + id + ".txt";
        File f = new File(cfgPath);
        if (f.exists() && !f.isFile()) {
            throw new IllegalArgumentException("Invalid cfgPath: "
                + cfgPath);
        }
        return cfgPath;
    }
    //get file extension
    public String getFileExtension(String fileStr) {
        String extension = "";
        int i = fileStr.lastIndexOf('.');
        if (i > 0) {
            extension = fileStr.substring(i+1).toUpperCase();
        }
        return extension;
    }
    //get target string for this module
    public String getTarget() {
        return typeEnum.getValue() + "_" + id;
    }
    //get target string with markdown for this module
    public String getTargetWithMarker() {
        return "<<<###_" + getTarget() + "_###>>>";
    }
    //save
    public void save(String cfgText) {
        FileUtilities.write(getCfgPath(), cfgText, "UTF-8");
    }
    
    //retrieve content
    public String retrieveContent() {
        String detail = getCfgPath();
        return detail;
    }
    //get resource data according to type and cfg content
    public String getResourceData() {
        //Only fetch content if file exists; otherwise use empty ""
        String cfgPath = getCfgPath();
        File f = new File(cfgPath);
        String detail = "";
        if (f.exists() && f.isFile()) {
            detail = FileUtilities.read(cfgPath, "UTF-8");
            if (detail == null) {
                detail = "";
            } 
        }
        return detail;
    }
    //Generate the content to write into cfg file
    public String genRecord(String text) {
        return text;
    }
    
    //Delete the module
    public boolean delete() {
        String cfgPath = getCfgPath();
        return FileUtilities.deleteFile(cfgPath);
    }
    
    //Start editor
    public void startEditor() {
        FileEditorController fileEditorController = null;
        try {
            fileEditorController = new FileEditorController(getCfgPath(), 
                new DesignInfoSet(designInfoSet),
                pageName, id, typeEnum);
        } catch (IOException ex) {
            System.out.println("IOException when launching editor");
            ex.printStackTrace();
        } catch(Exception ex) {
            System.out.println("Exception when launching editor");
            ex.printStackTrace();
        }
        fileEditorController.start();
    }
    
    //Insert identifier 
    private String insertMarker(String textItem, String target) {
        return "<!-- " + target + " START -->" 
            + textItem 
            + "<!-- " + target + " END -->";
    }
    public String getMarkedContent() {
        return insertMarker(retrieveContent(), getTarget());
    }
    
    public static void main(String[] args) {
        /*WebModuleDefault module = new WebModuleDefault(new DesignInfoSet(new HashMap<String, String>()),
            "index", 2);
        System.out.println(module.getID());
        System.out.println(module.getPageName());
        module.setPageName("contact");
        module.setID(3);
        System.out.println(module.getType());
        System.out.println(module.getPageName());
        System.out.println(module.getCfgPath());
        System.out.println(module.getFileExtension("test.code"));
        System.out.println(module.getTarget());
        System.out.println(module.getTargetWithMarker());
        System.out.println(module.retrieveContent());
        System.out.println(module.getResourceData());
        System.out.println(module.genRecord("Test Text for module"));
        System.out.println(module.getMarkedContent());
        module.startEditor();*/
    }
}