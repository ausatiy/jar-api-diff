package ru.uskov.apidiff.graph;

import ru.uskov.apidiff.graph.Graph;
import org.junit.Test;
import ru.uskov.apidiff.classesmodel.ClassInstance;
import ru.uskov.apidiff.classesmodel.Utils;
import ru.uskov.apidiff.objectnaming.JavaStyleObjectNameMapper;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class TestGraph {


    @Test
    public void testRenameClass() throws IOException {
        Set<ClassInstance> oldClasses = Utils.readClassesToSet("../test-resources/baseClasses/build/libs/baseClasses.jar", new JavaStyleObjectNameMapper());
        Set<ClassInstance> newClasses = Utils.readClassesToSet("../test-resources/renameBaseClasses/build/libs/renameBaseClasses.jar", new JavaStyleObjectNameMapper());

        Graph graph = new Graph(oldClasses);
        graph.pathTo(newClasses);

    }
}
