/* Command interface */

package org.dharmatech.controllers;
import java.util.HashMap;
import java.util.Set;

public class DesignInfoSet {
    private HashMap<String, String> designSet;
    public DesignInfoSet(HashMap<String, String> designSet) {
        this.designSet = new HashMap<String, String>(designSet);
    }
    public DesignInfoSet(DesignInfoSet designInfoSet) {
        this.designSet = new HashMap<String, String>();
        for (String key : designInfoSet.getAllKeys()) {
            this.designSet.put(key, designInfoSet.getDesignInfo(key));
        }
    }
    public String getDesignInfo(String key) {
        return designSet.get(key);
    }
    private Set<String> getAllKeys() {
        return designSet.keySet();
    }
    
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