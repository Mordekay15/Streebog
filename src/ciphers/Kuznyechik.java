package ciphers;

import statement.StateManager;
import utils.Computing;
import utils.Conversions;

import java.util.Arrays;


public final class Kuznyechik extends Cipher {
    public static final int BLOCK_SIZE = 16;
    public static final int ROUNDS = 9;
    public static final int KEY_SIZE = 32;

    // Straight linear conversion table
    private static final int[] SLCT = {
            0xFC, 0xEE, 0xDD, 0x11, 0xCF, 0x6E, 0x31, 0x16, 0xFB, 0xC4, 0xFA, 0xDA, 0x23, 0xC5, 0x04, 0x4D,
            0xE9, 0x77, 0xF0, 0xDB, 0x93, 0x2E, 0x99, 0xBA, 0x17, 0x36, 0xF1, 0xBB, 0x14, 0xCD, 0x5F, 0xC1,
            0xF9, 0x18, 0x65, 0x5A, 0xE2, 0x5C, 0xEF, 0x21, 0x81, 0x1C, 0x3C, 0x42, 0x8B, 0x01, 0x8E, 0x4F,
            0x05, 0x84, 0x02, 0xAE, 0xE3, 0x6A, 0x8F, 0xA0, 0x06, 0x0B, 0xED, 0x98, 0x7F, 0xD4, 0xD3, 0x1F,
            0xEB, 0x34, 0x2C, 0x51, 0xEA, 0xC8, 0x48, 0xAB, 0xF2, 0x2A, 0x68, 0xA2, 0xFD, 0x3A, 0xCE, 0xCC,
            0xB5, 0x70, 0x0E, 0x56, 0x08, 0x0C, 0x76, 0x12, 0xBF, 0x72, 0x13, 0x47, 0x9C, 0xB7, 0x5D, 0x87,
            0x15, 0xA1, 0x96, 0x29, 0x10, 0x7B, 0x9A, 0xC7, 0xF3, 0x91, 0x78, 0x6F, 0x9D, 0x9E, 0xB2, 0xB1,
            0x32, 0x75, 0x19, 0x3D, 0xFF, 0x35, 0x8A, 0x7E, 0x6D, 0x54, 0xC6, 0x80, 0xC3, 0xBD, 0x0D, 0x57,
            0xDF, 0xF5, 0x24, 0xA9, 0x3E, 0xA8, 0x43, 0xC9, 0xD7, 0x79, 0xD6, 0xF6, 0x7C, 0x22, 0xB9, 0x03,
            0xE0, 0x0F, 0xEC, 0xDE, 0x7A, 0x94, 0xB0, 0xBC, 0xDC, 0xE8, 0x28, 0x50, 0x4E, 0x33, 0x0A, 0x4A,
            0xA7, 0x97, 0x60, 0x73, 0x1E, 0x00, 0x62, 0x44, 0x1A, 0xB8, 0x38, 0x82, 0x64, 0x9F, 0x26, 0x41,
            0xAD, 0x45, 0x46, 0x92, 0x27, 0x5E, 0x55, 0x2F, 0x8C, 0xA3, 0xA5, 0x7D, 0x69, 0xD5, 0x95, 0x3B,
            0x07, 0x58, 0xB3, 0x40, 0x86, 0xAC, 0x1D, 0xF7, 0x30, 0x37, 0x6B, 0xE4, 0x88, 0xD9, 0xE7, 0x89,
            0xE1, 0x1B, 0x83, 0x49, 0x4C, 0x3F, 0xF8, 0xFE, 0x8D, 0x53, 0xAA, 0x90, 0xCA, 0xD8, 0x85, 0x61,
            0x20, 0x71, 0x67, 0xA4, 0x2D, 0x2B, 0x09, 0x5B, 0xCB, 0x9B, 0x25, 0xD0, 0xBE, 0xE5, 0x6C, 0x52,
            0x59, 0xA6, 0x74, 0xD2, 0xE6, 0xF4, 0xB4, 0xC0, 0xD1, 0x66, 0xAF, 0xC2, 0x39, 0x4B, 0x63, 0xB6
    };

