package algorithms;

import ciphers.CipherHolder;

import java.util.Arrays;


public final class ECB extends Algorithm {
    public static final Padding defaultPadding = Padding._OOOOOO;


    public ECB(CipherHolder holder, byte[] key, byte[] initialVector, Padding padding) {
        super(holder, key, initialVector, padding);
    }

    public ECB(CipherHolder holder, byte[] key, Padding padding) {
        this(holder, key, null, padding);
    }

    public ECB(CipherHolder holder, byte[] key) {
        this(holder, key, defaultPadding);
    }


    public static ECB withState(CipherHolder holder, byte[] key, byte[] initialVector, Padding padding) {
        ECB result = new ECB(holder, key, initialVector, padding);
        result.cipher = holder.instantiateWithState(key);
        return result;
    }

    public static ECB withState(CipherHolder holder, byte[] key, Padding padding) {
        return ECB.withState(holder, key, null, padding);
    }

    public static ECB withState(CipherHolder holder, byte[] key) {
        return ECB.withState(holder, key, null, defaultPadding);
    }


    @Override
    public byte[] encrypt(byte[] data) {
        int blockSize = cipher.getBlockSize();
        byte[] blocked = padding.pad(data, blockSize);
        blocksTotal = blocked.length / blockSize;
        blocksProcessed = 0;

        byte[] result = new byte[blocked.length];
        for (int i = 0; i < blocksTotal; i++) {
            byte[] block = Arrays.copyOfRange(blocked, blockSize * i, blockSize * (i + 1));
            System.arraycopy(cipher.encrypt(block), 0, result, blockSize * i, blockSize);
            blocksProcessed++;
        }

        return result;
    }

    @Override
    public byte[] decrypt(byte[] data) {
        int blockSize = cipher.getBlockSize();
        blocksTotal = data.length / blockSize;
        blocksProcessed = 0;

        byte[] result = new byte[data.length];
        for (int i = 0; i < blocksTotal; i++) {
            byte[] block = Arrays.copyOfRange(data, blockSize * i, blockSize * (i + 1));
            System.arraycopy(cipher.decrypt(block), 0, result, blockSize * i, blockSize);
            blocksProcessed++;
        }

        return padding.unpad(result);
    }
}
