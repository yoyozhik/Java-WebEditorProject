/* regular button extending basic button */

package org.dharmatech.views;

import javax.swing.*;
import java.awt.*;

public class RegJButton extends JButton{
    public RegJButton(String t) {
        super(t);
    }
    public RegJButton(String t, int len) {
        super(t);
        int tlen = t.length();
        if (tlen < len) {
            int left = (int) ((len - tlen) / 2);
            int right = len - left - tlen;
            StringBuilder leftSB = new StringBuilder();
            StringBuilder rightSB = new StringBuilder();
            for (int i = 0; i < left; i++) {
                leftSB.append(" "); 
            }
            for (int i = 0; i < right; i++) {
                rightSB.append(" "); 
            }
            String leftS = new String(leftSB);
            String rightS = new String(rightSB);
            t = leftS + t + rightS;
            setText(t);
        }
    }
}