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

public class PageContentJTextArea extends RegJTextArea {
    public PageContentJTextArea(int i, int j) {
        super(i, j);
        this.addCaretListener(new textAreaCaretListener(this));
    }
    public PageContentJTextArea() {
        super();
        this.addCaretListener(new textAreaCaretListener(this));
    }
    
    private class textAreaCaretListener implements CaretListener {
        private RegJTextArea tA;
        public textAreaCaretListener (RegJTextArea tA) {
            this.tA = tA;
        }
        public void caretUpdate(CaretEvent e) {
            updateCaretPosition();
        }
        private void updateCaretPosition() {
            Pattern p = Pattern.compile("[^ \n]");
            String content = tA.getText();
            int location = tA.getCaretPosition();
            if (!p.matcher(content.substring(0, location)).find()) {
                return; //Do nothing if at the beginning
            }
            int start = tA.getStartingSection(location, true);
            int seg1 = 0;
            int seg2 = 0;
            seg1 = (location < start) ? location : start;
            seg2 = (location > start) ? location : start;
            if (seg1 == seg2) {
                return;  //Do nothing if no change
            }
            if (!p.matcher(content.substring(seg1, seg2)).find()) {
                return; //Do nothing if only space/line-wrap difference
            }
            tA.setCaretPosition(start);
        }
    }
}
    