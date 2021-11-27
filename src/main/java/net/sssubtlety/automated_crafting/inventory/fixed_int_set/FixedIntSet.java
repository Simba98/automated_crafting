package net.sssubtlety.automated_crafting.inventory.fixed_int_set;

/*
    ArrayInventory set of integers that can only hold integers between (inclusive) 0 and size (exclusive)
*/
public abstract class FixedIntSet {
    public static FixedIntSet ofSize(int size) {
        return new RootFixedIntSet(size);
    }

    public abstract int size();

    public abstract boolean isEmpty();

    public abstract boolean contains(int i);

    public abstract boolean add(int integer);

    public abstract boolean remove(int i);

    public abstract void clear();
//    public static class Iterator implements java.util.Iterator<Integer> {
//        protected final RootFixedIntSet set;
//        protected int pos;
//        protected int next;
//
//        protected Iterator(RootFixedIntSet set) {
//            this.set = set;
//            this.pos = 0;
//            this.next = 0;
//            this.updateNext();
//        }
//
//        protected void updateNext() {
//            for(; next < set.valueMap.length; next++)
//                if (set.valueMap[next]) break;
//        }
//
//        @Override
//        public boolean hasNext() {
//            return next < set.valueMap.length;
//        }
//
//        @Override
//        public Integer next() {
//            pos = next;
//            updateNext();
//            return pos;
//        }
//
//        @Override
//        public void remove() {
//            set.remove(pos);
//        }
//    }
}
