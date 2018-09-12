package ru.uskov.apidiff.transform;

import ru.uskov.apidiff.classesmodel.Api;
import ru.uskov.apidiff.classesmodel.ClassInstance;
import ru.uskov.apidiff.classesmodel.MethodInstance;

import java.util.Set;

/**
 * All transforms (operations leading to changes of API) should implement this interface
 */
public interface TransformOperation {

    /**
     * @return weight of current transform operation
     */
    int getWeight();

    /**
     * @return newApi obtained as result of current transform
     */
    Api getNewApi();

    /**
     * TODO: this method could be usefull in order to create more pretyy difference output (in order to keep links between source and destination objects)
     */
    ClassInstance getSourceClass(ClassInstance classInstance);

    /**
     * TODO: this method could be usefull in order to create more pretyy difference output (in order to keep links between source and destination objects)
     */
    MethodInstance getSourceMethod(MethodInstance methodInstance);

}
