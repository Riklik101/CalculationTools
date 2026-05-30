package PasswordManagerProj;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * CrytographyCalc
 *
 * Simple utility to encrypt plaintext with a password and store entries in
 * PasswordManagerProj/HashStore.txt. Supports both encrypt (default) and
 * decrypt (-d / --decrypt) modes.
 *
 * Storage format (one entry per line):
 *   Base64(salt) : Base64(iv) : Base64(ciphertext)
 *
 * Key derivation: PBKDF2WithHmacSHA256
 * Encryption: AES/GCM/NoPadding (12 byte IV + 128-bit tag)
 */
public class CrytographyCalc {
    // Constants controlling key derivation and cipher parameters
    private static final int SALT_LEN = 16; // bytes
    private static final int IV_LEN = 12; // bytes for GCM recommended
    private static final int ITERATIONS = 100_000; // PBKDF2 iterations
    private static final int KEY_LEN = 256; // bits (AES-256)

    // Path to store encrypted entries (relative to project root)
    private static final Path STORE_PATH = Paths.get("PasswordManagerProj", "HashStore.txt");

    /**
     * Program entry point. Without flags it encrypts a user-provided plaintext.
     * With -d or --decrypt it enters decrypt mode.
     *
     * Supported usages:
     *   java ... CrytographyCalc            (encrypt interactive)
     *   java ... CrytographyCalc -d         (decrypt interactive)
     */
    public static void main(String[] args) {
        boolean decryptMode = false;
        for (String a : args) {
            if ("-d".equalsIgnoreCase(a) || "--decrypt".equalsIgnoreCase(a)) {
                decryptMode = true;
                break;
            }
        }

        if (decryptMode) {
            runDecryptFlow();
        } else {
            runEncryptFlow();
        }
    }

