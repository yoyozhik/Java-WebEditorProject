/* Page Class */
/* Author: Wei Zhang
   Version date: 2016 Jan 26
*/
/* The Page class handles everything of the Page itself
including:
1. Get page information
2. Save page content
3. Compile page
*/
/* API
public class Page {
    public Page(int level, String pageName, String pageTitle, boolean displayInNav,
        DesignInfoSet designInfoSet ) {}
    public void setDesignInfoSet(DesignInfoSet designInfoSet) {}
    public int getLevel() {}
    public String getPageName() {}
    public String getPageTitle() {}
    public boolean getDisplayInNav() {}
    public String getCfgName() {}
    public String getMainFullFileName() {}
    public String getMobileFullFileName() {}
    public String getDesignSetItem(String key) {}
    public String getMainFullFilePath() {}
    public String getMobileFullFilePath() {}
    public String getUploadParent() {}
    public String getResourceCfgDir() {}
    public String getResourcePageCfg() {} 
    public String getPageContent() {}
    public void save(String text) {}
    public void compileMain(String frameworkPath) {}
    public void compileMobile(String frameworkPath) {}
    public static void main(String[] args) {} 
}
*/

package org.dharmatech.models;
import org.dharmatech.utilities.*;
import org.dharmatech.controllers.DesignInfoSet;

import java.io.*;
import java.util.regex.*;
import java.util.HashMap;

