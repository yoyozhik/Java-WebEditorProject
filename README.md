# Java-WebEditorProject

Java Project: WebEditor featuring convenient maintenance and clean file hierarchy

==================== Project Brief ========================================

Code structure: Model-View-Controller
- models: model classes for data 
- views: GUI classes
- controllers: controllers for GUI and model communication
- utilities: common utilities, including file and unicode utilities

Deliverable:

A package of website files that can be uploaded to any hosting service via FTP directly

Inspired by the concept of Weebly: 

1. Modulize content and allow flexible assembly
2. Standardize each module with a module template
3. Users need little knowledge to maintain their deliverable (website)

Improvement/Difference:

1. Less restrictive: users have full access to all files compiled, as well as the webpage templates (including headers). Only some HTML skill is needed to make customized changes, including making additional optimizations to mobile version (e.g. make the embedded Google Calendar more mobile-friendly).
2. With source code, users have full access to module templates as well, although Java programming skill is needed to make changes
3. No dependency on service providers any more. Unlike Weebly where it does not allow PHP or ASP or database to co-exist (due to no access to the root folder), you can now keep these components in the same hosting service as you wish. You can also easily switch hosting services by simply copying the website files over.
4. File hiararchies are very clear and neatly organized. It is much easier to delete an uploaded file from your package compared to Weebly (where all files/images are placed in one single folder). That makes file deletion efficient if you need to clean up the space due to storage concerns in some hosting service providers.
5. Webpage content is labeled with markdowns for each module. This makes the page much easier to read if someone wants to do some debugging with his/her new template trials.

To Do (list to increase):

1. Create more templates. Currently you still need one with HTML skills to build your own initial framework template (but afterwards to update or maintain website content, it is of little effort). With more built-in templates, this effort can be eliminated as well.
2. Incorporate more modules. Currently supported: Title, Paragraph, Code, downloadable File, embedded Image, Gallary, and Divider. Modules in planning at the moment are media, and multi-column.
3. Implement module renumbering. Now module id's are numbered incrementally and cannot be manually changed (usually you don't have such a need).
4. Implement drag-n-drop.
5. Make navigation more elegant and support multi-level navigation.
6. Make the mobile pages more elegant.
7. Implement advanced listeners to prevent users from corrupting the well-formatted markdowns.
8. Enable richtext editing so that special formatting & adding hyperlinks would become easier.

==================== Project Usage Instruction ========================================

Current Platform: Windows

Mac/Linux support: Under Testing

Usage Instruction (Windows)
- Location: src\
- Compile: javac org\dharmatech\views\*.java org\dharmatech\controllers\*.java org\dharmatech\models\*.java org\dharmatech\utilities\*.java -d {your class dir}
- Execute: java org.dharmatech.controllers.WebEditorController


==================== Q & A ========================================

Q: Why are you supporting Windows instead of Mac/Linux as a starting point?

A: Considering the major customers, who have little knowledge about coding, they are mainly Windows and Mac users, not Linux users. And nowadays Windows is still the mainstream by looking at the market share, compared to Mac. So it makes sense to start with a Windows implementation.

We definitely will extend the support over to Mac and Linux, but Windows is a good starting point to make sure the majority can be benefited asap.

Actually, with Java implementation, it is by nature cross-platform. And this is also why we chose Java. The only thing remaining under testing is the difference in file systems, e.g. how a file path is represented and how two filepaths are considered referring to the same file.

Q: Is the deliverable mobile-friendly?

A: Yes. Although the mobile version of the deliverable is not that beautiful yet, it has been verified that the deliverable is considered mobile-friendly by Google Search, and therefore will receive the ranking bonus for mobile-friendly sites.

In addition, you can add more mobile-friendly treatments in the mobile_optExtra.txt (see sample). For example, if you are embedding Google calendar in your website, we recommend using the full view in Web version (more visualized), and using agenda view in Mobile version (capable of listing all details with the limited screen size).
