import java.io.*;
import java.util.Scanner;

public class FileEncryptDecrypt {
    private static final int SHIFT = 3; // Shift for Caesar cipher

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose an option:");
        System.out.println("1. Encrypt");
        System.out.println("2. Decrypt");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        System.out.println("Enter the file name or path:");
        String filePath = scanner.nextLine();

        // Generate output file name based on choice
        String outputFilePath = generateOutputFileName(filePath, choice);

        try {
            if (choice == 1) {
                encryptFile(filePath, outputFilePath);
                System.out.println("File encrypted successfully. Output saved to " + outputFilePath);
            } else if (choice == 2) {
                decryptFile(filePath, outputFilePath);
                System.out.println("File decrypted successfully. Output saved to " + outputFilePath);
            } else {
                System.out.println("Invalid choice. Please select 1 for encryption or 2 for decryption.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static String generateOutputFileName(String inputFilePath, int choice) {
        String prefix = (choice == 1) ? "encrypted_" : "decrypted_";
        String fileName = new File(inputFilePath).getName();
        String fileDir = new File(inputFilePath).getParent();

        // Create output file path
        return (fileDir != null ? fileDir + File.separator : "") + prefix + fileName;
    }

    private static void encryptFile(String inputFilePath, String outputFilePath) throws IOException {
        processFile(inputFilePath, outputFilePath, true);
    }

    private static void decryptFile(String inputFilePath, String outputFilePath) throws IOException {
        processFile(inputFilePath, outputFilePath, false);
    }

    private static void processFile(String inputFilePath, String outputFilePath, boolean encrypt) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String processedLine = encrypt ? encryptLine(line) : decryptLine(line);
                writer.write(processedLine);
                writer.newLine();
            }
        }
    }

    private static String encryptLine(String line) {
        return shiftCharacters(line, SHIFT);
    }

    private static String decryptLine(String line) {
        return shiftCharacters(line, -SHIFT);
    }

    private static String shiftCharacters(String line, int shift) {
        StringBuilder shifted = new StringBuilder();
        for (char c : line.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                int shiftedChar = (c - base + shift) % 26;
                if (shiftedChar < 0) {
                    shiftedChar += 26; // Wrap around for negative shifts
                }
                shifted.append((char) (base + shiftedChar));
            } else {
                shifted.append(c); // Non-letter characters remain unchanged
            }
        }
        return shifted.toString();
    }
}