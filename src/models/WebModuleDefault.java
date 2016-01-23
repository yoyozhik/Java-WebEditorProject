/* File Utilities 
*/
/* Author: Wei Zhang
   Latest Version: 2016 Jan 18
*/
/*API
class WebModuleDefault {
    public WebModuleDefault()
    
}
*/

package models;
import utilities.*;
import controllers.*;

import java.util.HashMap;
import java.util.regex.*;
import java.io.*;

public class WebModuleDefault {
    protected WebModuleEnum typeEnum = WebModuleEnum.CODE;
    private HashMap<String, String> designSet;
    private String pageName;
    private int id;
    //Constructor
    public WebModuleDefault(HashMap<String, String> designSet, String pageName, int id) {
        if (designSet == null) {
            throw new NullPointerException("Null designSet");
        }
        if (pageName == null) {
            throw new NullPointerException("Null pageName");
        }
        this.designSet = new HashMap<String, String> (designSet);
        this.pageName = pageName;
        this.id = id;
    }
    public void setDesignSet(HashMap<String, String> designSet) {
        this.designSet = new HashMap<String, String> (designSet);
    }
    public HashMap<String, String> getDesignSet() {
        return new HashMap<String, String> (designSet);
    }
    public String getDesignSetItem(String key) {
        return designSet.get(key);
    }
    public void setPageName(String pageName) {
        this.pageName = pageName;
    }
    public void setID(int id) {
        this.id = id;
    }
    public WebModuleEnum getType() {
        return typeEnum;
    }
    public String getDesignInfo(String key) {
        return designSet.get(key);
    }
    public String getPageName() {
        return pageName;
    }
    public int getID() {
        return id;
    }
    public String getCfgPath() {
        String rootDir = designSet.get("rootDir");
        String resourceDirRel = designSet.get("resourceDirRel");
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
    public String getTarget() {
        return typeEnum.getValue() + "_" + id;
    }
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
    
    public void startEditor() {
        FileEditorController fileEditorController = null;
        try {
            fileEditorController = new FileEditorController(getCfgPath(), 
                new HashMap<String, String>(designSet),
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
}