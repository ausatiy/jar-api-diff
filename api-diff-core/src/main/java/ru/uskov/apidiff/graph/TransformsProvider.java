package ru.uskov.apidiff.graph;

import ru.uskov.apidiff.classesmodel.ClassInstance;
import ru.uskov.apidiff.metric.Weights;
import ru.uskov.apidiff.transform.TransformOperation;
import ru.uskov.apidiff.transform.attributemanipulation.ChangeClassAttributesOperation;
import ru.uskov.apidiff.transform.classmanipulation.AddClassOperation;
import ru.uskov.apidiff.transform.classmanipulation.RemoveClassOperation;
import ru.uskov.apidiff.transform.classmanipulation.RenameClassOperation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface TransformsProvider {

    Set<TransformOperation> getOperations(Set<ClassInstance> oldApi, Set<ClassInstance> newApi, Weights weights);

    enum ChangeType {
        ADDED, REMOVED, ATTRIBUTES_CHANGED, METHODS_CHANGED
    }

    default Map<ClassInstance, ChangeType> getChangeTypes(Set<ClassInstance> oldApi, Set<ClassInstance> newApi) {
        oldApi = new HashSet<>(oldApi);
        newApi = new HashSet<>(newApi);
        oldApi.removeAll(newApi);
        newApi.removeAll(oldApi);

        Map<String, ClassInstance> sourceClassesMap = oldApi.stream().collect(Collectors.toMap(x->x.getName(), x->x));
        Map<String, ClassInstance> destinationClassesMap = newApi.stream().collect(Collectors.toMap(x->x.getName(), x->x));

        Map<ClassInstance, ChangeType> removed = oldApi.stream()
                .filter(x -> !destinationClassesMap.containsKey(x.getName()))
                .collect(Collectors.toMap(Function.identity(), x->ChangeType.REMOVED));
        Map<ClassInstance, ChangeType> added = newApi.stream()
                .filter(x -> !sourceClassesMap.containsKey(x.getName()))
                .collect(Collectors.toMap(Function.identity(), x->ChangeType.ADDED));
        Map<ClassInstance, ChangeType> changed = newApi.stream()
                .filter(x -> sourceClassesMap.containsKey(x.getName()))
                .collect(Collectors.toMap(Function.identity(), x-> x.attributesEqual(sourceClassesMap.get(x.getName())) ? ChangeType.METHODS_CHANGED : ChangeType.ATTRIBUTES_CHANGED));

        Map<ClassInstance, ChangeType> result = new HashMap<>();
        result.putAll(removed);
        result.putAll(added);
        result.putAll(changed);
        return result;
    }

    enum TRANSFORMS implements TransformsProvider {
        ADD_CLASS {
            @Override
            public Set<TransformOperation> getOperations(Set<ClassInstance> oldApi, Set<ClassInstance> newApi, Weights weights) {
                return getChangeTypes(oldApi, newApi).entrySet().stream()
                        .filter(x -> x.getValue() == ChangeType.ADDED)
                        .map(x -> new AddClassOperation(oldApi, x.getKey(), weights.getAddWeight()))
                        .collect(Collectors.toSet());
            }
        },
        REMOVE_CLASS {
            @Override
            public Set<TransformOperation> getOperations(Set<ClassInstance> oldApi, Set<ClassInstance> newApi, Weights weights) {
                //TODO allow to delete any file in order to get solution in any case
                return getChangeTypes(oldApi, newApi).entrySet().stream()
                        .filter(x -> x.getValue() == ChangeType.REMOVED)
                        .map(x -> new RemoveClassOperation(oldApi, x.getKey().getName(), weights.getRemoveWeight()))
                        .collect(Collectors.toSet());
            }
        },
        RENAME_CLASS {
            @Override
            public Set<TransformOperation> getOperations(Set<ClassInstance> oldApi, Set<ClassInstance> newApi, Weights weights) {
                Map<ClassInstance, ChangeType> changeTypeMap = getChangeTypes(oldApi, newApi);
                final Set<ClassInstance> removed = changeTypeMap.entrySet().stream()
                        .filter(x->x.getValue() == ChangeType.REMOVED)
                        .map(x->x.getKey())
                        .collect(Collectors.toSet());
                final Set<ClassInstance> added = changeTypeMap.entrySet().stream()
                        .filter(x->x.getValue() == ChangeType.ADDED)
                        .map(x->x.getKey())
                        .collect(Collectors.toSet());

                Set<TransformOperation> result = new HashSet<>();
                for (ClassInstance oldC : removed) {
                    for (ClassInstance newC : added) {
                        if (!oldC.equals(newC)) {
                            result.add(new RenameClassOperation(oldApi, oldC.getName(), newC.getName(), weights.getRenameClassWeight()));
                        }
                    }
                }
                return result;
            }
        },
        CHANGE_CLASS_ATTRIBUTES {
            @Override
            public Set<TransformOperation> getOperations(Set<ClassInstance> oldApi, Set<ClassInstance> newApi, Weights weights) {
                return getChangeTypes(oldApi, newApi).entrySet().stream()
                        .filter(x -> x.getValue() == ChangeType.ATTRIBUTES_CHANGED)
                        .map(x -> new ChangeClassAttributesOperation(oldApi, x.getKey(), weights.getChangeClassAttributes()))
                        .collect(Collectors.toSet());
            }
        }
        //TODO move class between packages
        //TOOD removed to new
    }
}
