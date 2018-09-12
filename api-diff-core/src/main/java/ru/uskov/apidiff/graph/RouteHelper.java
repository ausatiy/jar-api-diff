package ru.uskov.apidiff.graph;

import ru.uskov.apidiff.classesmodel.Api;
import ru.uskov.apidiff.classesmodel.ClassInstance;
import ru.uskov.apidiff.classesmodel.MethodInstance;
import ru.uskov.apidiff.metric.Weights;
import ru.uskov.apidiff.transform.ClassManipulation;
import ru.uskov.apidiff.transform.TransformOperation;
import ru.uskov.apidiff.transform.attributemanipulation.ChangeClassAttributesOperation;
import ru.uskov.apidiff.transform.attributemanipulation.ChangeMethodAttributesOperation;
import ru.uskov.apidiff.transform.classmanipulation.AddClassOperation;
import ru.uskov.apidiff.transform.classmanipulation.RemoveClassOperation;
import ru.uskov.apidiff.transform.classmanipulation.RenameClassOperation;
import ru.uskov.apidiff.transform.methodmanipulation.AddMethodOperation;
import ru.uskov.apidiff.transform.methodmanipulation.RemoveMethodOperation;
import ru.uskov.apidiff.transform.methodmanipulation.RenameMethodOperation;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Instance should be used to obtain possible transform operations for current {@Link GraphNode}
 * TODO: could be replaces with multiple implementations:
 *  - dummy - allways return all possible transforms - the longest execution time, but all solutions may be found
 *  - improved - do not return some transforms - better performance but some solutions could be lost
 */
public class RouteHelper {

    private final Weights weights;

    public enum RenamePolicy {
        /**
         * Rename of class A to B could be found only if A in not present in new api and B is not present in old api
         */
        REMOVED_TO_ADDED,
        /**
         * Only moves of classes between packages could be cound
         */
        PACKAGE_MOVE
    }

    private final RenamePolicy renamePolicy;

    public RouteHelper(RenamePolicy renamePolicy) {
        weights = new Weights();
        this.renamePolicy = renamePolicy;
    }

    private String withoutPackage(String className) {
        if (className.contains(".")) {
            return className.substring(className.lastIndexOf('.'));
        }
        return className;
    }

