package ru.uskov.apidiff.graph;

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

public class RouteHelper {

    private final Weights weights;

    public enum RenamePolicy {
        REMOVED_TO_ADDED, PACKAGE_MOVE
    }

    private final RenamePolicy renamePolicy;

    public RouteHelper() {
        weights = new Weights();
        renamePolicy = RenamePolicy.REMOVED_TO_ADDED;
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

    public Set<TransformOperation> getTransformOperations(GraphNode graphNode, Set<ClassInstance> newApi0) {
        // We use sorted collection in order to compare methods in the same order
        final Set<ClassInstance> oldApi = new HashSet<>(graphNode.getApi());
        oldApi.removeAll(newApi0);

        Set<ClassInstance> newApi = new TreeSet<>(new Comparator<ClassInstance>() {
            @Override
            public int compare(ClassInstance o1, ClassInstance o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        newApi.addAll(newApi0);
        newApi.removeAll(graphNode.getApi());

        final Set<TransformOperation> result = new HashSet<>();

        final Map<String, ClassInstance> sourceClassesMap = oldApi.stream()
                .collect(Collectors.toMap(x->x.getName(), x->x));
        final Map<String, ClassInstance> destinationClassesMap = newApi.stream()
                .collect(Collectors.toMap(x->x.getName(), x->x));

        final Set<String> classesToAdd = destinationClassesMap.keySet().stream()
                .filter(x -> !sourceClassesMap.keySet().contains(x))
                .filter(x->! classWasAddedRemovedRenamed(graphNode, x))
                .collect(Collectors.toSet());
        final Set<String> classesToRemove = sourceClassesMap.keySet().stream()
                .filter(x -> !destinationClassesMap.keySet().contains(x))
                .filter(x->! classWasAddedRemovedRenamed(graphNode, x))
                .collect(Collectors.toSet());

        // Add new classes
        result.addAll(classesToAdd.stream()
                .map(x -> new AddClassOperation(graphNode.getApi(), destinationClassesMap.get(x), weights.getAddWeight()))
                .collect(Collectors.toSet()));

        // remove classes
        result.addAll(classesToRemove.stream()
                .map(x -> new RemoveClassOperation(graphNode.getApi(), x, weights.getRemoveWeight()))
                .collect(Collectors.toSet()));

        //rename classes
        for (String oldC : classesToRemove) {
            for (String newC : classesToAdd) {
                // If rename is limited with package changes
                if (renamePolicy == RenamePolicy.PACKAGE_MOVE) {
                    String oldClassName = oldC.substring(oldC.lastIndexOf('.'));
                    String newClassName = newC.substring(newC.lastIndexOf('.'));
                    if (!oldClassName.equals(newClassName)) {
                        continue;
                    }
                }
                result.add(new RenameClassOperation(graphNode.getApi(), oldC, newC, weights.getRenameClassWeight()));

            }
        }

        // change class attributes (visibility, abstract/final/parent class)
        result.addAll(newApi.stream()
                .filter(x -> sourceClassesMap.containsKey(x.getName()) && (! x.attributesEqual(sourceClassesMap.get(x.getName()))))
                .map(x -> new ChangeClassAttributesOperation(graphNode.getApi(), x, weights.getChangeClassAttributes()))
                .collect(Collectors.toSet()));

        // we would deal with methods only when set of classes is the same
        if (sourceClassesMap.keySet().equals(destinationClassesMap.keySet()) && (!newApi.isEmpty())) {
            // It is performance optimisation: first we should finish with one class. You should not add transforms for all methods of all classes at once
            ClassInstance newClass = newApi.iterator().next();
            ClassInstance oldClass = sourceClassesMap.get(newClass.getName());

            assert ! newClass.equals(oldClass);
            // there are much less methods in class than classes in jar, thus we less care about performance
            Set<MethodInstance> newMethods = newClass.getMethods().stream()
                    .filter(x ->! oldClass.getMethods().contains(x))
                    .collect(Collectors.toSet());
            Set<MethodInstance> oldMethods = oldClass.getMethods().stream()
                    .filter(x ->! newClass.getMethods().contains(x))
                    .collect(Collectors.toSet());

            for (MethodInstance oldMethod : oldMethods) {
                result.add(new RemoveMethodOperation(graphNode.getApi(), oldClass, oldMethod, weights.getRemoveMethodWeight()));
            }
            for (MethodInstance newMethod : newMethods) {
                result.add(new AddMethodOperation(graphNode.getApi(), oldClass, newMethod, weights.getAddMethodWeight()));
                for (MethodInstance oldMethod : oldMethods) {
                    if (oldMethod.getName().equals(newMethod.getName()) && oldMethod.getParameters().equals(newMethod.getParameters())) {
                        result.add(new ChangeMethodAttributesOperation(graphNode.getApi(), oldClass, oldMethod, newMethod, weights.getChangeMethodAttributes()));
                    }
                    if ((! oldMethod.getName().equals(newMethod.getName())) &&
                            oldMethod.getParameters().equals(newMethod.getParameters()) &&
                            oldMethod.getInstructions().equals(newMethod.getInstructions())) {
                        result.add(new RenameMethodOperation(graphNode.getApi(), oldClass, oldMethod, newMethod, weights.getRenameMethodWeight()));
                    }
                }
            }
        }
        return result;
    }
}
