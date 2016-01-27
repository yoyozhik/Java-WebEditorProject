/* DesignInfoSet class */
/* Author: Wei Zhang 
Latest Update: Jan 18, 2016 */

/* For packing design information into one class
    and transfer between classes
   Encapsulate the hashmap which stores the actual data
   To prevent data corruption from outside   */

/* API
public class DesignInfoSet {
    public DesignInfoSet(HashMap<String, String> designSet) {}
    public DesignInfoSet(DesignInfoSet designInfoSet) {}
    public String getDesignInfo(String key) {}
    public static void main(String[] args) {}    
}
*/

package org.dharmatech.controllers;
import java.util.HashMap;
import java.util.Set;

public class DesignInfoSet {
    private HashMap<String, String> designSet; //Storing all necessary design info
    //Constructor
    //construct from a hashmap
    public DesignInfoSet(HashMap<String, String> designSet) {  
        this.designSet = new HashMap<String, String>(designSet);
    }
    //construct from another DesignInfoSet object
    public DesignInfoSet(DesignInfoSet designInfoSet) {    
        this.designSet = new HashMap<String, String>();
        for (String key : designInfoSet.getAllKeys()) {
            this.designSet.put(key, designInfoSet.getDesignInfo(key));
        }
    }
    //Fetch the content
    public String getDesignInfo(String key) {
        return designSet.get(key);
    }
    //helper: get all keys stored 
    private Set<String> getAllKeys() {
        return designSet.keySet();
    }
    
    //Test
    public static void main(String[] args) {
        HashMap<String, String> designSet = new HashMap<String, String>();
        designSet.put("KEYA", "DataA");
        designSet.put("KEYb", "ContentB");
        DesignInfoSet designInfoSet = new DesignInfoSet(designSet);
        DesignInfoSet designInfoSetCopy = new DesignInfoSet(designInfoSet);
        
        System.out.println(designInfoSet.getDesignInfo("KEYA"));
        System.out.println(designInfoSet.getDesignInfo("KEYb"));
        System.out.println(designInfoSetCopy.getDesignInfo("KEYA"));
        System.out.println(designInfoSetCopy.getDesignInfo("KEYb"));
    }
    
}