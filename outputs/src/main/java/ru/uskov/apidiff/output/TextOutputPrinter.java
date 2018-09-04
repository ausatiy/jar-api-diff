package ru.uskov.apidiff.output;

import ru.uskov.apidiff.graph.GraphNode;
import ru.uskov.apidiff.transform.TransformOperation;

import java.io.PrintStream;

public class TextOutputPrinter implements OutputPrinter{

    @Override
    public void accept(GraphNode node, PrintStream ps) {
        for (TransformOperation transformOperation : node.getTransforms()) {
            ps.println(transformOperation);
        }
    }
}
