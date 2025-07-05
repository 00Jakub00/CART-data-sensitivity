package datove.struktury;

import java.util.*;

public class ManagedDuplicatesCollection<T> implements Iterable<T> {
    private final boolean checkDuplicates;
    private final ArrayList<T> list;
    private final String typ;

    public ManagedDuplicatesCollection(boolean checkDuplicates, String typ) {
        this.checkDuplicates = checkDuplicates;
        this.list = new ArrayList<>();
        this.typ = typ;
    }

    public String getTyp() {
        return this.typ;
    }

    public void add(T element) {
        if (this.checkDuplicates) {
            if (!this.list.contains(element)) {
                this.list.add(element);
            }
        } else {
            this.list.add(element);
        }
    }


    public T get(int index) {
        return this.list.get(index);
    }

    public boolean isDuplicatesChecked() {
        return this.checkDuplicates;
    }

    @Override
    public Iterator<T> iterator() {
        return this.list.iterator();
    }

    public int size() {
        return this.list.size();
    }
}
