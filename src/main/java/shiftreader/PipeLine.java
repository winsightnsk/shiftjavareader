package shiftreader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PipeLine {
    private final char reversslash = (File.separatorChar == '\\') ? '/' : '\\';

    private String fullpath = String.valueOf(Paths.get("").toAbsolutePath());
    private String prefix = "";
    private boolean append = false;
    private boolean full = false;
    private final List<String> files = new ArrayList<>();
    private List<Path> touchedFiles = new ArrayList<>();

    public String getFullpath() { return fullpath; }
    public String getPrefix() { return prefix; }
    public boolean isFull() { return full; }
    public boolean isAppend() { return append; }
    public List<String> getFiles() {return Collections.unmodifiableList(files);}

    /**
     * Конструктор класса. Выполняет работу при создании экземпляра.
     * @param args - исходный массив параметров
     */
    public PipeLine(String[] args) {
        for (int i=0; i<args.length; i++) {
            if (args[i].charAt(0) == '-') {
                if (args[i].length() < 2) {
                    System.err.println("Не корректный ключ " + args[i]);
                    continue;
                }
                if (set_asf(args[i])) continue;
                if (args[i].charAt(1) == 'p' && set_p(args, i+1)) {
                    i++;
                    continue;
                }
                if (args[i].charAt(1) == 'o' && set_o(args, i+1)) {
                    i++;
                    continue;
                }
            }
            addFile(args[i]);
        }
        if (!this.files.isEmpty())
            for (String file : files) try (Scanner sf = new Scanner(new File(file))) {
                readFile(sf);
            } catch (Exception e) {
                System.err.print(e.getMessage());
            }
    }

    /**
     * Добавление пути файла в список к чтению
     * @param filename - путь полный или относительный
     */
    void addFile(String filename) {
        for (Character ch : List.of('<', '>', ':', reversslash, '"', '|', '?', '*')) {
            if (filename.indexOf(ch) != -1) {
                System.err.println("Имя файла содержит недопустимый символ '" + ch + "'");
                return;
            }
        }
        for (Path path : List.of(Paths.get(filename), Paths.get("").resolve(filename)))
            if (Files.exists(path) && Files.isRegularFile(path) && Files.isReadable(path)) {
                this.files.add(String.valueOf(path.toAbsolutePath()));
                return;
            }
        System.err.println("Не корректное имя файла '" + filename + "'");
    }

    /**
     * Установка пути к файлам записи.
     * @param arr - исходный массив параметров
     * @param i - индекс, указывающий на значение
     * @return Каталог существует, это каталог, доступен к записи
     */
    private boolean set_o(String[] arr, int i) {
        if (i >= arr.length) {
            System.err.println("Не корректное использование ключа \"o\"");
            return false;
        }
        Path path = Paths.get(arr[i]);
        if (Files.exists(path) && Files.isDirectory(path) && Files.isWritable(path)) {
            this.fullpath = String.valueOf(path.toAbsolutePath());
            return true;
        }

        System.err.println("Не корректный путь к каталогу");
        return true;
    }

    /**
     * Установка префикса по ключу -р
     * @param arr - исходный массив параметров
     * @param i - индекс параметра, указывающий на значение префикса
     * @return успех определения префикса
     */
    private boolean set_p(String[] arr, int i) {
        if (i >= arr.length) {
            System.err.println("Не корректное использование ключа \"p\"");
            return false;
        }
        for (Character ch : List.of('<', '>', ':', reversslash, '"', File.separatorChar, '|', '?', '*')) {
            if (arr[i].indexOf(ch) != -1) {
                System.err.println("Префикс содержит недопустимый символ '" + ch + "': " + arr[i]);
                return true;
            }
        }
        this.prefix = arr[i];
        return true;
    }

    /**
     * Установка ключей asf (остальные символы игнорируются с выведением ошибки в консоль
     * @param a - формат: ^[asf]+.*$
     * @return
     */
    private boolean set_asf(String a) {
        if (a.charAt(1) == 'a' || a.charAt(1) == 's' || a.charAt(1) == 'f') {
            for (int i=1; i<a.length(); i++) {
                switch (a.charAt(i)) {
                    case 'a' -> append = true;
                    case 's' -> full = false;
                    case 'f' -> full = true;
                    case 'o' -> System.err.println("Не корректное использование ключа \"o\"");
                    case 'p' -> System.err.println("Не корректное использование ключа \"p\"");
                    default -> System.err.println("Не корректный ключ  -" + a.charAt(i));
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Чтение одного файла
     * @param sf - указатель на подготовленный сканер
     */
    private void readFile(Scanner sf) {
        while (sf.hasNextLine()) {
            String line = sf.nextLine().trim();
            if (line.isEmpty()) continue;
            if (isInteger(line)) {
                addInteger(line);
            } else if (isFloat(line)) {
                addFloat(line);
            } else addString(line);
        }
    }
    private static boolean isInteger(String line) {
        final Pattern INTEGER = Pattern.compile("^[+-]?\\d+$");
        return (INTEGER.matcher(line).matches());
    }
    /** Может вернуть true на целое число. Игнорируем осознанно
     * т.к. метод приватный и принимается в чтении первым целочисленное значение */
    private static boolean isFloat(String line) {
        final Pattern FLOAT = Pattern.compile("^[+-]?(?:\\d+[.,]\\d*|\\d*[.,]\\d+|\\d+)(?:[eE][+-]?\\d+)?$");
        return (FLOAT.matcher(line).matches());
    }

    /**
     * Проверка доступности записи и очистка файла (в случае ключа -а)
     * @param path путь к файлу для записи
     * @return boolean готовность к записи
     */
    private boolean clearFile(Path path) {
        for (Path p : this.touchedFiles) if (p.equals(path)) return true;
        if (this.append
                && (!Files.exists(path)
                    || (Files.isRegularFile(path) && Files.isWritable(path)))) {
            this.touchedFiles.add(path);
            return true;
        }
        if (!this.append) {
            if (!Files.exists(path)) {
                this.touchedFiles.add(path);
                return true;
            }
            if (Files.isRegularFile(path) && Files.isWritable(path)) try {
                Files.writeString(path, "");
                this.touchedFiles.add(path);
                return true;
            } catch (Exception e) {
                System.err.print(e.getMessage() + " ");
            }
        }
        System.err.println("Ошибка доступа к " + path.toAbsolutePath());
        return false;
    }

    /**
     * Запись в файл целых чисел. Убирает ноли слева.
     * @param line - формат: ^[+-]?\d+$
     */
    private void addInteger(String line) {
        if (Pattern.compile("^[+-]0+$").matcher(line).matches()) line = String.valueOf(line.charAt(0) + '0');
        if (Pattern.compile("^0+$").matcher(line).matches()) line = "0";
        if (Pattern.compile("^[+-]?0+\\d+$").matcher(line).matches()) line = dropLeftNol(line);
        Path resFile = Paths.get(fullpath, prefix + "integers.txt");
        if (clearFile(resFile)) try (FileWriter writer = new FileWriter(resFile.toAbsolutePath().toString(), true)) {
            writer.write(line + "\n");
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }

    /**
     * Запись в файл дробных чисел. Убирает ноли слева и справа. Дописывает 0 возле точки
     * @param line - формат: ^[+-]?\d*\.?\d*([eE][+-]?\d+)?$
     */
    private void addFloat(String line) {
        line = line.replace(',', '.');
        if (Pattern.compile("^[+-]?0*\\.?0*(?:[eE][+-]?\\d+)?$").matcher(line).matches()) line = "0.0";
        // Паттерн для захвата и преобразования за один проход
        Pattern pattern = Pattern.compile(
                "^([+-]?)(\\d*)?(\\.\\d*)?(?:([eE][+-]?)(\\d+))?$"
        );

        Matcher matcher = pattern.matcher(line);
        String sb = matcher.matches() ? (matcher.group(1) == null ? "" : matcher.group(1)) +
                (matcher.group(2) == null ? "" : dropLeftNol(matcher.group(2))) +
                (matcher.group(3) == null ? "" : dropRightNol(matcher.group(3))) +
                (matcher.group(4) == null ? "" : matcher.group(4)) +
                (matcher.group(5) == null ? "" : dropLeftNol(matcher.group(5))) : line;

        Path resFile = Paths.get(fullpath, prefix + "floats.txt");
        if (clearFile(resFile)) try (FileWriter writer = new FileWriter(resFile.toAbsolutePath().toString(), true)) {
            writer.write(line + "\n");
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }

    /**
     * Запись в файл строк
     * @param line - строка для записи
     */
    private void addString(String line) {
        Path resFile = Paths.get(fullpath, prefix + "strings.txt");
        if (clearFile(resFile)) try (FileWriter writer = new FileWriter(resFile.toAbsolutePath().toString(), true)) {
            writer.write(line + "\n");
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }

    /**
     * Отсечка старших нолей целой части
     * @param txt - формат: ^[+-]?\d*$
     */
    private static String dropLeftNol(String txt) {
        StringBuilder sb = new StringBuilder();
        AtomicBoolean have_n = new AtomicBoolean(false);
        txt.chars().forEach(ch -> {
            if (ch == '-' || ch == '+' || have_n.get()) sb.append((char) ch);
            else if (ch != '0') {
                have_n.set(true);
                sb.append((char) ch);
            }
        });
        if (Pattern.compile("^[+-]?0+$").matcher(txt).matches()) sb.append('0');
        return sb.toString();
    }

    /**
     * Отсечка младших нолей дробной части
     * @param txt - формат: ^[.]?\d*$
     */
    private static String dropRightNol(String txt) {
        int nolCount = 0;
        for (int i=txt.length()-1; i>=0; i--) {
            if (txt.charAt(i) != '0') break;
            else nolCount++;
        }
        String result = txt.substring(0, txt.length() - nolCount);
        return result.equals(".") ? ".0" : result;
    }

}
