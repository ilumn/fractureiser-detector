package at.gamersi.fractureiserdebugsample;

public class Main {
    public static void main(String[] args) {
        String pattern1 = "85.217.144.130";
        String pattern2 = "56, 53, 46, 50, 49, 55, 46, 49, 52, 52, 46, 49, 51, 48";
        String pattern3 = "-74.-10.78.-106.12";
        String pattern4 = "114.-18.38.108.-100";
        String pattern5 = "-114.-18.38.108.-100";

        String searchString = pattern1 + " " + pattern2 + " " + pattern3 + " " + pattern4 + " " + pattern5;

        System.out.println("This sample jar file should be detected as malicious by the detector.");
        System.out.println("Search String: " + searchString);
    }
}
