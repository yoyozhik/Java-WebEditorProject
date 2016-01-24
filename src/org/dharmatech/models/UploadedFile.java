/* Uploaded File */

package org.dharmatech.models;

import java.io.*;

public class UploadedFile {
    private String filePath;
    private String fileSize;
    
    public UploadedFile(String filePath) {
        filePath = filePath.trim();
        setFilePath(filePath);
    }
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
    public String getFileName() {
        return (new File(filePath)).getName();
    }
    public String getParentPath() {
        return (new File((new File(filePath)).getAbsolutePath()))
            .getParent();
    }
    public String getFilePath() {
        return filePath;
    }
    public String getRelativeFilePath() {
        String relativeFilePath = filePath;
        int i = relativeFilePath.indexOf(File.separator + "uploads" + File.separator);
        if ( i > -1) {
            relativeFilePath = relativeFilePath.substring(i + 1);
        }
        return relativeFilePath;
    }
    public String getWebFilePath() {
        return getRelativeFilePath().replace(File.separator, "/");
    }
    public String getFileSize() {
        return fileSize;
    }
    public String getFileExtension() {
        String extension = "";
        int i = filePath.lastIndexOf('.');
        if (i > 0) {
            extension = filePath.substring(i+1).toUpperCase();
        }
        return extension;
    }
    public String genRecord() {
        return getRelativeFilePath();
    }
    
    public static void main(String[] args) {
        String p = "C:\\Users\\zhangwei\\Desktop\\WebEditor Java\\WebRoot\\Website\\Activities.html";
        p = "C:\\Users\\zhangwei\\Desktop\\WebEditor Java\\WebRoot\\Website\\uploads\\Newsletter\\FILE_18\\MBA Newsletter NovDec2015.pdf";
        System.out.println(p);
        File f = new File(p);
        System.out.println(f.exists());
    }
}