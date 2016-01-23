/* Controller for pages config */
/* Author: Wei Zhang
   Latest Version: 2016 Jan 20
*/
/*API
public class PagesCfgController {
    public PagesCfgController(String filePath)
    public void start()
    public static void main(String[] args)
}
*/
package controllers;
import views.*;
import models.*;
import utilities.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.table.*;

public class PagesCfgController {
    private PagesCfg pagesCfg;
    private Command reloadMain;
    //parameters
    private final String filePath;
    private final String[] colNames = {"Level", "Page Name", "Page Title", "Shown in Nav"};

    public PagesCfgController(String filePath, Command reloadMain) throws IOException {
        if (filePath == null) {
            throw new NullPointerException("Null filePath");
        }
        File f = new File(filePath);
        if (f.exists() && !f.isFile()) {
            throw new IOException("Already a dir: " + filePath);
        }
        this.filePath = filePath;
        this.reloadMain = reloadMain;
        pagesCfg = new PagesCfg();
    }

    //Start page cfg
    public void start() {
        if (pagesCfg == null) {
            throw new NullPointerException("Null pagesCfg");
        }
        basicSetup();
        init();
        pagesCfg.frameSetVisible("editorGUI", true);       
        pagesCfg.framePack("editorGUI");
    }

    //Basic setup
    private void basicSetup() {
        pagesCfg.buttonAddActionListener("save", new SaveActionListener());
        pagesCfg.buttonAddActionListener("close", new ExitActionListener());
        pagesCfg.buttonAddActionListener("insert", new InsertActionListener());
        pagesCfg.buttonAddActionListener("delete", new DeleteActionListener());
        pagesCfg.buttonAddActionListener("moveUp", new MoveActionListener(-1));
        pagesCfg.buttonAddActionListener("moveDown", new MoveActionListener(1));
        //PagesCfg.textAreaAddDocumentListener("table", new EditDocumentListener());
        pagesCfg.frameAddWindowListener("editorGUI", new PagesWindowListener());
    }
    //Initialize
    private void init() {
        (pagesCfg.tableGetColumn("pagesTb", colNames[0])).setPreferredWidth(
            Math.round(pagesCfg.tableGetPreferredSize("pagesTb").width * 0.1f));
        
        //Initiliaze format even if no data yet
        Object[][] data = {{1, "", "", false}};
        RegTableModel model = new RegTableModel(data, colNames);
        pagesCfg.tableSetModel("pagesTb", model);
        pagesCfg.tableSetRowSelectionInterval("pagesTb", 0, 0);

                //Load pages
        File f = new File(filePath);
        if (f.exists() && f.isFile()) {
            String text = FileUtilities.read(filePath, "UTF-8");
            String[] lines = text.split("\n");
            data = new Object[lines.length][4];
            int i = 0;
            for (String ln : lines) {
                String[] pars = ln.split(",");  //What if the content itself has ","?
                if (pars.length >= 4) {
                    //System.out.println(line);
                    int level = Integer.parseInt(pars[0]);
                    String name = pars[1].replace("\"", "");
                    String title = pars[2].replace("\"", "");
                    boolean showNav = Boolean.parseBoolean(pars[3].replace("\"", ""));
                    data[i][0] = new Integer(level);
                    data[i][1] = name;
                    data[i][2] = title;
                    data[i][3] = new Boolean(showNav);
                    i++;
                }
            }
            for (int j = i; j < lines.length; j++) { //Fill the rest
                data[j][0] = new Integer(1);
                data[j][1] = "";
                data[j][2] = "";
                data[j][3] = new Boolean(false);
            }
            model = new RegTableModel(data, colNames);
            pagesCfg.tableSetModel("pagesTb", model);
            (pagesCfg.tableGetColumn("pagesTb", colNames[0])).setPreferredWidth(
                Math.round(pagesCfg.tableGetPreferredSize("pagesTb").width * 0.05f));
            //table.getColumn(colNames[0]).setMaxWidth(20);
            pagesCfg.tableSetRowSelectionInterval("pagesTb", 0, 0); //Need to select something! (it has at least 1 empty row)
        }
    }
    
