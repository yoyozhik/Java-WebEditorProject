/* WebModule Factory0 */
/* Author: Wei Zhang
   Latest Version: 2016 Feb 28
*/
/*API
class WebModule {
    //public WebModule() {}
    public static WebModuleDefault createWebModule(DesignInfoSet designInfoSet,
        String pageName, int id, WebModuleEnum typeEnum) {}
    public static WebModuleDefault createWebModule(DesignInfoSet designInfoSet,
        String pageName, int id, String type) {}
}
*/

package org.dharmatech.models;
import org.dharmatech.utilities.*;
import org.dharmatech.controllers.DesignInfoSet;

import java.util.HashMap;
import java.util.regex.*;
import java.io.*;


public abstract class WebModule {
    public static WebModuleDefault createWebModule(DesignInfoSet designInfoSet, 
        String pageName, int id, WebModuleEnum typeEnum) {
        WebModuleDefault module = null;
        switch(typeEnum) {
            case TITLE: 
                module = new WebModuleTitle(designInfoSet, pageName, id);
                break;
            case PARAGRAPH: 
                module = new WebModuleParagraph(designInfoSet, pageName, id);
                break;
            case CODE: 
                module = new WebModuleCode(designInfoSet, pageName, id);
                break;
            case FILE: 
                module = new WebModuleFile(designInfoSet, pageName, id);
                break;
            case IMAGE: 
                module = new WebModuleImage(designInfoSet, pageName, id);
                break;
            case GALLERY: 
                module = new WebModuleGallery(designInfoSet, pageName, id);
                break;
            case DIVIDER: 
                module = new WebModuleDivider(designInfoSet, pageName, id);
                break;
            default:
                throw new IllegalArgumentException("Illeagal type: " + typeEnum.getValue());
        }
        return module;
    }
    
    public static WebModuleDefault createWebModule(DesignInfoSet designInfoSet, 
        String pageName, int id, String type) {
        return createWebModule(designInfoSet, pageName, id, 
            WebModuleEnum.getWebModuleEnumFromString(type));
    }
}