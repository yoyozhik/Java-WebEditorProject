/* Page Class */
/* Author: Wei Zhang
   Version date: 2016 Jan 15
*/
/* API
public class Page {
    public Page(String rootDir, String websiteDirRel, String resourceDirRel, 
        int level, String pageName, String pageTitle, boolean displayInNav) 
    
}
*/

package models;
import utilities.*;
import java.io.*;
import java.util.regex.*;
import java.util.HashMap;

public class Page {
    private int level;
    private String pageName;
    private String pageTitle;
    private boolean displayInNav;
    private HashMap<String, String> designSet;
    
    public Page(int level, String pageName, String pageTitle, boolean displayInNav,
        HashMap<String, String> designSet ) {
        if (designSet == null) {
            throw new NullPointerException("Null designSet when initializing Page");
        }
        if (pageName == null) {
            throw new NullPointerException("Null pageName when initializing Page");
        }
        if (pageTitle == null) {
            throw new NullPointerException("Null pageTitle when initializing Page");
        }
        setDesignSet(designSet);
        this.level = level;
        this.pageName = pageName;
        this.pageTitle = pageTitle;
        this.displayInNav = displayInNav;
    }
    public void setDesignSet(HashMap<String, String> designSet) {
        this.designSet = new HashMap<String, String>(designSet);
    }
    public int getLevel() {
        return(level);
    }
    public String getPageName() {
        return(pageName);
    }
    public String getPageTitle() {
        return(pageTitle);
    }
    public boolean getDisplayInNav() {
        return(displayInNav);
    }
    public String getCfgName() {
        return(pageName + ".txt");
    }
    public String getMainFullFileName() {
        return(pageName + ".html");
    }
    public String getMobileFullFileName() {
        return(pageName + ".php");
    }
    public String getDesignInfo(String key) {
        return designSet.get(key);
    }
    public String getMainFullFilePath() {
        String rootDir = designSet.get("rootDir");
        String websiteDirRel = designSet.get("websiteDirRel");
        if (rootDir == null) {
            throw new NullPointerException("Null rootDir");
        }
        if (websiteDirRel == null) {
            throw new NullPointerException("Null websiteDirRel");
        }
        String path = rootDir + File.separator 
            + websiteDirRel + File.separator
            + getMainFullFileName();
        return(path);
    }
    public String getMobileFullFilePath() {
        String rootDir = designSet.get("rootDir");
        String websiteDirRel = designSet.get("websiteDirRel");
        if (rootDir == null) {
            throw new NullPointerException("Null rootDir");
        }
        if (websiteDirRel == null) {
            throw new NullPointerException("Null websiteDirRel");
        }
        String path = rootDir + File.separator 
            + websiteDirRel + File.separator
            + "mobile" + File.separator
            + getMobileFullFileName();
        return(path);
    }
    //Get correponding parent dir of upload path
    public String getUploadParent() {
        String rootDir = designSet.get("rootDir");
        String websiteDirRel = designSet.get("websiteDirRel");
        if (rootDir == null) {
            throw new NullPointerException("Null rootDir");
        }
        if (websiteDirRel == null) {
            throw new NullPointerException("Null websiteDirRel");
        }
        String parent = rootDir + File.separator
            + websiteDirRel + File.separator
            + "uploads" + File.separator
            + getPageName();
        return(parent);
    }
    //Get correponding resource dir path
    public String getResourceCfgDir() {
        String rootDir = designSet.get("rootDir");
        String resourceDirRel = designSet.get("resourceDirRel");
        if (rootDir == null) {
            throw new NullPointerException("Null rootDir");
        }
        if (resourceDirRel == null) {
            throw new NullPointerException("Null resourceDirRel");
        }
        String dir = rootDir + File.separator 
            + resourceDirRel + File.separator 
            + getPageName();
        return(dir);
    }
    //Get correponding resource page cfg file path
    public String getResourcePageCfg() { 
        String path = getResourceCfgDir() + File.separator 
            + getCfgName();
        return(path);
    }
    
    public String getPageContent() {
        return(FileUtilities.read(getResourcePageCfg(), "UTF-8"));
    }
    
    /* 
    Save page
    */
    public void save(String text) {  //Save this page with the given content
        if (text == null) {
            throw new NullPointerException("Null text when saving");
        }
        FileUtilities.write(getResourcePageCfg(), text, "UTF-8");
    }
    
