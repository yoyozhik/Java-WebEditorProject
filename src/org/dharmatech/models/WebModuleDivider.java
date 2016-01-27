/* Divider module */

/* Author: Wei Zhang
   Latest Version: 2016 Jan 18
*/
/*API
class WebModuleDivider {
    public WebModuleDivider(DesignInfoSet designInfoSet, String pageName, int id) {}
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
import org.dharmatech.controllers.DesignInfoSet;

import java.util.HashMap;
import java.util.regex.*;
import java.io.*;


public class WebModuleDivider extends WebModuleDefault{
    //Constructor
    public WebModuleDivider(DesignInfoSet designInfoSet, String pageName, int id) {
        super(designInfoSet, pageName, id);
        this.typeEnum = WebModuleEnum.DIVIDER;
    }
    
    @Override
    public String getCfgPath() {
        return null;
    }

    //save
    @Override
    public void save(String cfgText) { //Nothing to save for divider
    }
    
    //retrieve content
    @Override
    public String retrieveContent() {
        return "\n<hr>\n";
    }
    
    @Override
    public String getResourceData() {
        return "";
    }
    
    @Override
    public String genRecord(String text) {
        return "";
    }
    //delete the module
    @Override
    public boolean delete() {
        return true;
    }
    //start editor
    @Override
    public void startEditor() {
        return;
    }
    
    public static void main(String[] args) {
        WebModuleDivider module = new WebModuleDivider(new DesignInfoSet(new HashMap<String, String>()),
            "index", 2);
        System.out.println(module.getID());
        System.out.println(module.getPageName());
        module.setPageName("contact");
        module.setID(3);
        System.out.println(module.getType());
        System.out.println(module.getPageName());
        System.out.println(module.getCfgPath());
        System.out.println(module.getFileExtension("test.div"));
        System.out.println(module.getTarget());
        System.out.println(module.getTargetWithMarker());
        System.out.println(module.retrieveContent());
        System.out.println(module.getResourceData());
        System.out.println(module.genRecord("Test Text for module"));
        System.out.println(module.getMarkedContent());
        module.startEditor();
    }
}