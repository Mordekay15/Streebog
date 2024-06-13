package ciphers;

import statement.StateManager;
import utils.Computing;
import utils.Conversions;

import java.util.Arrays;


public final class Magma extends Cipher {
    public static final int BLOCK_SIZE = 8;
    public static final int PART_SIZE = BLOCK_SIZE / 2;
    public static final int ROUNDS = 32;
    public static final int KEY_SIZE = 32;

    private static final int[][] PI = {
            {0x0c, 0x04, 0x06, 0x02, 0x0a, 0x05, 0x0b, 0x09, 0x0e, 0x08, 0x0d, 0x07, 0x00, 0x03, 0x0f, 0x01},
            {0x06, 0x08, 0x02, 0x03, 0x09, 0x0a, 0x05, 0x0c, 0x01, 0x0e, 0x04, 0x07, 0x0b, 0x0d, 0x00, 0x0f},
            {0x0b, 0x03, 0x05, 0x08, 0x02, 0x0f, 0x0a, 0x0d, 0x0e, 0x01, 0x07, 0x04, 0x0c, 0x09, 0x06, 0x00},
            {0x0c, 0x08, 0x02, 0x01, 0x0d, 0x04, 0x0f, 0x06, 0x07, 0x00, 0x0a, 0x05, 0x03, 0x0e, 0x09, 0x0b},
            {0x07, 0x0f, 0x05, 0x0a, 0x08, 0x01, 0x06, 0x0d, 0x00, 0x09, 0x03, 0x0e, 0x0b, 0x04, 0x02, 0x0c},
            {0x05, 0x0d, 0x0f, 0x06, 0x09, 0x02, 0x0c, 0x0a, 0x0b, 0x07, 0x08, 0x01, 0x04, 0x03, 0x0e, 0x00},
            {0x08, 0x0e, 0x02, 0x05, 0x06, 0x09, 0x01, 0x0c, 0x0f, 0x04, 0x0b, 0x00, 0x0d, 0x0a, 0x03, 0x07},
            {0x01, 0x07, 0x0e, 0x0d, 0x00, 0x05, 0x08, 0x03, 0x04, 0x0f, 0x0a, 0x06, 0x09, 0x0c, 0x0b, 0x02}
    };


    public Magma(byte[] key) {
        super(key);
    }

    public Magma(byte[] key, StateManager manager) {
        super(key, manager);
    }


    @Override
    protected byte[][] expandKey(byte[] key) {
        byte[][] iterKey = new byte[ROUNDS][PART_SIZE];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                addStep(String.format("Round key part %d", i * 8 + j), String.format("round_key_%d", j), key);
                System.arraycopy(key, j * 4, iterKey[i * 8 + j], 0, PART_SIZE);
                finalizeStep(iterKey[i * 8 + j]);
            }
        }
        for (int j = 0; j < 8; j++) {
            addStep(String.format("Round key part %d", 24 + j), String.format("round_key_%d", 7 - j), key);
            System.arraycopy(key, 28 - j * 4, iterKey[24 + j], 0, PART_SIZE);
            finalizeStep(iterKey[24 + j]);
        }
        return iterKey;
    }


    // Function T
    private byte[] transform(byte[] inData) {
        byte[] result = new byte[PART_SIZE];
        for (int i = 0; i < 4; i++) {
            int initialHigher = (inData[i] & 0xf0) >> 4;
            int initialLower = inData[i] & 0x0f;
            int transformedHigher = PI[(3 - i) * 2 + 1][initialHigher];
            int transformedLower = PI[(3 - i) * 2][initialLower];
            result[i] = (byte) ((transformedHigher << 4) | transformedLower);
        }
        return result;
    }


    // Function G
    private byte[] feistel(byte[] roundKey, byte[] leftBlock, byte[] rightBlock) {
        addStep("Add round key", "add_round_key", rightBlock, roundKey);
        byte[] k = Conversions.word(Conversions.word(rightBlock) + Conversions.word(roundKey));
        nextStep("Transform", "transform", k);
        byte[] t = transform(k);
        System.out.println("debug magma transofrm "+ Conversions.hex(t));
        nextStep("Shift left", "shift_left", t);
        int word = Conversions.word(t);
        byte[] e = Conversions.word(((word << 11) & 0xFFFFF800) | ((word >> 21) & 0x7FF));
        System.out.println("debug magma shift "+ Conversions.hex(e));
        nextStep("XOR with round key", "xor_with_round_key", leftBlock);
        byte[] xor = Computing.XOR(leftBlock, e);
        System.out.println("debug magma xor2 "+ Conversions.hex(xor) + "\n");
        finalizeStep(xor);
        return xor;
    }

    @Override
    public byte[] encrypt(byte[] data) {
        seed(data);
        byte[] leftBlock = Arrays.copyOfRange(data, 0, PART_SIZE);
        byte[] rightBlock = Arrays.copyOfRange(data, PART_SIZE, BLOCK_SIZE);

        for (int i = 0; i < ROUNDS - 1; i++) {
            addStep(String.format("Round %d", i), "feistel", Computing.join(leftBlock, rightBlock), key[i]);
            byte[] temp = rightBlock;
            rightBlock = feistel(key[i], leftBlock, rightBlock);
            leftBlock = temp;
            finalizeStep(Computing.join(leftBlock, rightBlock));
        }

        addStep(String.format("Round %d", ROUNDS - 1), "feistel_final", Computing.join(leftBlock, rightBlock), key[ROUNDS - 1]);
        leftBlock = feistel(key[ROUNDS - 1], leftBlock, rightBlock);
        byte[] result = Computing.join(leftBlock, rightBlock);
        finalizeStep(result);

        finalize(result);
        return result;
    }

    @Override
    public byte[] decrypt(byte[] data) {
        seed(data);
        byte[] leftBlock = Arrays.copyOfRange(data, 0, PART_SIZE);
        byte[] rightBlock = Arrays.copyOfRange(data, PART_SIZE, BLOCK_SIZE);

        for (int i = ROUNDS - 1; i > 0; i--) {
            addStep(String.format("Round %d", i), "feistel", Computing.join(leftBlock, rightBlock), key[i]);
            byte[] temp = rightBlock;
            rightBlock = feistel(key[i], leftBlock, rightBlock);
            leftBlock = temp;
            finalizeStep(Computing.join(leftBlock, rightBlock));
        }

        addStep(String.format("Round %d", 0), "feistel_final", Computing.join(leftBlock, rightBlock), key[0]);
        leftBlock = feistel(key[0], leftBlock, rightBlock);
        byte[] result = Computing.join(leftBlock, rightBlock);
        finalizeStep(result);

        finalize(result);
        return result;
    }


    @Override
    public int getBlockSize() {
        return BLOCK_SIZE;
    }
}