    /*
    Compile page
    */
    //Main page
    public void compileMain(String frameworkPath, String navText) { //Compile PC html page
        if (navText == null) {
            throw new NullPointerException("Null navText");
        }
        String compiledText = compilePageNoNav(frameworkPath);
        ContentParser cP = new ContentParser(designSet);
        //Navigation
        String navTextMod = compileNavMainPage(navText);
        compiledText = cP.compileNavigation(compiledText, navTextMod);
        //Handle mobile markers
        compiledText = compiledText.replace("<MOBILE>/", "");
        compiledText = UnicodeConvert.toUnicodes(compiledText);
        //Handle Website Info
        String websiteNameCfg = designSet.get("websiteNameCfg");
        if (websiteNameCfg == null) {
            throw new NullPointerException("Null websiteNameCfg");
        }
        String websiteName = FileUtilities.readLine(websiteNameCfg, "UTF-8");
        if (websiteName == null) {
            websiteName = "";
        }
        String websiteURLCfg = designSet.get("websiteURLCfg");
        if (websiteURLCfg == null) {
            throw new NullPointerException("Null websiteURLCfg");
        }
        String websiteURL = FileUtilities.readLine(websiteURLCfg, "UTF-8");
        if (websiteURL == null) {
            websiteURL = "";
        }
        compiledText = compiledText.replace("<WEBURL>", websiteURL);
        //Handle page title
        compiledText = compiledText.replace("<PAGETITLE>", pageTitle + " - " + websiteName);
        //Handle page name
        compiledText = compiledText.replace("<PAGENAME>", pageName);
        //Write
        FileUtilities.write(getMainFullFilePath(), compiledText, "UTF-8");
        
    }
    //Mobile page
    public void compileMobile(String frameworkPath, String navText) { //Compile mobile php page
        if (navText == null) {
            throw new NullPointerException("Null navText");
        }
        String compiledText = compilePageNoNav(frameworkPath);
        ContentParser cP = new ContentParser(designSet);
        //Navigation
        String navTextMod = compileNavMobilePage(navText);
        compiledText = cP.compileNavigation(compiledText, navTextMod);
        //Handle mobile markers
        compiledText = compiledText.replace("<MOBILE>/", "/mobile/");
        
        //Additional modifications required
        //This one is customized: Web URL is too long
        //Should think of a better and more generic way to handle
        //compiledText = compiledText.Replace(">https://sites.google.com/site/mbabuddhistfamilyprogram<",
        //    ">點此前往 Click Here<");
        //Update: Fix by searching for the link directly; only modify the ones at least 20 long
        Pattern p = Pattern.compile("(?i)href\\s*=([^<>]+)>\\s*([^<> ]{20}[^<> ]*)\\s*<");
        Matcher m = p.matcher(compiledText);
        while (m.find()) {
            compiledText = compiledText.replaceAll("(?i)href\\s*=([^<>]+)>\\s*([^<> ]{20}[^<> ]*)\\s*<", 
                "href=" + m.group(1) + ">點此前往 Click Here<");
            m = p.matcher(compiledText);
        }
        //All uploaded images/files have one more hierarchy now; use root
        compiledText = compiledText.replace("uploads", "/uploads") ;
        //Make sure no extra / is present
        compiledText = compiledText.replace("//uploads", "/uploads"); 
        //Calendar agenda view is better
        compiledText = compiledText.replace("calendar/embed?", "calendar/embed?mode=AGENDA&amp;");
        //Make a larger font size
        compiledText = compiledText.replace("style=\" border-width:", "style=\" font-size: 48px; border-width:");
        //Pages are now .php instead of .html 
        //This one is buggy and can cause unwanted conversion
        //But not an issue for the current website design
        //Need to implement a fix by collecting all compiled pages, and only change the ones compiled
        //compiledText = compiledText.replace(".html", ".php");
        //Simple fix
        String allPageNames = designSet.get("allPageNames");
        if (allPageNames == null) {
            throw new NullPointerException("Null allPageNames");
        }
        String[] allPageNameArray = allPageNames.split("\n");
        for (int i = 0; i < allPageNameArray.length; i++) {
            if (!allPageNameArray[i].equals("")) {
                compiledText = compiledText.replace(allPageNameArray[i] + ".html", allPageNameArray[i] + ".php");
            }
        }
        //Image needs to resize
        compiledText = compiledText.replaceAll("(?i)<img class=\"uploadedImage\" [^>]* src=",
            "<img class=\"uploadedImage\" max-width:100% max-height:100% src=");
            
        compiledText = UnicodeConvert.toUnicodes(compiledText);
        FileUtilities.write(getMobileFullFilePath(), compiledText, "UTF-8");
    }
    //Compile everything except navigation 
    private String compilePageNoNav(String frameworkPath) {
        if (frameworkPath == null) {
            throw new NullPointerException("Null FrameworkPath");
        }
        ContentParser cP = new ContentParser(designSet);
        //Main page
        String compiledText = FileUtilities.read(frameworkPath, "UTF-8");
        if (compiledText == null) {
            compiledText = "";
        }
        compiledText = cP.compileMainText(compiledText, getResourcePageCfg());
        //Replace content
        compiledText = cP.compileTitles(compiledText, pageName);
        compiledText = cP.compileParagraphs(compiledText, pageName);
        compiledText = cP.compileCodes(compiledText, pageName);
        compiledText = cP.compileFiles(compiledText, pageName);
        compiledText = cP.compileImages(compiledText, pageName);
        compiledText = cP.compileGalleries(compiledText, pageName);
        compiledText = cP.compileDividers(compiledText,pageName);
        cP = null;
        return(compiledText);
    }
    //compile navigation
    private String compileNavMainPage(String navText) {
        String origNavStr = "<td class=\"nav-entry\"><a class=\"nav\" href=\"" 
            + pageName + ".html\">" + pageTitle + "</a></td>";
        String thisNavStr = "<td class=\"nav-entry-selected\"><a class=\"nav\" href=\""
            + pageName + ".html\">" + pageTitle + "</a></td>";
        return(navText.replace(origNavStr, thisNavStr));
    }
    private String compileNavMobilePage(String navText) {
        String origNavStr = "<option value=\"/mobile/" 
            + pageName + ".php\"";
        String thisNavStr = "<option selected=\"selected\" value=\"/mobile/"
            + pageName + ".php\"";
        return(navText.replace(origNavStr, thisNavStr));
    }    
    
}