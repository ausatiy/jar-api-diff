package ru.uskov.apidiff.classesmodel;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InnerClassNode;
import ru.uskov.apidiff.classesmodel.objectnaming.ObjectNameMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 * The class that reads {@link ClassInstance} objects from jar file
 */
public class ClassParser {
    private final ObjectNameMapper objectNameMapper;

    private final String CLASS_FILE_EXTENSION = ".class";

    private boolean skipFile(String fileName) {
        return ! fileName.endsWith(CLASS_FILE_EXTENSION);
    }

    /**
     * Constructs the parser object with provided {@Link ObjectNameMapper}
     * @param objectNameMapper mapper will be used to convert byte-code style names onto human-readable form
     */
    public ClassParser(ObjectNameMapper objectNameMapper) {
        this.objectNameMapper = objectNameMapper;
    }

    /**
     * Read all class files from jar-file
     * @param jarFile file to read classes from
     * @return set of parsed class instances
     * @throws IOException if some error occured
     */
    public Set<ClassInstance> readClasses(File jarFile) throws IOException {
        Set<ClassInstance> result = new HashSet<>();
        try (InputStream fis = new FileInputStream(jarFile)) {
            try (ZipInputStream zis = new ZipInputStream(fis)) {
                while (true) {
                    final ZipEntry nextEntry = zis.getNextEntry();
                    if (nextEntry == null) {
                        break;
                    }
                    if (skipFile(nextEntry.getName())) {
                        continue;
                    }
                    final ClassInstance instance = getClass(zis);
                    result.add(instance);
                    zis.closeEntry();
                }
            }
        }
        return result;
    }

    /**
     * Reads single class instance from provided InputStream.
     * @param is input stream to read data from
     * @return constructed representation of java class
     * @throws IOException if data could not be read
     */
    public ClassInstance getClass(InputStream is) throws IOException {
        final ClassReader reader = new ClassReader(is);
        final ClassNode node = new ClassNode();
        reader.accept(node, 0);

        // obtain correct access properties of inner classes
        int access = node.access;
        for (InnerClassNode innerClassNode : node.innerClasses) {
            if (node.name.equals(innerClassNode.name)) {
                access = innerClassNode.access;
            }
        }
        final InstructionInstanceFactory instructionInstanceFactory = new InstructionInstanceFactory(objectNameMapper);
        final MethodInstanceFactory methodInstanceFactory = new MethodInstanceFactory(objectNameMapper,instructionInstanceFactory);

        return ImmutableClassInstance.builder()
                .name(objectNameMapper.convertClassName(node.name))
                .parent(objectNameMapper.convertClassName(node.superName))
                .visibility(Visibility.of(access))
                .isFinal((access & Opcodes.ACC_FINAL) != 0)
                .isAbstract((access & Opcodes.ACC_ABSTRACT) != 0)
                .isInterface((access & Opcodes.ACC_INTERFACE) != 0)
                .addAllInterfaces(
                        node.interfaces.stream()
                                .map(objectNameMapper::convertClassName)
                                .collect(Collectors.toSet()))
                .addAllMethods(
                        node.methods.stream()
                                .map(methodInstanceFactory::getMethodInstance)
                                .collect(Collectors.toSet()))
                .build();
    }

}
