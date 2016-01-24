/* Table Model */

package org.dharmatech.models;

import javax.swing.event.*;
import javax.swing.table.TableModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

//public class RegTableModel extends AbstractTableModel {
public class RegTableModel extends DefaultTableModel {
    //private String[] columnNames;
    //private Object[][] data;
    
    public RegTableModel(Object[][] data, String[] columnNames) {
        super(data, columnNames);
        //this.data = data;
        //this.columnNames = columnNames;
    }
    
    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
    
}