    // Reverse linear conversion table
    private static final int[] RLCT = {
            0xA5, 0x2D, 0x32, 0x8F, 0x0E, 0x30, 0x38, 0xC0, 0x54, 0xE6, 0x9E, 0x39, 0x55, 0x7E, 0x52, 0x91,
            0x64, 0x03, 0x57, 0x5A, 0x1C, 0x60, 0x07, 0x18, 0x21, 0x72, 0xA8, 0xD1, 0x29, 0xC6, 0xA4, 0x3F,
            0xE0, 0x27, 0x8D, 0x0C, 0x82, 0xEA, 0xAE, 0xB4, 0x9A, 0x63, 0x49, 0xE5, 0x42, 0xE4, 0x15, 0xB7,
            0xC8, 0x06, 0x70, 0x9D, 0x41, 0x75, 0x19, 0xC9, 0xAA, 0xFC, 0x4D, 0xBF, 0x2A, 0x73, 0x84, 0xD5,
            0xC3, 0xAF, 0x2B, 0x86, 0xA7, 0xB1, 0xB2, 0x5B, 0x46, 0xD3, 0x9F, 0xFD, 0xD4, 0x0F, 0x9C, 0x2F,
            0x9B, 0x43, 0xEF, 0xD9, 0x79, 0xB6, 0x53, 0x7F, 0xC1, 0xF0, 0x23, 0xE7, 0x25, 0x5E, 0xB5, 0x1E,
            0xA2, 0xDF, 0xA6, 0xFE, 0xAC, 0x22, 0xF9, 0xE2, 0x4A, 0xBC, 0x35, 0xCA, 0xEE, 0x78, 0x05, 0x6B,
            0x51, 0xE1, 0x59, 0xA3, 0xF2, 0x71, 0x56, 0x11, 0x6A, 0x89, 0x94, 0x65, 0x8C, 0xBB, 0x77, 0x3C,
            0x7B, 0x28, 0xAB, 0xD2, 0x31, 0xDE, 0xC4, 0x5F, 0xCC, 0xCF, 0x76, 0x2C, 0xB8, 0xD8, 0x2E, 0x36,
            0xDB, 0x69, 0xB3, 0x14, 0x95, 0xBE, 0x62, 0xA1, 0x3B, 0x16, 0x66, 0xE9, 0x5C, 0x6C, 0x6D, 0xAD,
            0x37, 0x61, 0x4B, 0xB9, 0xE3, 0xBA, 0xF1, 0xA0, 0x85, 0x83, 0xDA, 0x47, 0xC5, 0xB0, 0x33, 0xFA,
            0x96, 0x6F, 0x6E, 0xC2, 0xF6, 0x50, 0xFF, 0x5D, 0xA9, 0x8E, 0x17, 0x1B, 0x97, 0x7D, 0xEC, 0x58,
            0xF7, 0x1F, 0xFB, 0x7C, 0x09, 0x0D, 0x7A, 0x67, 0x45, 0x87, 0xDC, 0xE8, 0x4F, 0x1D, 0x4E, 0x04,
            0xEB, 0xF8, 0xF3, 0x3E, 0x3D, 0xBD, 0x8A, 0x88, 0xDD, 0xCD, 0x0B, 0x13, 0x98, 0x02, 0x93, 0x80,
            0x90, 0xD0, 0x24, 0x34, 0xCB, 0xED, 0xF4, 0xCE, 0x99, 0x10, 0x44, 0x40, 0x92, 0x3A, 0x01, 0x26,
            0x12, 0x1A, 0x48, 0x68, 0xF5, 0x81, 0x8B, 0xC7, 0xD6, 0x20, 0x0A, 0x08, 0x00, 0x4C, 0xD7, 0x74
    };

    // Linear conversion vector
    private static final int[] lVec = {1, 148, 32, 133, 16, 194, 192, 1, 251, 1, 192, 194, 16, 133, 32, 148};


    public Kuznyechik(byte[] key) {
        super(key);
    }

    public Kuznyechik(byte[] key, StateManager manager) {
        super(key, manager);
    }


    // Constant calculation function
    private byte[] getConstant(int iteration) {
        byte[] iter_num = new byte[BLOCK_SIZE];
        iter_num[BLOCK_SIZE - 1] = (byte) (iteration + 1);
        return rotate(iter_num);
    }

    @Override
    protected byte[][] expandKey(byte[] key) {
        byte[] key1 = Arrays.copyOfRange(key, 0, BLOCK_SIZE);
        byte[] key2 = Arrays.copyOfRange(key, BLOCK_SIZE, KEY_SIZE);

        byte[][] roundKeys = new byte[ROUNDS + 1][BLOCK_SIZE];
        addStep(String.format("Round key parts %d and %d", 0, 1), "round_key_plain", key);
        roundKeys[0] = key1;
        roundKeys[1] = key2;
//        System.out.println(Conversions.hex(key1));
//        System.out.println(Conversions.hex(key2));
//        System.out.println("\n\n\n");
        finalizeStep(key);

        for (int i = 0; i < 4; i++) {
            addStep(String.format("Round key parts %d and %d", i + 2, i + 3), "round_key_feistel", Computing.join(key1, key2));
            for (int j = 0; j < 8; j++) {
                addStep(String.format("Feistel round %d", j), "round", Computing.join(key1, key2));
                addStep(String.format("Constant computing %d", j), "constant", new byte[]{(byte) (j + 8 * i)});

                byte[] constant = getConstant(j + (8 * i));
//                System.out.println("\nКонстанта\n" + Conversions.hex(constant));

                nextStep(String.format("XOR key 1 with constant %d", j), "xor_with_constant", key1, constant);
                byte[] internal = Computing.XOR(key1, constant);
//                System.out.println("\nXOR\n" + Conversions.hex(internal));

                nextStep(String.format("Substitute bytes %d", j), "substitute", internal);
                internal = substitute(internal);
//                System.out.println("\nS\n" + Conversions.hex(internal) + "\n");

                nextStep(String.format("Rotate bytes %d", j), "rotate", internal);
                internal = rotate(internal);
//                System.out.println("\nL\n" + Conversions.hex(internal) + "\n");

                nextStep(String.format("XOR key 1 with result %d", j), "xor_with_result", key2, internal);
                internal = Computing.XOR(internal, key2);
//                System.out.println("\nXOR 2\n" + Conversions.hex(internal));
//                System.out.println("\n--------------------------------------------------\n");
                nextStep(String.format("Swap keys %d", j), "swap", Computing.join(key1, internal));
                key2 = key1;
                key1 = internal;
                finalizeStep(Computing.join(key1, key2));
                finalizeStep(Computing.join(key1, key2));
            }
            roundKeys[2 * i + 2] = key1;
            roundKeys[2 * i + 3] = key2;
            finalizeStep(Computing.join(key1, key2));
        }
        return roundKeys;
    }


