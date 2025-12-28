package shiftreader;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Calc {
    protected final String alias;

    protected final List<String> list = new ArrayList<>();

    public Calc(String alias) {
        this.alias = alias;
    }

    public void sort() {
        List<String> tmp = list.stream()
                .sorted(Comparator.comparingInt(String::length))
                .toList();
        list.clear();
        list.addAll(tmp);
    }
    public String sum() {
        return null;
    }
    public String average() {
        return null;
    }
    public void add(String line) {
        list.add(line);
    }

    @Override
    public String toString() {
        return alias;
    }
}
