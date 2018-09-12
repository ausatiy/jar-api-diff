package ru.uskov.apidiff.classesmodel;

import org.immutables.value.Value;

import java.util.List;

/**
 * Simplified representation of byte-code instruction. It is used in order to compare methods (for method rename detections)
 */
@Value.Immutable(prehash = true)
public interface InstructionInstance {

    int getOpcode();

    List<Object> getParameters();
}
