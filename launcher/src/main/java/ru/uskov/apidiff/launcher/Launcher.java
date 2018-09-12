package ru.uskov.apidiff.launcher;

import ru.uskov.apidiff.classesmodel.Api;
import ru.uskov.apidiff.classesmodel.ClassParser;
import ru.uskov.apidiff.classesmodel.objectnaming.JavaStyleObjectNameMapper;
import ru.uskov.apidiff.classesmodel.objectnaming.ObjectNameMapper;
import ru.uskov.apidiff.graph.Graph;
import ru.uskov.apidiff.graph.GraphNode;
import ru.uskov.apidiff.graph.RouteHelper;
import ru.uskov.apidiff.output.TextOutputPrinter;

import java.io.File;
import java.io.IOException;

public class Launcher {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage file1 file2");
            System.exit(1);
        } else {
            ObjectNameMapper mapper = new JavaStyleObjectNameMapper();
            final ClassParser classParser = new ClassParser(mapper);
            System.out.println("Reading classes...");
            try {
                Api from = classParser.readClasses(new File(args[0]));
                Api to = classParser.readClasses(new File(args[1]));

                System.out.println("Classes were read. Classes count " + from.getClassNames().size());

                GraphNode finalGraphNode = new Graph(from, RouteHelper.RenamePolicy.PACKAGE_MOVE).pathTo(to);
                new TextOutputPrinter().accept(finalGraphNode, x -> System.out.println(x));
            } catch (IOException e) {
                System.out.println("Could not read classes: " + e.getMessage());
                System.exit(1);
            }
        }
    }

}  
