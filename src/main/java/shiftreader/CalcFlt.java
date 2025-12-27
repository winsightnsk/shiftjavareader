//package shiftreader;
//
//public class CalcFlt {
//}
//
//5. Метод с Stream API (Java 8+):
//java
//public static Flt calculateAverageStream(List<Flt> numbers) {
//    if (numbers.isEmpty()) {
//        return new Flt(0.0, 0);
//    }
//
//    // Вычисляем среднее через Stream API
//    double average = numbers.stream()
//            .mapToDouble(Flt::toDouble)
//            .average()
//            .orElse(0.0);
//
//    // Нормализуем результат
//    return normalizeDouble(average);
//}
//
//private static Flt normalizeDouble(double value) {
//    if (value == 0.0) {
//        return new Flt(0.0, 0);
//    }
//
//    // Находим экспоненту через степень 10
//    long exp = 0;
//    double num = Math.abs(value);
//
//    if (num >= 1.0) {
//        while (num >= 10.0) {
//            num /= 10.0;
//            exp++;
//        }
//    } else {
//        while (num < 1.0) {
//            num *= 10.0;
//            exp--;
//        }
//    }
//
//    // Восстанавливаем знак
//    num = value < 0 ? -num : num;
//
//    return new Flt(num, exp);
//}
//6. Метод с кастомной арифметикой Flt (полностью в Flt-формате):
//java
//public static Flt calculateAverageFltOnly(List<Flt> numbers) {
//    if (numbers.isEmpty()) {
//        return new Flt(0.0, 0);
//    }
//
//    // Суммируем все числа
//    Flt sum = numbers.get(0);
//    for (int i = 1; i < numbers.size(); i++) {
//        sum = addFlt(sum, numbers.get(i));
//    }
//
//    // Делим на количество
//    Flt count = new Flt(numbers.size(), 0);
//    return divideFlt(sum, count);
//}
//
//private static Flt addFlt(Flt a, Flt b) {
//    // Приводим к общей экспоненте
//    long commonExp = Math.max(a.exp(), b.exp());
//
//    // Масштабируем числа
//    double aScaled = scaleNum(a, commonExp);
//    double bScaled = scaleNum(b, commonExp);
//
//    // Складываем
//    double sumScaled = aScaled + bScaled;
//
//    // Нормализуем результат
//    return normalize(new Flt(sumScaled, commonExp));
//}
//
//private static Flt divideFlt(Flt a, Flt b) {
//    // Деление: (num1 × 10^exp1) / (num2 × 10^exp2) = (num1/num2) × 10^(exp1-exp2)
//    double resultNum = a.num() / b.num();
//    long resultExp = a.exp() - b.exp();
//
//    return normalize(new Flt(resultNum, resultExp));
//}
//
//private static double scaleNum(Flt flt, long targetExp) {
//    double scaled = flt.num();
//    long expDiff = targetExp - flt.exp();
//
//    if (expDiff > 0) {
//        // Умножаем на 10^expDiff
//        scaled *= Math.pow(10, expDiff);
//    } else if (expDiff < 0) {
//        // Делим на 10^(-expDiff)
//        scaled /= Math.pow(10, -expDiff);
//    }
//
//    return scaled;
//}