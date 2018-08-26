package ru.uskov.apidiff.classesmodel;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InnerClassNode;

import java.io.IOException;
import java.util.zip.ZipInputStream;

public class ClassInstanceFactory {

    public ClassInstance getClass(ZipInputStream is) throws IOException {
        final ClassReader reader = new ClassReader(is);
        final ClassNode node = new ClassNode();
        reader.accept(node, 0);

        final String[] interfaces = new String[node.interfaces.size()];
        node.interfaces.toArray(interfaces);

        final MethodInstance[] methods = new MethodInstance[node.methods.size()];
        final MethodInstanceFactory methodInstanceFactory = new MethodInstanceFactory();
        for (int i = 0; i < node.methods.size(); i++) {
            methods[i] = methodInstanceFactory.getMethodInstance(node.methods.get(i));
        }

        int access = node.access;
        for (InnerClassNode innerClassNode : node.innerClasses) {
            if (node.name.equals(innerClassNode.name)) {
                access = innerClassNode.access;
            }
        }

        return ImmutableClassInstance.builder()
                .name(node.name)
                .parent(node.superName)
                .visibility(Visibility.of(access))
                .isFinal((access & Opcodes.ACC_FINAL) != 0)
                .isAbstract((access & Opcodes.ACC_ABSTRACT) != 0)
                .isInterface((access & Opcodes.ACC_INTERFACE) != 0)
                .addInterfaces(interfaces)
                .addMethods(methods)
                .build();
    }
}
