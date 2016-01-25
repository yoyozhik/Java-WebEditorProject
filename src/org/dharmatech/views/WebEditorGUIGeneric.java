/* Main central class of WebEditorGUIGeneric */
/* API
abstract class WebEditorGUIGeneric {
    public WebEditorGUIGeneric()
    public static void main(String[] args)
}
*/

package org.dharmatech.views;
import org.dharmatech.controllers.*;
import org.dharmatech.models.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.filechooser .*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.util.HashMap.*;

public abstract class WebEditorGUIGeneric {
    
    private HashMap<String, JFrame> frameGroup;
    private HashMap<String, RegJButton> buttonGroup;
    private HashMap<String, JLabel> labelGroup;
    private HashMap<String, RegJTextArea> textAreaGroup;
    private HashMap<String, JTextField> textFieldGroup;
    private HashMap<String, JMenuItem> menuItemGroup;
    private HashMap<String, RegJTable> tableGroup;
    private HashMap<String, RegJPanel> panelGroup;
        
    private HashMap<String, JComboBox<String>> comboBoxStrGroup;
    private HashMap<String, JComboBox<Integer>> comboBoxIntGroup;
    
    //Initialize object groups    
    public void initGUIObjsButton(HashMap<String, RegJButton> buttonGroup) {
        this.buttonGroup = buttonGroup;
    }
    public void initGUIObjsLabel(HashMap<String, JLabel> labelGroup) {
        this.labelGroup = labelGroup;
    }
    public void initGUIObjsTextArea(HashMap<String, RegJTextArea> textAreaGroup) {
        this.textAreaGroup = textAreaGroup;
    }
    public void initGUIObjsTextField(HashMap<String, JTextField> textFieldGroup) {
        this.textFieldGroup = textFieldGroup;
    }
    public void initGUIObjsFrame(HashMap<String, JFrame> frameGroup) {
        this.frameGroup = frameGroup;
    }
    public void initGUIObjsMenuItem(HashMap<String, JMenuItem> menuItemGroup) {
        this.menuItemGroup = menuItemGroup;
    }
    public void initGUIObjsTable(HashMap<String, RegJTable> tableGroup) {
        this.tableGroup = tableGroup;
    }
    public void initGUIObjsPanel(HashMap<String, RegJPanel> panelGroup) {
        this.panelGroup = panelGroup;
    }    
    public void initGUIObjsComboBoxStr(HashMap<String, JComboBox<String>> comboBoxStrGroup) {
        this.comboBoxStrGroup = comboBoxStrGroup;
    }
    public void initGUIObjsComboBoxInt(HashMap<String, JComboBox<Integer>> comboBoxIntGroup) {
        this.comboBoxIntGroup = comboBoxIntGroup;
    }

    public void addGUIObjsButton(String str, RegJButton obj) {
        buttonGroup.put(str, obj);
    }
    public void addGUIObjsLabel(String str, JLabel obj) {
        labelGroup.put(str, obj);
    }
    public void addGUIObjsTextArea(String str, RegJTextArea obj) {
        textAreaGroup.put(str, obj);
    }
    public void addGUIObjsTextField(String str, JTextField obj) {
        textFieldGroup.put(str, obj);
    }
    public void addGUIObjsFrame(String str, JFrame obj) {
        frameGroup.put(str, obj);
    }
    public void addGUIObjsMenuItem(String str, JMenuItem obj) {
        menuItemGroup.put(str, obj);
    }
    public void addGUIObjsTable(String str, RegJTable obj) {
        tableGroup.put(str, obj);
    }
    public void addGUIObjsPanel(String str, RegJPanel obj) {
        panelGroup.put(str, obj);
    }
    public void addGUIObjsComboBoxStr(String str, JComboBox<String> obj) {
        comboBoxStrGroup.put(str, obj);
    }
    public void addGUIObjsComboBoxInt(String str, JComboBox<Integer> obj) {
        comboBoxIntGroup.put(str, obj);
    }
    
    //object exists
    //Get the object
    public boolean objExists(String id, GUIObjTypeEnum type) {
        if (id == null) {
            throw new NullPointerException("Null " + type + " id");
        }
        HashMap group = null;
        switch(type) {
            case BUTTON: group = buttonGroup; break;
            case MENUITEM: group = menuItemGroup; break;
            case LABEL: group = labelGroup; break;
            case TEXTAREA: group = textAreaGroup; break;
            case TEXTFIELD: group = textFieldGroup; break;
            case FRAME: group = frameGroup; break;
            case TABLE: group = tableGroup; break;
            case PANEL: group = panelGroup; break;
            default: return false;
        }
        if (group == null) {
            return false;
        }
        Object obj = ((HashMap) group).get(id.toLowerCase());
        if (obj == null) {
            return false;
        }
        return true;
    }
    
