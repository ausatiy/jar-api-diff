package ru.uskov.apidiff.classesmodel;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InnerClassNode;
import ru.uskov.apidiff.classesmodel.objectnaming.ObjectNameMapper;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;

public class ClassInstanceFactory {

    private final ObjectNameMapper objectNameMapper;

    public ClassInstanceFactory(ObjectNameMapper mapper) {
        objectNameMapper = mapper;
    }

    public ClassInstance getClass(ZipInputStream is) throws IOException {
        final ClassReader reader = new ClassReader(is);
        final ClassNode node = new ClassNode();
        reader.accept(node, 0);

        int access = node.access;
        for (InnerClassNode innerClassNode : node.innerClasses) {
            if (node.name.equals(innerClassNode.name)) {
                access = innerClassNode.access;
            }
        }

        final MethodInstanceFactory methodInstanceFactory = new MethodInstanceFactory(objectNameMapper);

        return ImmutableClassInstance.builder()
                .name(objectNameMapper.convertClassName(node.name))
                .parent(objectNameMapper.convertClassName(node.superName))
                .visibility(Visibility.of(access))
                .isFinal((access & Opcodes.ACC_FINAL) != 0)
                .isAbstract((access & Opcodes.ACC_ABSTRACT) != 0)
                .isInterface((access & Opcodes.ACC_INTERFACE) != 0)
                .addAllInterfaces(node.interfaces.stream().map(objectNameMapper::convertClassName).collect(Collectors.toSet()))
                .addAllMethods(node.methods.stream().map(methodInstanceFactory::getMethodInstance).collect(Collectors.toSet()))
                .build();
    }
}
