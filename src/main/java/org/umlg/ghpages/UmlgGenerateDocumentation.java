package org.umlg.ghpages;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Date: 2014/04/12
 * Time: 9:40 AM
 */
public class UmlgGenerateDocumentation {

    public static void main(String[] args) throws IOException, InterruptedException {
        //Parse the markdown and produce a corresponding html menu fragment
        //Clear the generated-menu dir
        FileUtils.cleanDirectory(new File("./doc/markdown-parsed"));
        for (File md : getMarkdownFiles()) {
            FileUtils.deleteQuietly(new File("./" + md.getName().substring(0, md.getName().length() - 3) + ".html"));
            List<Menu> rootMenus = parseMd(md);

            //parse markdown
            ProcessBuilder pb = new ProcessBuilder("markdown", md.getAbsolutePath());
            pb.redirectOutput(ProcessBuilder.Redirect.appendTo(new File("./doc/markdown-parsed/" + md.getName() + ".parsed")));
            Process p = pb.start();
            p.waitFor();

            //insert the menu into the template
            String template;
            if (!hasMenu(md)) {
                template = FileUtils.readFileToString(new File("./doc/html_template/documentation_no_menu_template.html"));
            } else if (isSqlg(md)) {
                template = FileUtils.readFileToString(new File("./doc/html_template/documentation_sqlg_template.html"));
                String completeMenu = "";
                boolean first = true;
                for (Menu rootMenu : rootMenus) {
                    completeMenu += rootMenu.toHtml(first);
                    first = false;
                }
                template = template.replace("<!-- GENERATED MENU -->", completeMenu);
            } else {
                template = FileUtils.readFileToString(new File("./doc/html_template/documentation_template.html"));
                String completeMenu = "";
                boolean first = true;
                for (Menu rootMenu : rootMenus) {
                    completeMenu += rootMenu.toHtml(first);
                    first = false;
                }
                template = template.replace("<!-- GENERATED MENU -->", completeMenu);
            }

            //Insert the heading into the template
            List<String> rawMarkdown = FileUtils.readLines(md);
            String heading = rawMarkdown.get(0);
            heading = heading.replace("<!--", "").replace("-->","");
            template = template.replace("<!-- GENERATED HEADING -->", heading.trim());

            //insert the md content into the template with menu
            String content = FileUtils.readFileToString(new File("./doc/markdown-parsed/" + md.getName() + ".parsed"));
            content = content.replace("<pre>", "<pre class=\"prettyprint\">");
            content = content.replace("<code>", "<code class=\"language-java\">");
            template = template.replace("<!-- GENERATED CONTENT -->", content);


            if (hasMenu(md)) {
                //Insert the menu ids into the markdown produced html
                for (Menu rootMenu : rootMenus) {
                    template = rootMenu.insertAffixId(template);
                }
            }

            FileUtils.write(new File("./" + md.getName().substring(0, md.getName().length() - 3) + ".html"), template);
        }
        System.out.println("Done generating docs.");
    }

    private static boolean isSqlg(File md) {
        return md.getName().equals("sqlg.md");
    }

    private static boolean hasMenu(File md) {
        return !md.getName().equals("doc_home.md") && !md.getName().equals("introduction.md");
    }

    private static List<Menu> parseMd(File md) throws IOException {
        List<Menu> rootMenus = new ArrayList<>();
        Menu current = null;
        String mdAsString = FileUtils.readFileToString(md);
        int indexOfHash = mdAsString.indexOf("#");
        while (indexOfHash != -1) {
            int indexOfNewLine = mdAsString.indexOf("\n", indexOfHash);
            String heading = mdAsString.substring(indexOfHash, indexOfNewLine);
            if (current == null) {
                //The root menu
                current = new Menu(heading);
                rootMenus.add(current);
            } else {
                Menu child = new Menu(heading);
                if (numberOfHashes(heading) > current.menuNumberOfHashes()) {
                    current.children.add(child);
                    child.parent = current;
                    current = child;
                } else {
                    Menu parent = current.findParentFor(child);
                    if (parent != null) {
                        parent.children.add(child);
                        child.parent = parent;
                        current = child;
                    } else {
                        current = child;
                        rootMenus.add(current);
                    }
                }
            }
            indexOfHash = mdAsString.indexOf("\n#", indexOfNewLine);
            if (indexOfHash != -1) {
                indexOfHash = indexOfHash + 1;
            }

        }
        return rootMenus;

    }

    private static Collection<File> getMarkdownFiles() {
        return FileUtils.listFiles(new File("./doc/markdown"), new IOFileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().endsWith(".md");
            }

            @Override
            public boolean accept(File file, String s) {
                return s.endsWith(".md");
            }

        }, null);
    }

    private static class Menu {

        private String name;
        private Menu parent;
        private List<Menu> children = new ArrayList<Menu>();

        private Menu(String name) {
            this.name = name;
        }

        private int menuNumberOfHashes() {
            return numberOfHashes(this.name);
        }

        public Menu findParentFor(Menu child) {
            if (numberOfHashes(this.name) >= child.menuNumberOfHashes()) {
                if (this.parent != null) {
                    return this.parent.findParentFor(child);
                } else {
                    return null;
                }
            } else {
                return this;
            }
        }

        public String toHtml(boolean first) throws IOException {
            StringBuilder html = new StringBuilder();
            toHtml(html, first, false, false);
            return html.toString();
        }

        private void toHtml(StringBuilder html, boolean first, boolean printUlStart, boolean printUlEnd) {
            if (printUlStart) {
                html.append("<ul class=\"nav\">\n");
            }
            if (first) {
                html.append("<li class=\"active\">");
            } else {
                html.append("<li>");
            }
            html.append("<a href=\"#" + this.getId() + "\">" + this.name.replace("#", "") + "</a>");
            int count = 1;
            for (Menu child : this.children) {
                child.toHtml(html, false, count == 1, count == this.children.size());
                count++;
            }
            html.append("</li>");
            if (printUlEnd) {
                html.append("</ul>\n");
            }
        }

        private String getId() {
            return name.replace(" ", "").replace("#", "") + "ID";
        }

        public String insertAffixId(String content) {
            content = content.replace(
                    "<h" + String.valueOf(numberOfHashes(this.name)) + ">" + this.name.replace("#", ""),
                    "<h" + String.valueOf(numberOfHashes(this.name)) + " id=\"" + this.name.replace("#", "").replace(" ", "") + "ID\"" + ">" + this.name.replace("#", ""));
            for (Menu child : this.children) {
                content = child.insertAffixId(content);
            }
            return content;
        }
    }

    private static int numberOfHashes(String name) {
        int count = 0;
        for (int i = 0; i < name.length(); i++) {
            if (name.charAt(i) == '#') {
                count++;
            } else {
                break;
            }
        }
        return count;
    }

}
