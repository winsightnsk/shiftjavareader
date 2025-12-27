package shiftreader;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CalcInt {
    private static boolean less_left(String a, String b) {
        if (a.charAt(0) == '-' && b.charAt(0) != '-') return true;
        if (a.charAt(0) != '-' && b.charAt(0) == '-') return false;
        if (a.charAt(0) == '-' && b.charAt(0) == '-') {
            String c = a;
            a = b;
            b = c;
        }
        Pattern pattern = Pattern.compile("^[+-]?0*(\\d+)$");
        Matcher matcher = pattern.matcher(a);
        a = (matcher.matches()) ? matcher.group(1) : "0";
        matcher = pattern.matcher(b);
        b = (matcher.matches()) ? matcher.group(1) : "0";
        if (a.length() < b.length()) return true;
        if (a.length() > b.length()) return false;
        return a.compareTo(b) < 0;
    }

    public static void sort(List<String> list) {
        for (int i=0; i<list.size()-1; i++) {
            int mini = i;
            for (int j=i+1; j<list.size(); j++) if (less_left(list.get(j), list.get(mini))) mini = j;
            if (mini>i) {
                String x = list.get(i);
                list.set(i, list.get(mini));
                list.set(mini, x);
            }
        }
    }

    public static String sum(List<String> list) {
        try {
            long result = 0;
            for (String s : list) result += Long.parseLong(s);
            return String.valueOf(result);
        } catch (Exception e) {
            try {
                double result = 0;
                for (String s : list) result += Double.parseDouble(s);
                return String.valueOf(result);
            } catch (Exception ee) {
                System.err.println(ee.toString());
                return null;
            }
        }
    }

    public static String average(List<String> list) {
        String sum_str = sum(list);
        if (sum_str == null) return null;
        try {
            long summa = Long.parseLong(sum_str);
            return String.valueOf(summa / list.size());
        } catch (Exception e) {
            try {
                double summa = Double.parseDouble(sum_str);
                return String.valueOf(summa / (double) list.size());
            } catch (Exception ee) {
                System.err.println(ee.toString());
                return null;
            }
        }
    }

}
