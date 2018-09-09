package ru.uskov.apidiff.classesmodel;

import org.immutables.value.Value;

import java.util.List;

@Value.Immutable(prehash = true)
public interface InstructionInstance {

    int getOpcode();

    List<Object> getParameters();
}
