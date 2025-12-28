package shiftreader;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class CalcFlt extends Calc {
    public BigDecimal bdSum = null;

    public CalcFlt(String alias) {
        super(alias);
    }

    public void sort() {
        List<BigDecimal> decimalList = convertToBigDecimalList();
        list.clear();
        if (decimalList != null) {
            Collections.sort(decimalList);
            for (BigDecimal bd : decimalList) list.add(bd.toEngineeringString());
        }
    }

    private BigDecimal bdSum() {
        List<BigDecimal> decimalList = convertToBigDecimalList();
        if (decimalList == null) return null;
        BigDecimal sum = BigDecimal.ZERO;
        try {
            for (BigDecimal num : decimalList) sum = sum.add(num);
        } catch (Exception e) {
            System.err.println("Слишком большие дробные числа для арифметических операций");
            return bdSum = null;
        }
        return bdSum = sum;
    }
    @Override
    public String sum() {
        if (bdSum == null) bdSum();
        return (bdSum == null) ? null
                : bdSum.stripTrailingZeros().toEngineeringString();
    }
    @Override
    public String average() {
        List<BigDecimal> decimalList = convertToBigDecimalList();
        if (decimalList == null) return null;

        BigDecimal sum = bdSum();
        if (sum == null) return null;
        BigDecimal count = new BigDecimal(decimalList.size());

        return sum.divide(count, 10, RoundingMode.HALF_UP).stripTrailingZeros().toEngineeringString();
    }

    private List<BigDecimal> convertToBigDecimalList() {
        List<BigDecimal> decimalList = new ArrayList<>();
        for (String str : list) {
            try {
                decimalList.add(new BigDecimal(str));
            } catch (Exception e) {
                System.err.println("Ошибка обработки числа: " + str);
                return null;
            }
        }
        return decimalList;
    }
}
