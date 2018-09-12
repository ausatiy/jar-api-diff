package ru.uskov.apidiff.output;

import ru.uskov.apidiff.graph.GraphNode;
import ru.uskov.apidiff.transform.TransformOperation;

import java.io.PrintStream;
import java.util.function.Consumer;

public class TextOutputPrinter implements OutputPrinter{

    @Override
    public void accept(GraphNode node, Consumer<String> consumer) {
        for (TransformOperation transformOperation : node.getTransforms()) {
            consumer.accept(transformOperation.toString());
        }
    }
}