    //Get the object
    private Object getObj(String id, GUIObjTypeEnum type) {
        if (id == null) {
            throw new NullPointerException("Null " + type + " id");
        }
        //Object group = null;
        HashMap group = null;
        switch(type) {
            case BUTTON: group = buttonGroup; break;
            case MENUITEM: group = menuItemGroup; break;
            case LABEL: group = labelGroup; break;
            case TEXTAREA: group = textAreaGroup; break;
            case TEXTFIELD: group = textFieldGroup; break;
            case FRAME: group = frameGroup; break;
            case TABLE: group = tableGroup; break;
            case PANEL: group = panelGroup; break;
        }
        if (group == null) {
            throw new NullPointerException("Null " + type + " group");
        }
        Object obj = ((HashMap) group).get(id.toLowerCase());
        if (obj == null) {
            throw new IllegalArgumentException(type + " id does not exist: " + id);
        }
        return obj;
    }
    
    //Get the object for parameterized combobox
    //Items with generics cannot be handled using the getObj way
    private JComboBox<String> getObjComboBoxStr(String id) {
        if (id == null) {
            throw new NullPointerException("Null " + "Combobox<String>" + " id");
        }
        if (comboBoxStrGroup == null) {
            throw new NullPointerException("Null " + "ComboBox<String>" + " group");
        }
        JComboBox<String> cB = comboBoxStrGroup.get(id.toLowerCase());
        if (cB == null) {
            throw new IllegalArgumentException("ComboBox<String>" + " id does not exist: " + id);
        }
        return cB;
    }
    private JComboBox<Integer> getObjComboBoxInt(String id) {
        if (id == null) {
            throw new NullPointerException("Null " + "Combobox<Integer>" + " id");
        }
        if (comboBoxIntGroup == null) {
            throw new NullPointerException("Null " + "ComboBox<Integer>" + " group");
        }
        JComboBox<Integer> cB = comboBoxIntGroup.get(id.toLowerCase());
        if (cB == null) {
            throw new IllegalArgumentException("ComboBox<Integer>" + " id does not exist: " + id);
        }
        return cB;
    }
    
    
    
    //AddActionListeners
    public void buttonAddActionListener(String id, ActionListener listener) {
        if (listener == null) {
            throw new NullPointerException("Null button listener");
        }        
        ((RegJButton) getObj(id, GUIObjTypeEnum.BUTTON)).addActionListener(listener);
    }
    //Button click
    public void buttonClick(String id) {
        ((RegJButton) getObj(id, GUIObjTypeEnum.BUTTON)).doClick();
    }
    //Button enable/disable
    public void buttonSetEnabled(String id, boolean enabled) {
        ((RegJButton) getObj(id, GUIObjTypeEnum.BUTTON)).setEnabled(enabled);
    }
    
    //MenuItem
    //AddActionListeners
    public void menuItemAddActionListener(String id, ActionListener listener) {
        if (listener == null) {
            throw new NullPointerException("Null menuItem listener");
        }        
        ((JMenuItem) getObj(id, GUIObjTypeEnum.MENUITEM)).addActionListener(listener);
    }
    //Set enabled
    public void menuItemSetEnabled(String id, boolean enabled) {
        ((JMenuItem) getObj(id, GUIObjTypeEnum.MENUITEM)).setEnabled(enabled);
    }
   
    //TextAreas
    //Get text in textArea
    public String textAreaGetText(String id) {
        return ((RegJTextArea) getObj(id, GUIObjTypeEnum.TEXTAREA)).getText();
    }
    //Set text in textArea
    public void textAreaSetText(String id, String text) {
        ((RegJTextArea) getObj(id, GUIObjTypeEnum.TEXTAREA)).setText(text);
    }
    //Set Caret Position in textArea
    public void textAreaSetCaretPosition(String id, int position) {
        ((RegJTextArea) getObj(id, GUIObjTypeEnum.TEXTAREA)).setCaretPosition(position);
    }
    //Get Caret Position in textArea
    public int textAreaGetCaretPosition(String id) {
        return ((RegJTextArea) getObj(id, GUIObjTypeEnum.TEXTAREA)).getCaretPosition();
    }
    //Set selected part
    public void textAreaSetSelection(String id, int start, int end) {
       ((RegJTextArea) getObj(id, GUIObjTypeEnum.TEXTAREA)).select(start, end);
    }
    //Get selected text
    public String textAreaGetSelectedText(String id) {
       return ((RegJTextArea) getObj(id, GUIObjTypeEnum.TEXTAREA)).getSelectedText();
    }
    //Get selected text starting location
    public int textAreaGetSelectionStart(String id) {
       return ((RegJTextArea) getObj(id, GUIObjTypeEnum.TEXTAREA)).getSelectionStart();
    }
    //Get selected text ending location
    public int textAreaGetSelectionEnd(String id) {
       return ((RegJTextArea) getObj(id, GUIObjTypeEnum.TEXTAREA)).getSelectionEnd();
    }
    //Request focus
    public void textAreaRequestFocus(String id) {
        ((RegJTextArea) getObj(id, GUIObjTypeEnum.TEXTAREA)).requestFocus();
    }
    //Add document listener
    public void textAreaAddDocumentListener(String id, DocumentListener listener) {
        ((RegJTextArea) getObj(id, GUIObjTypeEnum.TEXTAREA)).getDocument().addDocumentListener(listener);
    }
    //Set info //Customized info
    public void textAreaSetInfo(String id, DesignInfoSet designInfoSet, String pageName) {
        ((RegJTextArea) getObj(id, GUIObjTypeEnum.TEXTAREA)).setInfo(designInfoSet, pageName);
    }
    
