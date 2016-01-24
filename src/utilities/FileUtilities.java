/* File Utilities 
*/
/* Author: Wei Zhang
   Latest Version: 2016 Jan 17
*/
/*API
class FileUtilities {
    public FileUtilities()
    public static String read(String filepath)
    public static String read(String filepath, String enc)
    public static void write(String filepath, String text)
    public static void write(String filepath, String text, String enc)
    public static void parentCreate(String filepath) 
}
*/
/* Note:
Making static methods to avoid initilization overhead.
Not thread safe!
This app has no multi-thread scenario so it is safe.
*/
package utilities;

import java.io.*;
import java.nio.file.*;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.regex.*;

//import java.nio.file.StandardCopyOption.*;
public class FileUtilities {
    private static final String UTF8_BOM = Character.toString((char) (65279));
    private static final boolean ADD_UTF8_BOM = true;
    
    public FileUtilities() {
    }

    
    //Read
    //Read one line without encoding
    public static String readLine(String filepath) {
        File f = new File(filepath);
        if (!f.exists() || !f.isFile()) {
            return null;
        }
        try {
            BufferedReader reader = new BufferedReader(
                new FileReader(filepath));
            String line = reader.readLine();
            //UnicodeConvert uc = new UnicodeConvert();
            reader.close();
            return line;
        } catch (IOException ex) {
            System.out.println("Error reading one line at " + filepath);
            ex.printStackTrace();
        }
        return null;
    }
    //Read one line with encoding
    public static String readLine(String filepath, String enc) {
        File f = new File(filepath);
        if (!f.exists() || !f.isFile()) {
            return null;
        }
        try {
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filepath), enc));
            String line = reader.readLine();
            if (enc.equals("UTF-8")) {
                line = line.replace(UTF8_BOM, "");
            }
            //UnicodeConvert uc = new UnicodeConvert();
            reader.close();
            return line;
        } catch (IOException ex) {
            System.out.println("Error reading file content at " + filepath);
            ex.printStackTrace();
        }
        return null;
    }
    
    //Read without encoding
    public static String read(String filepath) {
        File f = new File(filepath);
        if (!f.exists() || !f.isFile()) {
            return null;
        }
        try {
            BufferedReader reader = new BufferedReader(
                new FileReader(filepath));
            StringBuilder lines = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.append(line);
                lines.append("\n");
            }
            line = new String(lines);
            //UnicodeConvert uc = new UnicodeConvert();
            reader.close();
            return line;
        } catch (IOException ex) {
            System.out.println("Error reading file content at " + filepath);
            ex.printStackTrace();
        }
        return null;
    }
    //Read with encoding
    public static String read(String filepath, String enc) {
        File f = new File(filepath);
        if (!f.exists() || !f.isFile()) {
            return null;
        }
        try {
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filepath), enc));
            StringBuilder lines = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.append(line);
                lines.append("\n");
            }
            line = new String(lines);
            if (enc.equals("UTF-8")) {
                line = line.replace(UTF8_BOM, "");
            }
            //UnicodeConvert uc = new UnicodeConvert();
            reader.close();
            return line;
        } catch (IOException ex) {
            System.out.println("Error reading file content at " + filepath);
            ex.printStackTrace();
        }
        return null;
    }
    //Write
    //Write without encoding
    public static void write(String filepath, String text) {
        File f = new File(filepath);
        if (f.exists() && !f.isFile()) {
            throw new IllegalArgumentException("Is already a folder: " + filepath);
        }
        parentCreate(filepath);
            
        try {
            BufferedWriter writer = new BufferedWriter(
                new FileWriter(filepath));
            writer.write(text);
            writer.close();
        } catch (Exception ex) {
            System.out.println("Error saving " + filepath);
            ex.printStackTrace();
        }
    }
    
    //Write with encoding
    public static void write(String filepath, String text, String enc) {
        File f = new File(filepath);
        if (f.exists() && !f.isFile()) {
            throw new IllegalArgumentException("Is already a folder: " + filepath);
        }
        parentCreate(filepath);
        
        try {
            BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filepath), enc));
            if (enc.equals("UTF-8") && ADD_UTF8_BOM) {
                text = UTF8_BOM + text;
            }
            writer.write(text);
            writer.close();
        } catch (Exception ex) {
            System.out.println("Error saving " + filepath);
            ex.printStackTrace();
        }
    }
    
    //Create parent folder
    public static void parentCreate(String filepath) {
        File f = new File((new File(filepath)).getAbsolutePath());
        File dir = new File(f.getParent());
        if (dir != null && !dir.exists()) {
            dir.mkdirs();
        }
    }
    
    //Copy a file
    public static void copyFile(String fileSourceStr, String fileDestStr, boolean overwrite) 
        throws FileAlreadyExistsException  {
        if (fileSourceStr == null) {
            throw new NullPointerException("Null fileSourceStr");
        }
        if (fileDestStr == null) {
            throw new NullPointerException("Null fileDestStr");
        }
        File s = new File(fileSourceStr);
        File f = new File(fileDestStr);
        if (s.equals(f)) {  //Nothing to do if identical 
            return;
        }
        if (!overwrite && f.exists()) {
            throw new FileAlreadyExistsException ("Already exists: cannot copy to " 
                + fileDestStr);
        }
        try {
            if (overwrite) {
                Files.copy(Paths.get(fileSourceStr), Paths.get(fileDestStr), 
                    StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.copy(Paths.get(fileSourceStr), Paths.get(fileDestStr));
            }
        } catch (Exception ex) {
            System.out.println("Copy Exception");
            ex.printStackTrace();
        }
        return;
    }
    
    //Rename a file
    public static boolean renameFile(String fileSourceStr, String fileDestStr) 
        throws FileAlreadyExistsException {
        if (fileSourceStr == null) {
            throw new NullPointerException("Null fileSourceStr");
        }
        if (fileDestStr == null) {
            throw new NullPointerException("Null fileDestStr");
        }
        boolean success = false;
        File s = new File(fileSourceStr);
        File f = new File(fileDestStr);
        if (s.equals(f)) {  //Nothing to do if identical 
            return true;
        }
        if (f.exists()) {
            throw new FileAlreadyExistsException ("Already exists: cannot rename to " 
                + fileDestStr);
        }
        try {
            success = s.renameTo(f);
            if (!success) {
                System.out.println("Error: Renaming failure: " + 
                    fileSourceStr + " => " + fileDestStr);
            }
        } catch (Exception ex) {
            System.out.println("Rename Exception");
            ex.printStackTrace();
        }
        return success;
    }
    
    public static String uploadFile(String fileSourceStr, String fileDestStr) {
        File src = new File(fileSourceStr);  //source file
        File dest = new File(fileDestStr);    //target file
        String status = "Identical file; nothing executed.";
        //Check if the two files are identical locations
        if (!src.equals(dest)) { //Nothing to do if equal
            //Check if the folder did not change, meaning a renaming only
            String sourceDir = src.getParentFile().getAbsolutePath();
            String uploadDir = dest.getParentFile().getAbsolutePath();
            if ((new File(sourceDir)).equals(new File(uploadDir))) { //rename
                status = "Renaming";
                boolean success = false;
                try {
                    success = renameFile(fileSourceStr, fileDestStr);
                } catch (FileAlreadyExistsException ex) {
                    status = "Error: Target file exits when renaming: " + fileDestStr;
                    System.out.println(status);
                    return status;
                } catch (Exception ex) {
                    status = "Error: Exception during renaming.";
                    System.out.println(status);
                    ex.printStackTrace();
                    return status;
                }
                if (!success) {
                    status += " failed.";
                    return status;
                }
            } else { //upload: copy file over
                status = "Copying";
                parentCreate(fileDestStr);
                try {
                    copyFile(fileSourceStr, fileDestStr, true);
                } catch (FileAlreadyExistsException ex) {
                    status = "Error: Target file exits when copying: " + fileDestStr;
                    System.out.println(status);
                    return status;
                } catch (Exception ex) {
                    status = "Error: Exception during copying.";
                    System.out.println(status);
                    ex.printStackTrace();                    
                    return status;
                }
            }
        }
        return status + " done.";
    }
    
    
    //Create and Upload resized image
    public static void uploadResizedImage(String origImagePath, int widthMax, int heightMax, String outputPath) {
        if (origImagePath == null) {
            throw new NullPointerException("Null origImagePath");
        }
        if (outputPath == null) {
            throw new NullPointerException("Null outputPath");
        }
        String format = getFormat(origImagePath);
        if (!format.equals(getFormat(outputPath))) {
            throw new IllegalArgumentException("Format mismatch: File " 
                + origImagePath + "vs. File "
                + outputPath);
        }
        checkFormat(format);
        BufferedImage bufImg = loadImage(origImagePath);
        bufImg = resizeImage(bufImg, format, widthMax, heightMax);
        saveImage(bufImg, format, outputPath);
    }
    
    //load image into BufferedImage
    public static BufferedImage loadImage(String origImagePath) {
        if (origImagePath == null) {
            throw new NullPointerException("Null origImagePath");
        }
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(origImagePath));
        } catch (IOException ex) {
            System.out.println("IOException during image loading: " + origImagePath);
            ex.printStackTrace();
        }
        return img;
    }
    //Create a resized BufferedImage
    public static BufferedImage resizeImage(BufferedImage bufImg, String format, int widthMax, int heightMax) {
        checkFormat(format);
        if (bufImg == null) {
            throw new NullPointerException("Null bufImg");
        }
        int width = widthMax;
        int height = heightMax;
        if (widthMax <= 0 && heightMax <= 0) {
            throw new IllegalArgumentException("Both widthMax & heightMax are 0");
        } else if (bufImg.getWidth() <= 0) {
            throw new IllegalArgumentException("Image widthMax is 0");
        } else if (bufImg.getHeight() <= 0) {
            throw new IllegalArgumentException("Image heightMax is 0");
        } else if (widthMax <= 0) {
            width = Math.round(bufImg.getWidth() * heightMax / bufImg.getHeight());
        } else if (heightMax <= 0) {
            height = Math.round(bufImg.getHeight() * widthMax / bufImg.getWidth());
        } else {
            width = Math.round(bufImg.getWidth() * heightMax / bufImg.getHeight());
            if (width > widthMax) {
                width = widthMax;
                height = Math.round(bufImg.getHeight() * widthMax / bufImg.getWidth());
            }
        }
        Image img = bufImg.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        BufferedImage newBufImg = new BufferedImage(width, height, getRGBType(format));
        Graphics2D g2d = newBufImg.createGraphics();
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();
        return (newBufImg);
    }
    //Save the BufferedImage
    public static void saveImage(BufferedImage bufImg, String format, String outputPath) {
        checkFormat(format);
        if (bufImg == null) {
            throw new NullPointerException("Null bufImg");
        }
        if (outputPath == null) {
            throw new NullPointerException("Null outputPath");
        }
        if (!format.equals(getFormat(outputPath))) {
            throw new IllegalArgumentException("Format mismatch: " 
                + format + "vs. that of "
                + outputPath);
        }
        
        File output = new File(outputPath);
        if (output.exists() && !output.isFile()) {
            throw new IllegalArgumentException("Is an existing dir: " + outputPath);                
        }
        try {
            //write image
            ImageIO.write(bufImg, format, output);
        } catch (IOException ex) {
            System.out.println("Exception when writing image");
            ex.printStackTrace();
        }
    }
    private static String getFormat(String path) {
        String format = "";
        int i = path.lastIndexOf(".");
        if (i >= 0) {
            format = path.substring(i + 1).toLowerCase();
        } else {
            throw new IllegalArgumentException("Path has no format:"
                + path);
        }
        return format;
    }
    private static void checkFormat(String format) {
        if (format == null) {
            throw new NullPointerException("Null format");
        }
        switch (format.toLowerCase()) {
            case "bmp":
            case "wbmp":
            case "png":
            case "jpg":
            case "jpeg":
            case "gif":break;
            default: 
                throw new IllegalArgumentException("Image format not supported: " + format);
        }
    }
    
    private static int getRGBType(String format) {
        int rgbType = -1;
        switch (format.toLowerCase()) {
            case "bmp":
            case "wbmp":
            case "jpg":
            case "jpeg": rgbType = BufferedImage.TYPE_INT_RGB; 
                break;
            case "png":
            case "gif": rgbType = BufferedImage.TYPE_INT_ARGB; 
                break;
            default: 
                throw new IllegalArgumentException("Image format not supported: " + format);
        }
        return rgbType;
    }
    
    //Advanced split function to take care of comma inside quotation marks
    public static String[] recordSplit(String str) {
        Pattern p = Pattern.compile("([^\"]*?|\"[^\"]*?\"),");
        String s = str.trim() + ",";  //Need one more "," at the end
        Matcher m = p.matcher(s);
        ArrayList<String> strArrList = new ArrayList<String>();
        while (m.find()) {
            //System.out.println(m.start());
            //System.out.println(m.end());
            strArrList.add(m.group(1).replace("\"", ""));
        }
        String[] records = new String[strArrList.size()];
        for (int i = 0; i < records.length; i++) {
            records[i] = strArrList.get(i);
        }
        return records;
    }
    
    public static boolean deleteFile(String targetPath) {
        File f = new File(targetPath);
        if (f.exists() && f.isFile()) {
            try {
               f.delete();
            } catch (SecurityException ex) {
                System.out.println("SecurityException when deleting "
                    + targetPath);
                ex.printStackTrace();
                return false;
            } catch (Exception ex) {
                System.out.println("Exception when deleting "
                    + targetPath);
                ex.printStackTrace();
                return false;
            }
        }
        return true;
    }
    
    public static boolean deleteFolder(String targetPath) {
        boolean success = true;
        File d = new File(targetPath);
        if (d.exists() && d.isDirectory()) {
            File[] allSubs = d.listFiles();
            for (File sF : allSubs) {
                if (sF.isFile()) {
                    //deleteFile needs to go first, otherwise it won't be run if success == false
                    success =  deleteFile(sF.getAbsolutePath()) && success;
                } else {
                    success =  deleteFolder(sF.getAbsolutePath()) && success;
                }
            }
        }
        try {
           d.delete();
        } catch (SecurityException ex) {
            System.out.println("SecurityException when deleting "
                + targetPath);
            ex.printStackTrace();
            return false;
        } catch (Exception ex) {
            System.out.println("Exception when deleting "
                + targetPath);
            ex.printStackTrace();
            return false;
        }
        return success;
    }
    
    public static String autoEllipsis(String text, int maxLen) {
        if (text == null) {
            throw new NullPointerException("Null text");
        }
        int textLen = text.length();
        if (textLen > maxLen) {
            int firstHalf = Math.round((maxLen - 3) / 2);
            return text.substring(0, firstHalf)
                + "..."
                + text.substring(firstHalf + textLen - maxLen + 3, textLen);
        } else {
            return text;
        }
    }
    public static String autoEllipsis(String text) {
        return autoEllipsis(text, 60);
    }
    
    public static void main(String[] args) {
        String s = "\"hahaha, this is it\",,umm,okay,\"\",\"well okay\",";
        String[] r = recordSplit(s);
        for (int i = 0; i < r.length; i++) {
            System.out.println(i + ":" + r[i]);
        }
    }
}