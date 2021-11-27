package net.sssubtlety.automated_crafting.inventory.fixed_int_set;

import java.util.Arrays;

public class RootFixedIntSet extends FixedIntSet {
    protected final boolean[] valueMap;
    protected int size;

    protected RootFixedIntSet(boolean[] valueMap, int size) {
        this.valueMap = valueMap;
        this.size = size;
    }

    public RootFixedIntSet(int capacity) {
        this(new boolean[capacity], 0);
    }

    public FixedIntSet subSet(int begin, int end) {
//        if (end < begin) throw new IllegalArgumentException("end must not be less than begin.");
//        if (isOutOfBounds(begin)) throw new IllegalArgumentException("begin is out of bounds.");
//        if (end >= valueMap.length) throw new IllegalArgumentException("end is out of bounds.");

        return new SubFixedIntSet(this, begin, end);
    }

    protected boolean isOutOfBounds(int integer) {
        return integer < 0 || integer >= valueMap.length;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

//    @Override
//    public boolean contains(Object o) {
//        if (!(o instanceof Integer integer)) return false;
//        return contains(integer.intValue());
//    }

    @Override
    public boolean contains(int i) {
        return !isOutOfBounds(i) && valueMap[i];
    }

//    @NotNull
//    @Override
//    public Iterator iterator() {
//        return new Iterator(this);
//    }

//    @Override
//    public Object @NotNull [] toArray() {
//        final Object[] objArray = new Object[size];
//        for (int value = 0, iO = 0; iO  < size; value++) {
//            if (valueMap[value]) {
//                objArray[iO] = value;
//                iO++;
//            }
//        }
//        return objArray;
//    }

//    @Override
//    public <T> T @NotNull [] toArray(T @NotNull [] a) {
//        for (int value = 0, iA = 0, valueCount = 0; iA  < a.length && value < valueMap.length && valueCount < size; value++) {
//            if (valueMap[value]) {
//                a[iA] = (T) Integer.valueOf(value);
//                iA++;
//                valueCount++;
//            }
//        }
//        return a;
//    }

    @Override
    public boolean add(int integer) {
        if (isOutOfBounds(integer)) throw new IllegalArgumentException("Attempting to add integer that is out of bounds.");

        if (valueMap[integer]) return false;
        valueMap[integer] = true;
        size++;
        return true;
    }

//    @Override
//    public boolean remove(Object o) {
//        if (o instanceof Integer integer) return remove(integer.intValue());
//        return false;
//    }

    @Override
    public boolean remove(int i) {
        if (isOutOfBounds(i) || !valueMap[i]) return false;
        valueMap[i] = false;
        size--;
        return true;
    }

//    @Override
//    public boolean containsAll(@NotNull Collection<?> c) {
//        for (Object o : c)
//            if (!contains(o)) return false;
//
//        return true;
//    }
//
//    @Override
//    public boolean addAll(@NotNull Collection<? extends Integer> c) {
//        boolean changed = false;
//        for (Integer integer : c)
//            changed |= add(integer);
//
//        return changed;
//    }
//
//    @Override
//    public boolean retainAll(@NotNull Collection<?> c) {
//        boolean changed = false;
//        {
//            Iterator itr = iterator();
//            Integer integer;
//            while (itr.hasNext()) {
//                integer = itr.next();
//                if (!c.contains(integer)) {
//                    changed = true;
//                    itr.remove();
//                }
//            }
//        }
//        return changed;
//    }
//
//    @Override
//    public boolean removeAll(@NotNull Collection<?> c) {
//        boolean changed = false;
//        for (Object o : c)
//            changed |= remove(o);
//
//        return changed;
//    }

    @Override
    public void clear() {
        Arrays.fill(valueMap, false);
        size = 0;
    }
}
