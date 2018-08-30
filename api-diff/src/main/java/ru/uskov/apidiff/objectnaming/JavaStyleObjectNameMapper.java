package ru.uskov.apidiff.objectnaming;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JavaStyleObjectNameMapper implements ObjectNameMapper {
    @Override
    public String convertClassName(String rawClassName) {
        rawClassName = rawClassName.replaceAll("^java/lang/", "");
        return rawClassName.replace('/', '.');
    }

    @Override
    public List<String> convertArgumentTypes(String rawType) {
        List<String> result = new ArrayList<>();
        final String initialRawType = rawType;

        if ("V".equals(rawType)) {
            return Collections.singletonList("void");
        }

        String suffix = "";
        while (rawType.length() > 0) {
            char nextChar = rawType.charAt(0);
            rawType = rawType.substring(1);
            switch (nextChar) {
                case '[': suffix+= "[]"; break;
                case 'B': result.add("byte" + suffix); break;
                case 'C': result.add("char" + suffix); break;
                case 'D': result.add("double" + suffix); break;
                case 'F': result.add("float" + suffix); break;
                case 'I': result.add("int" + suffix); break;
                case 'J': result.add("long" + suffix); break;
                case 'S': result.add("short" + suffix); break;
                case 'Z': result.add("boolean" + suffix); break;
                case 'L':
                    final int semicolIndex = rawType.indexOf(';');
                    result.add(convertClassName(rawType.substring(0, semicolIndex)) + suffix);
                    rawType = rawType.substring(semicolIndex + 1);
                    break;
                default: throw new IllegalArgumentException(String.format("Could not parse argument types [%s].", initialRawType));
            }
            if (nextChar != '[') {
                suffix = "";
            }
        }
        return result;
    }
}
