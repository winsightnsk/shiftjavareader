package shiftreader;

import java.util.Comparator;
import java.util.List;

public final class CalcStr extends Calc {
    public CalcStr(String alias) {
        super(alias);
    }
//    public static void sort(List<String> inlist) {
//        List<String> tmp = inlist.stream()
//                .sorted(Comparator.comparingInt(String::length))
//                .toList();
//        inlist.clear();
//        inlist.addAll(tmp);
//    }
}
