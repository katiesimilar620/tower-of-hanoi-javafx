package hanoi.towerofhanoi;

import hanoi.towerofhanoi.controllers.SaveSlot;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.Serial;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final int SALT_LENGTH = 16;
    private static final int HASH_ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;

    private String username;
    private String password; // stored as "base64(salt):base64(hash)", never plaintext
    private String email;

    private SaveSlot slot1;
    private SaveSlot slot2;
    private SaveSlot slot3;


    public User(String username, String email, String password) {
        this.setPassword(password);
        this.setUsername(username);
        this.setEmail(email);
        slot1 = new SaveSlot();
        slot2 = new SaveSlot();
        slot3 = new SaveSlot();
    }

    public User(){}

    public void clear() {
        this.setPassword(null);
        this.setUsername(null);
        this.setEmail(null);
        this.setSlot1(null);
        this.setSlot2(null);
        this.setSlot3(null);
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the stored password hash (salt + derived key), not the plaintext password.
     * Use {@link #checkPassword(String)} to verify a login attempt instead of comparing this directly.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Hashes and stores the given plaintext password using PBKDF2 with a random salt.
     * The plaintext itself is never kept.
     */
    public void setPassword(String plainPassword) {
        if (plainPassword == null) {
            this.password = null;
            return;
        }
        byte[] salt = generateSalt();
        byte[] hash = hashPassword(plainPassword.toCharArray(), salt);
        this.password = Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
    }

    /**
     * Verifies a login attempt against the stored hash.
     */
    public boolean checkPassword(String plainPassword) {
        if (password == null || plainPassword == null || !password.contains(":")) {
            return false;
        }
        String[] parts = password.split(":", 2);
        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] expectedHash = Base64.getDecoder().decode(parts[1]);
        byte[] actualHash = hashPassword(plainPassword.toCharArray(), salt);
        return java.security.MessageDigest.isEqual(expectedHash, actualHash);
    }

    private static byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    private static byte[] hashPassword(char[] password, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, HASH_ITERATIONS, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return factory.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Failed to hash password", e);
        }
    }


    public SaveSlot getSlot1() {
        return slot1;
    }

    public void setSlot1(SaveSlot slot1) {
        this.slot1 = slot1;
    }

    public SaveSlot getSlot2() {
        return slot2;
    }

    public void setSlot2(SaveSlot slot2) {
        this.slot2 = slot2;
    }

    public SaveSlot getSlot3() {
        return slot3;
    }

    public void setSlot3(SaveSlot slot3) {
        this.slot3 = slot3;
    }
}
