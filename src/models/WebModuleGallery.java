/* Gallery Utilities 
*/
/* Author: Wei Zhang
   Latest Version: 2016 Jan 18
*/
/*API
class WebModuleGallery {
    public WebModuleGallery()
    
}
*/

package models;
import utilities.*;
import controllers.*;
import views.*;

import java.util.HashMap;
import java.util.regex.*;
import java.io.*;


public class WebModuleGallery extends WebModuleDefault{
    private Object[][] data = null;
    private int npr = 0;
    private int descriptMode = 0;
    //Constructor
    public WebModuleGallery(HashMap<String, String> designSet, String pageName, int id) {
        super(designSet, pageName, id);
        this.typeEnum = WebModuleEnum.GALLERY;
    }
    
    //Retrieve data from cfg file if previously set
    private void genCfgInfo() {
        String cfgPath = getCfgPath();
        File f = new File(cfgPath);
        if (f.exists() && f.isFile()) {
            String text = FileUtilities.read(cfgPath, "UTF-8");
            String[] lines = text.split("\n");
            this.data = new Object[lines.length][3];
            int i = 0;
            for (String ln : lines) {
                String[] pars = ln.split(",");  //What if the content itself has ","?
                if (pars.length >= 5) {
                    //System.out.println(line);
                    String sourcePath = getFullUploadedPath(pars[0].replace("\"", ""));
                    String fileName = (new File(sourcePath)).getName();
                    String fileDescription = pars[1].replace("\"", "");
                    this.npr = Integer.parseInt(pars[2]);
                    this.descriptMode = Integer.parseInt(pars[3]);
                    //String galleryID = pars[4].replace("\"", "");
                    data[i][0] = sourcePath;
                    data[i][1] = fileName;
                    data[i][2] = fileDescription;
                    i++;
                }
            }
            for (int j = i; j < lines.length; j++) { //Fill the rest
                data[j][0] = "";
                data[j][1] = "";
                data[j][2] = "";
            }
        }        
    }

    public Object[][] getData() {
        if (data == null) {
            genCfgInfo();
        }
        return data;
    }
    public int getNpr() {
        if (npr == 0) {
            genCfgInfo();
        }
        return npr;
    }
    public int getDescriptMode() {
        if (descriptMode == 0) {
            genCfgInfo();
        }
        return descriptMode;
    }
    
    public int getMaxNpr() {
        return 5;
    }
    public int getDefNpr() {
        return 4;
    }
    public String[] getDescriptModeStrs() {
        String[] descriptModeStrs = {"1 - Caption When Hover", "2 - Caption Always", "3 - No Caption"};
        return descriptModeStrs;
    }
    public int getDefDescriptMode() {
        return 1;
    }
    public String getUploadDir() {
        return getDesignInfo("rootDir") + File.separator 
            + getDesignInfo("websiteDirRel") + File.separator
            + "uploads" + File.separator
            + getPageName() + File.separator
            + typeEnum.getValue() + "_" + getID();
    }
    //retrieve content
    @Override
    public String retrieveContent() {
        String rootDir = getDesignInfo("rootDir");
        String websiteDirRel = getDesignInfo("websiteDirRel");
        if (rootDir == null) {
            throw new NullPointerException("Null rootDir");
        }
        if (websiteDirRel == null) {
            throw new NullPointerException("Null websiteDirRel");
        }
        String webDir = rootDir + File.separator + websiteDirRel;
        String text = getResourceData();
        String[] lines = text.split("\n");
        StringBuilder textSB = new StringBuilder("");
        for (String ln : lines) {
            String[] pars = ln.split(",");  //What if the content itself has ","?
            if (pars.length >= 5) {
                String filePath = pars[0].replace("\"", "");
                String fileName = (new File(filePath)).getName();
                String fileParent = (new File(filePath)).getParent();
                String thumbPath = fileParent + File.separator + "thumb-" + fileName;
                String mobilePath = fileParent + File.separator + "mobile-" + fileName;
                String fileDescription = pars[1].replace("\"", "");
                int npr = Integer.parseInt(pars[2]);
                int descriptMode = Integer.parseInt(pars[3]);
                String galleryClass = pars[4].replace("\"", "");
                int colWidth = Math.round(100 / npr) - 1;
                
                filePath = filePath.replace(File.separator, "/");
                thumbPath = thumbPath.replace(File.separator, "/");
                mobilePath = mobilePath.replace(File.separator, "/");
                
                if (!fileDescription.equals("") && descriptMode == 2) {
                    textSB.append("<a class='hovertext' "); 
                    textSB.append(" alt=\"" + fileDescription + "\"");
                    textSB.append(" href=\"" + filePath + "\"");
                    textSB.append(" data-lightbox=\"" + galleryClass + "\"");
                    textSB.append(" data-title=\"" + fileDescription + "\">");
                    textSB.append("<img style=\"width:" + colWidth + "%;\"");
                    textSB.append(" src=\"" + thumbPath + "\"></a>\n");
                } else if (!fileDescription.equals("") && descriptMode == 1) {
                    textSB.append("<a class='alwaystext' ");
                    textSB.append(" alt=\"" + fileDescription + "\"");
                    textSB.append(" href=\"" + filePath + "\"");
                    textSB.append(" data-lightbox=\"" + galleryClass + "\"");
                    textSB.append(" data-title=\"" + fileDescription + "\">");
                    textSB.append("<img style=\"width:" + colWidth + "%;\"");
                    textSB.append(" src=\"" + thumbPath + "\"></a>\n");
                } else {
                    textSB.append("<a");
                    textSB.append(" alt=\"" + fileDescription + "\"");
                    textSB.append(" href=\"" + filePath + "\"");
                    textSB.append(" data-lightbox=\"" + galleryClass + "\"");
                    textSB.append(" data-title=\"" + fileDescription + "\">");
                    textSB.append("<img style=\"width:" + colWidth + "%;\"");
                    textSB.append(" src=\"" + thumbPath + "\"></a>\n");
                }
            }
        }
        String detail = new String(textSB);
        //detail = detail.replace("uploads/", "/uploads/");
        //detail = detail.replace("//uploads/", "/uploads/");
        return detail;
    }
    
