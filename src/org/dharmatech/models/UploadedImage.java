/* Uploaded File */
/* Author: Wei Zhang
   Version date: 2016 Jan 23
*/
/* API
public class UploadedImage {
    public UploadedImage(String filePath, String uploadsResourcesRel) {}
    public UploadedImage(String imageRecord, String uploadsResourcesRel) {}
    public void setFilePath(String filePath) {}
    public String getFileName() {}
    public String getParentPath() {}
    public String getFilePath() {}
    public String getRelativeFilePath() {}
    public String getWebFilePath() {}
    public String getFileSize() {}
    public String getFileExtension() {}
    public void setWidth(String width) {}
    public void setWidth(int width) {}
    public void setHeight(String height) {}
    public void setHeight(int height) {}
    public String getWidth() {}
    public String getHeight() {}
    public String genRecord() {}
    public static void main(String[] args) {}
}
*/
package org.dharmatech.models;

import java.io.*;
import java.util.regex.*;

public class UploadedImage extends UploadedFile {
    private String width = "";
    private String height = "";
    //constructor with the file path, width string, height string, and upload path constant
    public UploadedImage(String filePath, String width, String height, String uploadsResourcesRel) {
        super(filePath, uploadsResourcesRel);
        if (width == null) {
            throw new NullPointerException("Null width.");
        }
        if (height == null) {
            throw new NullPointerException("Null height.");
        }
        this.width = width;
        this.height = height;
    }
    //constructor with the image record, and upload path constant
    public UploadedImage(String imageRecord, String uploadsResourcesRel) {
        this(parseRecord(imageRecord, 1), 
            parseRecord(imageRecord, 2), 
            parseRecord(imageRecord, 3),
            uploadsResourcesRel);
    }
    //set width string
    public void setWidth(String width) {
        this.width = width;
    }
    //set width string
    public void setWidth(int width) {
        this.width = Integer.toString(width);
    }
    //set height string
    public void setHeight(String height) {
        this.height = height;
    }
    //set height string
    public void setHeight(int height) {
        this.height = Integer.toString(height);
    }
    //get width string
    public String getWidth() {
        return width;
    }
    //get height string
    public String getHeight() {
        return height;
    }
    //Override
    public String genRecord() {
        return getRelativeFilePath() 
            + " width:" + width 
            + " height:" + height;
    }
    //Parse image record line
    //Made static to allow constructor call before super()
    private static String parseRecord(String record, int i) {
        if (i < 1 || i > 3) {
            throw new IllegalArgumentException("i can only be 1, 2, 3");
        }
        record = record.trim();
        //record = record.replace("(", "\(");
        //record = record.replace(")", "\)");
        Pattern p = Pattern.compile("(?i)(.*?) width:(\\S*) height:(\\S*)");
        Matcher m = p.matcher(record);
        if (m.find()) {
            return m.group(i);
        } else {
            return null;
        }
    }
    
    public static void main(String[] args) {
        String path = "C:\\Users\\NovDec2015.png";
        UploadedImage upF = new UploadedImage(path, "w:1", "h:2", "uploads");
        
        System.out.println(upF.getFileName());
        System.out.println(upF.getParentPath());
        System.out.println(upF.getFilePath());
        System.out.println(upF.getRelativeFilePath());
        System.out.println(upF.getWebFilePath());
        System.out.println(upF.getFileSize());
        System.out.println(upF.getFileExtension());
        System.out.println(upF.genRecord());
        System.out.println(upF.getWidth());
        System.out.println(upF.getHeight());
        upF.setWidth(3);
        upF.setHeight(6);
        System.out.println(upF.getWidth());
        System.out.println(upF.getHeight());
    }
}