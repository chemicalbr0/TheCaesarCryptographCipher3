
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TheCaesarCryptographCipher {

    static final char[] ALPHABET = {'а', 'б', 'в', 'г', 'д', 'е', 'ж', 'з',
            'и', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ',
            'ъ', 'ы', 'ь', 'э', 'я', '.', ',', '«', '»', '"', '\'', ':', '!', '?', ' '};

    static int cipherKey;

    static Path pathForNewFile;

    static String pathOfFile;

    static boolean run = true;

    public static void main(String[] args) throws IOException {
        while (run) {
            Scanner sc = new Scanner(System.in);
            System.out.println(""" 
                    Welcome to Caesar Cryptograph Cipher
                    Select an action:
                    1 - encrypt
                    2 - decrypt
                    3 - brute force
                    4 - EXIT
                    """);
            int chose = Integer.parseInt(sc.nextLine());
            switch (chose) {
                case 1 -> {
                    System.out.println("Enter the path to the original file (.txt)");
                    pathOfFile = sc.nextLine();
                    if(Files.notExists(Path.of(pathOfFile)))
                    {
                        System.err.println("File if not exists, please try again!");
                        break;
                    }
                    System.out.println("The path where to create the encrypted file with name (.txt)");
                    String strPath = sc.nextLine();
                    if(!strPath.endsWith(".txt"))
                    {
                        System.err.println("Incorrect or Unknown format. Try again!");
                        break;
                    }
                    pathForNewFile = Path.of(strPath);
                    createNewTextFile(pathForNewFile);
                    System.out.println("Enter key");
                    cipherKey = sc.nextInt();
                    if(cipherKey < 0 || cipherKey > ALPHABET.length - 1)
                    {
                        System.err.println("Еhe key is out of range, try again!");
                        break;
                    }
                    encrypt(pathOfFile, cipherKey);
                }
                case 2 -> {
                    System.out.println("Enter the path to the encrypted file (.txt)");
                    pathOfFile = sc.nextLine();
                    if(Files.notExists(Path.of(pathOfFile)))
                    {
                        System.err.println("File if not exists, please try again.");
                        break;
                    }
                    System.out.println("Enter the path where to create the decrypted file with name (.txt)");
                    String strPath = sc.nextLine();
                    if(!strPath.endsWith(".txt"))
                    {
                        System.err.println("Incorrect or Unknown format. Try again!");
                        break;
                    }
                    pathForNewFile = Path.of(strPath);
                    createNewTextFile(pathForNewFile);
                    System.out.println("Enter a well-known key");
                    cipherKey = sc.nextInt();
                    if(cipherKey < 0 || cipherKey > ALPHABET.length - 1)
                    {
                        System.err.println("Еhe key is out of range, try again!");
                        break;
                    }
                    decrypt(pathOfFile, cipherKey);
                }
                case 3 -> {
                    System.out.println("Enter the path to brute forcing the encrypted file (.txt)");
                    if(Files.notExists(Path.of(pathOfFile)))
                    {
                        System.err.println("File if not exists, please try again.");
                        break;
                    }
                    bruteForce(sc.nextLine());
                }
                case 4 -> run = false;
            }
        }
    }

    static char[] encryptAlphabet(char[] ALPHABET, int cipherKey) {
        char[] encryptedAlphabet = new char[ALPHABET.length];
        System.arraycopy(ALPHABET, cipherKey, encryptedAlphabet, 0, ALPHABET.length - cipherKey);
        System.arraycopy(ALPHABET, 0, encryptedAlphabet, ALPHABET.length - cipherKey, cipherKey);
        return encryptedAlphabet;
    }

    static void createNewTextFile(Path path) throws IOException {
        pathForNewFile = path;
        if (Files.exists(pathForNewFile)) {
            Files.delete(pathForNewFile);
            Files.createFile(pathForNewFile);
        } else Files.createFile(pathForNewFile);
    }

    static void encrypt(String pathText, int cipherKey) throws IOException {
        char[] encryptedAlphabet = encryptAlphabet(ALPHABET, cipherKey);
        List<String> strings = Files.readAllLines(Path.of(pathText));
        List<String> encryptedStrings = new ArrayList<>();
        for (String string : strings) {
            String str = string.toLowerCase();
            char[] charArray = str.toCharArray();
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
        Files.write(pathForNewFile, encryptedStrings);
    }

    static void decrypt(String pathText, int cipherKey) throws IOException {
        char[] encryptedAlphabet = encryptAlphabet(ALPHABET, cipherKey);
        List<String> strings = Files.readAllLines(Path.of(pathText));
        List<String> encryptedStrings = new ArrayList<>();
        for (String string : strings) {
            String str = string.toLowerCase();
            char[] charArray = str.toCharArray();
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
        Files.write(pathForNewFile, encryptedStrings);
    }

    static void bruteForce(String path) throws IOException {
        Scanner sc = new Scanner(System.in);
        boolean isGood = false;
        List<String> strings = Files.readAllLines(Path.of(path));
        int cipherKey = 0;
        char[] charArray = strings.getFirst().toCharArray();
        while (cipherKey < ALPHABET.length || !isGood) {
            char[] encryptedAlphabet = encryptAlphabet(ALPHABET, cipherKey);
            char[] tempChars = new char[charArray.length];
            for (int j = 0; j < charArray.length; j++) {
                for (int k = 0; k < ALPHABET.length; k++) {
                    if (charArray[j] == encryptedAlphabet[k]) {
                        tempChars[j] = ALPHABET[k];
                    }
                }
            }
            System.out.println("Cipher key is - " + cipherKey);
            System.out.println("Check it, if good enter 1, else 2");
            System.out.println(Arrays.toString(tempChars));
            int i = Integer.parseInt(sc.nextLine());
            switch (i){
                case 1 -> {
                    System.out.println("Enter the path where to create the decrypted file with name (.txt)");
                    String strPath = sc.nextLine();
                    if(!strPath.endsWith(".txt"))
                    {
                        System.err.println("Incorrect or Unknown format. Try again!");
                        isGood = true;
                    }
                    pathForNewFile = Path.of(strPath);
                    createNewTextFile(pathForNewFile);
                    decrypt(path, cipherKey);
                    isGood = true;
                }
                case 2 ->  cipherKey++;
            }
        }
    }
}
