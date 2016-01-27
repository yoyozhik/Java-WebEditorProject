/* File module */

/* Author: Wei Zhang
   Latest Version: 2016 Jan 23
*/
/*API
class WebModuleFile {
    public WebModuleFile(DesignInfoSet designInfoSet, String pageName, int id) {}
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


public class WebModuleFile extends WebModuleDefault{
    private String sourcePath = null;
    private String fileName = null;
    //Constructor
    public WebModuleFile(DesignInfoSet designInfoSet, String pageName, int id) {
        super(designInfoSet, pageName, id);
        this.typeEnum = WebModuleEnum.FILE;
    }
    //get upload dir
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
            UploadedFile upFile = new UploadedFile(line, getDesignSetItem("uploadsResourcesRel"));
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
        String rootDir = getDesignSetItem("rootDir");
        String websiteDirRel = getDesignSetItem("websiteDirRel");
        String imagesResourcesRel = getDesignSetItem("imagesResourcesRel");
        if (rootDir == null) {
            throw new NullPointerException("Null rootDir");
        }
        if (websiteDirRel == null) {
            throw new NullPointerException("Null websiteDirRel");
        }
        if (imagesResourcesRel == null) {
            throw new NullPointerException("Null imagesResourcesRel");
        }
        String webDir = rootDir + File.separator + websiteDirRel;
        UploadedFile upFile = new UploadedFile(webDir + File.separator + detail, 
            getDesignSetItem("uploadsResourcesRel"));
        detail = "<div>\n<table><tr><td><a href=\"" + upFile.getWebFilePath()
            + "\"><img width=\"40\" height=\"40\" "
            + "src=\"" + imagesResourcesRel + "/" 
            + getFileIconFile() + "\"></a></td><td><table><tr><td class=\"File\">"
            + upFile.getFileName() + "</td></tr><tr><td class=\"FileDownload\"><a href=\""
            + upFile.getWebFilePath() + "\">Download</a> <span class=\"FileDownloadSize\">Size: "
            + upFile.getFileSize() + "</span></td></tr></table></td></tr></table>\n</div>";
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
            + File.separator + typicalIcon);
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
    public String genRecord(String destPath) {
        return (new UploadedFile(destPath, getDesignSetItem("uploadsResourcesRel"))).genRecord();
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
        WebModuleFile module = new WebModuleFile(new DesignInfoSet(new HashMap<String, String>()),
            "index", 2);
        System.out.println(module.getID());
        System.out.println(module.getPageName());
        module.setPageName("contact");
        module.setID(3);
        System.out.println(module.getType());
        System.out.println(module.getPageName());
        System.out.println(module.getCfgPath());
        System.out.println(module.getFileExtension("Test.pdf"));
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