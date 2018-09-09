package ru.uskov.apidiff.classesmodel.objectnaming;

import java.util.List;

/**
 * Mapper provides policy of changing class names and method types in order to make them easier to read
 */
public interface ObjectNameMapper {

    /**
     * Converts the raw class name read from class file into user-friendly form
     * @param rawClassName class name to be converted
     * @return converted class name
     */
    String convertClassName(String rawClassName);

    /**
     * Parses argument types obtained from a method signature and converts into a list of human-readable types
     * @param rawTypes method description with argument types obtained from class file
     * @return list of human-readable types
     */
    List<String> convertArgumentTypes(String rawTypes);
}