    // This is performance optimisation. We do not allow to add/remove previously added/removed classes
    private boolean classWasAddedRemovedRenamed(GraphNode node, String className) {
        for (TransformOperation transformOperation : node.getTransforms()) {
            if (transformOperation instanceof ClassManipulation) {
                if (((ClassManipulation)transformOperation).affectsClass(className)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns list of transforms to check.
     * TODO: at the moment the method is too long and contains some unclear logics required to improve performance. It could be more simple if A* instead of Dijkstra were used in {@link Graph}
     *
     * The following assumptions are made:
     * - class could be renamed from <b>not found in new api name</b> to <b>not found in old api</b>
     * - each class could be renamed/removed/added only once.
     * These suppositions lead to some lost solutions: A - removed, B -> renamed to A
     *
     * - methods could be affected only class names are the same
     * - change attrubites of classes/methods is preferable to add/remove
     *
     * - methods are processed class by class. This supposition improves performance, but does not let to detect moves of static methods between classes     *
     *
     * @param graphNode graph node representing current state of api
     * @param newApi api should be obtained
     * @return list of transforms to check
     */
    public Set<TransformOperation> getTransformOperations(GraphNode graphNode, Api newApi) {
        final Set<TransformOperation> result = new HashSet<>();

        final List<String> newClassNames = newApi.getClassNames().stream()
                .filter(x -> graphNode.getApi().get(x) == null)
                .sorted()
                .collect(Collectors.toList());
        final List<String> removedClassNames = graphNode.getApi().getClassNames().stream()
                .filter(x -> newApi.get(x) == null)
                .sorted()
                .collect(Collectors.toList());

        for (String newClass : newClassNames) {
            if (classWasAddedRemovedRenamed(graphNode, newClass)) {
                return Collections.emptySet();// The class with the same name was alredy affected. This node could not be a solution
            }
        }
        for (String newClass : removedClassNames) {
            if (classWasAddedRemovedRenamed(graphNode, newClass)) {
                return Collections.emptySet();// The class with the same name was alredy affected. This node could not be a solution
            }
        }


        // Add or rename class
        if (! newClassNames.isEmpty()) {
            final String newClassName = newClassNames.get(0);
            result.add(new AddClassOperation(graphNode.getApi(), newApi.get(newClassName), weights.getAddWeight()));
            for (String oldC : removedClassNames) {
                if (renameAllowed(oldC, newClassName)) {
                    result.add(new RenameClassOperation(graphNode.getApi(), oldC, newClassName, weights.getRenameClassWeight()));
                }
            }
        }

        // Remove or rename class
        if (! removedClassNames.isEmpty()) {
            final String oldC = removedClassNames.get(0);
            result.add(new RemoveClassOperation(graphNode.getApi(), oldC, weights.getRemoveWeight()));
        }


        // Other operations are acceptable if list of classes is the same
        if (! result.isEmpty()) {
            return result;
        }
        for (String className : newApi.getClassNames()) {
            final ClassInstance oldClass = graphNode.getApi().get(className);
            final ClassInstance newClass = newApi.get(className);
            if (!oldClass.attributesEqual(newClass)) {
                return Collections.singleton(new ChangeClassAttributesOperation(graphNode.getApi(), newClass, weights.getChangeClassAttributes()));// prevent wide search
            }
        }

        // Lists of classes are the same, class attributes are the same. Start dealing with methods

        for (String className : newApi.getClassNames()) {
            final ClassInstance oldClass = graphNode.getApi().get(className);
            final ClassInstance newClass = newApi.get(className);
            if (oldClass.attributesEqual(newClass) && (!oldClass.equals(newClass))) {
                // methods are different
                List<MethodInstance> newMethods = newClass.getMethods().stream()
                        .filter(x -> !oldClass.getMethods().contains(x))
                        .sorted()
                        .collect(Collectors.toList());
                List<MethodInstance> oldMethods = oldClass.getMethods().stream()
                        .filter(x -> !newClass.getMethods().contains(x))
                        .sorted()
                        .collect(Collectors.toList());

                if (!newMethods.isEmpty()) {
                    MethodInstance newMethod = newMethods.iterator().next();
                    for (MethodInstance oldMethod : oldMethods) {
                        if (oldMethod.getName().equals(newMethod.getName()) && oldMethod.getParameters().equals(newMethod.getParameters())) {
                            return Collections.singleton(new ChangeMethodAttributesOperation(graphNode.getApi(), oldClass, oldMethod, newMethod, weights.getChangeMethodAttributes()));// it is expected that it is better to change attributes than to add/remove/rename method
                        }
                        if ((!oldMethod.getName().equals(newMethod.getName())) &&
                                oldMethod.getParameters().equals(newMethod.getParameters()) &&
                                oldMethod.getInstructions().equals(newMethod.getInstructions())) {
                            return Collections.singleton(new RenameMethodOperation(graphNode.getApi(), oldClass, oldMethod, newMethod, weights.getRenameMethodWeight()));
                        }
                    }
                    return Collections.singleton(new AddMethodOperation(graphNode.getApi(), oldClass, newMethod, weights.getAddMethodWeight()));
                }
                if (!oldMethods.isEmpty()) {
                    return Collections.singleton(new RemoveMethodOperation(graphNode.getApi(), oldClass, oldMethods.iterator().next(), weights.getRemoveMethodWeight()));
                }
            }
        }
        return Collections.emptySet();
    }

    private boolean renameAllowed(String oldC, String newClassName) {
        return (renamePolicy == RenamePolicy.REMOVED_TO_ADDED) || (withoutPackage(oldC).equals(newClassName));
    }
}
