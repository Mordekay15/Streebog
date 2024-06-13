package algorithms;

import ciphers.Cipher;
import ciphers.CipherHolder;
import utils.Computing;
import utils.Conversions;

import java.math.BigInteger;
import java.util.Arrays;


public final class MAC {
    private static final byte[] B128 = {
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x87
    };


    private final Cipher cipher;
    private final int blockSize;
    private int blocksTotal;
    private int blocksProcessed;

    private final byte[] key1;
    private final byte[] key2;

    public MAC(CipherHolder holder, byte[] key) {
        this.cipher = holder.instantiate(key);
        this.blockSize = cipher.getBlockSize();

        byte[] valueR = cipher.encrypt(new byte[blockSize]);
        this.key1 = getMACKey(valueR);
        this.key2 = getMACKey(key1);
    }


    public int getBlocksTotal() {
        return blocksTotal;
    }

    public int getBlocksProcessed() {
        return blocksProcessed;
    }


    private byte[] getMACKey(byte[] value) {
        byte[] valueB = B128;
        BigInteger int_value = new BigInteger(1, value).shiftLeft(1);
        byte[] result = value[0] == 0 ? int_value.toByteArray() : Computing.XOR(int_value.toByteArray(), valueB);
        return Arrays.copyOfRange(result, result.length - blockSize, result.length);
    }

    private byte[] getMAC(byte[] data) {
        int padSize = data.length % blockSize;
        int blockNum = data.length / blockSize;
        byte[] prevBlock = new byte[blockSize];
        blocksTotal = blockNum + (data.length % blockSize != 0 ? 1 : 0);
        blocksProcessed = 0;

        for (int i = 0; i < blockNum - 1; i++) {
            byte[] block = Arrays.copyOfRange(data, blockSize * i, blockSize * (i + 1));
            prevBlock = cipher.encrypt(Computing.XOR(prevBlock, block));
            blocksProcessed++;
        }
        byte[] remains = Arrays.copyOfRange(data, blockSize * (blockNum - 1), data.length);

        byte[] block;
        if (padSize == 0) {
            block = Arrays.copyOfRange(data, data.length - blockSize, data.length);
            prevBlock = cipher.encrypt(Computing.XOR(prevBlock, block));
            return Computing.XOR(Computing.XOR(remains, prevBlock), key1);
        } else {
            block = Arrays.copyOfRange(data, data.length - 2 * blockSize + padSize, data.length - blockSize + padSize);
            prevBlock = cipher.encrypt(Computing.XOR(prevBlock, block));
            remains = Padding.NO_3.pad(remains, blockSize);
            blocksProcessed++;
            return Computing.XOR(Computing.XOR(remains, prevBlock), key2);
        }
    }


    public Cipher getCipher() {
        return cipher;
    }


    public byte[] subscribe(byte[] data, int length) {
        if (length > blockSize) {
            throw new RuntimeException(String.format("Запрашиваемый размер MAC на %d больше размера блока (%d)", length, blockSize));
        }
        return Arrays.copyOf(getMAC(data), length);
    }

    public byte[] subscribe(byte[] data) {
        return subscribe(data, blockSize);
    }

    public byte[] subscribeString(String data, int length) {
        return subscribe(Conversions.raw(data), length);
    }

    public byte[] subscribeString(String data) {
        return subscribeString(data, blockSize);
    }
}
