/* File Utilities 
*/
/* Author: Wei Zhang
   Latest Version: 2016 Jan 18
*/
/*API
class WebModuleCode {
    public WebModuleCode()
    
}
*/

package models;
import utilities.*;
import controllers.DesignInfoSet;

import java.util.HashMap;
import java.util.regex.*;
import java.io.*;


public class WebModuleCode extends WebModuleDefault{
    //Constructor
    public WebModuleCode(DesignInfoSet designInfoSet, String pageName, int id) {
        super(designInfoSet, pageName, id);
        this.typeEnum = WebModuleEnum.CODE;
    }
    
    //retrieve content
    @Override
    public String retrieveContent() {
        String detail = getResourceData();
        return detail;
    }
    
}