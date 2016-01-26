# Java-WebEditorProject
Java Project: WebEditor featuring convenient maintenance and clean file hierarchy

Deliverable:
A package of website files that can be uploaded to any hosting service via FTP directly

Inspired by the concept of Weebly: 
1. Modulize content and allow flexible assembly
2. Standardize each module with a module template
3. Users need little knowledge to maintain their deliverable (website)

Improvement/Difference:
1. Less restrictive: users have full access to all files compiled, as well as the webpage templates (including headers). Only HTML skill is needed to make customized changes.
2. With source code, users have full access to module templates as well, although Java programming skill is needed to make changes
3. No dependency on service providers any more. Unlike Weebly where it does not allow PHP or ASP or database to co-exist (due to no-access to the root folder), you can now keep these components in the same hosting service as you wish.
4. File hiararchies are very clear and neatly organized. It is much easier to delete an uploaded file from your package compared to Weebly. That makes file deletion efficient if you need to clean up the space due to storage concerns in some hosting service providers.
5. Webpage content is labeled with markdowns for each module. This makes the page much easier to read if someone wants to do some debugging with his/her new template trials.

To Do (list to increase):
1. Create more templates. Currently you still need one with HTML skills to build your own initial framework template (but afterwards to update or maintain website content, it is of little effort). With more built-in templates, this effort can be eliminated as well.
2. Incorporate more modules. Currently supported: Title, Paragraph, Code, downloadable File, embedded Image, Gallary, and Divider. Modules in planning at the moment are media, and multi-column.
3. Implement module renumbering. Now module id's are numbered incrementally and cannot be manually changed (usually you don't have such a need).
4. Implement drag-n-drop.
5. Make navigation more elegant and support multi-level navigation.

Current Platform: Windows
Mac/Linux support: Under Testing

Usage Instruction (Windows)
Location: src\
Compile: javac views\*.java controllers\*.java models\*.java utilities\*.java -d <your class dir>
Execute: java controllers.WebEditorController
