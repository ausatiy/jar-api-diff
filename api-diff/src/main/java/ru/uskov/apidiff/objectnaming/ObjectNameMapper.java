package ru.uskov.apidiff.objectnaming;

import java.util.List;

public interface ObjectNameMapper {

    String convertClassName(String rawClassName);

    List<String> convertArgumentTypes(String rawTypes);
}
