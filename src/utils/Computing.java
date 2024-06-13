package utils;


public final class Computing {
    // XOR two byte arrays
    public static byte[] XOR(byte[] a, byte[] b) {
        int len = Math.min(a.length, b.length);
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = (byte) (a[i] ^ b[i]);
        }

        return result;
    }

    //XOR for Streebog
    public static void XOR(byte[] a, long[] b, byte[] c) {
        for (int i = 0; i < 64; i++) {
            c[i] = (byte) (a[i] ^ b[i]);
        }
    }
    public static void XOR(byte[] a, byte[] b, byte[] c) {
        for (int i = 0; i < 64; i++) {
            c[i] = (byte) (a[i] ^ b[i]);
        }
    }

    // Join two byte arrays
    public static byte[] join(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    // Galois field multiplication
    public static byte multiplyGF(byte a, byte b) {
        byte c = 0;
        for (int i = 0; i < 8; i++) {
            if ((b & 1) == 1) c ^= a;
            byte hi_bit = (byte) (a & 0x80);
            a <<= 1;
            if (hi_bit < 0) a ^= 0xc3; // polinom  x^8+x^7+x^6+x+1
            b >>= 1;
        }
        return c;
    }
}