    public void upload(Object[][] data) {
        StringBuilder textSB = new StringBuilder();
        int rCount = data.length;
        for (int i = 0; i < rCount; i++) {
            String sourcePath = getFullUploadedPath(data[i][0].toString());
            if (sourcePath.equals("")) {
                continue;
            }
            String uploadPath = getUploadDir() + File.separator + data[i][1].toString();
            //Upload
            FileUtilities.uploadFile(sourcePath, uploadPath);
            UploadedImage upImage = new UploadedImage(uploadPath, "", "");
            //Create resized images and upload
            File s = new File(uploadPath);
            //Thumb
            String thumbPath = s.getParent() + File.separator
                + "thumb-" + s.getName();
            FileUtilities.uploadResizedImage(uploadPath, 300, 300, thumbPath);
            //Mobile
            String mobilePath = s.getParent() + File.separator
                + "mobile-" + s.getName();
            FileUtilities.uploadResizedImage(uploadPath, 50, 50, mobilePath);            
        }        
    }
    
    @Override
    public String genRecord(String text) {
        return null;
    }
    public String genRecord(Object[][] data, int npr, int descriptMode) {
        StringBuilder textSB = new StringBuilder();
        int rCount = data.length;
        for (int i = 0; i < rCount; i++) {
            if (data[i][0] == null || data[i][0].toString().equals("")) {
                continue;
            }
            String sourcePath = getFullUploadedPath(data[i][0].toString());
            String uploadPath = getUploadDir() + File.separator + data[i][1].toString();
            UploadedImage upImage = new UploadedImage(uploadPath, "", "");
            //Add to record
            textSB.append("\"");
            textSB.append(upImage.getRelativeFilePath());
            textSB.append("\"");
            textSB.append(",");
            textSB.append("\"");
            textSB.append(data[i][2].toString());
            textSB.append("\"");
            textSB.append(",");
            textSB.append(npr);
            textSB.append(",");
            textSB.append(descriptMode);
            textSB.append(",");
            textSB.append(WebModuleEnum.GALLERY.getValue());
            textSB.append("_");
            textSB.append(getID());
            textSB.append("\n");
        }
        String text = new String(textSB);
        return text;
    }
    //start editor
    @Override
    public void startEditor() {
        GalleryUploaderController uploaderController = null;
        try {
            uploaderController 
                = new GalleryUploaderController(getDesignSet(), getCfgPath(), getUploadDir(), getPageName(), getID());
        } catch (IOException ex) {
            System.out.println("IOException when launching uploader");
            ex.printStackTrace();
        } catch(Exception ex) {
            System.out.println("Exception when launching uploader");
        }
        uploaderController.start();
    }
    //Get full uploaded path from a partial one starting with "uploads\"
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