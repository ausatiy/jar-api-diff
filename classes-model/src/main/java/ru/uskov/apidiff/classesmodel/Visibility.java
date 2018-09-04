package ru.uskov.apidiff.classesmodel;

import org.objectweb.asm.Opcodes;
public enum Visibility {
    PUBLIC("public"),
    PROTECTED("protected"),
    PACKAGE_LOCAL(""),
    PRIVATE("private");

    private final String description;

    Visibility(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static Visibility of(int accessFlags) {
        if ((accessFlags & Opcodes.ACC_PUBLIC) != 0) {
            return PUBLIC;
        }
        if ((accessFlags & Opcodes.ACC_PRIVATE) != 0) {
            return PRIVATE;
        }
        if ((accessFlags & Opcodes.ACC_PROTECTED) != 0) {
            return PROTECTED;
        }
        return PACKAGE_LOCAL;
    }
}
