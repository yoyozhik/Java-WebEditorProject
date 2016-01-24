/* Page Class */
/* Author: Wei Zhang
   Version date: 2016 Jan 19
*/
/* API

*/
package org.dharmatech.models;

public enum GUIObjTypeEnum {
    BUTTON("BUTTON"), 
    LABEL("LABEL"), 
    TEXTAREA("TEXTAREA"), 
    TEXTFIELD("TEXTFIELD"), 
    FRAME("FRAME"), 
    MENUITEM("MENUITEM"), 
    TABLE("TABLE"), 
    PANEL("PANEL"), 
    COMBOBOXSTR("COMBOBOXSTR"),
    COMBOBOXINT("COMBOBOXINT");

    
    String val;
    GUIObjTypeEnum(String val) {
        this.val = val;
    }
    public String getValue() {
        return (val);
    }
}