    //TextField
    //Get text in textField
    public String textFieldGetText(String id) {
        return ((JTextField) getObj(id, GUIObjTypeEnum.TEXTFIELD)).getText();
    }
    //Set text in textArea
    public void textFieldSetText(String id, String text) {
        ((JTextField) getObj(id, GUIObjTypeEnum.TEXTFIELD)).setText(text);
    }
    //Set tooltip in textField
    public void textFieldSetToolTipText(String id, String s) {
        ((JTextField) getObj(id, GUIObjTypeEnum.TEXTFIELD)).setToolTipText(s);
    }
    //Add document listener
    public void textFieldAddDocumentListener(String id, DocumentListener listener) {
        ((JTextField) getObj(id, GUIObjTypeEnum.TEXTFIELD)).getDocument().addDocumentListener(listener);
    }
    
    //Combobox<String>
    //Load content
    public void comboBoxStrAddItem(String id, String item) {
        (getObjComboBoxStr(id)).addItem(item);
    }
    //Clear up content
    public void comboBoxStrRemoveAllItems(String id) {
        (getObjComboBoxStr(id)).removeAllItems();
    }
    //Get Item Count
    public int comboBoxStrGetItemCount(String id) {
        return getObjComboBoxStr(id).getItemCount();
    }
    //Set Selected Index
    public void comboBoxStrSetSelectedIndex(String id, int index) {
        JComboBox<String> cB = getObjComboBoxStr(id);
        if (index < -1 || index >= comboBoxStrGetItemCount(id)) {
            throw new IndexOutOfBoundsException("Index out of bound for combobox set selection: "
                + id);
        }
        cB.setSelectedIndex(index);
    }
    //Get Selected Index
    public int comboBoxStrGetSelectedIndex(String id) {
        return getObjComboBoxStr(id).getSelectedIndex();
    }
    //AddActionListeners
    public void comboBoxStrAddActionListener(String id, ActionListener listener) {
        if (listener == null) {
            throw new NullPointerException("Null comboBox<String> listener");
        }        
        getObjComboBoxStr(id).addActionListener(listener);
    }
    
    //Combobox<Integer>
    //Load content
    public void comboBoxIntAddItem(String id, Integer item) {
        (getObjComboBoxInt(id)).addItem(item);
    }
    //Clear up content
    public void comboBoxIntRemoveAllItems(String id) {
        (getObjComboBoxInt(id)).removeAllItems();
    }
    //Get Item Count
    public int comboBoxIntGetItemCount(String id) {
        return getObjComboBoxInt(id).getItemCount();
    }
    //Set Selected Index
    public void comboBoxIntSetSelectedIndex(String id, int index) {
        JComboBox<Integer> cB = getObjComboBoxInt(id);
        if (index < -1 || index >= comboBoxIntGetItemCount(id)) {
            throw new IndexOutOfBoundsException("Index out of bound for combobox set selection: "
                + id);
        }
        cB.setSelectedIndex(index);
    }
    //Get Selected Index
    public int comboBoxIntGetSelectedIndex(String id) {
        return getObjComboBoxInt(id).getSelectedIndex();
    }
    //AddActionListeners
    public void comboBoxIntAddActionListener(String id, ActionListener listener) {
        if (listener == null) {
            throw new NullPointerException("Null comboBox<Integer> listener");
        }        
        getObjComboBoxInt(id).addActionListener(listener);
    }
    
    
    //Label
    //Get label text
    public String labelGetText(String id) {
        return ((JLabel) getObj(id, GUIObjTypeEnum.LABEL)).getText();
    }
    //Set label text
    public void labelSetText(String id, String s) {
        ((JLabel) getObj(id, GUIObjTypeEnum.LABEL)).setText(s);
    }
    //Set label tooltip
    public void labelSetToolTipText(String id, String s) {
        ((JLabel) getObj(id, GUIObjTypeEnum.LABEL)).setToolTipText(s);
    }    
    
