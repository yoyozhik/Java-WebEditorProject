/* File Utilities 
*/
/* Author: Wei Zhang
   Latest Version: 2016 Jan 18
*/
/*API
class WebModuleParagraph {
    public WebModuleGParagraph()
    
}
*/

package models;
import utilities.*;

import java.util.HashMap;
import java.util.regex.*;
import java.io.*;


public class WebModuleParagraph extends WebModuleDefault{
    //Constructor
    public WebModuleParagraph(HashMap<String, String> designSet, String pageName, int id) {
        super(designSet, pageName, id);
        this.typeEnum = WebModuleEnum.PARAGRAPH;
    }
    
    //retrieve content
    @Override
    public String retrieveContent() {
        String detail = getResourceData();
        detail = detail.replaceAll("\n", "</p>\n<p>");
        detail = "<p>" + detail + "</p>";
        detail = detail.replaceAll("<p>\\s*</p>", "<p>&nbsp;</p>");
        return detail;
    }
    
}