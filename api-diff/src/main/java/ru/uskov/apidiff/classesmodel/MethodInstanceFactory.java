package ru.uskov.apidiff.classesmodel;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class MethodInstanceFactory {

    private String convertType(String jvmType) {
        if (jvmType.equals("V")) {
            return "void";
        }
        if (jvmType.startsWith("L")) {
            return jvmType;//TODO
        }

        return jvmType;
    }

    public MethodInstance getMethodInstance(MethodNode node) {
        //TODO annotations are skipped
        //node.attrs
        //node.desc
        //node.instructions
        //node.localVariables
        final String[] signatureParts = node.desc.substring(1).split("\\)");
        final String returnType = convertType(signatureParts[1]);
        //TODO add exceptions, arguments
        return ImmutableMethodInstance.builder()
                .name(node.name)
                .visibility(Visibility.of(node.access))
                .isFinal((node.access & Opcodes.ACC_FINAL) != 0)
                .isAbstract((node.access & Opcodes.ACC_ABSTRACT) != 0)
                .isStatic((node.access & Opcodes.ACC_STATIC) != 0)
                .returnType(returnType)
                .build();
    }
}
