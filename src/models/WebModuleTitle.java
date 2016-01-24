/* File Utilities 
*/
/* Author: Wei Zhang
   Latest Version: 2016 Jan 18
*/
/*API
class WebModuleTitle {
    public WebModuleTitle()
    
}
*/

package models;
import utilities.*;
import controllers.DesignInfoSet;

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
    
}