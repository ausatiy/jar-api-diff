package ru.uskov.apidiff.demo;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestDemo {

    @Test
    public void testDemo() throws IOException {
        ObjectNameMapper mapper = new JavaStyleObjectNameMapper();
        final ClassParser classParser = new ClassParser(mapper);

        Api from = classParser.readClasses(new File("../test-resources/demoFrom/build/libs/demoFrom.jar"));
        Api to = classParser.readClasses(new File("../test-resources/demoTo/build/libs/demoTo.jar"));

        GraphNode finalGraphNode = new Graph(from, RouteHelper.RenamePolicy.REMOVED_TO_ADDED).pathTo(to);

        final List<String> transforms = new ArrayList<>();
        new TextOutputPrinter().accept(finalGraphNode, x -> transforms.add(x));
        assertEquals(15, transforms.size());

        assertThat(transforms, CoreMatchers.hasItem("Class \"demo.classes.Class2Remove\" was removed."));
        assertThat(transforms, CoreMatchers.hasItem("Class \"demo.classes.Class2Add\" with 21 methods was added."));
        assertThat(transforms, CoreMatchers.hasItem("Class \"demo.classes.Class2Rename\" was renamed to \"demo.classes.ClassRenamed2\"."));
        assertThat(transforms, CoreMatchers.hasItem("Class \"demo.classes.SignatureChangedFrom\" was renamed to \"demo.classes.SignatureChangedTo\"."));
        assertThat(transforms, CoreMatchers.hasItem("Class signature changed from \"public abstract class demo.classes.SignatureChangedTo extends java.io.InputStream implements java.io.Serializable\" to \"public final class demo.classes.SignatureChangedTo implements java.io.Serializable\"."));
        assertThat(transforms, CoreMatchers.hasItem("In class \"demo.methods.ClassWithMethods\" method \"public void added()\" was added."));
        assertThat(transforms, CoreMatchers.hasItem("In class \"demo.methods.ClassWithMethods\" method \"public void removed_2(byte)\" was added."));
        assertThat(transforms, CoreMatchers.hasItem("In class \"demo.methods.ClassWithMethods\" method \"public void renameFrom_2()\" was renamed to \"renameTo_1\"."));
        assertThat(transforms, CoreMatchers.hasItem("In class \"demo.methods.ClassWithMethods\" method \"public void renameFrom_1()\" was renamed to \"renameTo_2\"."));
        assertThat(transforms, CoreMatchers.hasItem("In class \"demo.methods.ClassWithMethods\" method \"public void renameTo_3()\" was added."));
        assertThat(transforms, CoreMatchers.hasItem("In class \"demo.methods.ClassWithMethods\" method \"public void signatureChanged_1()\" changed to \"public String signatureChanged_1()\"."));
        assertThat(transforms, CoreMatchers.hasItem("In class \"demo.methods.ClassWithMethods\" method \"public final void signatureChanged_2() throws java.io.IOException\" changed to \"protected final void signatureChanged_2()\"."));
        assertThat(transforms, CoreMatchers.hasItem("In class \"demo.methods.ClassWithMethods\" method \"public void removed_1()\" was removed."));
        assertThat(transforms, CoreMatchers.hasItem("In class \"demo.methods.ClassWithMethods\" method \"public void removed_2(int)\" was removed."));
        assertThat(transforms, CoreMatchers.hasItem("In class \"demo.methods.ClassWithMethods\" method \"public void renameFrom_3()\" was removed."));
        System.out.println("----------");
        System.out.println("Transforms: " + transforms.stream().collect(Collectors.joining("\n")));

    }




}
