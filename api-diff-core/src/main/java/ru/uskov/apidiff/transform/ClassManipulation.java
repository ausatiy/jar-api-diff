package ru.uskov.apidiff.transform;

/**
 * Maker interface indicating that operation creates/deletes/renames class
 */
public interface ClassManipulation {

    boolean affectsClass(String name);
}
