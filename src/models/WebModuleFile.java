/* File Utilities 
*/
/* Author: Wei Zhang
   Latest Version: 2016 Jan 18
*/
/*API
class WebModuleFile {
    public WebModuleFile()
    
}
*/

package models;
import utilities.*;
import controllers.*;
import views.*;

import java.util.HashMap;
import java.util.regex.*;
import java.io.*;


public class WebModuleFile extends WebModuleDefault{
    private String sourcePath = null;
    private String fileName = null;
    //Constructor
    public WebModuleFile(HashMap<String, String> designSet, String pageName, int id) {
        super(designSet, pageName, id);
        this.typeEnum = WebModuleEnum.FILE;
    }
    
    public String getUploadDir() {
        return getDesignInfo("rootDir") + File.separator 
            + getDesignInfo("websiteDirRel") + File.separator
            + "uploads" + File.separator
            + getPageName() + File.separator
            + typeEnum.getValue() + "_" + getID();
    }
    
    private void genCfgInfo() {
        String cfgPath = getCfgPath();
        String line = FileUtilities.readLine(cfgPath, "UTF-8");
        if (line != null) { //Already uploaded before
            line = getFullUploadedPath(line.trim());
            UploadedFile upFile = new UploadedFile(line);
            sourcePath = upFile.getFilePath(); 
            fileName = upFile.getFileName();
        }
    }
    
    public String getSourcePath() {
        if (sourcePath == null) {
            genCfgInfo();
        }
        return sourcePath;
    }
    
    public String getFileName() {
        if (fileName == null) {
            genCfgInfo();
        }
        return fileName;
    }
    
    //retrieve content
    @Override
    public String retrieveContent() {
        String detail = getResourceData();
        String rootDir = getDesignInfo("rootDir");
        String websiteDirRel = getDesignInfo("websiteDirRel");
        if (rootDir == null) {
            throw new NullPointerException("Null rootDir");
        }
        if (websiteDirRel == null) {
            throw new NullPointerException("Null websiteDirRel");
        }
        String webDir = rootDir + File.separator + websiteDirRel;
        UploadedFile upFile = new UploadedFile(webDir + File.separator + detail);
        detail = "<div>\n<table><tr><td><a href=\"" + upFile.getWebFilePath()
            + "\"><img width=\"40\" height=\"40\" src=\"/images/" 
            + upFile.getFileExtension() + ".PNG\"></a></td><td><table><tr><td class=\"File\">"
            + upFile.getFileName() + "</td></tr><tr><td class=\"FileDownload\"><a href=\""
            + upFile.getWebFilePath() + "\">Download</a> <span class=\"FileDownloadSize\">Size: "
            + upFile.getFileSize() + "</span></td></tr></table></td></tr></table>\n</div>";
        return detail;
    }
    
    //upload
    public String upload(String sourcePath, String destPath) {
        if (sourcePath == null) {
            throw new NullPointerException("Null sourcePath");
        }
        String status = FileUtilities.uploadFile(sourcePath, destPath);
        return status;
    }
    
    @Override
    public String genRecord(String destPath) {
        return (new UploadedFile(destPath)).genRecord();
    }
    //save
    //start editor
    @Override
    public void startEditor() {
        FileUploaderController uploaderController = null;
        try {
            uploaderController 
                = new FileUploaderController(getDesignSet(), 
                getCfgPath(), getUploadDir(), getPageName(), getID(), WebModuleEnum.FILE);
        } catch (IOException ex) {
            System.out.println("IOException when launching uploader");
            ex.printStackTrace();
        } catch(Exception ex) {
            System.out.println("Exception when launching uploader");
            ex.printStackTrace();
        }
        uploaderController.start();
    }
    //Get full uploaded path from a partial one starting with "uploads\"
    protected String getFullUploadedPath(String uploadedPath) {
        if (uploadedPath.contains("uploads" + File.separator) 
            && !uploadedPath.contains(File.separator + "uploads" + File.separator)) { //not full path
            String rootDir = getDesignSetItem("rootDir");
            String websiteDirRel = getDesignSetItem("websiteDirRel");
            if (rootDir == null) {
                throw new NullPointerException("Null rootDir");
            }
            if (websiteDirRel == null) {
                throw new NullPointerException("Null websiteDirRel");
            }
            return rootDir + File.separator + websiteDirRel + File.separator + uploadedPath;
        }
        return uploadedPath;
    } 
}