package ru.uskov.apidiff.output;

import ru.uskov.apidiff.graph.GraphNode;

import java.io.PrintStream;

public interface OutputPrinter {

    void accept(GraphNode node, PrintStream ps);
}
