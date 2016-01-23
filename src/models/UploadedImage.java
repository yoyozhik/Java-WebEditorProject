/* Uploaded File */

package models;
import java.io.*;
import java.util.regex.*;

public class UploadedImage extends UploadedFile {
    private String width = "";
    private String height = "";
    
    public UploadedImage(String filePath, String width, String height) {
        super(filePath);
        if (width == null) {
            throw new NullPointerException("Null width.");
        }
        if (height == null) {
            throw new NullPointerException("Null height.");
        }
        this.width = width;
        this.height = height;
    }
    public UploadedImage(String imageRecord) {
        this(parseRecord(imageRecord, 1), 
            parseRecord(imageRecord, 2), 
            parseRecord(imageRecord, 3));
    }
    public void setWidth(String width) {
        this.width = width;
    }
    public void setWidth(int width) {
        this.width = Integer.toString(width);
    }
    public void setHeight(String height) {
        this.height = height;
    }
    public void setHeight(int height) {
        this.height = Integer.toString(height);
    }
    public String getWidth() {
        return width;
    }
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
        Pattern p = Pattern.compile("(?i)(.*?) width:(\\S*) height:(\\S*)");
        Matcher m = p.matcher(record);
        if (m.find()) {
            return m.group(i);
        } else {
            return null;
        }
    }
    
    public static void main(String[] args) {
    }
}