package algorithms;

import ciphers.CipherHolder;
import ciphers.Kuznyechik;
import utils.Computing;

import java.util.Arrays;


public final class CTR extends Algorithm {

    static final byte[] defaultIVMagma = {
            (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78
    };

    static final byte[] defaultIVKuznyechik = {
            (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x90, (byte) 0xab, (byte) 0xce, (byte) 0xf0
    };

    public CTR(CipherHolder holder, byte[] key, byte[] initialVector, Padding padding) {
        super(holder, key, initialVector, padding);
    }

    public CTR(CipherHolder holder, byte[] key, byte[] initialVector) {
        super(holder, key, initialVector, null);
    }

    public CTR(CipherHolder holder, byte[] key) {
        super(holder, key, holder.getHolding() == Kuznyechik.class ? defaultIVKuznyechik : defaultIVMagma, null);
    }


    private byte[] rotateCounter(byte[] counter, int blockSize) {
        byte[] bit = new byte[blockSize];
        bit[blockSize - 1] = 0x01;

        int internal = 0;
        byte[] nctr = Arrays.copyOf(counter, blockSize);
        for (int i = 0; i < blockSize; i++) {
            internal = nctr[i] + bit[i] + (internal << 8);
            nctr[i] = (byte) (internal & 0xff);
        }
        return nctr;
    }


    @Override
    public byte[] encrypt(byte[] data) {
        int blockSize = cipher.getBlockSize();
        int blockNum = data.length / blockSize;
        blocksTotal = blockNum + (data.length % blockSize != 0 ? 1 : 0);
        blocksProcessed = 0;

        byte[] counter = new byte[blockSize];
        System.arraycopy(initialVector, 0, counter, 0, initialVector.length);

        byte[] result = new byte[data.length];
        for (int i = 0; i < blockNum; i++) {
            byte[] gamma = cipher.encrypt(counter);
            counter = rotateCounter(counter, blockSize);
            byte[] block = Arrays.copyOfRange(data, blockSize * i, blockSize * (i + 1));
            System.arraycopy(Computing.XOR(block, gamma), 0, result, blockSize * i, blockSize);
            blocksProcessed++;
        }
        if (data.length % blockSize != 0) {
            byte[] gamma = cipher.encrypt(counter);
            byte[] block = Arrays.copyOfRange(data, blockSize * blockNum, data.length);
            byte[] encrypted = Computing.XOR(block, gamma);
            System.arraycopy(encrypted, 0, result, blockSize * blockNum, encrypted.length);
            blocksProcessed++;
        }

        return result;
    }

    @Override
    public byte[] decrypt(byte[] data) {
        return encrypt(data);
    }
}
