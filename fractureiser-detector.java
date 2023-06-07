import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class JarDecompiler {
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String jarFilePath;
        String searchString = "85.217.144.130";
        boolean isInfected = false;

        try {
            System.out.print("Enter the path to the JAR file: ");
            jarFilePath = reader.readLine();

            // Extract the contents of the JAR file
            Process process = Runtime.getRuntime().exec("jar xf " + jarFilePath);
            process.waitFor();

            // Recursively search for the string in all extracted files
            isInfected = searchInDirectory(new File("."), searchString);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (isInfected) {
            System.out.println("Warning: The JAR file contains infected files.");
        }
    }

    private static boolean searchInDirectory(File directory, String searchString) {
        boolean isInfected = false;
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    isInfected |= searchInDirectory(file, searchString);
                } else if (file.isFile() && file.getName().endsWith(".java")) {
                    isInfected |= searchInFile(file, searchString);
                }
            }
        }
        return isInfected;
    }

    private static boolean searchInFile(File file, String searchString) {
        boolean isInfected = false;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                if (line.contains(searchString)) {
                    isInfected = true;
                    System.out.println("Warning: File " + file.getPath() + " is infected!");
                }
                lineNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isInfected;
    }
}
