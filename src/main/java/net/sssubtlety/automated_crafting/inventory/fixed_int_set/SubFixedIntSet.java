package net.sssubtlety.automated_crafting.inventory.fixed_int_set;

/*
    Live view of a subset of a RootFixedIntSet
    This should be treated similarly to an Iterator: do not save references to it
    Behavior is undefined if a SubFixedIntSet's root is operated on directly
    (Changes to the subset update the size of the root, but not vice versa)
*/
class SubFixedIntSet extends FixedIntSet {
    private final int begin;
    private final int end;
    private final RootFixedIntSet root;
    private int subSize;

    public SubFixedIntSet(RootFixedIntSet root, int begin, int end) {
        this.root = root;
        this.begin = begin;
        this.end = end;
        {
            final int subLen = end - begin;
            final int lenRemaining = root.valueMap.length - subLen;
            if (lenRemaining < subLen) {
                this.subSize = root.size;
                for (int i = 0; i < begin; i++)
                    if (root.valueMap[i]) this.subSize--;

                for (int i = end; i < root.valueMap.length; i++)
                    if (root.valueMap[i]) this.subSize--;

            } else {
                this.subSize = 0;
                for (int i = begin; i < end; i++)
                    if (root.valueMap[i]) this.subSize++;

            }
        }
    }

    @Override
    public int size() {
        return subSize;
    }

    @Override
    public boolean isEmpty() {
        return subSize == 0;
    }

    @Override
    public boolean contains(int i) {
        int iOffset = getRootI(i);
        return !isOutOfBounds(iOffset) && root.contains(iOffset);
    }

    private int getRootI(int i) {
        return i + begin;
    }

    @Override
    public boolean add(int i) {
        boolean added = root.add(getRootI(i));
        if (added) subSize++;
        return added;
    }

    @Override
    public boolean remove(int i) {
        boolean removed = root.remove(getRootI(i));
        if (removed) subSize--;
        return removed;
    }

    @Override
    public void clear() {
        for (int i = begin; i < end; i++) {
            root.remove(i);
        }
        subSize = 0;
    }

    public boolean isOutOfBounds(int integer) {
        return integer >= begin && integer < end;
    }
}
