package shiftreader;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Path dir_path = Paths.get("").toAbsolutePath();
        String dir_str = String.valueOf(dir_path);
//        String filePath = dir_str + fName;

//        try (Scanner Sf = new Scanner(new File(filePath))) {
//            goprogram(Sf, dir_str);
//        } catch (FileNotFoundException e) {
//            System.out.print("Input error. File doesn't exist");
//        }

        System.out.println(dir_str);
    }
}