public class Page {
    private int level;
    private String pageName;
    private String pageTitle;
    private boolean displayInNav;
    private DesignInfoSet designInfoSet;
    //constructor taking the page elements: level, pagename, pagetitle, 
    //and whether to display in navigation
    //plus DesignInfoSet for compiling usage
    public Page(int level, String pageName, String pageTitle, boolean displayInNav,
        DesignInfoSet designInfoSet ) {
        if (designInfoSet == null) {
            throw new NullPointerException("Null designInfoSet when initializing Page");
        }
        if (pageName == null) {
            throw new NullPointerException("Null pageName when initializing Page");
        }
        if (pageTitle == null) {
            throw new NullPointerException("Null pageTitle when initializing Page");
        }
        setDesignInfoSet(designInfoSet);
        this.level = level;
        this.pageName = pageName;
        this.pageTitle = pageTitle;
        this.displayInNav = displayInNav;
    }
    //Reset design info without reinstantialting
    public void setDesignInfoSet(DesignInfoSet designInfoSet) {
        this.designInfoSet = new DesignInfoSet(designInfoSet);
    }
    //get page level
    public int getLevel() {
        return(level);
    }
    //get page name
    public String getPageName() {
        return(pageName);
    }
    //get page title
    public String getPageTitle() {
        return(pageTitle);
    }
    //get page info on whether to display in navigation
    public boolean getDisplayInNav() {
        return(displayInNav);
    }
    //get page cfg path
    public String getCfgName() {
        return(pageName + ".txt");
    }
    //get the full name of the main webpage
    public String getMainFullFileName() {
        return(pageName + ".html");
    }
    //get the full name of the mobile webpage
    public String getMobileFullFileName() {
        return(pageName + ".php");
    }
    //get the target design info
    public String getDesignSetItem(String key) {
        return designInfoSet.getDesignInfo(key);
    }
    //get the full path of the main webpage
    public String getMainFullFilePath() {
        String rootDir = getDesignSetItem("rootDir");
        String websiteDirRel = getDesignSetItem("websiteDirRel");
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
    //get the full path of the mobile webpage
    public String getMobileFullFilePath() {
        String rootDir = getDesignSetItem("rootDir");
        String websiteDirRel = getDesignSetItem("websiteDirRel");
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
        String rootDir = getDesignSetItem("rootDir");
        String websiteDirRel = getDesignSetItem("websiteDirRel");
        String uploadsResourcesRel = getDesignSetItem("uploadsResourcesRel");
        if (rootDir == null) {
            throw new NullPointerException("Null rootDir");
        }
        if (websiteDirRel == null) {
            throw new NullPointerException("Null websiteDirRel");
        }
        if (uploadsResourcesRel == null) {
            throw new NullPointerException("Null uploadsResourcesRel");
        }
        String parent = rootDir + File.separator
            + websiteDirRel + File.separator
            + uploadsResourcesRel + File.separator
            + getPageName();
        return(parent);
    }
    //Get correponding resource dir path
    public String getResourceCfgDir() {
        String rootDir = getDesignSetItem("rootDir");
        String resourceDirRel = getDesignSetItem("resourceDirRel");
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
    //Get the page content in the page configurations 
    //(the text displayed in the main textarea in WebEditor)
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
    public void compileMain(String frameworkPath) { //Compile PC html page
        //if (navText == null) {
        //    throw new NullPointerException("Null navText");
        //}
        String compiledText = compilePageNoNav(frameworkPath);
        //ContentParser cP = new ContentParser(new DesignInfoSet(designInfoSet));
        //Navigation
        //String navTextMod = compileNavMainPage(navText);
        String navTextMod = compileNavMainPage();
        //compiledText = cP.compileNavigation(compiledText, navTextMod);
        compiledText = compileNavigation(compiledText, navTextMod);
        //Handle markers
        compiledText = handleMarkers(compiledText, false);
        
        //Unicode
        compiledText = UnicodeConvert.toUnicodes(compiledText);        
        //Write
        //FileUtilities.write(getMainFullFilePath(), compiledText, "UTF-8");
        FileUtilities.write(getMainFullFilePath(), compiledText);
    }
    //Mobile page
    public void compileMobile(String frameworkPath) { //Compile mobile php page
        //if (navText == null) {
        //    throw new NullPointerException("Null navText");
        //}
        String compiledText = compilePageNoNav(frameworkPath);
        //ContentParser cP = new ContentParser(new DesignInfoSet(designInfoSet));
        //Navigation
        //String navTextMod = compileNavMobilePage(navText);
        String navTextMod = compileNavMobilePage();
        //compiledText = cP.compileNavigation(compiledText, navTextMod);
        compiledText = compileNavigation(compiledText, navTextMod);
        //Handle markers
        compiledText = handleMarkers(compiledText, true);

        //Additional modifications required
        //Php header for navigation
        compiledText = "<?php\n"
            + "if (isset($_POST['nav'])) {\n" 
            + "    header(\"Location: $_POST[nav]\");\n"
            + "}\n" 
            + "?>\n"
            + compiledText;
        
        //This one is customized: Web URL is too long
        //Should think of a better and more generic way to handle
        //compiledText = compiledText.Replace(">https://sites.google.com/site/mbabuddhistfamilyprogram<",
        //    ">點此前往 Click Here<");
        //Update: Fix by searching for the link directly; only modify the ones at least 20 chars long
        Pattern p = Pattern.compile("(?i)href\\s*=([^<>]+)>\\s*([^<> ]{20}[^<> ]*)\\s*<");
        Matcher m = p.matcher(compiledText);
        while (m.find()) {
            compiledText = compiledText.replaceAll("(?i)href\\s*=" 
                + m.group(1).replace("\\","\\\\") + ">\\s*" 
                + m.group(2).replace("\\","\\\\") + "\\s*<", 
                "href=" + m.group(1) + ">" 
                + UnicodeConvert.toUnicodes("&#40670;&#27492;&#21069;&#24448;") + " Click Here<"); 
                //Chinese characters cannot be handled directly as a String
                //點此前往 the unicode is &#40670;&#27492;&#21069;&#24448;
            m = p.matcher(compiledText);
        }
        
        //All uploaded images/files have one more hierarchy now; use root
        String uploadsResourcesRel = getDesignSetItem("uploadsResourcesRel");
        if (uploadsResourcesRel == null) {
            throw new NullPointerException("Null uploadsResourcesRel");
        }
        compiledText = compiledText.replace(uploadsResourcesRel, "/" + uploadsResourcesRel) ;
        //Make sure no extra / is present
        compiledText = compiledText.replace("//" + uploadsResourcesRel, "/" + uploadsResourcesRel); 
        
        //Do the similar for images-resources
        String imagesResourcesRel = getDesignSetItem("imagesResourcesRel");
        if (imagesResourcesRel == null) {
            throw new NullPointerException("Null imagesResourcesRel");
        }
        compiledText = compiledText.replace(imagesResourcesRel, "/" + imagesResourcesRel) ;
        compiledText = compiledText.replace("//" + imagesResourcesRel, "/" + imagesResourcesRel); 
        
        //Calendar agenda view is better
        compiledText = compiledText.replace("calendar/embed?", "calendar/embed?mode=AGENDA&amp;");
        
        //Make a larger font size
        compiledText = compiledText.replace("style=\" border-width:", "style=\" font-size: 48px; border-width:");
        
        //Pages are now .php instead of .html 
        //Need to implement a fix by collecting all compiled pages, and only change the ones compiled
        //Simple fix compared to "compiledText = compiledText.replace(".html", ".php");"
        String allPageNames = getDesignSetItem("allPageNames");
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
        //FileUtilities.write(getMobileFullFilePath(), compiledText, "UTF-8");
        FileUtilities.write(getMobileFullFilePath(), compiledText);
    }
    //Handle markers
    private String handleMarkers(String text, boolean isMobile) {
        String compiledText = text;
        //Handle mobile markers
        if (isMobile) {
            compiledText = compiledText.replace("<MOBILE>/", "/mobile/");
        } else {
            compiledText = compiledText.replace("<MOBILE>/", "");
        }
        //Handle Website Info
        String websiteNameCfg = getDesignSetItem("websiteNameCfg");
        if (websiteNameCfg == null) {
            throw new NullPointerException("Null websiteNameCfg");
        }
        String websiteName = FileUtilities.readLine(websiteNameCfg, "UTF-8");
        if (websiteName == null) {
            websiteName = "";
        }
        String websiteURLCfg = getDesignSetItem("websiteURLCfg");
        if (websiteURLCfg == null) {
            throw new NullPointerException("Null websiteURLCfg");
        }
        String websiteURL = FileUtilities.readLine(websiteURLCfg, "UTF-8");
        if (websiteURL == null) {
            websiteURL = "";
        }
        String imagesResourcesRel = getDesignSetItem("imagesResourcesRel");
        if (imagesResourcesRel == null) {
            throw new NullPointerException("Null imagesResourcesRel");
        }
        String uploadsResourcesRel = getDesignSetItem("uploadsResourcesRel");
        if (uploadsResourcesRel == null) {
            throw new NullPointerException("Null uploadsResourcesRel");
        }
        compiledText = compiledText.replaceAll("(?i)<WEBURL>", websiteURL);
        compiledText = compiledText.replaceAll("(?i)<WEBNAME>", websiteName);
        //Handle page title
        compiledText = compiledText.replaceAll("(?i)<PAGETITLE>", pageTitle + " - " + websiteName);
        //Handle page name
        compiledText = compiledText.replaceAll("(?i)<PAGENAME>", pageName);
        //Handle image resource dir
        compiledText = compiledText.replaceAll("(?i)<imagesResourcesRel>", imagesResourcesRel);
        //Handle upload resource dir
        compiledText = compiledText.replaceAll("(?i)<uploadsResourcesRel>", uploadsResourcesRel);
        
        return compiledText;
    }
    //Compile everything except navigation 
    private String compilePageNoNav(String frameworkPath) {
        if (frameworkPath == null) {
            throw new NullPointerException("Null FrameworkPath");
        }
        //ContentParser cP = new ContentParser(new DesignInfoSet(designInfoSet));
        //Main page
        String compiledText = FileUtilities.read(frameworkPath, "UTF-8");
        if (compiledText == null) {
            compiledText = "";
        }
        //compiledText = cP.compileMainText(compiledText, getResourcePageCfg());
        //Replace content
        //compiledText = cP.compileTitles(compiledText, pageName);
        //compiledText = cP.compileParagraphs(compiledText, pageName);
        //compiledText = cP.compileCodes(compiledText, pageName);
        //compiledText = cP.compileFiles(compiledText, pageName);
        //compiledText = cP.compileImages(compiledText, pageName);
        //compiledText = cP.compileGalleries(compiledText, pageName);
        //compiledText = cP.compileDividers(compiledText,pageName);
        
        compiledText = compileMainText(compiledText, getResourcePageCfg());
        //Replace content
        compiledText = compileTitles(compiledText, pageName);
        compiledText = compileParagraphs(compiledText, pageName);
        compiledText = compileCodes(compiledText, pageName);
        compiledText = compileFiles(compiledText, pageName);
        compiledText = compileImages(compiledText, pageName);
        compiledText = compileGalleries(compiledText, pageName);
        compiledText = compileDividers(compiledText,pageName);
        //cP = null;
        return(compiledText);
    }
    //compile navigation for main
    private String compileNavMainPage() {
        //For navigation, this is the only place one page needs to know about others
        String allPageNames = getDesignSetItem("allPageNames");
        String allPageTitles = getDesignSetItem("allPageTitles");
        String allDisplayInNav = getDesignSetItem("allDisplayInNav");
        if (allPageNames == null) {
            throw new NullPointerException("Null allPageNames");
        }
        if (allPageTitles == null) {
            throw new NullPointerException("Null allPageTitles");
        }
        if (allDisplayInNav == null) {
            throw new NullPointerException("Null allDisplayInNav");
        }
        String[] allPageNameArray = allPageNames.split("\n");
        String[] allPageTitleArray = allPageTitles.split("\n");
        String[] allDisplayInNavArray = allDisplayInNav.split("\n");
        if (allPageNameArray.length != allPageTitleArray.length 
            || allPageNameArray.length != allDisplayInNavArray.length) {
            throw new IllegalArgumentException("Page name / title / displayInNav array sizes do not match! "
                + allPageNameArray.length 
                + "vs." + allPageTitleArray.length 
                + "vs." + allDisplayInNavArray.length);
        }
        StringBuilder navListText = new StringBuilder("");
        String navClassOther = "nav-entry";
        String navClassSelected = "nav-entry-selected";
        String navClassThis;
        for (int i = 0; i < allPageNameArray.length; i++) {
            if (!allPageNameArray[i].equals("") && (Boolean.parseBoolean(allDisplayInNavArray[i]))) {
                if (getPageName().equals(allPageNameArray[i])) {
                    navClassThis = navClassSelected;
                } else {
                    navClassThis = navClassOther;
                }
                navListText.append("\n<tr><td class=\""+ navClassThis + "\"><a class=\"nav\" href=\""
                    + allPageNameArray[i] + ".html\">"
                    + allPageTitleArray[i] + "</a></td></tr>\n");
            }            
        }
        return new String(navListText);
    }
    //Compile navigation for mobile 
    private String compileNavMobilePage() {
        String allPageNames = getDesignSetItem("allPageNames");
        String allPageTitles = getDesignSetItem("allPageTitles");
        String allDisplayInNav = getDesignSetItem("allDisplayInNav");
        if (allPageNames == null) {
            throw new NullPointerException("Null allPageNames");
        }
        if (allPageTitles == null) {
            throw new NullPointerException("Null allPageTitles");
        }
        if (allDisplayInNav == null) {
            throw new NullPointerException("Null allDisplayInNav");
        }
        String[] allPageNameArray = allPageNames.split("\n");
        String[] allPageTitleArray = allPageTitles.split("\n");
        String[] allDisplayInNavArray = allDisplayInNav.split("\n");
        if (allPageNameArray.length != allPageTitleArray.length 
            || allPageNameArray.length != allDisplayInNavArray.length) {
            throw new IllegalArgumentException("Page name / title / displayInNav array sizes do not match! "
                + allPageNameArray.length 
                + "vs." + allPageTitleArray.length 
                + "vs." + allDisplayInNavArray.length);
        }
        StringBuilder navListText = new StringBuilder("");
        String navSelected = "selected=\"selected\" ";
        String navNonSelected = "";
        String navSelectedThis;
        for (int i = 0; i < allPageNameArray.length; i++) {
            if (!allPageNameArray[i].equals("") && (Boolean.parseBoolean(allDisplayInNavArray[i]))) {
                if (getPageName().equals(allPageNameArray[i])) {
                    navSelectedThis = navSelected;
                } else {
                    navSelectedThis = navNonSelected;
                }
                //use php & option to achieve compact navigation in mobile
                navListText.append("<option " + navSelectedThis + "value=\"/mobile/" 
                    + allPageNameArray[i] + ".php\">" 
                    + allPageTitleArray[i] + "</option>\n");
            }            
        }
        return new String(navListText);
    }
    
    /*************************************************************/
    /*************** Compiling routines **************************/
    /*************************************************************/
    //Framework compile
    /* This is compiling for the whole website, not for single pages
    So it is better to fit in this class, not in Page class */
    //MainText
    private String compileMainText(String text, String pageCfg) {
        String mainText = FileUtilities.read(pageCfg, "UTF-8");
        if (mainText == null) {
            mainText = "";
        }
        String compiledText = text.replace("<<<###_MAINTEXT_###>>>", insertMarker(mainText, "MAINTEXT"));
        return(compiledText);
    }
    //Navigation
    private String compileNavigation(String text, String navText) {
        String compiledText = text.replace("<<<###_NAVIGATION_###>>>", insertMarker(navText, "NAVIGATION"));
        return(compiledText);
    }
    
    //WebModule Compile
    //Compile routine
    private String compileRoutine(String text, String pageName, WebModuleEnum typeEnum) {
        if (text == null) {
            throw new NullPointerException("Null text when compiling " + typeEnum.getValue());
        }
        if (pageName == null) {
            throw new NullPointerException("Null pageName when compiling " + typeEnum.getValue());
        }
        if (typeEnum == null) {
            throw new NullPointerException("Null typeEnum when compiling " + typeEnum.getValue());
        }
        String compiledText = new String(text);
        //ContentParser cP = new ContentParser(new DesignInfoSet(designInfoSet));
        int id = ContentParser.patternIdFind(compiledText, typeEnum.getValue());
        while (id >= 1) {
            WebModuleDefault module = null;
            switch (typeEnum) {
                case TITLE: 
                    module = new WebModuleTitle(new DesignInfoSet(designInfoSet), pageName, id);
                    break;
                case PARAGRAPH: 
                    module = new WebModuleParagraph(new DesignInfoSet(designInfoSet), pageName, id);
                    break;
                case CODE: 
                    module = new WebModuleCode(new DesignInfoSet(designInfoSet), pageName, id);
                    break;
                case FILE: 
                    module = new WebModuleFile(new DesignInfoSet(designInfoSet), pageName, id);
                    break;
                case IMAGE: 
                    module = new WebModuleImage(new DesignInfoSet(designInfoSet), pageName, id);
                    break;
                case GALLERY: 
                    module = new WebModuleGallery(new DesignInfoSet(designInfoSet), pageName, id);
                    break;
                case DIVIDER: 
                    module = new WebModuleDivider(new DesignInfoSet(designInfoSet), pageName, id);
                    break;
                default:
                    throw new IllegalArgumentException("Type " 
                        + typeEnum.getValue() + " is not legal");    
            }
            compiledText = compiledText.replaceAll("(?i)" + module.getTargetWithMarker(), module.getMarkedContent());
            id = ContentParser.patternIdFind(compiledText, typeEnum.getValue());
        }        
        return(compiledText);
    }
    //Title
    private String compileTitles(String text, String pageName) {
        return compileRoutine(text, pageName, WebModuleEnum.TITLE);
    }    
    //Paragraph
    private String compileParagraphs(String text, String pageName) {
        return compileRoutine(text, pageName, WebModuleEnum.PARAGRAPH);
    }
    //Code
    private String compileCodes(String text, String pageName) {
        return compileRoutine(text, pageName, WebModuleEnum.CODE);
    }
    //File
    private String compileFiles(String text, String pageName) {
        return compileRoutine(text, pageName, WebModuleEnum.FILE);
    }
    //Image
    private String compileImages(String text, String pageName) {
        return compileRoutine(text, pageName, WebModuleEnum.IMAGE);
    }
    //Gallery
    private String compileGalleries(String text, String pageName) {
        return compileRoutine(text, pageName, WebModuleEnum.GALLERY);
    }
    //Divider
    private String compileDividers(String text, String pageName) {
        return compileRoutine(text, pageName, WebModuleEnum.DIVIDER);
    }
    //Insert identifier 
    private String insertMarker(String textItem, String target) {
        return("<!-- " + target + " START -->" 
            + textItem 
            + "<!-- " + target + " END -->");
    }    
    private String insertMarker(String text, WebModuleEnum typeEnum, int id) {
        return(insertMarker(text, typeEnum.getValue() + "_" + id));
    }    
    
    public static void main(String[] args) {
        Page p = new Page(1, "Index", "Home Page", true, 
            new DesignInfoSet(new HashMap<String, String>()));
        System.out.println(p.getLevel());
        System.out.println(p.getPageName());
        System.out.println(p.getPageTitle());
        System.out.println(p.getDisplayInNav());
        System.out.println(p.getCfgName());
        System.out.println(p.getMainFullFileName());
        System.out.println(p.getMobileFullFileName());
        System.out.println(p.getMainFullFilePath());
        System.out.println(p.getMobileFullFilePath());
        System.out.println(p.getUploadParent());
        System.out.println(p.getResourceCfgDir());
        System.out.println(p.getResourcePageCfg()); 
        System.out.println(p.getPageContent());
    }  
    
}