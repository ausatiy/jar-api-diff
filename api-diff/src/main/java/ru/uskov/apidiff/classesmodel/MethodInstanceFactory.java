package ru.uskov.apidiff.classesmodel;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;
import ru.uskov.apidiff.objectnaming.ObjectNameMapper;

import java.util.List;
import java.util.stream.Collectors;

public class MethodInstanceFactory {

    private final ObjectNameMapper objectNameMapper;

    public MethodInstanceFactory(ObjectNameMapper objectNameMapper) {
        this.objectNameMapper = objectNameMapper;
    }

    public MethodInstance getMethodInstance(MethodNode node) {
        //TODO annotations are skipped
        //node.attrs
        //node.desc
        //node.instructions
        //node.localVariables
        final String[] signatureParts = node.desc.substring(1).split("\\)");
        final List<String> returnType = objectNameMapper.convertArgumentTypes(signatureParts[1]);
        if (returnType.size() != 1) {
            throw new RuntimeException("Could not parse method signature " + node.desc);
        }

        //TODO add exceptions, arguments
        return ImmutableMethodInstance.builder()
                .name(node.name)
                .visibility(Visibility.of(node.access))
                .isFinal((node.access & Opcodes.ACC_FINAL) != 0)
                .isAbstract((node.access & Opcodes.ACC_ABSTRACT) != 0)
                .isStatic((node.access & Opcodes.ACC_STATIC) != 0)
                .returnType(returnType.get(0))
                .addAllExceptions(node.exceptions.stream().map(objectNameMapper::convertClassName).collect(Collectors.toSet()))
                .addAllParameters(objectNameMapper.convertArgumentTypes(signatureParts[0]))
                .build();
    }
}
