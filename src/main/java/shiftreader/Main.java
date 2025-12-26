package shiftreader;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        if (args.length > 0) {
            if (args[0].equals("--help")) printHelp();
            PipeLine pl = new PipeLine(args);
        } else {
            System.out.println("Не хватает параметров для работы программы");
            System.out.println("--help для отображения справки");
        }
//        Path dir_path = Paths.get("").toAbsolutePath();
//        String dir_str = String.valueOf(dir_path);
//        String filePath = dir_str + fName;

//        try (Scanner Sf = new Scanner(new File(filePath))) {
//            goprogram(Sf, dir_str);
//        } catch (FileNotFoundException e) {
//            System.out.print("Input error. File doesn't exist");
//        }

//        System.out.println(dir_str);
    }

    public static void printHelp() {
        StringBuilder helpText = new StringBuilder();

        helpText.append("==================================================\n");
        helpText.append("УТИЛИТА ФИЛЬТРАЦИИ СОДЕРЖИМОГО ФАЙЛОВ\n");
        helpText.append("==================================================\n\n");

        helpText.append("НАЗНАЧЕНИЕ:\n");
        helpText.append("  Фильтрация содержимого входных файлов с разделением данных\n");
        helpText.append("  по типам (целые числа, вещественные числа, строки) и запись\n");
        helpText.append("  результатов в соответствующие выходные файлы.\n\n");

        helpText.append("СИНТАКСИС:\n");
        helpText.append("  <исполняемый файл> [ОПЦИИ] [файл1 ... файлN]\n");

        helpText.append("ОПЦИИ:\n");
        helpText.append("  -o ПУТЬ               Задает путь для выходных файлов\n");
        helpText.append("                        По умолчанию: текущая директория\n\n");

        helpText.append("  -p ПРЕФИКС            Задает префикс имен выходных файлов\n");
        helpText.append("                        По умолчанию: без префикса\n\n");

        helpText.append("  -a                    Режим добавления в существующие файлы\n");
        helpText.append("                        По умолчанию: файлы перезаписываются\n\n");

        helpText.append("  -s                    Вывод краткой статистики\n");
        helpText.append("                        (количество элементов каждого типа)\n\n");

        helpText.append("  -f                    Вывод полной статистики\n");
        helpText.append("                        Для чисел: мин, макс, сумма, среднее\n");
        helpText.append("                        Для строк: мин/макс длина строки\n\n");

        helpText.append("  --help                Вывод этого справочного сообщения\n\n");

        helpText.append("ВЫХОДНЫЕ ФАЙЛЫ:\n");
        helpText.append("  <префикс>integers.txt   Целые числа\n");
        helpText.append("  <префикс>floats.txt     Вещественные числа\n");
        helpText.append("  <префикс>strings.txt    Строки\n\n");

        helpText.append("==================================================\n");

        System.out.println(helpText.toString());
    }

}
