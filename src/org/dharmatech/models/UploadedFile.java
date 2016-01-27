/* Uploaded File */
/* Author: Wei Zhang
   Version date: 2016 Jan 23
*/
/* API
public class UploadedFile {
    public UploadedFile(String filePath, String uploadsResourcesRel) {}
    public void setFilePath(String filePath) {}
    public String getFileName() {}
    public String getParentPath() {}
    public String getFilePath() {}
    public String getRelativeFilePath() {}
    public String getWebFilePath() {}
    public String getFileSize() {}
    public String getFileExtension() {}
    public String genRecord() {}
    public static void main(String[] args) {}
}
*/
package org.dharmatech.models;

import java.io.*;

public class UploadedFile {
    private String filePath;
    private String fileSize;
    private final String uploadsResourcesRel;
    //constructor with the file path, and the upload path string constant
    public UploadedFile(String filePath, String uploadsResourcesRel) {
        this.uploadsResourcesRel = uploadsResourcesRel;
        filePath = filePath.trim();
        setFilePath(filePath);
    }
    //Calculate the file size 
    private String fileSizeConvert(long fileSizeInBytes) {
        double fileSizeD = fileSizeInBytes;
        if (fileSizeD < 1024) {
            return fileSizeD + "Byte";
        }
        fileSizeD = ((double) Math.round(fileSizeD/1024*100))/100;
        if (fileSizeD < 1024) {
            return fileSizeD + "KB";
        }
        fileSizeD = ((double) Math.round(fileSizeD/1024*100))/100;
        if (fileSizeD < 1024) {
            return fileSizeD + "MB";
        }
        fileSizeD = ((double) Math.round(fileSizeD/1024*100))/100;
        if (fileSizeD < 1024) {
            return fileSizeD + "GB";
        }
        fileSizeD = ((double) Math.round(fileSizeD/1024*100))/100;
        return fileSizeD + "TB";        
    }
    //Set the file path
    public void setFilePath(String filePath) {
        if (filePath == null) {
            throw new NullPointerException("Null filePath.");
        }
        this.filePath = filePath;
        File f = new File(filePath);
        if (!f.exists() || !f.isFile()) {
            //throw new IllegalArgumentException("Not a legal existing file: \""
            //    + filePath + "\"");
        }
        this.fileSize = fileSizeConvert(f.length());
    }
    //Get file name
    public String getFileName() {
        return (new File(filePath)).getName();
    }
    //Get parent absolute path
    public String getParentPath() {
        return (new File((new File(filePath)).getAbsolutePath()))
            .getParent();
    }
    //get file path
    public String getFilePath() {
        return filePath;
    }
    //get relative file path without anything before "uploads"
    public String getRelativeFilePath() {
        String relativeFilePath = filePath;
        int i = relativeFilePath.indexOf(File.separator + uploadsResourcesRel + File.separator);
        if ( i > -1) {
            relativeFilePath = relativeFilePath.substring(i + 1);
        }
        return relativeFilePath;
    }
    //get the web-recognizable path by replacing separator with "/"
    public String getWebFilePath() {
        return getRelativeFilePath().replace(File.separator, "/");
    }
    //get file size expression
    public String getFileSize() {
        return fileSize;
    }
    //get file extension
    public String getFileExtension() {
        String extension = "";
        int i = filePath.lastIndexOf('.');
        if (i > 0) {
            extension = filePath.substring(i+1).toUpperCase();
        }
        return extension;
    }
    //get the record string to store in cfg
    public String genRecord() {
        return getRelativeFilePath();
    }
    
    public static void main(String[] args) {
        String path = "C:\\Users\\NovDec2015.pdf";
        UploadedFile upF = new UploadedFile(path, "uploads");
        
        System.out.println(upF.getFileName());
        System.out.println(upF.getParentPath());
        System.out.println(upF.getFilePath());
        System.out.println(upF.getRelativeFilePath());
        System.out.println(upF.getWebFilePath());
        System.out.println(upF.getFileSize());
        System.out.println(upF.getFileExtension());
        System.out.println(upF.genRecord());
    }
}