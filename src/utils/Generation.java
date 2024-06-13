package utils;


import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class Generation {
    public static byte[] randomBytes(int num) {
        byte[] bytes = new byte[num];
        try {
            SecureRandom.getInstanceStrong().nextBytes(bytes);
            return bytes;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            new Random().nextBytes(bytes);
            return bytes;
        }
    }
}
