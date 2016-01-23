/* Configure Pages */
/*API
public class PagesCfg {
    public PagesCfg(String filepath)
    public void start(JFrame frame) 
    public void start(JFrame frame, WebEditor webEditor) 
    public static void main(String[] args)
}
*/
package views;
import models.*;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.*;
import java.util.*;

public class PagesCfg extends WebEditorGUIGeneric {
    
    private JFrame editorGUI;
    private RegJTable pagesTb;
    private WebEditor webEditor;
    private RegJButton save;
    private RegJButton close;
    private RegJButton insert;
    private RegJButton delete;
    private RegJButton moveUp;
    private RegJButton moveDown;
    
    private final String[] colNames = {"Level", "Page Name", "Page Title", "Shown in Nav"};
    public PagesCfg() {
        setupGUI();
        HashMap<String, RegJButton> pcButtonGroup = new HashMap<String, RegJButton>();
        //HashMap<String, RegJTextArea> pcTextAreaGroup = new HashMap<String, RegJTextArea>();
        //HashMap<String, JLabel> pcLabelGroup = new HashMap<String, JLabel>();
        HashMap<String, JFrame> pcFrameGroup = new HashMap<String, JFrame>();
        HashMap<String, RegJTable> pcTableGroup = new HashMap<String, RegJTable>();
        
        pcButtonGroup.put("save".toLowerCase(), save);
        pcButtonGroup.put("close".toLowerCase(), close);
        pcButtonGroup.put("insert".toLowerCase(), insert);
        pcButtonGroup.put("delete".toLowerCase(), delete);
        pcButtonGroup.put("moveUp".toLowerCase(), moveUp);
        pcButtonGroup.put("moveDown".toLowerCase(), moveDown);
        
        pcFrameGroup.put("editorGUI".toLowerCase(), editorGUI);
        
        pcTableGroup.put("pagesTb".toLowerCase(), pagesTb);

        initGUIObjsButton(pcButtonGroup);
        //initGUIObjsLabel(pcLabelGroup);
        //initGUIObjsTextArea(pcTextAreaGroup);
        initGUIObjsFrame(pcFrameGroup);
        initGUIObjsTable(pcTableGroup);

    }

    //GUI setup
    private void setupGUI() {
        JFrame editorGUI = new JFrame();
        this.editorGUI = editorGUI;
        RegJPanel top = new RegJPanel();
        RegJPanel center = new RegJPanel();
        RegJPanel bot = new RegJPanel();
        editorGUI.getContentPane().add(BorderLayout.NORTH, top);
        editorGUI.getContentPane().add(BorderLayout.CENTER, center);
        editorGUI.getContentPane().add(BorderLayout.SOUTH, bot);
        
        //Top
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        
        //Center
        center.setLayout(new BorderLayout());
        RegJPanel left = new RegJPanel();
        center.add(BorderLayout.WEST, left);
        //Table
        Object data[][] = new Object[1][4];
        RegJTable pagesTb = new RegJTable(data, colNames);
        JScrollPane scroll = new JScrollPane(pagesTb);
        scroll.setVerticalScrollBarPolicy(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        left.add(scroll);
        this.pagesTb = pagesTb;
        //Buttons
        RegJPanel right = new RegJPanel();
        center.add(BorderLayout.CENTER, right);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        RegJButton insert = new RegJButton("Insert");
        RegJButton delete = new RegJButton("Delete");
        RegJButton moveUp = new RegJButton("Move Up");
        RegJButton moveDown = new RegJButton("Move Down");
        right.add(insert);
        right.add(delete);
        right.add(moveUp);
        right.add(moveDown);
        Dimension butMax = moveDown.getMaximumSize();
        insert.setMaximumSize(butMax);
        delete.setMaximumSize(butMax);
        moveUp.setMaximumSize(butMax);
        moveDown.setMaximumSize(butMax);
        this.insert = insert;
        this.delete = delete;
        this.moveUp = moveUp;
        this.moveDown = moveDown;
        //table.getDocument().getDocumentListener(new EditDocumentListener());
        //Bot
        bot.setLayout(new FlowLayout(FlowLayout.LEADING));
        RegJButton save = new RegJButton("Save");
        RegJButton close = new RegJButton("Close");
        bot.add(save);
        bot.add(close);
        this.save = save;
        this.close = close;
        
        //GUI
        editorGUI.setSize(700,500);
        
    }
    
    
    public static void main(String[] args) {
        
    }
}
