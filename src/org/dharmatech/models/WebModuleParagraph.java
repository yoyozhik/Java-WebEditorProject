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

package org.dharmatech.models;
import org.dharmatech.utilities.*;
import org.dharmatech.controllers.DesignInfoSet;

import java.util.HashMap;
import java.util.regex.*;
import java.io.*;


public class WebModuleParagraph extends WebModuleDefault{
    //Constructor
    public WebModuleParagraph(DesignInfoSet designInfoSet, String pageName, int id) {
        super(designInfoSet, pageName, id);
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