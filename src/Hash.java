import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


public class Hash {
    public static String getSHA(String input) throws NoSuchAlgorithmException
    {

        MessageDigest md = MessageDigest.getInstance("SHA-256");

        return encode(md.digest(input.getBytes(StandardCharsets.UTF_8)));
    }

    private static String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }
}