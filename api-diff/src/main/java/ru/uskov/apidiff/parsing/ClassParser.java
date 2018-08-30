package ru.uskov.apidiff.parsing;

import ru.uskov.apidiff.classesmodel.ClassInstance;
import ru.uskov.apidiff.classesmodel.ClassInstanceFactory;
import ru.uskov.apidiff.objectnaming.ObjectNameMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ClassParser {
    private final ClassInstanceFactory classInstanceFactory;

    private final String CLASS_FILE_EXTENSION = ".class";

    private boolean skipFile(String fileName) {
        return ! fileName.endsWith(CLASS_FILE_EXTENSION);
    }

    public ClassParser(ObjectNameMapper objectNameMapper) {
        classInstanceFactory = new ClassInstanceFactory(objectNameMapper);
    }

    //TODO javadoc
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
                    final ClassInstance instance = classInstanceFactory.getClass(zis);
                    result.add(instance);
                    zis.closeEntry();
                }
            }
        }
        return result;
    }
}
