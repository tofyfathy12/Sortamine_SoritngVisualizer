import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

public class UpdatePackages {
    public static void main(String[] args) throws Exception {
        String baseDir = "d:/Engineering/CSED/2nd Year/2nd Semester/Data Structures and Algorithms/Labs/Sortamine/src/main/java/com/app/sortamine";
        File dir = new File(baseDir);
        processDirectory(dir);
    }

    private static void processDirectory(File dir) throws Exception {
        if (!dir.isDirectory())
            return;
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                processDirectory(f);
            } else if (f.getName().endsWith(".java")) {
                String content = new String(Files.readAllBytes(f.toPath()), StandardCharsets.UTF_8);
                boolean changed = false;

                // Package updates
                String parent = f.getParentFile().getName();
                if (parent.equals("controllers") && content.contains("package com.app.sortamine;")) {
                    content = content.replace("package com.app.sortamine;", "package com.app.sortamine.controllers;");
                    changed = true;
                } else if (parent.equals("utils") && content.contains("package com.app.sortamine;")) {
                    content = content.replace("package com.app.sortamine;", "package com.app.sortamine.utils;");
                    changed = true;
                } else if (parent.equals("events") && content.contains("package com.app.sortamine.models;")) {
                    content = content.replace("package com.app.sortamine.models;", "package com.app.sortamine.events;");
                    changed = true;
                } else if (parent.equals("algorithms") && content.contains("package com.app.sortamine.strategies;")) {
                    content = content.replace("package com.app.sortamine.strategies;",
                            "package com.app.sortamine.algorithms;");
                    changed = true;
                }

                // Now check for dependencies that were formerly in `com.app.sortamine` but are
                // now in subpackages
                // Since they were in same package, they wouldn't have imports! If they use
                // `Controller` or `BarGenerator`, they might need imports now.
                // Looking at the code: BarGenerator and Controller probably don't refer to each
                // other.
                // However, the script doesn't handle missing imports cleanly. Let's see if we
                // need to manually add them by compiling.

                // Import updates
                if (content.contains("import com.app.sortamine.Controller;")) {
                    content = content.replace("import com.app.sortamine.Controller;",
                            "import com.app.sortamine.Controller;");
                    changed = true;
                }
                if (content.contains("import com.app.sortamine.BarGenerator;")) {
                    content = content.replace("import com.app.sortamine.BarGenerator;",
                            "import com.app.sortamine.utils.BarGenerator;");
                    changed = true;
                }
                if (content.contains("import com.app.sortamine.models.SortingEventType;")) {
                    content = content.replace("import com.app.sortamine.models.SortingEventType;",
                            "import com.app.sortamine.events.SortingEventType;");
                    changed = true;
                }

                String newContent = content.replaceAll("import com\\.app\\.sortamine\\.models\\.([A-Za-z0-9]+Event);",
                        "import com.app.sortamine.events.$1;");
                if (!newContent.equals(content)) {
                    content = newContent;
                    changed = true;
                }
                newContent = content.replaceAll("import com\\.app\\.sortamine\\.strategies\\.",
                        "import com.app.sortamine.algorithms.");
                if (!newContent.equals(content)) {
                    content = newContent;
                    changed = true;
                }

                // Add imports for things that were in root com.app.sortamine and moved, if they
                // are used but not imported
                // MinHeap uses SwapEvent, etc? That's models to models, now models to events.
                // MinHeap needs `import com.app.sortamine.events.*`?
                // Wait, MinHeap and Event classes were both in `models` package! So they didn't
                // have an import.
                // Now MinHeap is in models, events are in events. MinHeap needs an import for
                // events!
                if (f.getName().equals("MinHeap.java")) {
                    if (!content.contains("import com.app.sortamine.events.")) {
                        content = content.replace("package com.app.sortamine.models;",
                                "package com.app.sortamine.models;\n\nimport com.app.sortamine.events.*;");
                        changed = true;
                    }
                }

                if (changed) {
                    Files.write(f.toPath(), content.getBytes(StandardCharsets.UTF_8));
                }
            }
        }
    }
}