    /**
     * Interactive encryption flow: prompt for plaintext and password, encrypt,
     * and append a Base64(salt):Base64(iv):Base64(ciphertext) line to store.
     */
    private static void runEncryptFlow() {
        // Use try-with-resources to auto-close the Scanner
        try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8)) {
            System.out.print("Enter plaintext to encrypt: ");
            String plaintext = scanner.nextLine();

            // Obtain password chars using helper (handles console vs piped stdin)
            char[] passwordChars = getPasswordChars(scanner);

            // Perform encryption and get a single-line encoded representation
            String encoded = encryptBase64(plaintext, passwordChars);

            // Zero the password in memory for hygiene
            java.util.Arrays.fill(passwordChars, '\0');

            // Ensure directory exists and append the entry
            try {
                Files.createDirectories(STORE_PATH.getParent());
                String line = encoded + System.lineSeparator();
                Files.writeString(STORE_PATH, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                System.out.println("Encrypted entry appended to: " + STORE_PATH);
            } catch (Exception e) {
                System.err.println("Failed to write entry: " + e.getMessage());
                e.printStackTrace(System.err);
            }
        } catch (Exception e) {
            System.err.println("Encryption failed: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    /**
     * Interactive decrypt flow:
     *  - Reads all entries from the store (if any)
     *  - Prints a numbered list (showing only the first 16 chars of ciphertext for identification)
     *  - Prompts the user to enter a number or paste a stored line
     *  - Prompts for password and attempts decryption
     */
    private static void runDecryptFlow() {
        try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8)) {
            // Read stored lines (if the file doesn't exist, notify and exit)
            List<String> lines;
            if (Files.exists(STORE_PATH)) {
                lines = Files.readAllLines(STORE_PATH, StandardCharsets.UTF_8);
            } else {
                System.out.println("No store found at: " + STORE_PATH + "\nNothing to decrypt.");
                return;
            }

            // Filter out empty lines and trim
            lines.removeIf(String::isBlank);

            if (lines.isEmpty()) {
                System.out.println("Store is empty: no entries to decrypt.");
                return;
            }

            // Display a numbered list of entries (short preview)
            System.out.println("Stored entries:");
            for (int i = 0; i < lines.size(); i++) {
                String preview = lines.get(i);
                // Show only the ciphertext part preview (last segment) and limit length for readability
                String[] parts = preview.split(":");
                String ctPreview = (parts.length >= 3) ? parts[2] : preview;
                if (ctPreview.length() > 16) ctPreview = ctPreview.substring(0, 16) + "..";
                System.out.printf("%d) %s\n", i + 1, ctPreview);
            }

            System.out.print("Enter entry number to decrypt (or paste full stored line): ");
            String selection = scanner.nextLine().trim();

            String storedLine;
            // If the input is a number, use that index; otherwise treat the input as the full line
            try {
                int idx = Integer.parseInt(selection);
                if (idx < 1 || idx > lines.size()) {
                    System.err.println("Invalid entry number.");
                    return;
                }
                storedLine = lines.get(idx - 1).trim();
            } catch (NumberFormatException nfe) {
                // Not a number, assume it's the full stored line
                storedLine = selection;
            }

            // Obtain password chars using helper (handles console vs piped stdin)
            char[] passwordChars = getPasswordChars(scanner);

            try {
                String plaintext = decryptBase64(storedLine, passwordChars);
                System.out.println("Decrypted plaintext: " + plaintext);
            } catch (Exception e) {
                System.err.println("Decryption failed: " + e.getMessage());
                e.printStackTrace(System.err);
            } finally {
                java.util.Arrays.fill(passwordChars, '\0');
            }
        } catch (Exception e) {
            System.err.println("Decrypt flow failed: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    /**
     * Helper to obtain password chars. Prefers System.console().readPassword() when
     * a console is available and no stdin data is waiting; otherwise reads a line
     * from the provided Scanner (this allows piping to work).
     */
    private static char[] getPasswordChars(Scanner scanner) {
        try {
            // If a console exists and there's no data waiting on stdin, use readPassword
            if (System.console() != null) {
                try {
                    if (System.in.available() == 0) {
                        return System.console().readPassword("Enter password: ");
                    }
                } catch (IOException ioe) {
                    // Fall back to reading from scanner below
                }
            }

            // Fall back to scanner input (password will be echoed)
            System.out.print("Enter password: ");
            String pwd = scanner.nextLine();
            return pwd.toCharArray();
        } catch (Exception e) {
            // As a last resort, return an empty password
            return new char[0];
        }
    }

    /**
     * Encrypts plaintext with a password and returns a single-line encoded string
     * containing Base64(salt):Base64(iv):Base64(ciphertext).
     */
    private static String encryptBase64(String plaintext, char[] password) throws Exception {
        SecureRandom random = new SecureRandom();

        // Generate a random salt for PBKDF2
        byte[] salt = new byte[SALT_LEN];
        random.nextBytes(salt);

        // Derive the AES key from password and salt
        SecretKey key = deriveKey(password, salt);

        // Generate a random IV for GCM
        byte[] iv = new byte[IV_LEN];
        random.nextBytes(iv);

        // Initialize cipher (AES-GCM)
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, iv); // 128-bit auth tag
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);

        // Encrypt
        byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

        // Return salt:iv:ciphertext all Base64 encoded and separated by ':'
        String sSalt = Base64.getEncoder().encodeToString(salt);
        String sIv = Base64.getEncoder().encodeToString(iv);
        String sCt = Base64.getEncoder().encodeToString(ciphertext);

        return sSalt + ":" + sIv + ":" + sCt;
    }

    /**
     * Decrypts a stored Base64(salt):Base64(iv):Base64(ciphertext) line using the
     * provided password and returns the plaintext string.
     */
    private static String decryptBase64(String storedLine, char[] password) throws Exception {
        // Parse the stored line into parts
        String[] parts = storedLine.split(":");
        if (parts.length < 3) throw new IllegalArgumentException("Stored line does not contain salt:iv:ciphertext");

        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] iv = Base64.getDecoder().decode(parts[1]);
        byte[] ciphertext = Base64.getDecoder().decode(parts[2]);

        // Derive key from provided password and stored salt
        SecretKey key = deriveKey(password, salt);

        // Initialize cipher for decryption
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);

        // Decrypt and return plaintext
        byte[] plaintextBytes = cipher.doFinal(ciphertext);
        return new String(plaintextBytes, StandardCharsets.UTF_8);
    }

    /**
     * Derives an AES SecretKey from a password and salt using PBKDF2WithHmacSHA256.
     */
    private static SecretKey deriveKey(char[] password, byte[] salt) throws Exception {
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LEN);
        byte[] keyBytes = skf.generateSecret(spec).getEncoded();
        return new SecretKeySpec(keyBytes, "AES");
    }
}

