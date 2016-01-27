/* Title module */

/* Author: Wei Zhang
   Latest Version: 2016 Jan 18
*/
/*API
class WebModuleTitle {
    public WebModuleTitle(DesignInfoSet designInfoSet, String pageName, int id) {}
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


public class WebModuleTitle extends WebModuleDefault{
    //Constructor
    public WebModuleTitle(DesignInfoSet designInfoSet, String pageName, int id) {
        super(designInfoSet, pageName, id);
        this.typeEnum = WebModuleEnum.TITLE;
    }
   
    //retrieve content
    @Override
    public String retrieveContent() {
        //Only fetch content if file exists; otherwise use empty ""
        String detail = getResourceData();
        detail = detail.replaceAll("\n", "</p>\n<p class=\"title\">");
        detail = "<p class=\"title\">" + detail + "</p>";
        return detail;
    }
    
    public static void main(String[] args) {
        WebModuleTitle module = new WebModuleTitle(new DesignInfoSet(new HashMap<String, String>()),
            "index", 2);
        System.out.println(module.getID());
        System.out.println(module.getPageName());
        module.setPageName("contact");
        module.setID(3);
        System.out.println(module.getType());
        System.out.println(module.getPageName());
        System.out.println(module.getCfgPath());
        System.out.println(module.getFileExtension("test.t"));
        System.out.println(module.getTarget());
        System.out.println(module.getTargetWithMarker());
        System.out.println(module.retrieveContent());
        System.out.println(module.getResourceData());
        System.out.println(module.genRecord("Test Text for module"));
        module.startEditor();
    }    
}