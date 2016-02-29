/* Page Class */
/* Author: Wei Zhang
   Version date: 2016 Jan 15
*/
/* API
public enum WebModuleEnum {
    WebModuleEnum(String val) {}
    public String getValue() {}
    public static WebModuleEnum 
        getWebModuleEnumFromString(String type) {}
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
    
    //Convert string type into enum type
    public static WebModuleEnum getWebModuleEnumFromString(String type) {
        type = type.toUpperCase();
        switch(type) {
            case "TITLE": 
                return WebModuleEnum.TITLE;
            case "PARAGRAPH": 
                return WebModuleEnum.PARAGRAPH;
            case "CODE": 
                return WebModuleEnum.CODE;
            case "FILE": 
                return WebModuleEnum.FILE;
            case "IMAGE": 
                return WebModuleEnum.IMAGE;
            case "GALLERY": 
                return WebModuleEnum.GALLERY;
            case "DIVIDER": 
                return WebModuleEnum.DIVIDER;
            default:
                throw new IllegalArgumentException("Illeagal type: " + type);
        }
    }
}
