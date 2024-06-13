package algorithms;

import ciphers.CipherHolder;
import ciphers.Kuznyechik;
import utils.Computing;

import java.util.Arrays;


public final class CBC extends Algorithm {
    static final byte[] defaultIVKuznyechik = {
            (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x90, (byte) 0xab, (byte) 0xce, (byte) 0xf0,
            (byte) 0xa1, (byte) 0xb2, (byte) 0xc3, (byte) 0xd4, (byte) 0xe5, (byte) 0xf0, (byte) 0x01, (byte) 0x12,
            (byte) 0x23, (byte) 0x34, (byte) 0x45, (byte) 0x56, (byte) 0x67, (byte) 0x78, (byte) 0x89, (byte) 0x90,
            (byte) 0x12, (byte) 0x13, (byte) 0x14, (byte) 0x15, (byte) 0x16, (byte) 0x17, (byte) 0x18, (byte) 0x19
    };

    static final byte[] defaultIVMagma = {
            (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x90, (byte) 0xab, (byte) 0xcd, (byte) 0xef,
            (byte) 0x23, (byte) 0x45, (byte) 0x67, (byte) 0x89, (byte) 0x0a, (byte) 0xbc, (byte) 0xde, (byte) 0xf1,
            (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x90, (byte) 0xab, (byte) 0xcd, (byte) 0xef, (byte) 0x12
    };


    static Padding defaultPadding = Padding._OOOOOO;


    public CBC(CipherHolder cipher, byte[] key, byte[] initialVector, Padding padding) {
        super(cipher, key, initialVector, padding);
    }

    public CBC(CipherHolder holder, byte[] key, Padding padding) {
        this(holder, key, holder.getHolding() == Kuznyechik.class ? defaultIVKuznyechik : defaultIVMagma, padding);
    }

    public CBC(CipherHolder holder, byte[] key, byte[] initialVector) {
        this(holder, key, initialVector, defaultPadding);
    }

    public CBC(CipherHolder holder, byte[] key) {
        this(holder, key, defaultPadding);
    }


    @Override
    public byte[] encrypt(byte[] data) {
        int blockSize = cipher.getBlockSize();
        byte[] blocked = padding.pad(data, blockSize);
        blocksTotal = blocked.length / blockSize;
        blocksProcessed = 0;

        byte[] vector = Arrays.copyOfRange(initialVector, 0, initialVector.length);
        byte[] result = new byte[blocked.length];
        for (int i = 0; i < blocksTotal; i++) {
            byte[] block = Arrays.copyOfRange(blocked, blockSize * i, blockSize * (i + 1));
            byte[] encrypted = cipher.encrypt(Computing.XOR(vector, block));
            System.arraycopy(encrypted, 0, result, blockSize * i, blockSize);
            rotateVector(vector, encrypted, blockSize);
            blocksProcessed++;
        }

        return result;
    }

    @Override
    public byte[] decrypt(byte[] data) {
        int blockSize = cipher.getBlockSize();
        blocksTotal = data.length / blockSize;
        blocksProcessed = 0;

        byte[] vector = Arrays.copyOfRange(initialVector, 0, initialVector.length);
        byte[] result = new byte[data.length];
        for (int i = 0; i < blocksTotal; i++) {
            byte[] block = Arrays.copyOfRange(data, blockSize * i, blockSize * (i + 1));
            byte[] decrypted = Computing.XOR(vector, cipher.decrypt(block));
            System.arraycopy(decrypted, 0, result, blockSize * i, blockSize);
            rotateVector(vector, block, blockSize);
            blocksProcessed++;
        }

        return padding.unpad(result);
    }
}
