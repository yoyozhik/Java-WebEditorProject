/* Page Class */
/* Author: Wei Zhang
   Version date: 2016 Jan 15
*/
/* API
public enum WebModuleEnum {
    WebModuleEnum(String val) {}
    public String getValue() {}
}
*/
package org.dharmatech.models;

public enum WebModuleEnum {
    TITLE("TITLE"), 
    PARAGRAPH("PARAGRAPH"), 
    CODE("CODE"), 
    FILE("FILE"), 
    IMAGE("IMAGE"), 
    GALLERY("GALLERY"), 
    DIVIDER("DIVIDER");
    
    String val;
    WebModuleEnum(String val) {
        this.val = val;
    }
    //Get the String value of the enum
    public String getValue() {
        return (val);
    }
}
