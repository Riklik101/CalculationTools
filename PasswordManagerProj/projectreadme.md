PasswordManagerProj
====================

Overview
--------
This small Java utility encrypts user-provided plaintext with a password and
appends the result to `PasswordManagerProj/HashStore.txt`. It also supports
decrypting stored entries.

Implementation notes
--------------------
- Key derivation: PBKDF2WithHmacSHA256 (salted PBKDF2)
- Cipher: AES/GCM/NoPadding (AES-256 with 12-byte IV and 128-bit auth tag)
- Stored entry format (one entry per line):

  Base64(salt) : Base64(iv) : Base64(ciphertext)

  Example line:
  D2ioYjqpQlvTGnHeEmh5MQ==:HSVFJUxGi2R2iGnf:f0cYeFyy2PJF7gIfrr2Ct+611FDg

Requirements
------------
- A JDK that supports PBKDF2WithHmacSHA256 and AES/GCM (Java 11+ recommended, but other compatible JDKs are fine).
- No external libraries required for the core crypto (the project already contains some libs for other parts; this class uses standard Java APIs).

Build
-----
From the repository root (Windows cmd.exe examples):

```cmd
javac -cp "lib/*" "c:\Users\Eklavya\Documents\CodeVS\CalculationTools\PasswordManagerProj\CrytographyCalc.java"
```

Run (encrypt)
--------------
Interactive encrypt mode (default):

```cmd
java -cp "c:\Users\Eklavya\Documents\CodeVS\CalculationTools;lib/*" PasswordManagerProj.CrytographyCalc
```

The program will prompt for:
- plaintext to encrypt
- password (uses Console.readPassword when available; otherwise reads from stdin)

After success, an entry is appended to `PasswordManagerProj/HashStore.txt`.

Run (decrypt)
--------------
Start decrypt mode with `-d` or `--decrypt`:

```cmd
java -cp "c:\Users\Eklavya\Documents\CodeVS\CalculationTools;lib/*" PasswordManagerProj.CrytographyCalc -d
```

Behavior in decrypt mode:
- The program lists stored entries with short previews.
- Enter the entry number to decrypt, or paste the full stored line.
- Enter the password used to encrypt that entry.
- If the password is correct, the plaintext will be printed.

Non-interactive / piping examples
---------------------------------
You can pipe inputs (useful for quick tests). Example: encrypt by piping plaintext then password:

```cmd
cmd /c "echo mySecretPlaintext&echo myPassword123" | java -cp "c:\Users\Eklavya\Documents\CodeVS\CalculationTools;lib/*" PasswordManagerProj.CrytographyCalc
```

Decrypt by piping the entry number and password (replace N with entry index):

```cmd
cmd /c "echo N&echo myPassword123" | java -cp "c:\Users\Eklavya\Documents\CodeVS\CalculationTools;lib/*" PasswordManagerProj.CrytographyCalc -d
```

Notes & security
----------------
- Passwords are zeroed in memory after use (char[] is cleared), but the program is a simple utility and not hardened for high-security production use.
- The `HashStore.txt` file holds encrypted ciphertext and salts — if you use weak passwords they can be brute-forced.
- Consider using a dedicated, audited secret manager for real sensitive data.

Troubleshooting
---------------
- If decryption fails with an authentication error, ensure you are using the exact same password and the stored line wasn't corrupted.
- If `PBKDF2WithHmacSHA256` is not available on your JDK, upgrade to a later JDK (or ensure your security providers support it).

Next steps / enhancements
-------------------------
- Add `--index N` and `--line "..."` CLI flags to avoid interactive prompts.
- Add unit tests (JUnit) for round-trip encrypt/decrypt.
- Add an explicit `--help` command.

License
-------
This file is part of your project. See repository LICENSE for overall project licensing.

