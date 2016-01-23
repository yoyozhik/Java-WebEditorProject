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

import java.util.HashMap;
import java.util.regex.*;
import java.io.*;


public class WebModuleCode extends WebModuleDefault{
    //Constructor
    public WebModuleCode(HashMap<String, String> designSet, String pageName, int id) {
        super(designSet, pageName, id);
        this.typeEnum = WebModuleEnum.CODE;
    }
    
    //retrieve content
    @Override
    public String retrieveContent() {
        String detail = getResourceData();
        return detail;
    }
    
}