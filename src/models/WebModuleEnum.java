/* Page Class */
/* Author: Wei Zhang
   Version date: 2016 Jan 15
*/
/* API

*/
package models;

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
    public String getValue() {
        return (val);
    }
}
