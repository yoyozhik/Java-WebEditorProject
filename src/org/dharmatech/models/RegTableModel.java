/* Table Model */
/* Author: Wei Zhang
   Version date: 2016 Jan 15
*/
/* API
public class RegTableModel extends DefaultTableModel {
    public RegTableModel(Object[][] data, String[] columnNames) {}
    public Class getColumnClass(int c) {}
    //Others inheriting from DefaultTableModel
}
*/
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
    
    //Override to get the actual class instead of all Strings
    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
    
}