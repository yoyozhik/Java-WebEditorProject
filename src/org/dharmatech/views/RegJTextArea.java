/* regular textarea extending basic textarea */

package org.dharmatech.views;
import org.dharmatech.utilities.*;
import org.dharmatech.controllers.*;
import org.dharmatech.models.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.*;
import javax.swing.event.*;
import javax.swing.undo.UndoManager;
import javax.swing.text.Document;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CannotRedoException;

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
        int location = viewToModel(e.getPoint());
        String type = "";
        int id = 0;
        int start = getStartingSection(location, false);
        Matcher m = ContentParser.patternTypeIdFind(content.substring(start));
        if (m.find()) {
            type = m.group(1);
            id = Integer.parseInt(m.group(2));
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
    
    //Find the starting location for a valid segment
    //location: the reference location in the text
    //matchEnd: 
    //True: if location is near the end of segment, match to its end
    //False: if location is near the end of segment, still match to start
    protected int getStartingSection(int location, boolean matchEnd) {
        if (designInfoSet == null) {
            return 0;
        }
        String content = getText();
        int maxTry = 6;
        int tryI = 0;
        String type = "";
        int id = 0;
        //ContentParser cP = new ContentParser(designInfoSet);
        Matcher m = null;
        int start = 0;
        int end = 0;
        boolean mFind = false;
        
        if (matchEnd) {
            //If we are at the end of the text
            end = content.substring(location, content.length())
                .indexOf("<<<###_");
            if (end < 0) { //Already at the end
                int preEnd = content.substring(location, content.length())
                    .lastIndexOf(">");
                if (preEnd >= 0) {
                    return location + preEnd + 1;
                }
                return location;
            }
        }
        //Otherwise
        //If the location is inside a segment, we can find matches 
        //Between the beginning of the text and the location + 8 
        //For the next "<<<###_"
        //But we use 8 + 8 = 16 so that it locates to the next segment
        //If it is near the end
        int delta = 8;
        if (matchEnd) {
            delta = 8 + 8;
        }
        start = content.substring(0, 
            (location + delta > content.length()) ?
            content.length() : location + delta )
            .lastIndexOf("<<<###_");
        if (start < 0) {  //Did not find any
            return 0;
        } else {
            if (start > location) { //Near end
                int preEnd = content.substring(location, start + 1)
                    .lastIndexOf(">");
                if (preEnd >= 0) {
                    return location + preEnd + 1;
                }
            }
            return start;
        }        
    }
    
    
    //Enable undo function
    //Code adapted from exampledepot.com code example
    public void enableUndo() {
        // Listener
        Document doc = getDocument();
        final UndoManager undo = new UndoManager();
        // Listen for undo and redo events
        doc.addUndoableEditListener(new UndoableEditListener() {
            public void undoableEditHappened(UndoableEditEvent evt) {
                undo.addEdit(evt.getEdit());
            }
        });

        // Undo
        // Create an undo action and add it to the text component
        getActionMap().put("Undo",
            new AbstractAction("Undo") {
                public void actionPerformed(ActionEvent evt) {
                    try {
                        if (undo.canUndo()) {
                            undo.undo();
                        }
                    } catch (CannotUndoException e) {
                    }
                }
            }
        );
        // Bind the undo action to ctl-Z
        getInputMap().put(KeyStroke.getKeyStroke("control Z"), "Undo");

        // Redo
        // Create a redo action and add it to the text component
        getActionMap().put("Redo",
            new AbstractAction("Redo") {
                public void actionPerformed(ActionEvent evt) {
                    try {
                        if (undo.canRedo()) {
                            undo.redo();
                        }
                    } catch (CannotRedoException e) {
                    }
                }
            }
        );
        // Bind the redo action to ctl-Y
        getInputMap().put(KeyStroke.getKeyStroke("control Y"), "Redo");
    }
}
    