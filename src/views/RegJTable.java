/* Table GUI */

package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.table.*;

public class RegJTable extends JTable {
    public RegJTable() {
        super();
    }
    public RegJTable(Object[][] data, String[] colNames) {
        super(data, colNames);
    }
    @Override
    public String getToolTipText(MouseEvent e) {
        String tip = null;
        Point p = e.getPoint();
        int rowIndex = rowAtPoint(p);
        int colIndex = columnAtPoint(p);
        //int realColumnIndex = convertColumnIndexToModel(colIndex);
        tip = getValueAt(rowIndex, colIndex).toString();
        return tip;
    }
}
