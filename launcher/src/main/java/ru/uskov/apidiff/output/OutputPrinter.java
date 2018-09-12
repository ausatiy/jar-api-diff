package ru.uskov.apidiff.output;

import ru.uskov.apidiff.graph.GraphNode;

import java.util.function.Consumer;

public interface OutputPrinter {

    void accept(GraphNode node, Consumer<String> consumer);
}
