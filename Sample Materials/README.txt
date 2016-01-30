Sample configurations for your convenience:

================================
=== File Description: ==========
================================

1. images-resource: The folder where you put all image resources needed by the website, e.g. logo, Facebook icon, framework background, etc. The sample contains common icons and the images used by the sample framework.

2. styles: The folder containing the css stylesheets used by the website. stylesheet.css is for the PC html, and stylesheet-mobile.css is for the mobile page. The lightbox directory in the sample is an open-source implementation of lightbox style used for Gallery.

3. Framework.txt: Framework for PC page. See below for key markdowns.

4. Framework-mobile.txt: Framework for mobile page. See below for key markdowns.

5. Website_Name.txt: Defining website name.

6. Website_URL.txt: Defining website URL.

7. mobile_optExtra.txt: extra mobile optimization as needed; see notes in the file for format guidelines

================================
=== How to Use Sample: =========
================================

Once you have specified the design root in the app:.

1. Put images-resource under:
<Your run directory>/Design-Resources/
This is also where you would see a "cfg" directory.
You can use this to double check the location.

2. Copy "styles" to:
<Your design root>/Website/
You can also open the stylesheet.css and stylesheet-mobile.css, and copy the content to the Style & Mobile Style setup in the app. But the lightbox folder can only be placed manually at the moment.

3. Copy Framework.txt, Framework-mobile.txt, Website_Name.txt, Website_URL.txt, and mobile_optExtra.txt to:
<Your design root>/Resource/
You can also open the files and copy the content into the setups in the app.

================================
=== Markdown Notes: ============
================================

Markdowns in framework.txt and framework-mobile.txt are listed as follows.

<<<###_MAINTEXT_###>>>: to be replaced by main page content.
<<<###_NAVIGATION_###>>>: to be replaced by navigation sidebar/menu.
<WEBURL>: to be replaced by website url.
<WEBNAME>: to be replaced by website name.
<PAGETITLE>: to be replaced by page title.
<PAGENAME>: to be replaced by page name.
<imagesResourcesRel>: to be replaced by "imagesResourcesRel", which can be changed by updating source code.
<uploadsResourcesRel>: to be replaced by "uploads", which can be changed by updating source code.