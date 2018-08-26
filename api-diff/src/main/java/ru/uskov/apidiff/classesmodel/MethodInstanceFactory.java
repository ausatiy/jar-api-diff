package ru.uskov.apidiff.classesmodel;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class MethodInstanceFactory {

    public MethodInstance getMethodInstance(MethodNode node) {
        //TODO annotations are skipped
        //node.attrs
        //node.desc
        //node.instructions
        //node.localVariables

        return ImmutableMethodInstance.builder()
                .name(node.name)
                .visibility(Visibility.of(node.access))
                .isFinal((node.access & Opcodes.ACC_FINAL) != 0)
                .isAbstract((node.access & Opcodes.ACC_ABSTRACT) != 0)
                .build();
    }
}
