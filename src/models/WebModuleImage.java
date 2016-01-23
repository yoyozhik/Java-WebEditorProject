/* Image Utilities 
*/
/* Author: Wei Zhang
   Latest Version: 2016 Jan 18
*/
/*API
class WebModuleImage {
    public WebModuleImage()
    
}
*/

package models;
import utilities.*;
import controllers.*;
import views.*;

import java.util.HashMap;
import java.util.regex.*;
import java.io.*;


public class WebModuleImage extends WebModuleDefault{
    private String sourcePath = null;
    private String width = null;
    private String height = null;
    private String fileName = null;

    //Constructor
    public WebModuleImage(HashMap<String, String> designSet, String pageName, int id) {
        super(designSet, pageName, id);
        this.typeEnum = WebModuleEnum.IMAGE;
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
            UploadedImage upFile = new UploadedImage(line);
            sourcePath = upFile.getFilePath(); 
            width = upFile.getWidth();
            height = upFile.getHeight();            
            fileName = upFile.getFileName();
        }
    }
    
    public String getSourcePath() {
        if (sourcePath == null) {
            genCfgInfo();
        }
        return sourcePath;
    }    
    public String getWidth() {
        if (width == null) {
            genCfgInfo();
        }
        return width;
    }    
    public String getHeight() {
        if (height == null) {
            genCfgInfo();
        }
        return height;
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
        UploadedImage upImage = new UploadedImage(webDir + File.separator + detail);
        Pattern p = Pattern.compile("[^ ]");
        String imageWidthStr = "";
        if (p.matcher(upImage.getWidth()).find()) {
            imageWidthStr = " width:\"" + upImage.getWidth() + "\"";
        }
        String imageHeightStr = "";
        if (p.matcher(upImage.getHeight()).find()) {
            imageHeightStr = " height:\"" + upImage.getHeight() + "\"";
        }
        detail = "<img class=\"uploadedImage\" " + imageWidthStr + imageHeightStr 
            + " src=\"" + upImage.getWebFilePath()
            + "\">";
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
    public String genRecord(String text) {
        return null;
    }
    //Not overriding because argument list is different
    public String genRecord(String destPath, String width, String height) {
        if (width == null) {
            throw new NullPointerException("Null width");
        }
        if (height == null) {
            throw new NullPointerException("Null height");
        }
        return (new UploadedImage(destPath, width, height)).genRecord();
    }
    
    //start editor
    @Override
    public void startEditor() {
        FileUploaderController uploaderController = null;
        try {
            uploaderController 
                = new FileUploaderController(getDesignSet(), 
                getCfgPath(), getUploadDir(), getPageName(), getID(), WebModuleEnum.IMAGE);
        } catch (IOException ex) {
            System.out.println("IOException when launching uploader");
            ex.printStackTrace();
        } catch(Exception ex) {
            System.out.println("Exception when launching uploader");
        }
        uploaderController.start();
    }
    
    private String getFullUploadedPath(String uploadedPath) {
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