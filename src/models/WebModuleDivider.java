/* File Utilities 
*/
/* Author: Wei Zhang
   Latest Version: 2016 Jan 18
*/
/*API
class WebModuleDivider {
    public WebModuleDivider()
    
}
*/

package models;
import utilities.*;
import controllers.DesignInfoSet;

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
}