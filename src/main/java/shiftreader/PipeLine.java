package shiftreader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PipeLine {

    private String fullpath = String.valueOf(Paths.get("").toAbsolutePath());
    private String prefix = "";
    private boolean append = false;
    private boolean full = false;
    private final List<String> files = new ArrayList<>();

    public String getFullpath() { return fullpath; }
    public String getPrefix() { return prefix; }
    public boolean isFull() { return full; }
    public boolean isAppend() { return append; }
    public List<String> getFiles() {return Collections.unmodifiableList(files);}

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
        if (validateParams()) readFiles();
    }

    void addFile(String filename) {
        for (Character ch : List.of('<','>',':','\\','"','/','|','?','*')) {
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

    private boolean set_p(String[] arr, int i) {
        if (i >= arr.length) {
            System.err.println("Не корректное использование ключа \"p\"");
            return false;
        }
        for (Character ch : List.of('<','>',':','\\','"','/','|','?','*')) {
            if (arr[i].indexOf(ch) != -1) {
                System.err.println("Префикс содержит недопустимый символ '" + ch + "': " + arr[i]);
                return true;
            }
        }
        this.prefix = arr[i];
        return true;
    }

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

    private boolean validateParams() {
        return true;
    }

    private void readFiles() {

    }

}
