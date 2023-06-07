import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class fractureiserdetector {
    private static final String PROCYON_JAR_PATH = "procyon-decompiler.jar";

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String jarFilePath;
        String[] searchStrings = {
                "85.217.144.130",
                "56, 53, 46, 50, 49, 55, 46, 49, 52, 52, 46, 49, 51, 48",
                "-74.-10.78.-106.12",
                "114.-18.38.108.-100",
                "-114.-18.38.108.-100"
        };
        boolean isInfected = false;

        try {
            System.out.print("Enter the path to the JAR file: ");
            jarFilePath = reader.readLine();

            // Check if Procyon decompiler is installed
            if (!isProcyonInstalled()) {
                installProcyon();
            }

            // Create a temporary directory for decompiled files
            File decompiledDir = createTempDirectory();

            // Decompile the JAR file using Procyon decompiler
            decompileJar(jarFilePath, decompiledDir.getAbsolutePath());

            // Recursively search for the strings in the decompiled files
            isInfected = searchInDirectory(decompiledDir, searchStrings);

            // Cleanup: Delete the temporary directory
            cleanup(decompiledDir);
        } catch (IOException e) {
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

    private static boolean isProcyonInstalled() {
        File procyonFile = new File(PROCYON_JAR_PATH);
        return procyonFile.exists();
    }

    private static void installProcyon() {
        try {
            // Download Procyon decompiler JAR file
            System.out.println("Downloading Procyon decompiler...");
            String procyonUrl = "https://github.com/mstrobel/procyon/releases/download/v0.6.0/procyon-decompiler-0.6.0.jar";
            Process process = Runtime.getRuntime().exec("curl -L -o " + PROCYON_JAR_PATH + " " + procyonUrl);
            process.waitFor();

            // Verify if the download was successful
            File procyonFile = new File(PROCYON_JAR_PATH);
            if (!procyonFile.exists()) {
                throw new IOException("Procyon decompiler installation failed.");
            }

            System.out.println("Procyon decompiler installed successfully.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static File createTempDirectory() throws IOException {
        return Files.createTempDirectory("decompiled").toFile();
    }

    private static void decompileJar(String jarFilePath, String outputDir) {
        try {
            System.out.println("Decompiling the JAR file...");
            Process process = Runtime.getRuntime().exec("java -jar " + PROCYON_JAR_PATH + " -o " + outputDir + " " + jarFilePath);
            process.waitFor();
            System.out.println("Decompilation completed successfully.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static boolean searchInDirectory(File directory, String[] searchStrings) {
        boolean isInfected = false;
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    isInfected |= searchInDirectory(file, searchStrings);
                } else if (file.isFile() && file.getName().endsWith(".java")) {
                    isInfected |= searchInFile(file, searchStrings);
                }
            }
        }
        return isInfected;
    }

    private static boolean searchInFile(File file, String[] searchStrings) {
        boolean isInfected = false;
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(file.getAbsolutePath()))) {
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                for (String searchString : searchStrings) {
                    if (line.contains(searchString)) {
                        isInfected = true;
                        System.out.println("Warning: File " + file.getPath() + " is infected!");
                        break;
                    }
                }
                lineNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isInfected;
    }

    private static void cleanup(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    cleanup(file);
                }
            }
        }
        directory.delete();
    }
}
