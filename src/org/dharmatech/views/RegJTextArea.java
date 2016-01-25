/* regular textarea extending basic textarea */

package org.dharmatech.views;
import org.dharmatech.utilities.*;
import org.dharmatech.controllers.*;
import org.dharmatech.models.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.*;

public class RegJTextArea extends JTextArea {
    private DesignInfoSet designInfoSet;
    private String pageName;
    public RegJTextArea(int i, int j) {
        super(i, j);
    }
    public RegJTextArea() {
        super();
    }
    public void setInfo(DesignInfoSet designInfoSet, String pageName) {
        if (designInfoSet == null) {
            throw new NullPointerException("Null designInfoSet");
        }
        if (pageName == null) {
            throw new NullPointerException("Null pageName");
        }
        this.designInfoSet = new DesignInfoSet(designInfoSet);
        this.pageName = pageName;
    }
    @Override
    public String getToolTipText(MouseEvent e) {
        if (designInfoSet == null) {
            return null;
        }
        String content = getText();
        //int location = getCaretPosition();
        int location = viewToModel(e.getPoint());
        int maxTry = 6;
        int tryI = 0;
        String type = "";
        int id = 0;
        ContentParser cP = new ContentParser(designInfoSet);
        while (tryI < maxTry) {
            int start = location - tryI * 5;
            int end = location + tryI * 5;
            if (start < 0) {
                start = 0;
            }
            if (end > content.length()) {
                end = content.length();
            }
            Matcher m = cP.patternTypeIdFind(content.substring(start, end));
            if (m.find()) {
                type = m.group(1);
                id = Integer.parseInt(m.group(2));
                break;
            }
            tryI++;
        }
        if (!type.equals("")) {
            WebModuleDefault module = null;
            switch (type.toUpperCase()) {
                case "TITLE":
                    module = new WebModuleTitle(designInfoSet, pageName, id);
                    break;
                case "PARAGRAPH":
                    module = new WebModuleParagraph(designInfoSet, pageName, id);
                    break;
                case "CODE":
                    module = new WebModuleCode(designInfoSet, pageName, id);
                    break;
                case "FILE":
                    module = new WebModuleFile(designInfoSet, pageName, id);
                    break;
                case "IMAGE":
                    module = new WebModuleImage(designInfoSet, pageName, id);
                    break;
                case "GALLERY":
                    module = new WebModuleGallery(designInfoSet, pageName, id);
                    break;
                case "DIVIDER":
                    module = new WebModuleDivider(designInfoSet, pageName, id);
                    break;
                default:
                    return null;
            }            
            String tip = FileUtilities.autoEllipsis(module.getResourceData(), 100, false);
            if (tip != null && !tip.equals("") && module.getTarget() != null) {
                return module.getTarget() + ": " + tip;
            }
        }
        return null;
    }
}
    