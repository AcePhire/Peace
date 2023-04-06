import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HexFormat;
import javax.crypto.spec.SecretKeySpec;

public class AES {
    private SecretKey key;
    private final int KEY_SIZE = 128;
    private final int T_LEN = 128;
    private Cipher encryptionCipher;

    private byte[] keyBytes;

    public void init() throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(KEY_SIZE);
        key = generator.generateKey();
        System.out.println(encode(key.getEncoded()));
    }

    public void initKey(String key){
        keyBytes = key.getBytes(StandardCharsets.UTF_8);
    }

    public String encrypt(String message) throws Exception {
        byte[] messageInBytes = message.getBytes(StandardCharsets.UTF_8);
        encryptionCipher = Cipher.getInstance("AES");
        SecretKeySpec spec = new SecretKeySpec(keyBytes, "AES");
        encryptionCipher.init(Cipher.ENCRYPT_MODE, spec);
        byte[] encryptedBytes = encryptionCipher.doFinal(messageInBytes);
        return encode(encryptedBytes);
    }

    public String decrypt(String encryptedMessage) throws Exception {
        byte[] messageInBytes = decode(encryptedMessage);
        Cipher decryptionCipher = Cipher.getInstance("AES");
        SecretKeySpec spec = new SecretKeySpec(keyBytes, "AES");
        decryptionCipher.init(Cipher.DECRYPT_MODE, spec);
        byte[] decryptedBytes = decryptionCipher.doFinal(messageInBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    private String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    private byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }

}