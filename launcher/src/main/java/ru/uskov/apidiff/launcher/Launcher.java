package ru.uskov.apidiff.launcher;

import ru.uskov.apidiff.classesmodel.ClassInstance;
import ru.uskov.apidiff.classesmodel.ClassParser;
import ru.uskov.apidiff.classesmodel.objectnaming.JavaStyleObjectNameMapper;
import ru.uskov.apidiff.classesmodel.objectnaming.ObjectNameMapper;
import ru.uskov.apidiff.graph.Graph;
import ru.uskov.apidiff.graph.GraphNode;
import ru.uskov.apidiff.output.TextOutputPrinter;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class Launcher {

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Usage file1 file2");
            System.exit(1);
        } else {
            ObjectNameMapper mapper = new JavaStyleObjectNameMapper();
            final ClassParser classParser = new ClassParser(mapper);
            Set<ClassInstance> from = classParser.readClasses(new File(args[0]));
            Set<ClassInstance> to = classParser.readClasses(new File(args[1]));

            GraphNode finalGraphNode = new Graph(from).pathTo(to);
             new TextOutputPrinter().accept(finalGraphNode, System.out);

        }
    }

}  
