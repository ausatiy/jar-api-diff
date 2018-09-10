package ru.uskov.apidiff.metric;

public class Weights {

    public int getRenameClassWeight() {
        return 1000;
    }

    public int getRemoveWeight() {
        return 1000;
    }

    public int getAddWeight() {
        return 1000;
    }

    public int getChangeClassAttributes() {
        return 100;
    }

    public int getRemoveMethodWeight() {
        return 100;
    }

    public int getAddMethodWeight() {
        return 100;
    }

    public int getRenameMethodWeight() {
        return 10;
    }

    public int getChangeMethodAttributes() {
        return 10;
    }
}
