package src.main.board;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;

public class FIlePaths {
    public static void loadCardFile(String filePath) {
        // Your loading logic here
        System.out.println("Loading from: " + filePath);
    }

    public static void main(String[] args) {
        String baseDir = System.getProperty("user.dir");
        System.out.println(baseDir);
        Path possiblePath1 = Paths.get(baseDir, "MontanaOpoly", "src", "dependencies", "cards.txt");
        Path possiblePath2 = Paths.get(baseDir, "src", "dependencies", "cards.txt");

        File file1 = possiblePath1.toFile();
        File file2 = possiblePath2.toFile();

        if (file1.exists()) {
            System.out.println("File 1 exists");
            loadCardFile(file1.getPath());
        } else if (file2.exists()) {
            System.out.println("File 2 exists");
            loadCardFile(file2.getPath());
        } else {
            System.err.println("Card file not found in known locations.");
        }
    }
}