    private class PagesWindowListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent we) {
            //if (webEditorGUI != null) {
            //    webEditorGUI.setEnabled(true);
            //}
        }
    }
    //Listeners
    private class SaveActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            save();
            reloadMain.execute(null);
        }
    }
    private class ExitActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            close();
        }
    }
    
    private class InsertActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            int currentLoc = pagesCfg.tableGetSelectedRow("pagesTb");
            if (currentLoc < 0) {  //Handle no-selection case
                insertRow(0);
            } else {
                insertRow(currentLoc);
            }
            pagesCfg.tableSetRowSelectionInterval("pagesTb", currentLoc + 1, currentLoc + 1);
            pagesCfg.buttonSetEnabled("save", true);
        }
    }
    private class DeleteActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            int currentLoc = pagesCfg.tableGetSelectedRow("pagesTb");
            if (currentLoc < 0) { //Handle no-selection case
                return;
            }
            deleteRow(currentLoc);
            RegTableModel tbModel = (RegTableModel) pagesCfg.tableGetModel("pagesTb");
            if (tbModel.getRowCount() > currentLoc) {
                pagesCfg.tableSetRowSelectionInterval("pagesTb", currentLoc, currentLoc);
            } else if (tbModel.getRowCount() > 0) {
                pagesCfg.tableSetRowSelectionInterval("pagesTb", currentLoc - 1, currentLoc - 1);
            }
            pagesCfg.buttonSetEnabled("save", true);
        }
    }
    private class MoveActionListener implements ActionListener {
        private int direction = -1; //-1: up; +1: down
        public MoveActionListener(int direction) {
            if (direction <= 0) {
                this.direction = -1;  //up
            } else {
                this.direction = 1;
            }
        }
        @Override
        public void actionPerformed(ActionEvent event) {
            int currentLoc = pagesCfg.tableGetSelectedRow("pagesTb");
            if (currentLoc < 0) {
                return;
            }
            boolean swapped = swapRow(currentLoc, currentLoc + direction);
            if (swapped) {
                pagesCfg.tableSetRowSelectionInterval("pagesTb", currentLoc + direction, currentLoc + direction);
                pagesCfg.buttonSetEnabled("save", true);
            }
        }
    }
    
    private void insertRow(int i) {
        RegTableModel tbModel = (RegTableModel) pagesCfg.tableGetModel("pagesTb");
        if (i < 0 
            || (tbModel.getRowCount() > 0 && i >= tbModel.getRowCount())
            || (tbModel.getRowCount() == 0 && i > 0)){
            throw new IndexOutOfBoundsException("i = " + i 
                + "; i must be within 0 and " 
                + tbModel.getRowCount() + "-1");
        }
        Object[] newRow = {1, "", "", false};
        tbModel.insertRow(i, newRow);
    }
    
    private void deleteRow(int i) {
        RegTableModel tbModel = (RegTableModel) pagesCfg.tableGetModel("pagesTb");
        if (i < 0 || i >= tbModel.getRowCount()) {
            throw new IndexOutOfBoundsException("i = " + i 
                + "; i must be within 0 and " 
                + tbModel.getRowCount() + "-1");
        }
        tbModel.removeRow(i);
    }
    
    private boolean swapRow(int i, int j) {
       RegTableModel tbModel = (RegTableModel) pagesCfg.tableGetModel("pagesTb");
       if (i < 0 || i >= tbModel.getRowCount()) {
            throw new IndexOutOfBoundsException("i = " + i 
                + "; i must be within 0 and " 
                + tbModel.getRowCount() + "-1");
        }
        if (j < 0 || j >= tbModel.getRowCount()) { //Do not swap invalid range
            return false;
        }
        tbModel.moveRow(i, i, j);
        return true;
    }
    //private class EditDocumentListener implements DocumentListener {
    //    @Override
    //    public void changedUpdate(DocumentEvent event) {
    //        save.setEnabled(true);
    //    }
    //    public void insertUpdate(DocumentEvent event) {
    //        save.setEnabled(true);
    //    }
    //    public void removeUpdate(DocumentEvent event) {
    //        save.setEnabled(true);
    //    }
    //}
    //Save
    private void save() {
        File f = new File(filePath);
        if (f.exists() && !f.isFile()) {
            throw new IllegalArgumentException("Is already a folder: " + filePath);
        }
        if (!f.exists()) {
            File dir = new File(f.getParent());
            if (dir != null & !dir.exists()) {
                dir.mkdirs();
            }
        }
    
        StringBuilder textSB = new StringBuilder();
        TableModel tbModel = pagesCfg.tableGetModel("pagesTb");
        int rCount = tbModel.getRowCount();
        int cCount = tbModel.getColumnCount();
        for (int i = 0; i < rCount; i++) {
            for (int j = 0; j < cCount; j++) {
                if (j > 0) {
                    textSB.append("\"");
                    textSB.append(tbModel.getValueAt(i, j).toString());
                    textSB.append("\"");
                } else {
                    textSB.append(tbModel.getValueAt(i, j).toString());
                }
                if (j < cCount - 1) {
                    textSB.append(",");
                }
            }
            textSB.append("\n");
        }
        String text = new String(textSB);
    
        FileUtilities.write(filePath, text, "UTF-8");
        pagesCfg.buttonSetEnabled("save", false);
    }
    //Exit
    private void close() {
        pagesCfg.frameDispatchEvent("editorGUI", WindowEvent.WINDOW_CLOSING);
    }
}