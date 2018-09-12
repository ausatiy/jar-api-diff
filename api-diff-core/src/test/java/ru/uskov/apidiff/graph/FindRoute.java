package ru.uskov.apidiff.graph;

import org.junit.Test;
import ru.uskov.apidiff.classesmodel.Api;
import ru.uskov.apidiff.classesmodel.ClassInstance;
import ru.uskov.apidiff.classesmodel.Utils;
import ru.uskov.apidiff.classesmodel.objectnaming.JavaStyleObjectNameMapper;
import ru.uskov.apidiff.transform.classmanipulation.RenameClassOperation;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

public class FindRoute {


    @Test
    public void testRenameClass() throws IOException {
        Api oldClasses = Utils.readClasses("../test-resources/baseClasses/build/libs/baseClasses.jar", new JavaStyleObjectNameMapper());
        Api newClasses = Utils.readClasses("../test-resources/renameBaseClasses/build/libs/renameBaseClasses.jar", new JavaStyleObjectNameMapper());
        GraphNode finalGraphNode = new Graph(oldClasses, RouteHelper.RenamePolicy.REMOVED_TO_ADDED).pathTo(newClasses);
        assertNotNull(finalGraphNode);
        assertEquals(newClasses, finalGraphNode.getApi());
        assertEquals(1, finalGraphNode.getTransforms().size());
    }

    @Test
    public void testAddRmClasses() throws IOException {
        Api oldClasses = Utils.readClasses("../test-resources/baseClasses/build/libs/baseClasses.jar", new JavaStyleObjectNameMapper());
        Api newClasses = Utils.readClasses("../test-resources/addRmRenameClasses/build/libs/addRmRenameClasses.jar", new JavaStyleObjectNameMapper());
        GraphNode finalGraphNode = new Graph(oldClasses, RouteHelper.RenamePolicy.REMOVED_TO_ADDED).pathTo(newClasses);
        assertNotNull(finalGraphNode);
        assertEquals(newClasses, finalGraphNode.getApi());
        assertEquals(6, finalGraphNode.getTransforms().size());
    }

}
