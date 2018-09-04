TODOs:
1. Parse
2. Output
3. Test data
4. Difference
5. Solve issue with imutables

Assumptions:
1. No fields?
2. No annotations


Questions:
1. Rename class -> All mentions in methods are automatically updated



Possible optimisation:

    /**
     * This method is used for performance optimisation only. Nothing will break if this method will always return false.
     * 
     * In general transform operations produce non-commutative group in the set of classes. Thus the graph nodes representing transformations
     * o1(o2(x)) and o2(o1(x)) could be unequal.
     * @param operation
     * @return true if current operation and operation are commutative (i.e. if  this.apply(operation.apply) == operation.apply(this.apply))
     */
    boolean isCommunicativeTo(TransformOperation operation);

    @Override
    public boolean isCommunicativeTo(TransformOperation operation) {
        if (! (operation instanceof RenameClassOperation)) {
            return false;
        }
        RenameClassOperation operation1 = (RenameClassOperation) operation;
        if (from.equals(operation1.from) || to.equals(operation1.to) ||
                to.equals(operation1.from) || from.equals(operation1.to)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public int compareTo(@NotNull TransformOperation o) {
        if (! (o instanceof RenameClassOperation)) {
            throw new IllegalStateException("TODO");
        }
        RenameClassOperation o1 = (RenameClassOperation) o;
        if (from.compareTo(o1.from) != 0) {
            return from.compareTo(o1.from);
        }
        return to.compareTo(o1.to);
    }