    private byte[] substitute(byte[] data) {
        byte[] result = new byte[BLOCK_SIZE];
        for (int i = 0; i < BLOCK_SIZE; i++) {
            result[i] = (byte) SLCT[data[i] < 0 ? data[i] + 256 : data[i]];
        }
        return result;
    }

    private byte[] substituteReversed(byte[] data) {
        byte[] result = new byte[BLOCK_SIZE];
        for (int i = 0; i < BLOCK_SIZE; i++) {
            result[i] = (byte) RLCT[data[i] < 0 ? data[i] + 256 : data[i]];
        }
        return result;
    }


    private byte[] linearReversed(byte[] data) {
        byte a_15 = 0;
        byte[] internal = new byte[BLOCK_SIZE];
        for (int i = 0; i < BLOCK_SIZE; i++) {
            if (i != 0) {
                internal[i - 1] = data[i];
            }
            a_15 ^= Computing.multiplyGF(data[i], (byte) lVec[i]);
        }
        internal[BLOCK_SIZE - 1] = a_15;
        return internal;
    }

    private byte[] linear(byte[] data) {
        byte a_0 = data[BLOCK_SIZE - 1];
        byte[] internal = new byte[BLOCK_SIZE];
        for (int i = 0; i < BLOCK_SIZE; i++) {
            if (i != 0) {
                internal[i] = data[i - 1];
            }
            a_0 ^= Computing.multiplyGF(internal[i], (byte) lVec[i]);
        }
        internal[0] = a_0;
        return internal;
    }


    private byte[] rotate(byte[] data) {
        for (int i = 0; i < BLOCK_SIZE; i++) {
            addStep(String.format("Linear convert %d", i), String.format("linear_convert %d", i), data);
            data = linear(data);
            finalizeStep(data);
        }
        return data;
    }

    private byte[] rotateReversed(byte[] data) {
        for (int i = 0; i < BLOCK_SIZE; i++) {
            addStep("Linear convert (reversed)", "linear_convert_reversed", data);
            data = linearReversed(data);
            finalizeStep(data);
        }
        return data;
    }


    @Override
    public byte[] encrypt(byte[] data) {
        seed(data);
        byte[] result = Arrays.copyOfRange(data, 0, BLOCK_SIZE);
        for (int i = 0; i < ROUNDS; i++) {
            addStep(String.format("Round %d", i), "feistel", result, key[i]);
            addStep(String.format("XOR with round key %d", i), "xor_with_round_key", result, key[i]);
            result = Computing.XOR(key[i], result);
            nextStep(String.format("Substitute bytes %d", i), "substitute", result);
            result = substitute(result);
            nextStep(String.format("Rotate bytes %d", i), "rotate", result);
            result = rotate(result);
            finalizeStep(result);
            finalizeStep(result);
        }

        addStep(String.format("Round %d", ROUNDS), "feistel_final", result, key[ROUNDS]);
        result = Computing.XOR(result, key[ROUNDS]);
        finalizeStep(result);

        finalize(result);
        return result;
    }

    @Override
    public byte[] decrypt(byte[] data) {
        seed(data);
        byte[] result = Arrays.copyOfRange(data, 0, BLOCK_SIZE);

        addStep(String.format("Round %d", ROUNDS), "feistel_final", result, key[ROUNDS]);
        result = Computing.XOR(result, key[ROUNDS]);
        finalizeStep(result);

        for (int i = ROUNDS - 1; i >= 0; i--) {
            addStep(String.format("Round %d", i), "feistel", result, key[i]);
            addStep("Rotate bytes (reversed)", "rotate_reversed", result);
            result = rotateReversed(result);
            nextStep("Substitute bytes (reversed)", "substitute_reversed", result);
            result = substituteReversed(result);
            nextStep("XOR with round key", "xor_with_round_key", result, key[i]);
            result = Computing.XOR(key[i], result);
            finalizeStep(result);
            finalizeStep(result);
        }

        finalize(result);
        return result;
    }


    @Override
    public int getBlockSize() {
        return BLOCK_SIZE;
    }
}
