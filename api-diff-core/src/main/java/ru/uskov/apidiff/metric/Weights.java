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
}
