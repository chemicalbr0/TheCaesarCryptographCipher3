import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TheCaesarCryptographCipher {
    static Scanner sc = new Scanner(System.in);

    static final char[] ALPHABET = {'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'я', 'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Я', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '.', ',', '«', '»', '"', '\'', ':', '!', '?', ' '};

    static Path pathForNewFile;

    static boolean isGood = false;

    static boolean run = true;

    static String pathOfFile;

    static int cipherKey;

    public static void main(String[] args) throws IOException {
        while (run) {
            printMenu();
            int chose = Integer.parseInt(sc.nextLine());
            switch (chose) {
                case 1 -> menuEncrypt();
                case 2 -> menuDecrypt();
                case 3 -> menuBruteForce();
                case 4 -> run = false;
            }
        }
    }

    static void printMenu() {
        System.out.println(""" 
                Welcome to Caesar Cryptograph Cipher
                Select an action:
                1 - encrypt
                2 - decrypt
                3 - brute force
                4 - EXIT
                """);
    }

    static void menuEncrypt() throws IOException {
        System.out.println("Enter the path to the original file (.txt)");
        pathOfFile = sc.nextLine();
        if (Files.notExists(Path.of(pathOfFile))) {
            System.err.println("File if not exists, please try again!");
        }
        System.out.println("The path where to create the encrypted file with name (.txt)");
        String strPath = sc.nextLine();
        if (!strPath.endsWith(".txt")) {
            System.err.println("Incorrect or Unknown format. Try again!");
        }
        pathForNewFile = Path.of(strPath);
        createNewTextFile(pathForNewFile);
        System.out.println("Enter key");
        cipherKey = Integer.parseInt(sc.nextLine());
        if (cipherKey < 0 || cipherKey > ALPHABET.length - 1) {
            System.err.println("Еhe key is out of range, try again!");
        }
        encrypt(pathOfFile, cipherKey);
    }

    static void menuDecrypt() throws IOException {
        System.out.println("Enter the path to the encrypted file (.txt)");
        pathOfFile = sc.nextLine();
        if (Files.notExists(Path.of(pathOfFile))) {
            System.err.println("File if not exists, please try again.");
        }
        System.out.println("Enter the path where to create the decrypted file with name (.txt)");
        String strPath = sc.nextLine();
        if (!strPath.endsWith(".txt")) {
            System.err.println("Incorrect or Unknown format. Try again!");
        }
        pathForNewFile = Path.of(strPath);
        createNewTextFile(pathForNewFile);
        System.out.println("Enter a well-known key");
        cipherKey = Integer.parseInt(sc.nextLine());
        if (cipherKey < 0 || cipherKey > ALPHABET.length - 1) {
            System.err.println("Еhe key is out of range, try again!");
        }
        decrypt(pathOfFile, cipherKey);
    }

    static void menuBruteForce() throws IOException {
        printBruteForceWorkInfo();
        System.out.println("Enter the path to brute forcing the encrypted file (.txt)");
        pathOfFile = sc.nextLine();
        if (Files.notExists(Path.of(pathOfFile))) {
            System.err.println("File if not exists, please try again.");
        }
        bruteForce(pathOfFile);
    }

    private static void printBruteForceWorkInfo() {
        System.out.println("""
                Hacking works on the principle of going through all the key options.
                From one line of text, you can guess whether the key suits you or not.
                _____________________________________________________________________
                """);
    }

    static void createNewTextFile(Path path) throws IOException {
        pathForNewFile = path;
        if (Files.exists(pathForNewFile)) {
            Files.delete(pathForNewFile);
            Files.createFile(pathForNewFile);
        } else Files.createFile(pathForNewFile);
    }

    static char[] encryptAlphabet(char[] ALPHABET, int cipherKey) {
        char[] encryptedAlphabet = new char[ALPHABET.length];
        System.arraycopy(ALPHABET, cipherKey, encryptedAlphabet, 0, ALPHABET.length - cipherKey);
        System.arraycopy(ALPHABET, 0, encryptedAlphabet, ALPHABET.length - cipherKey, cipherKey);
        return encryptedAlphabet;
    }

    static void encrypt(String pathText, int cipherKey) throws IOException {
        char[] encryptedAlphabet = encryptAlphabet(ALPHABET, cipherKey);
        List<String> strings = Files.readAllLines(Path.of(pathText));
        List<String> encryptedStrings = new ArrayList<>();
        enumerationForEncrypt(strings, encryptedAlphabet, encryptedStrings);
        Files.write(pathForNewFile, encryptedStrings);
    }

    private static void enumerationForEncrypt(List<String> strings, char[] encryptedAlphabet, List<String> encryptedStrings) {
        for (String string : strings) {
            char[] charArray = string.toCharArray();
            for (int j = 0; j < charArray.length; j++) {
                for (int k = 0; k < ALPHABET.length; k++) {
                    if (charArray[j] == ALPHABET[k]) {
                        charArray[j] = encryptedAlphabet[k];
                        break;
                    }
                }
            }
            encryptedStrings.add(String.valueOf(charArray));
        }
    }

    static void decrypt(String pathText, int cipherKey) throws IOException {
        char[] encryptedAlphabet = encryptAlphabet(ALPHABET, cipherKey);
        List<String> strings = Files.readAllLines(Path.of(pathText));
        List<String> encryptedStrings = new ArrayList<>();
        enumerationForDecrypt(strings, encryptedAlphabet, encryptedStrings);
        Files.write(pathForNewFile, encryptedStrings);
    }

    private static void enumerationForDecrypt(List<String> strings, char[] encryptedAlphabet, List<String> encryptedStrings) {
        for (String string : strings) {
            char[] charArray = string.toCharArray();
            for (int j = 0; j < charArray.length; j++) {
                for (int k = 0; k < ALPHABET.length; k++) {
                    if (charArray[j] == encryptedAlphabet[k]) {
                        charArray[j] = ALPHABET[k];
                        break;
                    }
                }
            }
            encryptedStrings.add(String.valueOf(charArray));
        }
    }

    static void bruteForce(String path) throws IOException {
        List<String> strings = Files.readAllLines(Path.of(path));
        cipherKey = 0;
        char[] charArray = strings.getFirst().toCharArray();
        while (cipherKey < ALPHABET.length && !isGood) {
            char[] encryptedAlphabet = encryptAlphabet(ALPHABET, cipherKey);
            char[] tempChars = new char[charArray.length];
            enumerationForBruteForce(path, charArray, encryptedAlphabet, tempChars);
        }
    }

    private static void enumerationForBruteForce(String path, char[] charArray, char[] encryptedAlphabet, char[] tempChars) throws IOException {
        for (int j = 0; j < charArray.length; j++) {
            for (int k = 0; k < ALPHABET.length; k++) {
                if (charArray[j] == encryptedAlphabet[k]) {
                    tempChars[j] = ALPHABET[k];
                }
            }
        }
        bruteForceMenu(tempChars, path);
    }

    static void bruteForceMenu(char[] tempChars, String path) throws IOException {
        System.out.println("Cipher key is - " + cipherKey);
        System.out.println("Check it, if good enter 1, else 2");
        System.out.println(Arrays.toString(tempChars));
        int i = Integer.parseInt(sc.nextLine());
        switch (i) {
            case 1 -> writeHackedText(path);
            case 2 -> cipherKey++;
        }
    }

    private static void writeHackedText(String path) throws IOException {
        System.out.println("Enter the path where to create the decrypted file with name (.txt)");
        String strPath = sc.nextLine();
        if (!strPath.endsWith(".txt")) {
            System.err.println("Incorrect or Unknown format. Try again!");
            isGood = true;
        }
        pathForNewFile = Path.of(strPath);
        createNewTextFile(pathForNewFile);
        decrypt(path, cipherKey);
        isGood = true;
    }

}
