/* Image Utilities 
*/
/* Author: Wei Zhang
   Latest Version: 2016 Jan 23
*/
/*API
class WebModuleImage {
    public WebModuleImage(DesignInfoSet designInfoSet, String pageName, int id) {}
    public void setDesignInfoSet(DesignInfoSet designInfoSet) {}
    public DesignInfoSet getDesignInfoSet() {}
    public String getDesignSetItem(String key) {}
    public void setPageName(String pageName) {}
    public void setID(int id) {}
    public WebModuleEnum getType() {}
    public String getPageName() {}
    public int getID() {}
    public String getCfgPath() {}
    public String getFileExtension(String fileStr) {}
    public String getTarget() {}
    public String getTargetWithMarker() {}
    public void save(String cfgText) {}
    public String retrieveContent() {}
    public String getResourceData() {}
    public String genRecord(String text) {}
    public boolean delete() {}
    public void startEditor() {}
    public String getMarkedContent() {}    
    
    public String getUploadDir() {}
    public String getSourcePath() {}
    public String getFileName() {}
    public String upload(String sourcePath, String destPath) {}
    protected String getFullUploadedPath(String uploadedPath) {}
    public static void main(String[] args) {}
}
*/

package org.dharmatech.models;
import org.dharmatech.utilities.*;
import org.dharmatech.controllers.*;
import org.dharmatech.views.*;

import java.util.HashMap;
import java.util.regex.*;
import java.io.*;


public class WebModuleImage extends WebModuleDefault{
    private String sourcePath = null;
    private String width = null;
    private String height = null;
    private String fileName = null;

    //Constructor
    public WebModuleImage(DesignInfoSet designInfoSet, String pageName, int id) {
        super(designInfoSet, pageName, id);
        this.typeEnum = WebModuleEnum.IMAGE;
    }
    
    public String getUploadDir() {
        String uploadsResourcesRel = getDesignSetItem("uploadsResourcesRel");
        if (uploadsResourcesRel == null) {
            throw new NullPointerException("Null uploadsResourcesRel");
        }
        return getDesignSetItem("rootDir") + File.separator 
            + getDesignSetItem("websiteDirRel") + File.separator
            + uploadsResourcesRel + File.separator
            + getPageName() + File.separator
            + typeEnum.getValue() + "_" + getID();
    }
    
    private void genCfgInfo() {
        String cfgPath = getCfgPath();
        String line = FileUtilities.readLine(cfgPath, "UTF-8");
        if (line != null) { //Already uploaded before
            line = FileUtilities.readProcSeparator(line);
            line = getFullUploadedPath(line.trim());
            UploadedImage upFile = new UploadedImage(line, 
                getDesignSetItem("uploadsResourcesRel"));
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
        String rootDir = getDesignSetItem("rootDir");
        String websiteDirRel = getDesignSetItem("websiteDirRel");
        if (rootDir == null) {
            throw new NullPointerException("Null rootDir");
        }
        if (websiteDirRel == null) {
            throw new NullPointerException("Null websiteDirRel");
        }
        String webDir = rootDir + File.separator + websiteDirRel;
        UploadedImage upImage = new UploadedImage(webDir + File.separator + detail,
            getDesignSetItem("uploadsResourcesRel"));
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
    
    //Get displayable image file
    private String getFileIconFile() {
        String imagesResourcesDesignResourceRel 
            = getDesignSetItem("imagesResourcesDesignResourceRel");
        if (imagesResourcesDesignResourceRel == null) {
            throw new NullPointerException("Null imagesResourcesDesignResourceRel");
        }
        String typicalIcon = getFileExtension(getFileName()) + ".PNG";
        File f = new File(imagesResourcesDesignResourceRel 
            + File.separator + "typicalIcon");
        if (f.exists()) {
            return typicalIcon;
        } else {
            return "FILE.PNG";
        }
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
        String line = (new UploadedImage(destPath, width, height,
            getDesignSetItem("uploadsResourcesRel"))).genRecord();
        return line;
    }
    
    //Delete the module
    @Override
    public boolean delete() {
        //Delete uploaded files
        String uploadDir = getUploadDir();
        boolean success = FileUtilities.deleteFolder(uploadDir);
        //Delete cfg file
        String cfgPath = getCfgPath();
        success = FileUtilities.deleteFile(cfgPath) && success;
        return success;
    }

    
    //start editor
    @Override
    public void startEditor() {
        FileUploaderController uploaderController = null;
        try {
            uploaderController 
                = new FileUploaderController(getDesignInfoSet(), 
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
        String uploadsResourcesRel = getDesignSetItem("uploadsResourcesRel");
        if (uploadsResourcesRel == null) {
            throw new NullPointerException("Null uploadsResourcesRel");
        }
        if (uploadedPath.contains(uploadsResourcesRel + File.separator) 
            && !uploadedPath.contains(File.separator + uploadsResourcesRel + File.separator)) { //not full path
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
    
    public static void main(String[] args) {
        WebModuleImage module = new WebModuleImage(new DesignInfoSet(new HashMap<String, String>()),
            "index", 2);
        System.out.println(module.getID());
        System.out.println(module.getPageName());
        module.setPageName("contact");
        module.setID(3);
        System.out.println(module.getType());
        System.out.println(module.getPageName());
        System.out.println(module.getCfgPath());
        System.out.println(module.getFileExtension("Test.png"));
        System.out.println(module.getTarget());
        System.out.println(module.getTargetWithMarker());
        System.out.println(module.retrieveContent());
        System.out.println(module.getResourceData());
        System.out.println(module.genRecord("test Text for Record"));
        System.out.println(module.getMarkedContent());
        System.out.println(module.getUploadDir());
        System.out.println(module.getSourcePath());
        System.out.println(module.getFileName());
        System.out.println(module.getFullUploadedPath(module.getSourcePath()));
        module.startEditor();
    } 
}