    //Table
    //Set table model
    public void tableSetModel(String id, RegTableModel model) {
        ((RegJTable) getObj(id, GUIObjTypeEnum.TABLE)).setModel(model);
    }
    //Get column by name
    public TableColumn tableGetColumn(String id, String colName) {
        return ((RegJTable) getObj(id, GUIObjTypeEnum.TABLE)).getColumn(colName);
    }
    //Get current table model
    public TableModel tableGetModel(String id) {
        return ((RegJTable) getObj(id, GUIObjTypeEnum.TABLE)).getModel();
    }    
    public Dimension tableGetPreferredSize(String id) {
        return ((RegJTable) getObj(id, GUIObjTypeEnum.TABLE)).getPreferredSize();
    }
    public int tableGetSelectedRow(String id) {
        return ((RegJTable) getObj(id, GUIObjTypeEnum.TABLE)).getSelectedRow();
    }
    public int tableGetSelectedColumn(String id) {
        return ((RegJTable) getObj(id, GUIObjTypeEnum.TABLE)).getSelectedColumn();
    }
    public void tableSetRowSelectionInterval(String id, int start, int end) {
        ((RegJTable) getObj(id, GUIObjTypeEnum.TABLE)).setRowSelectionInterval(start, end);
    }
    public void tableSetColumnSelectionInterval(String id, int start, int end) {
        ((RegJTable) getObj(id, GUIObjTypeEnum.TABLE)).setColumnSelectionInterval(start, end);
    }
    
    //Panel
    //SetVisible
    public void panelSetVisible(String id, boolean visible) {
        ((RegJPanel) getObj(id, GUIObjTypeEnum.PANEL)).setVisible(visible);
    }
    //Set Layout
    public void panelSetLayout(String id, LayoutManager lObj) {
        ((RegJPanel) getObj(id, GUIObjTypeEnum.PANEL)).setLayout(lObj);
    }
    //Set Box Layout
    public void panelSetBoxLayout(String id, int style) {
        RegJPanel p = (RegJPanel) getObj(id, GUIObjTypeEnum.PANEL);
        p.setLayout(new BoxLayout(p, style));
    }
    //Add Component
    public void panelAddComponent(String id, Component cObj) {
        ((RegJPanel) getObj(id, GUIObjTypeEnum.PANEL)).add(cObj);
    }
    //Set RemoveAll
    public void panelRemoveAllItems(String id) {
        RegJPanel p = ((RegJPanel) getObj(id, GUIObjTypeEnum.PANEL));
        p.removeAll();
        p.revalidate();
        p.repaint();
    }
    //Frame
    //SetVisible
    public void frameSetVisible(String id, boolean visible) {
        ((JFrame) getObj(id, GUIObjTypeEnum.FRAME)).setVisible(visible);
    }
    //Define default close operation
    public void frameSetDefaultCloseOperation(String id, int closeStrategy) {
        ((JFrame) getObj(id, GUIObjTypeEnum.FRAME)).setDefaultCloseOperation(closeStrategy);
    }
    //Set window listener
    public void frameAddWindowListener(String id, WindowAdapter listener) {
        ((JFrame) getObj(id, GUIObjTypeEnum.FRAME)).addWindowListener(listener);
    }
    //Dispatch event
    public void frameDispatchEvent(String id, int dispatchStrategy) {
        ((JFrame) getObj(id, GUIObjTypeEnum.FRAME)).dispatchEvent(new WindowEvent((JFrame) getObj(id, GUIObjTypeEnum.FRAME), dispatchStrategy));
    }
    //frame pack
    public void framePack(String id) {
        ((JFrame) getObj(id, GUIObjTypeEnum.FRAME)).pack();
    }
    //frame open file dialog
    public JFileChooser frameFileChooser(String id, Integer mode, FileNameExtensionFilter filter) {
        JFileChooser fileOpen = new JFileChooser();
        if (mode != null) {
            fileOpen.setFileSelectionMode(mode);
        }
        if (filter != null) {
            fileOpen.setFileFilter(filter);
        }            
        fileOpen.showOpenDialog((JFrame) getObj(id, GUIObjTypeEnum.FRAME));
        return fileOpen;
    }
    //frame Get an JFileChooser
    public void frameShowOpenDialog(String id, JFileChooser fileOpen) {
        fileOpen.showOpenDialog((JFrame) getObj(id, GUIObjTypeEnum.FRAME));        
    }
}