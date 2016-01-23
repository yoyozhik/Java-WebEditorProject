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

import java.util.HashMap;
import java.util.regex.*;
import java.io.*;


public class WebModuleDivider extends WebModuleDefault{
    //Constructor
    public WebModuleDivider(HashMap<String, String> designSet, String pageName, int id) {
        super(designSet, pageName, id);
        this.typeEnum = WebModuleEnum.DIVIDER;
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
    
    //start editor
    @Override
    public void startEditor() {
        return;
    }
}